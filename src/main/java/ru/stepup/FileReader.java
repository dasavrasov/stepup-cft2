package ru.stepup;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class FileReader {
    @Value("${file.path}")
    private String filePath;
    public List<String> readFile() {
        List<String> lines = new ArrayList<>();
        try {
            File folder = new File(filePath);
            if (folder.isDirectory()) {
                for (File file : folder.listFiles()) {
                    Scanner scanner = new Scanner(file);
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        lines.add(line);
                    }
                    scanner.close();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return lines;
    }
}