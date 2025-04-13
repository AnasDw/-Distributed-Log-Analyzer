package src.providers;

import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Writes the final JSON report to a file.
 */
public class JsonReportWriterService {

    public void write(String outputPath, JSONObject report) {
        try (FileWriter fileWriter = new FileWriter(outputPath)) {
            fileWriter.write(report.toString(4)); // pretty-print
            System.out.println("✅ Report saved to " + outputPath);
        } catch (IOException e) {
            System.out.println("error saving report");
        }
    }
}
