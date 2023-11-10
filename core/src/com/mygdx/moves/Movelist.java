package com.mygdx.moves;

import com.badlogic.gdx.graphics.Texture;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.commands.Player;
import com.mygdx.utils.json.FrameRangeData;
import com.mygdx.utils.json.TempData;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Movelist {
    private ArrayList<Move> movelist;

    public Movelist(Player player) {
        String moveInfoDirectory = "moves/Fighter1/moveinfo";
        movelist = new ArrayList<>();

        // Uzyskaj listę plików JSON w katalogu moveInfoDirectory
        File folder = new File(moveInfoDirectory);
        System.out.println("moveInfoDirectory absolute path: " + folder.getAbsolutePath());
        File[] files = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".json");
            }
        });

        if (files != null) {
            ObjectMapper objectMapper = new ObjectMapper();

            for (File file : files) {
                try {
                    // Wczytaj JSON z pliku
                    TempData moveInfo = objectMapper.readValue(file, TempData.class);

                    // Odczytaj nazwę pliku JSON i zbuduj odpowiednią ścieżkę do pliku PNG
                    String jsonFileName = file.getName();
                    String pngFileName = jsonFileName.replace(".json", ".png");
                    String spriteSheetPath = "moves/Fighter1/spritesheets/" + pngFileName;

                    List<FrameRangeData> frameRangeDataList = moveInfo.getFrameRangeDataList();

                    // Utwórz nowy ruch na podstawie wczytanych danych i dodaj go do listy ruchów
                    Move move = createMoveFromFrameRangeDataList(frameRangeDataList, spriteSheetPath);
                    movelist.add(move);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Move createMoveFromFrameRangeDataList(List<FrameRangeData> frameRangeDataList, String spriteSheetPath) {
        Texture spriteSheet = new Texture(spriteSheetPath);
        Move move = new Move(spriteSheet, 0);

        for (FrameRangeData frameRangeData : frameRangeDataList) {
            Frame frame = frameRangeData.toFrame(spriteSheet);
            move.addFrame(frame);
        }

        return move;
    }

    public Move getMove(int index) {
        return movelist.get(index);
    }

    public ArrayList<Move> getMovelist() {
        return movelist;
    }
}
