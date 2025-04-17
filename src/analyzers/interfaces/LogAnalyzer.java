package src.analyzers.interfaces;

import org.json.JSONObject;
import src.analyzers.types.AnalyzerType;
import src.domain.LogEntry;

import java.util.List;

public interface LogAnalyzer {
    AnalyzerType getName();

    JSONObject analyze(List<LogEntry> entries);
}
