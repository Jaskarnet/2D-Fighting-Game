package com.mygdx.utils.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MoveFileReader {
    public List<FrameRangeData> readFrameRangeDataListFromFile(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            TempData moveData = objectMapper.readValue(new File(filePath), TempData.class);
            return moveData.getFrameRangeDataList();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
