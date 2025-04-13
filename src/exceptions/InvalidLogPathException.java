package src.exceptions;

/**
 * Thrown when the provided log directory path is invalid (non-existent or not a directory).
 */
public class InvalidLogPathException extends RuntimeException {

    public InvalidLogPathException(String path) {
        super("❌ Invalid log path: " + path);
    }
}
