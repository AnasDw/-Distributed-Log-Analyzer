package src.analyzers.factory;

import src.analyzers.AnomalyDetector;
import src.analyzers.CommonSourcesAnalyzer;
import src.analyzers.CountLevelsAnalyzer;
import src.analyzers.interfaces.LogAnalyzer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Creates LogAnalyzer instances based on configuration.
 */
public class AnalyzerFactory {

    /**
     * Creates all analyzers listed in config.
     *
     * @param types           List of analyzer names
     * @param anomalyLevels   List of log levels to analyze for anomalies
     * @param windowInSeconds Time window in seconds
     * @param threshold       Minimum number of messages in window to trigger anomaly
     * @return List of active LogAnalyzer implementations
     */
    public static List<LogAnalyzer> createAnalyzers(
            List<String> types,
            List<String> anomalyLevels,
            int windowInSeconds,
            int threshold
    ) {
        List<LogAnalyzer> analyzers = new ArrayList<>();

        for (String type : types) {
            switch (type.trim().toUpperCase()) {
                case "COUNT_LEVELS":
                    analyzers.add(new CountLevelsAnalyzer());
                    break;

                case "FIND_COMMON_SOURCE":
                    analyzers.add(new CommonSourcesAnalyzer());
                    break;

                case "DETECT_ANOMALIES":
                    Set<String> levelsSet = new HashSet<>();
                    for (String level : anomalyLevels) {
                        levelsSet.add(level.trim().toUpperCase());
                    }
                    analyzers.add(new AnomalyDetector(levelsSet, windowInSeconds, threshold));
                    break;

                default:
                    System.out.println("⚠️ Unknown analysis type: " + type);
                    break;
            }
        }

        return analyzers;
    }
}
