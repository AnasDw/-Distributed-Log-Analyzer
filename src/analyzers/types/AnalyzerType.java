package src.analyzers.types;


public enum AnalyzerType {
    COUNT_LEVELS,
    FIND_COMMON_SOURCE,
    DETECT_ANOMALIES;

    public static AnalyzerType fromString(String value) {
        return AnalyzerType.valueOf(value.trim().toUpperCase());
    }
}

