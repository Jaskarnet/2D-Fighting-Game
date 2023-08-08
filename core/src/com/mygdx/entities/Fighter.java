package com.mygdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.commands.Command;
import com.mygdx.commands.InputHandler;
import com.mygdx.commands.Player;
import com.mygdx.moves.Move;

import java.util.Collection;

public class Fighter extends Entity {
    InputHandler inputHandler;
    int health;
    boolean isCrouching;
    Rectangle hurtbox, hitbox;
    Collection<Move> movelist;

    public Fighter(int x, int y, Texture texture, Player player, int leftButton, int rightButton, int undoButton, int commandHistorySize) {
        super(x, y, texture);
        inputHandler = new InputHandler(this, player, leftButton, rightButton, undoButton, commandHistorySize);
        health = 3;
        this.isCrouching = false;
        hurtbox = new Rectangle(x, y, 64, 64);
    }

    public void moveTo(int x, int y) {
        int boundedX = Math.min(Math.max(x, 0), 720);
        int boundedY = Math.min(Math.max(y, 0), 800);
        setX(boundedX);
        setY(boundedY);
        hurtbox.setX(boundedX);
        hurtbox.setY(boundedY);
    }

    @Override
    public void update() {
        Command command = inputHandler.handleInput();
        command.execute();
    }

    public Rectangle getHurtbox() {
        return hurtbox;
    }

    public void setHurtbox(Rectangle hurtbox) {
        this.hurtbox = hurtbox;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }

    public void setInputHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }
}
