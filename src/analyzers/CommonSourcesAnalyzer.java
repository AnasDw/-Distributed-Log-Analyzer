package src.analyzers;

import org.json.JSONObject;
import src.analyzers.interfaces.LogAnalyzer;
import src.domain.LogEntry;

import java.util.*;

public class CommonSourcesAnalyzer implements LogAnalyzer {

    @Override
    public String getName() {
        return "FIND_COMMON_SOURCE";
    }

    @Override
    public JSONObject analyze(List<LogEntry> entries) {
        Map<String, Integer> sourceCounts = new HashMap<>();

        for (LogEntry entry : entries) {
            String source = entry.getSource();
            sourceCounts.put(source, sourceCounts.getOrDefault(source, 0) + 1);
        }

        // Build JSON fields
        List<String> sources = new ArrayList<>(sourceCounts.keySet());
        List<Integer> counts = new ArrayList<>();
        for (String source : sources) {
            counts.add(sourceCounts.get(source));
        }

        String mostCommon = Collections.max(sourceCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
        int mostCommonCount = sourceCounts.get(mostCommon);

        String leastCommon = Collections.min(sourceCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
        int leastCommonCount = sourceCounts.get(leastCommon);

        JSONObject result = new JSONObject();
        result.put("sources", sources.toString());
        result.put("source_counts", counts.toString());
        result.put("most_common_source", mostCommon);
        result.put("most_common_source_count", mostCommonCount);
        result.put("least_common_source", leastCommon);
        result.put("least_common_source_count", leastCommonCount);

        return result;
    }
}

