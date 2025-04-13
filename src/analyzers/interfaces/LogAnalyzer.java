package src.analyzers.interfaces;

import org.json.JSONObject;
import src.domain.LogEntry;

import java.util.List;

public interface LogAnalyzer {
    String getName();

    JSONObject analyze(List<LogEntry> entries);
}
