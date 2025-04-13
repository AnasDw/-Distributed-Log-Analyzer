package src.providers;

import src.exceptions.ConfigurationLoadException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigManagerProvider is responsible for loading configuration properties
 * from a properties file and exposing them through strongly-typed accessors.
 * <p>
 * This class provides default values for each configuration key in case
 * they are missing from the config file.
 * <p>
 * Usage:
 * <pre>
 *     ConfigManagerProvider config = new ConfigManagerProvider("config.properties");
 *     String dir = config.getLogDirectory();
 * </pre>
 */
public class ConfigManagerProvider {

    // Property keys
    private static final String LOG_DIRECTORY_KEY = "log.directory";
    private static final String THREAD_POOL_SIZE_KEY = "thread.pool.size";
    private static final String OUTPUT_FILE_KEY = "output.file";
    private static final String ANALYSIS_TYPES_KEY = "log.analysis";
    private static final String ANOMALY_LEVELS_KEY = "log.analysis.anomalies.levels";
    private static final String ANOMALY_WINDOW_KEY = "log.analysis.anomalies.window";
    private static final String ANOMALY_THRESHOLD_KEY = "log.analysis.anomalies.threshold";
    // Default values
    private static final String DEFAULT_LOG_DIRECTORY = "logs";
    private static final String DEFAULT_THREAD_POOL_SIZE = "5";
    private static final String DEFAULT_OUTPUT_FILE = "log_report.json";
    private static final String DEFAULT_ANALYSIS_TYPES = "COUNT_LEVELS";
    private static final String DEFAULT_ANOMALY_LEVELS = "ERROR";
    private static final String DEFAULT_ANOMALY_WINDOW = "60";
    private static final String DEFAULT_ANOMALY_THRESHOLD = "5";
    private final Properties props = new Properties();

    /**
     * Constructs a ConfigManagerProvider instance by loading properties from a given file path.
     *
     * @param path the path to the config.properties file
     * @throws ConfigurationLoadException if the config file cannot be loaded
     */
    public ConfigManagerProvider(String path) throws ConfigurationLoadException {
        try (FileInputStream fis = new FileInputStream(path)) {
            props.load(fis);
            System.out.println("✅ Configuration loaded from: " + path);
        } catch (IOException e) {
            throw new ConfigurationLoadException("Failed to load configuration file: " + path, e);
        }
    }

    /**
     * @return the directory containing the log files
     */
    public String getLogDirectory() {
        return props.getProperty(LOG_DIRECTORY_KEY, DEFAULT_LOG_DIRECTORY);
    }

    /**
     * @return the number of threads to use in the thread pool
     */
    public int getThreadPoolSize() {
        return Integer.parseInt(props.getProperty(THREAD_POOL_SIZE_KEY, DEFAULT_THREAD_POOL_SIZE));
    }

    /**
     * @return the output file path for the final JSON report
     */
    public String getOutputFile() {
        return props.getProperty(OUTPUT_FILE_KEY, DEFAULT_OUTPUT_FILE);
    }

    /**
     * @return an array of analysis types to apply (e.g. COUNT_LEVELS, DETECT_ANOMALIES)
     */
    public String[] getAnalysisTypes() {
        return props.getProperty(ANALYSIS_TYPES_KEY, DEFAULT_ANALYSIS_TYPES).split(",");
    }

    /**
     * @return an array of log levels to be considered for anomaly detection
     */
    public String[] getAnomalyLevels() {
        return props.getProperty(ANOMALY_LEVELS_KEY, DEFAULT_ANOMALY_LEVELS).split(",");
    }

    /**
     * @return the time window in seconds for detecting anomalies
     */
    public int getAnomalyWindowSeconds() {
        return Integer.parseInt(props.getProperty(ANOMALY_WINDOW_KEY, DEFAULT_ANOMALY_WINDOW));
    }

    /**
     * @return the threshold (number of logs) above which an anomaly is triggered
     */
    public int getAnomalyThreshold() {
        return Integer.parseInt(props.getProperty(ANOMALY_THRESHOLD_KEY, DEFAULT_ANOMALY_THRESHOLD));
    }
}
