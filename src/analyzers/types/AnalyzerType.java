package src.analyzers.types;

/**
 * Enum representing the available types of log analyzers.
 */
public enum AnalyzerType {
    COUNT_LEVELS,
    FIND_COMMON_SOURCE,
    DETECT_ANOMALIES;

    /**
     * Converts a string (case-insensitive) to an AnalyzerType enum.
     *
     * @param value The string representation of the analyzer type
     * @return The matching AnalyzerType enum
     * @throws IllegalArgumentException if the string doesn't match any known type
     */
    public static AnalyzerType fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("AnalyzerType cannot be null.");
        }
        return AnalyzerType.valueOf(value.trim().toUpperCase());
    }
}
