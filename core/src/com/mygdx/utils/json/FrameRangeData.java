package com.mygdx.utils.json;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.moves.Frame;

import java.util.ArrayList;
import java.util.List;

public class FrameRangeData {
    private int startFrame, endFrame, xAxisMovement, yAxisMovement;
    private SpriteData spriteData;

    public FrameRangeData(int startFrame, int endFrame, int xAxisMovement, int yAxisMovement, SpriteData spriteData) {
        this.startFrame = startFrame;
        this.endFrame = endFrame;
        this.xAxisMovement = xAxisMovement;
        this.yAxisMovement = yAxisMovement;
        this.spriteData = spriteData;
    }

    public FrameRangeData() {

    }

    public int getStartFrame() {
        return startFrame;
    }

    public void setStartFrame(int startFrame) {
        this.startFrame = startFrame;
    }

    public int getEndFrame() {
        return endFrame;
    }

    public void setEndFrame(int endFrame) {
        this.endFrame = endFrame;
    }

    public int getxAxisMovement() {
        return xAxisMovement;
    }

    public void setxAxisMovement(int xAxisMovement) {
        this.xAxisMovement = xAxisMovement;
    }

    public int getyAxisMovement() {
        return yAxisMovement;
    }

    public void setyAxisMovement(int yAxisMovement) {
        this.yAxisMovement = yAxisMovement;
    }

    public SpriteData getSpriteData() {
        return spriteData;
    }

    public void setSpriteData(SpriteData spriteData) {
        this.spriteData = spriteData;
    }

    public Frame toFrame(Texture spriteSheet) {
        List<Box> hurtboxes = new ArrayList<>(spriteData.getHurtboxes());
        List<Box> hitboxes = new ArrayList<>(spriteData.getHitboxes());
        return new Frame(spriteSheet, spriteData.getIndex(), xAxisMovement, yAxisMovement, hurtboxes, hitboxes);
    }

}
