package src.analyzers;

import org.json.JSONObject;
import src.analyzers.interfaces.LogAnalyzer;
import src.analyzers.types.AnalyzerType;
import src.domain.LogEntry;

import java.time.Duration;
import java.util.*;

public class AnomalyDetector implements LogAnalyzer {

    private final Set<String> targetLevels;
    private final int windowInSeconds;
    private final int threshold;

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
        Map<String, List<String>> anomaliesByFile = new HashMap<>();

        List<LogEntry> filtered = new ArrayList<>();
        for (LogEntry entry : entries) {
            if (targetLevels.contains(entry.getLevel().toUpperCase())) {
                filtered.add(entry);
            }
        }

        // Sort by timestamp
        filtered.sort(Comparator.comparing(LogEntry::getTimestamp));

        Set<String> anomalyTimestamps = new HashSet<>();
        for (int i = 0; i < filtered.size(); i++) {
            int count = 1;
            String current = filtered.get(i).getTimestamp().toString();
            for (int j = i + 1; j < filtered.size(); j++) {
                Duration duration = Duration.between(filtered.get(i).getTimestamp(), filtered.get(j).getTimestamp());
                if (duration.getSeconds() <= windowInSeconds) {
                    count++;
                } else {
                    break;
                }
            }
            if (count >= threshold) {
                anomalyTimestamps.add(current); // first log's timestamp in the window
            }
        }

        result.put("anomalies", anomalyTimestamps);
        result.put("anomalies_count", anomalyTimestamps.size());

        return result;
    }
}
