package src.tasks;

import org.json.JSONObject;
import src.analyzers.interfaces.LogAnalyzer;
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
 * Processes one log file: parses and analyzes it using provided analyzers.
 */
public class LogFileProcessingTask implements Callable<Map<String, JSONObject>> {

    private final File logFile;
    private final List<LogAnalyzer> analyzers;
    private final LogParser parser = new LogParser();

    public LogFileProcessingTask(File logFile, List<LogAnalyzer> analyzers) {
        this.logFile = logFile;
        this.analyzers = analyzers;
    }

    @Override
    public Map<String, JSONObject> call() {
        List<LogEntry> entries = new ArrayList<>();
        Map<String, JSONObject> results = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                LogEntry entry = parser.parseLine(line);
                if (entry != null) entries.add(entry);
            }
        } catch (IOException e) {
            System.out.println("error processing file " + logFile.getName());
            return results; // empty result on failure
        }

        // Run analyzers on this file's entries
        for (LogAnalyzer analyzer : analyzers) {
            try {
                results.put(analyzer.getName(), analyzer.analyze(entries));
            } catch (Exception e) {
                System.out.println("❌ Analyzer " + analyzer.getName() + " failed on file " + logFile.getName());
            }
        }

        return results;
    }
}
