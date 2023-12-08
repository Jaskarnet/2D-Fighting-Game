package com.mygdx.utils.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Iterator;
import java.util.Map.Entry;

public class MoveFileReader {
    private ObjectMapper objectMapper = new ObjectMapper();

    public List<FrameRangeData> readFrameRangeDataListFromFile(String filePath) {
        try {
            TempData moveData = objectMapper.readValue(new File(filePath), TempData.class);
            return moveData.getFrameRangeDataList();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void reverseJsonDataAndSaveToFile(String filePath) {
        try {
            JsonNode rootNode = objectMapper.readTree(new File(filePath));

            // Odwróć logikę JSON-a
            reverseBoxes(rootNode);
            reverseXAxisMovement(rootNode);

            // Zapisz zmodyfikowany JSON do pliku
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), rootNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void copyAndReverseMoveInfo(String sourceFolder, String destFolder) {
        File sourceDir = new File(sourceFolder);
        File destDir = new File(destFolder);

        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        if (sourceDir.exists() && sourceDir.isDirectory()) {
            File[] files = sourceDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".json")) {
                        try {
                            File destFile = new File(destDir, file.getName());
                            Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            reverseJsonDataAndSaveToFile(destFile.getAbsolutePath());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void reverseBoxes(JsonNode node) {
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;

            Iterator<Entry<String, JsonNode>> fields = objectNode.fields();
            while (fields.hasNext()) {
                Entry<String, JsonNode> entry = fields.next();
                String fieldName = entry.getKey();
                JsonNode fieldValue = entry.getValue();

                if (fieldName.equals("minX")) {
                    double minX = fieldValue.asDouble();
                    double maxX = objectNode.get("maxX").asDouble();

                    objectNode.put("minX", 100.0 - maxX);
                    objectNode.put("maxX", 100.0 - minX);
                }

                reverseBoxes(fieldValue);
            }
        } else if (node.isArray()) {
            for (JsonNode element : node) {
                reverseBoxes(element);
            }
        }
    }

    private void reverseXAxisMovement(JsonNode node) {
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;

            Iterator<Entry<String, JsonNode>> fields = objectNode.fields();
            while (fields.hasNext()) {
                Entry<String, JsonNode> entry = fields.next();
                String fieldName = entry.getKey();
                JsonNode fieldValue = entry.getValue();

                if (fieldName.equals("xAxisMovement")) {
                    double xAxisMovement = fieldValue.asDouble();
                    objectNode.put("xAxisMovement", -xAxisMovement);
                }

                reverseXAxisMovement(fieldValue);
            }
        } else if (node.isArray()) {
            for (JsonNode element : node) {
                reverseXAxisMovement(element);
            }
        }
    }
}
