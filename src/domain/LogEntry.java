package src.domain;

import java.time.LocalDateTime;

/**
 * Represents a single log entry parsed from a log file.
 * Format:
 * [TIMESTAMP] [LEVEL] [SOURCE] [MESSAGE]
 */
public class LogEntry {
    private final LocalDateTime timestamp;
    private final String level;
    private final String source;
    private final String message;

    /**
     * Constructs a new LogEntry.
     *
     * @param timestamp the timestamp of the log
     * @param level     the log level (e.g., INFO, ERROR, WARNING)
     * @param source    the log source (e.g., Server1, Database)
     * @param message   the log message
     */
    public LogEntry(LocalDateTime timestamp, String level, String source, String message) {
        this.timestamp = timestamp;
        this.level = level;
        this.source = source;
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getLevel() {
        return level;
    }

    public String getSource() {
        return source;
    }
    

    @Override
    public String toString() {
        return "[" + timestamp + "] [" + level + "] [" + source + "] [" + message + "]";
    }
}
