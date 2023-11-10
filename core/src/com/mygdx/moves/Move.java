package com.mygdx.moves;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class Move {
    private ArrayList<Frame> frames;
    private Texture spriteSheet;
    private int damage;


    public Move(Texture spriteSheet, int damage) {
        this.spriteSheet = spriteSheet;
        this.frames = new ArrayList<>();
        this.damage = damage;
    }

    public void addFrame(Frame frame) {
        frames.add(frame);
    }

    public Frame getFrame(int index) {
        return frames.get(index);
    }

    public int getFrameCount() {
        return frames.size();
    }

    public Texture getSpriteSheet() {
        return spriteSheet;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
