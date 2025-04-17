package src.analyzers.factory;

import src.analyzers.AnomalyDetector;
import src.analyzers.CommonSourcesAnalyzer;
import src.analyzers.CountLevelsAnalyzer;
import src.analyzers.interfaces.LogAnalyzer;
import src.analyzers.types.AnalyzerType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
            try {
                switch (AnalyzerType.fromString(type)) {
                    case COUNT_LEVELS -> analyzers.add(new CountLevelsAnalyzer());
                    case FIND_COMMON_SOURCE -> analyzers.add(new CommonSourcesAnalyzer());
                    case DETECT_ANOMALIES -> {
                        Set<String> levelsSet = anomalyLevels.stream()
                                .map(String::toUpperCase)
                                .collect(Collectors.toSet());
                        analyzers.add(new AnomalyDetector(levelsSet, windowInSeconds, threshold));
                    }
                }
            } catch (IllegalArgumentException e) {
                System.out.println("⚠️ Unknown analyzer type: " + type);
            }
        }


        return analyzers;
    }
}
