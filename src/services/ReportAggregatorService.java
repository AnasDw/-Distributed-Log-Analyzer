package src.services;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportAggregatorService {

    public JSONObject mergeResults(
            List<Map<String, JSONObject>> allTaskResults,
            List<String> analyzerTypes,
            List<String> fileNames
    ) {
        JSONObject report = new JSONObject();

        Map<String, JSONObject> countLevelsMerged = new HashMap<>();
        Map<String, JSONObject> findCommonSourcesMerged = new HashMap<>();
        JSONObject anomaliesCombined = new JSONObject();

        int fileIndex = 0;

        for (Map<String, JSONObject> taskResult : allTaskResults) {
            String currentFile = fileNames.get(fileIndex++);

            for (String type : analyzerTypes) {
                JSONObject result = taskResult.get(type);

                if (result == null) continue;

                switch (type.toUpperCase()) {
                    case "COUNT_LEVELS":
                        mergeCounts(countLevelsMerged, result);
                        break;

                    case "FIND_COMMON_SOURCE":
                        mergeCounts(findCommonSourcesMerged, result);
                        break;

                    case "DETECT_ANOMALIES":
                        if (result.getInt("anomalies_count") > 0) {
                            anomaliesCombined.put(currentFile, result);
                        }
                        break;
                }
            }
        }

        if (!countLevelsMerged.isEmpty()) report.put("COUNT_LEVELS", new JSONObject(countLevelsMerged));
        if (!findCommonSourcesMerged.isEmpty())
            report.put("FIND_COMMON_SOURCE", new JSONObject(findCommonSourcesMerged));
        if (anomaliesCombined.isEmpty()) {
            report.put("DETECT_ANOMALIES", new JSONArray()); // as per spec
        } else {
            report.put("DETECT_ANOMALIES", List.of(anomaliesCombined));
        }

        return report;
    }

    private void mergeCounts(Map<String, JSONObject> merged, JSONObject incoming) {
        for (String key : incoming.keySet()) {
            int value = incoming.getInt(key);

            merged.putIfAbsent(key, new JSONObject().put("value", 0));
            int current = merged.get(key).getInt("value");
            merged.get(key).put("value", current + value);
        }
    }
}

