package src.services;

import org.json.JSONArray;
import org.json.JSONObject;
import src.analyzers.types.AnalyzerType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Aggregates results from multiple log file analyses into a single JSON report.
 */
public class ReportAggregatorService {

    /**
     * Merges the analysis results from each log file into a final report.
     *
     * @param allTaskResults List of results from each log file (one per file)
     * @param analyzerTypes  List of analyzer types to include (as strings)
     * @param fileNames      File names, one-to-one mapped with allTaskResults
     * @return JSONObject containing the combined analysis report
     */
    public JSONObject mergeResults(
            List<Map<AnalyzerType, JSONObject>> allTaskResults,
            List<String> analyzerTypes,
            List<String> fileNames
    ) {
        JSONObject report = new JSONObject();

        Map<String, Integer> countLevelsMerged = new HashMap<>();
        JSONObject anomaliesCombined = new JSONObject();

        int fileIndex = 0;

        // Process each file's analysis result
        for (Map<AnalyzerType, JSONObject> taskResult : allTaskResults) {
            String currentFile = fileNames.get(fileIndex++);

            for (String typeStr : analyzerTypes) {
                AnalyzerType type = AnalyzerType.fromString(typeStr);
                JSONObject result = taskResult.get(type);

                if (result == null) continue;

                switch (type) {
                    case COUNT_LEVELS -> {
                        // Merge log level counts like {"info": 3, "error": 2}
                        mergeSimpleCounts(countLevelsMerged, result);
                    }

                    case FIND_COMMON_SOURCE -> {
                        // Take only the first available result (no merge logic for this)
                        if (!report.has(type.toString())) {
                            report.put(type.toString(), result);
                        }
                    }

                    case DETECT_ANOMALIES -> {
                        // Only include files with at least one anomaly
                        if (result.getInt("anomalies_count") > 0) {
                            anomaliesCombined.put(currentFile, result);
                        }
                    }
                }
            }
        }

        // Add merged COUNT_LEVELS results
        if (!countLevelsMerged.isEmpty()) {
            report.put(AnalyzerType.COUNT_LEVELS.toString(), new JSONObject(countLevelsMerged));
        }

        // Add DETECT_ANOMALIES results
        if (anomaliesCombined.isEmpty()) {
            report.put(AnalyzerType.DETECT_ANOMALIES.toString(), new JSONArray());
        } else {
            // Convert map to a JSON object and wrap it properly
            report.put(AnalyzerType.DETECT_ANOMALIES.toString(), anomaliesCombined);
        }

        return report;
    }

    /**
     * Merges JSON objects with simple key-value counts into a single map.
     * E.g., merging {"info": 2} + {"info": 3} -> {"info": 5}
     *
     * @param merged   The cumulative map to update
     * @param incoming The new JSONObject to merge
     */
    private void mergeSimpleCounts(Map<String, Integer> merged, JSONObject incoming) {
        for (String key : incoming.keySet()) {
            int value = incoming.getInt(key);
            merged.put(key, merged.getOrDefault(key, 0) + value);
        }
    }
}
