package com.examples;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JSONToColumnTXTConverter {

    public static void main(String[] args) {
        String jsonFilePath = "input.json"; // Replace with your JSON file path
        String outputFilePath = "output.txt";

        try {
            String json = readJSONFromFile(jsonFilePath);
            JSONObject jsonObject = new JSONObject(json);
            List<String> tsvLines = convertToTSV(jsonObject);

            writeToFile(outputFilePath, tsvLines);

            System.out.println("Conversion completed. Data saved to " + outputFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readJSONFromFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }

        reader.close();
        return jsonBuilder.toString();
    }

    private static List<String> convertToTSV(JSONObject jsonObject) throws JSONException {
        List<String> tsvLines = new ArrayList<>();
        extractData("", jsonObject, tsvLines);
        return tsvLines;
    }

    private static void extractData(String prefix, JSONObject jsonObject, List<String> tsvLines) throws JSONException {
        Iterator<String> keysIterator = jsonObject.keys();
        while (keysIterator.hasNext()) {
            String key = keysIterator.next();
            Object value = jsonObject.get(key);

            if (value instanceof JSONObject) {
                extractData(prefix + key + "_", (JSONObject) value, tsvLines);
            } else if (value instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) value;
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (jsonArray.get(i) instanceof JSONObject) {
                        extractData(prefix + key + i + "_", jsonArray.getJSONObject(i), tsvLines);
                    }
                }
            } else {
                tsvLines.add(prefix + key + "\t" + value.toString());
            }
        }
    }


    private static void writeToFile(String filename, List<String> lines) throws IOException {
        FileWriter writer = new FileWriter(filename);
        for (String line : lines) {
            writer.write(line);
            writer.write("\n");
        }
        writer.close();
    }
}
