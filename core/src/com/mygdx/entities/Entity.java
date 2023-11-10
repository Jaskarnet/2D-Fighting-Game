package com.mygdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Entity {
    private int x;
    private int y;
    private TextureRegion textureRegion;

    Entity(int x, int y, TextureRegion textureRegion) {
        this.x = x;
        this.y = y;
        this.textureRegion = textureRegion;
    }

    Entity(int x, int y) {
        this.x = x;
        this.y = y;
    }

    abstract public void update();

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }
}
