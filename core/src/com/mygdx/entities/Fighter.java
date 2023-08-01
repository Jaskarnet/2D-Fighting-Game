package com.mygdx.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;

import java.awt.*;

public class Fighter extends Entity{
    boolean isCrouching;

    public Fighter(int x, int y, Texture texture) {
        super(x, y, texture);
        isCrouching = false;
    }

    @Override
    public void update() {
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) setX(getX() - (int) (200 * Gdx.graphics.getDeltaTime()));
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) setX(getX() + (int) (200 * Gdx.graphics.getDeltaTime()));
    }
}
