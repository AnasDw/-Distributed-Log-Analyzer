package src.analyzers;

import org.json.JSONArray;
import org.json.JSONObject;
import src.analyzers.interfaces.LogAnalyzer;
import src.analyzers.types.AnalyzerType;
import src.domain.LogEntry;

import java.time.Duration;
import java.util.*;

/**
 * Analyzer that detects bursts of target log levels (e.g., ERROR, WARN) within a time window.
 * Flags timestamps where a threshold of logs occur in a short window.
 */
public class AnomalyDetector implements LogAnalyzer {

    private final Set<String> targetLevels;   // Levels to watch (e.g., ERROR, WARN)
    private final int windowInSeconds;        // Time window size
    private final int threshold;              // Count threshold to trigger anomaly

    public AnomalyDetector(Set<String> levels, int windowInSeconds, int threshold) {
        this.targetLevels = levels;
        this.windowInSeconds = windowInSeconds;
        this.threshold = threshold;
    }

    @Override
    public AnalyzerType getName() {
        return AnalyzerType.DETECT_ANOMALIES;
    }

    @Override
    public JSONObject analyze(List<LogEntry> entries) {
        JSONObject result = new JSONObject();
        Set<String> anomalyTimestamps = new HashSet<>();

        // Step 1: Filter only entries with the target levels
        List<LogEntry> filtered = new ArrayList<>();
        for (LogEntry entry : entries) {
            if (entry.getLevel() == null || entry.getTimestamp() == null) continue;
            if (targetLevels.contains(entry.getLevel().toUpperCase())) {
                filtered.add(entry);
            }
        }

        // Step 2: Sort filtered logs by timestamp
        filtered.sort(Comparator.comparing(LogEntry::getTimestamp));

        // Step 3: Sliding window to detect bursts of anomalies
        for (int i = 0; i < filtered.size(); i++) {
            int count = 1;
            for (int j = i + 1; j < filtered.size(); j++) {
                Duration duration = Duration.between(
                        filtered.get(i).getTimestamp(),
                        filtered.get(j).getTimestamp()
                );
                if (duration.getSeconds() <= windowInSeconds) {
                    count++;
                } else {
                    break;
                }
            }
            if (count >= threshold) {
                anomalyTimestamps.add(filtered.get(i).getTimestamp().toString());
            }
        }

        List<String> sortedAnomalies = new ArrayList<>(anomalyTimestamps);
        Collections.sort(sortedAnomalies);

        result.put("anomalies", new JSONArray(sortedAnomalies));
        result.put("anomalies_count", anomalyTimestamps.size());

        return result;
    }
}
