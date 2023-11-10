package com.mygdx.utils.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties("spriteDataList")
public class TempData {
    private String chosenFighterFolder;
    private String moveName;
    private String spritesheetFilePath;
    private List<SpriteData> spriteDataList;
    private List<FrameRangeData> frameRangeDataList;
    private boolean fighterChosen;
    private boolean moveNamed;

    public String getChosenFighterFolder() {
        return chosenFighterFolder;
    }

    public void setChosenFighterFolder(String chosenFighterFolder) {
        this.chosenFighterFolder = chosenFighterFolder;
    }

    public String getMoveName() {
        return moveName;
    }

    public void setMoveName(String moveName) {
        this.moveName = moveName;
    }

    public String getSpritesheetFilePath() {
        return spritesheetFilePath;
    }

    public void setSpritesheetFilePath(String spritesheetFilePath) {
        this.spritesheetFilePath = spritesheetFilePath;
    }

    public List<SpriteData> getSpriteDataList() {
        return spriteDataList;
    }

    public void setSpriteDataList(List<SpriteData> spriteDataList) {
        this.spriteDataList = spriteDataList;
    }

    public List<FrameRangeData> getFrameRangeDataList() {
        return frameRangeDataList;
    }

    public void setFrameRangeDataList(List<FrameRangeData> frameRangeDataList) {
        this.frameRangeDataList = frameRangeDataList;
    }

    public boolean isFighterChosen() {
        return fighterChosen;
    }

    public void setFighterChosen(boolean fighterChosen) {
        this.fighterChosen = fighterChosen;
    }

    public boolean isMoveNamed() {
        return moveNamed;
    }

    public void setMoveNamed(boolean moveNamed) {
        this.moveNamed = moveNamed;
    }
}

