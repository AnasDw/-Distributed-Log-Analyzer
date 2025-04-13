package src.analyzers;

import org.json.JSONObject;
import src.analyzers.interfaces.LogAnalyzer;
import src.domain.LogEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountLevelsAnalyzer implements LogAnalyzer {

    @Override
    public String getName() {
        return "COUNT_LEVELS";
    }

    @Override
    public JSONObject analyze(List<LogEntry> entries) {
        Map<String, Integer> levelCounts = new HashMap<>();

        for (LogEntry entry : entries) {
            String level = entry.getLevel().toLowerCase();
            levelCounts.put(level, levelCounts.getOrDefault(level, 0) + 1);
        }

        return new JSONObject(levelCounts);
    }
}
