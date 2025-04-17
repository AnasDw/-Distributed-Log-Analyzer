package src.analyzers;

import org.json.JSONObject;
import src.analyzers.interfaces.LogAnalyzer;
import src.analyzers.types.AnalyzerType;
import src.domain.LogEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Analyzer that counts the number of log entries per log level (e.g., info, warn, error).
 */
public class CountLevelsAnalyzer implements LogAnalyzer {

    /**
     * Returns the identifier for this analyzer.
     */
    @Override
    public AnalyzerType getName() {
        return AnalyzerType.COUNT_LEVELS;
    }

    /**
     * Analyzes the given list of log entries and returns a JSON object with counts per log level.
     *
     * @param entries List of parsed log entries to analyze
     * @return JSONObject where keys are log levels (e.g., "info", "error") and values are counts
     */
    @Override
    public JSONObject analyze(List<LogEntry> entries) {
        Map<String, Integer> levelCounts = new HashMap<>();

        for (LogEntry entry : entries) {
            if (entry.getLevel() == null) continue; // Skip if level is missing

            String level = entry.getLevel().toLowerCase();
            levelCounts.put(level, levelCounts.getOrDefault(level, 0) + 1);
        }

        return new JSONObject(levelCounts);
    }
}
