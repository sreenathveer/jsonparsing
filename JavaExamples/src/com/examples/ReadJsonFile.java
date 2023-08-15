package com.examples;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadJsonFile {
    public static void main(String[] args) {
        String inputFilePath = "input.json"; // Relative path from project root

        try {
            String jsonContent = readJsonFile(inputFilePath);
            System.out.println("JSON Content:\n" + jsonContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readJsonFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        reader.close();
        return content.toString();
    }
}

