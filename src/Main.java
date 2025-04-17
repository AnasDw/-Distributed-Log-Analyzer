package src;

import org.json.JSONObject;
import src.analyzers.factory.AnalyzerFactory;
import src.analyzers.interfaces.LogAnalyzer;
import src.analyzers.types.AnalyzerType;
import src.exceptions.ConfigurationLoadException;
import src.exceptions.InvalidLogPathException;
import src.providers.ConfigManagerProvider;
import src.providers.JsonReportWriterService;
import src.providers.ThreadPoolExecutorProvider;
import src.services.FileReaderService;
import src.services.ReportAggregatorService;
import src.tasks.LogFileProcessingTask;
import src.utils.ExitCodes;
import src.utils.Timer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Entry point for the Distributed Log Analyzer.
 * Responsible for:
 * - Loading configuration
 * - Validating log directory
 * - Processing log files (parse + analyze)
 * - Aggregating & saving report
 */
public class Main {
    private static final String CONFIG_FILE = "config.properties";

    public static void main(String[] args) {
        try {
            Timer timer = new Timer();
            timer.start();

            // Load configuration
            ConfigManagerProvider config = new ConfigManagerProvider(CONFIG_FILE);

            // Get all log files
            FileReaderService fileReader = new FileReaderService();
            List<File> logFiles = fileReader.getLogFiles(config.getLogDirectory());

            // Create analyzers from config
            List<LogAnalyzer> analyzers = AnalyzerFactory.createAnalyzers(
                    Arrays.asList(config.getAnalysisTypes()),
                    Arrays.asList(config.getAnomalyLevels()),
                    config.getAnomalyWindowSeconds(),
                    config.getAnomalyThreshold()
            );

            // Thread pool
            ThreadPoolExecutorProvider executorProvider =
                    new ThreadPoolExecutorProvider(config.getThreadPoolSize());
            ExecutorService executor = executorProvider.getExecutorService();

            // Submit file processing tasks (parse + analyze per file)
            List<Future<Map<AnalyzerType, JSONObject>>> futures = new ArrayList<>();
            for (File logFile : logFiles) {
                futures.add(executor.submit(new LogFileProcessingTask(logFile, analyzers)));
            }

            // Collect all results per file
            List<Map<AnalyzerType, JSONObject>> allResults = new ArrayList<>();
            List<String> fileNames = new ArrayList<>();

            for (int i = 0; i < futures.size(); i++) {
                try {
                    Map<AnalyzerType, JSONObject> result = futures.get(i).get();
                    allResults.add(result);
                    fileNames.add(logFiles.get(i).getName());
                } catch (Exception e) {
                    System.out.println("❌ Error collecting results for " + logFiles.get(i).getName());
                }
            }

            // Aggregate into full report
            ReportAggregatorService aggregator = new ReportAggregatorService();
            JSONObject finalReport = aggregator.mergeResults(
                    allResults,
                    Arrays.asList(config.getAnalysisTypes()),
                    fileNames
            );

            // Write JSON report
            new JsonReportWriterService().write(config.getOutputFile(), finalReport);

            executorProvider.shutdown();

            timer.stopAndPrint("Log analysis");

            System.exit(ExitCodes.SUCCESS);
        } catch (ConfigurationLoadException | InvalidLogPathException e) {
            System.out.println(e.getMessage());
            System.exit(ExitCodes.ERROR);
        } catch (Exception e) {
            System.out.println("❌ Unexpected error: " + e.getMessage());
            e.printStackTrace();
            System.exit(ExitCodes.ERROR);
        }
    }
}
