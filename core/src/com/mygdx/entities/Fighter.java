package com.mygdx.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.commands.Direction;
import com.mygdx.moves.Move;

import java.awt.*;
import java.util.Collection;

public class Fighter extends Entity{
    boolean isCrouching;
    Collection<Move> movelist;

    public Fighter(int x, int y, Texture texture) {
        super(x, y, texture);
        this.isCrouching = false;
    }

    public void moveTo(int x, int y) {
        setX(x);
        setY(y);
    }

    @Override
    public void update() {

    }
}
