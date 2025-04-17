package src.tasks;

import org.json.JSONObject;
import src.analyzers.interfaces.LogAnalyzer;
import src.analyzers.types.AnalyzerType;
import src.domain.LogEntry;
import src.parsers.LogParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Task to process a single log file.
 * This includes:
 * - Reading the log file line by line
 * - Parsing each line into a LogEntry
 * - Running all configured analyzers on the parsed log entries
 * - Returning analysis results as a map (AnalyzerType -> JSON report)
 */
public class LogFileProcessingTask implements Callable<Map<AnalyzerType, JSONObject>> {

    private final File logFile;                       // The log file to process
    private final List<LogAnalyzer> analyzers;        // List of analyzers to run on the parsed logs
    private final LogParser parser = new LogParser(); // Parser to convert raw log lines to LogEntry objects

    public LogFileProcessingTask(File logFile, List<LogAnalyzer> analyzers) {
        this.logFile = logFile;
        this.analyzers = analyzers;
    }

    /**
     * Processes the log file:
     * - Parses it into LogEntry objects
     * - Runs each analyzer on the entries
     *
     * @return A map of analyzer results, keyed by AnalyzerType
     */
    @Override
    public Map<AnalyzerType, JSONObject> call() {
        List<LogEntry> entries = new ArrayList<>(); // Holds all successfully parsed log entries
        Map<AnalyzerType, JSONObject> results = new HashMap<>(); // Holds analysis results

        // Step 1: Read and parse the log file line-by-line
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                LogEntry entry = parser.parseLine(line);
                if (entry != null) {
                    entries.add(entry); // Only add non-null entries
                }
            }
        } catch (IOException e) {
            // If the file can't be read, log the issue and return an empty result
            System.out.println("❌ Error reading file: " + logFile.getName());
            return results;
        }

        // Step 2: Run each analyzer on the parsed entries
        for (LogAnalyzer analyzer : analyzers) {
            try {
                // Run analysis and store the result
                results.put(analyzer.getName(), analyzer.analyze(entries));
            } catch (Exception e) {
                // Catch individual analyzer failure so one failure doesn't break all
                System.out.println("❌ Analyzer " + analyzer.getName() + " failed on file " + logFile.getName());
            }
        }

        return results;
    }
}
