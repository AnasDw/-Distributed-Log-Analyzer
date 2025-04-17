package src.services;

import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Responsible for writing the final JSON analysis report to a file.
 */
public class JsonReportWriterService {

    /**
     * Writes the given JSON report to the specified file path.
     * The output is pretty-printed for readability.
     *
     * @param outputPath the path to the output file (e.g., "report.json")
     * @param report     the JSON object to write
     */
    public void write(String outputPath, JSONObject report) {
        try (FileWriter fileWriter = new FileWriter(outputPath)) {
            fileWriter.write(report.toString(4));
            System.out.println("✅ Report saved to: " + outputPath);
        } catch (IOException e) {
            System.out.println("❌ Failed to save report to: " + outputPath);
            System.out.println("   Reason: " + e.getMessage());
        }
    }
}
