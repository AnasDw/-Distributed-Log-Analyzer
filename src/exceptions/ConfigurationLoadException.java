package src.exceptions;

/**
 * Thrown when the ConfigManagerProvider fails to load the configuration
 * file due to issues like file not found or parsing error.
 */
public class ConfigurationLoadException extends Exception {

    /**
     * Constructs a ConfigurationLoadException with a message.
     *
     * @param message the detail message
     */
    public ConfigurationLoadException(String message) {
        super(message);
    }

    /**
     * Constructs a ConfigurationLoadException with a message and cause.
     *
     * @param message the detail message
     * @param cause the underlying cause of the exception
     */
    public ConfigurationLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
