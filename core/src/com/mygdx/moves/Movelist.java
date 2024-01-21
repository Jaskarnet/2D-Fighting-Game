package com.mygdx.moves;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
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
    private String moveInfoDirectory;
    private String spriteSheetDirectory;
    private AssetManager assetManager;

    public Movelist(Player player, AssetManager assetManager) {
        this.assetManager = assetManager;
        int index = player.ordinal() + 1;
        if (index > 2) index = index - 2;

        // Zdefiniuj ścieżki do katalogów
        moveInfoDirectory = "moves/Fighter" + index + "/moveinfo/";
        spriteSheetDirectory = "moves/Fighter" + index + "/spritesheets/";

        movelist = new ArrayList<>();
        FileHandle assetsFile = Gdx.files.internal("assets.txt");
        String[] assetList = assetsFile.readString().split("\n");

        for (String asset : assetList) {
            asset = asset.replace("\r","");
            //System.out.println(moveInfoDirectory + "(...)" + ".json");
            //System.out.println(asset);
            if (asset.startsWith(moveInfoDirectory) && asset.endsWith(".json")) {
                //System.out.println("git");
                processAsset(asset);
            }
        }
    }

    public void processAsset(String asset) {
        FileHandle file = Gdx.files.internal(asset);

        try {
            TempData moveInfo = new ObjectMapper().readValue(file.read(), TempData.class);
            String jsonFileName = file.name();
            String pngFileName = jsonFileName.replace(".json", ".png");
            String spriteSheetPath = spriteSheetDirectory + pngFileName;

            List<FrameRangeData> frameRangeDataList = moveInfo.getFrameRangeDataList();
            Move move = createMoveFromFrameRangeDataList(frameRangeDataList, spriteSheetPath, assetManager);
            movelist.add(move);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Move createMoveFromFrameRangeDataList(List<FrameRangeData> frameRangeDataList, String spriteSheetPath, AssetManager assetManager) {
        Texture spriteSheet = assetManager.get(spriteSheetPath, Texture.class);
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

    public int getMoveIndex(Move move) {
        return movelist.indexOf(move);
    }

    public ArrayList<Move> getMovelist() {
        return movelist;
    }

    public String getMoveInfoDirectory() {
        return moveInfoDirectory;
    }

    public void setMoveInfoDirectory(String moveInfoDirectory) {
        this.moveInfoDirectory = moveInfoDirectory;
    }

    public String getSpriteSheetDirectory() {
        return spriteSheetDirectory;
    }

    public void setSpriteSheetDirectory(String spriteSheetDirectory) {
        this.spriteSheetDirectory = spriteSheetDirectory;
    }
}
