package src.parsers;

import src.domain.LogEntry;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * LogParser is responsible for parsing individual log lines
 * into LogEntry objects based on the expected log format:
 * [TIMESTAMP] [LEVEL] [SOURCE] [MESSAGE]
 */
public class LogParser {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Parses a single line of a log file.
     *
     * @param line the line to parse
     * @return a LogEntry object if the line is valid, or null if malformed
     */
    public LogEntry parseLine(String line) {
        try {
            // Remove outer brackets and split into 4 parts
            String[] parts = line.split("\\] \\[");
            if (parts.length != 4) {
                System.out.println("unexpected input " + line);
                return null;
            }

            // Clean brackets
            String timestampStr = parts[0].substring(1);
            String level = parts[1];
            String source = parts[2];
            String message = parts[3].substring(0, parts[3].length() - 1);

            LocalDateTime timestamp = LocalDateTime.parse(timestampStr, TIMESTAMP_FORMATTER);

            return new LogEntry(timestamp, level, source, message);
        } catch (DateTimeParseException | StringIndexOutOfBoundsException | NullPointerException e) {
            System.out.println("unexpected input " + line);
            return null;
        }
    }
}
