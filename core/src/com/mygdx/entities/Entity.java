package com.mygdx.entities;

import com.badlogic.gdx.graphics.Texture;

public abstract class Entity {
    private int x;
    private int y;
    private Texture texture;

    Entity(int x, int y, Texture texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;
    }

    abstract public void update();

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
