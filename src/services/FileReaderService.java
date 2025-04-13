package src.services;

import src.exceptions.InvalidLogPathException;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * FileReaderService is responsible for:
 * - Validating the existence of the log directory
 * - Retrieving all .log files inside the given directory
 */
public class FileReaderService {

    /**
     * Gets all .log files from a valid directory.
     * If the directory is invalid, prints an error and exits (per assignment spec).
     *
     * @param directoryPath Path to the log directory
     * @return List of .log files
     */
    public List<File> getLogFiles(String directoryPath) {
        File directory = new File(directoryPath);

        // Validate the directory
        if (!directory.exists() || !directory.isDirectory()) {
            throw new InvalidLogPathException("Directory not found or is not a directory.");
        }

        // Filter for .log files only
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".log"));

        List<File> logFiles = new ArrayList<>();
        if (files != null) {
            Collections.addAll(logFiles, files);
        }

        System.out.println("📄 Found " + logFiles.size() + " log files.");
        return logFiles;
    }
}

