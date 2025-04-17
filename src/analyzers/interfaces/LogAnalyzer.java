package src.analyzers.interfaces;

import org.json.JSONObject;
import src.analyzers.types.AnalyzerType;
import src.domain.LogEntry;

import java.util.List;

/**
 * Interface that all log analyzers must implement.
 * Each analyzer processes a list of log entries and produces a JSON result.
 */
public interface LogAnalyzer {

    /**
     * Returns the type/name of this analyzer.
     *
     * @return the AnalyzerType enum value associated with this analyzer
     */
    AnalyzerType getName();

    /**
     * Performs analysis on the given list of log entries.
     *
     * @param entries the list of parsed log entries to analyze
     * @return a JSONObject representing the analysis result
     */
    JSONObject analyze(List<LogEntry> entries);
}
