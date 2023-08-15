package com.examples;


import org.json.JSONArray;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TransposeJsonToTxt {
    public static void main(String[] args) {
        String inputFilePath = "input.json"; // Relative path from project root
        String outputFilePath = "output.txt"; // Relative path for the output file

        try {
            String jsonContent = readJsonFile(inputFilePath);
            JSONObject jsonObject = new JSONObject(jsonContent);
            List<String> tsvLines = transposeJSON(jsonObject);

            writeToFile(outputFilePath, tsvLines);

            System.out.println("Transposed data written to " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readJsonFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    private static List<String> transposeJSON(JSONObject jsonObject) {
        List<String> tsvLines = new ArrayList<>();
        StringBuilder keysLine = new StringBuilder();
        StringBuilder valuesLine = new StringBuilder();

        extractData("", jsonObject, keysLine, valuesLine);
        tsvLines.add(keysLine.toString());
        tsvLines.add(valuesLine.toString());

        return tsvLines;
    }

    private static void extractData(String prefix, JSONObject jsonObject, StringBuilder keysLine, StringBuilder valuesLine) {
        Iterator<String> keysIterator = jsonObject.keys();

        while (keysIterator.hasNext()) {
            String key = keysIterator.next();
            Object value = jsonObject.get(key);

            if (value instanceof JSONObject) {
                extractData(prefix + key + ".", (JSONObject) value, keysLine, valuesLine);
            } else if (value instanceof String) {
                keysLine.append(key).append("\t");
                valuesLine.append(value).append("\t");
            } else if (value instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) value;
                StringBuilder arrayValues = new StringBuilder();
                for (int i = 0; i < jsonArray.length(); i++) {
                    Object arrayValue = jsonArray.get(i);
                    if (arrayValue instanceof String) {
                        if (i > 0) {
                            arrayValues.append(", ");
                        }
                        arrayValues.append(arrayValue);
                    }
                }
                keysLine.append(key).append("\t");
                valuesLine.append(arrayValues.toString()).append("\t");
            }
        }
    }

    private static void writeToFile(String filePath, List<String> lines) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}
