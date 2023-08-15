package com.examples;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TransposeJsonFromProjectRoot {
    public static void main(String[] args) {
        String inputFilePath = "input.json"; // Relative path from project root
        String outputFilePath = "output.txt"; // Relative path for the output file

        try {
            JSONObject jsonObject = new JSONObject(new FileReader(inputFilePath));
            List<String> tsvLines = transposeJSON(jsonObject);

            writeToFile(outputFilePath, tsvLines);

            System.out.println("Transposed data written to " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> transposeJSON(JSONObject jsonObject) {
        List<String> tsvLines = new ArrayList<>();
        JSONArray keysArray = jsonObject.names();
        
        if (keysArray != null) {
            List<String> keys = new ArrayList<>();
            for (int i = 0; i < keysArray.length(); i++) {
            	 try {
					if (keysArray.get(i) instanceof String) {
					     try {
							keys.add(keysArray.getString(i));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					 }
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }

        // Add header row with keys
        StringBuilder header = new StringBuilder("Keys\t");
        for (String key : keys) {
            header.append(key).append("\t");
        }
        tsvLines.add(header.toString());

        // Find the maximum number of values for a key
        int maxValues = 0;
        for (String key : keys) {
            Object value = null;
			try {
				value = jsonObject.get(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            if (value instanceof JSONObject) {
                JSONObject subObject = (JSONObject) value;
                maxValues = Math.max(maxValues, subObject.length());
            } else if (value instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) value;
                maxValues = Math.max(maxValues, jsonArray.length());
            } else {
                maxValues = Math.max(maxValues, 1);
            }
        }

        // Add values row by row
        for (int row = 0; row < maxValues; row++) {
            StringBuilder rowBuilder = new StringBuilder(row + "\t");
            for (String key : keys) {
                Object value = null;
				try {
					value = jsonObject.get(key);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                if (value instanceof JSONObject) {
                    JSONObject subObject = (JSONObject) value;
                    if (row < subObject.length()) {
                        try {
							rowBuilder.append(subObject.get(subObject.names().getString(row))).append("\t");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    } else {
                        rowBuilder.append("\t");
                    }
                } else if (value instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) value;
                    if (row < jsonArray.length()) {
                        try {
							rowBuilder.append(jsonArray.get(row)).append("\t");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    } else {
                        rowBuilder.append("\t");
                    }
                } else {
                    rowBuilder.append(value).append("\t");
                }
            }
            tsvLines.add(rowBuilder.toString());
        }
        } else {
            System.out.println("No keys found in the JSON object.");
        }

        return tsvLines;
    }

    private static void writeToFile(String filePath, List<String> lines) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        for (String line : lines) {
            writer.write(line);
            writer.newLine();
        }
        writer.close();
    }
}


