package src.services;

import org.json.JSONArray;
import org.json.JSONObject;
import src.analyzers.types.AnalyzerType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportAggregatorService {

    public JSONObject mergeResults(
            List<Map<AnalyzerType, JSONObject>> allTaskResults,
            List<String> analyzerTypes,
            List<String> fileNames
    ) {
        JSONObject report = new JSONObject();

        Map<String, Integer> countLevelsMerged = new HashMap<>();
        JSONObject anomaliesCombined = new JSONObject();

        int fileIndex = 0;

        for (Map<AnalyzerType, JSONObject> taskResult : allTaskResults) {
            String currentFile = fileNames.get(fileIndex++);

            for (String typeStr : analyzerTypes) {
                AnalyzerType type = AnalyzerType.fromString(typeStr);
                JSONObject result = taskResult.get(type);

                if (result == null) continue;

                switch (type) {
                    case COUNT_LEVELS -> mergeSimpleCounts(countLevelsMerged, result);

                    case FIND_COMMON_SOURCE -> {
                        // Take first valid one only (no merge logic)
                        if (!report.has(type.toString())) {
                            report.put(type.toString(), result);
                        }
                    }

                    case DETECT_ANOMALIES -> {
                        if (result.getInt("anomalies_count") > 0) {
                            anomaliesCombined.put(currentFile, result);
                        }
                    }
                }
            }
        }

        // Add merged results to final report
        if (!countLevelsMerged.isEmpty())
            report.put(AnalyzerType.COUNT_LEVELS.toString(), new JSONObject(countLevelsMerged));

        if (anomaliesCombined.isEmpty()) {
            report.put(AnalyzerType.DETECT_ANOMALIES.toString(), new JSONArray());
        } else {
            report.put(AnalyzerType.DETECT_ANOMALIES.toString(), List.of(anomaliesCombined));
        }

        return report;
    }

    /**
     * Merge key-value JSONs like { "info": 3, "error": 2 }
     */
    private void mergeSimpleCounts(Map<String, Integer> merged, JSONObject incoming) {
        for (String key : incoming.keySet()) {
            int value = incoming.getInt(key);
            merged.put(key, merged.getOrDefault(key, 0) + value);
        }
    }
}
