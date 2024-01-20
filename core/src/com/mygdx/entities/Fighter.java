package com.mygdx.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.esotericsoftware.kryo.Kryo;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.commands.*;
import com.mygdx.game.GameState;
import com.mygdx.game.Multiplayer;
import com.mygdx.moves.Frame;
import com.mygdx.moves.Move;
import com.mygdx.moves.Movelist;
import com.mygdx.engine.Collision;

import java.util.ArrayList;
import java.util.List;

import static com.mygdx.engine.Collision.getDirection;

public class Fighter extends Entity {
    int initialX, initialY;
    InputHandler inputHandler;
    State state;
    int health, maxHealth;
    private int currentFrame;
    boolean isBlockStunnedHigh, isBlockStunnedMid, isBlockStunnedLow;
    boolean isHitStunnedHigh, isHitStunnedMid, isHitStunnedLow;
    List<Rectangle> hurtboxes, hitboxes;
    Movelist movelist;
    Player player;
    Multiplayer multiplayer;
    int roundsWon;
    AssetManager assetManager;

    public Fighter(int x, int y, Player player, int backwardButton, int forwardButton, int crouchButton, int attackButton, int commandHistorySize, AssetManager assetManager) {
        super(x, y);
        this.assetManager = assetManager;
        this.initialX = x;
        this.initialY = y;
        this.roundsWon = 0;
        inputHandler = new InputHandler(this, player, forwardButton, backwardButton, crouchButton, attackButton, commandHistorySize);
        this.player = player;
        health = 3;
        maxHealth = 3;
        this.isBlockStunnedHigh = false;
        this.isBlockStunnedMid = false;
        this.isBlockStunnedLow = false;
        this.isHitStunnedHigh = false;
        this.isHitStunnedMid = false;
        this.isHitStunnedLow = false;
        movelist = new Movelist(player, assetManager);
        movelist.getMove(State.HIGH_ATTACK.getId()).setDamage(3);
        movelist.getMove(State.MID_ATTACK.getId()).setDamage(2);
        movelist.getMove(State.LOW_ATTACK.getId()).setDamage(1);
        hurtboxes = movelist.getMove(0).getFrame(0).getHurtboxes();
        hitboxes = movelist.getMove(0).getFrame(0).getHitboxes();
        state = State.NEUTRAL;
        setTextureRegion(movelist.getMove(state.ordinal()).getFrame(currentFrame).getSprite());
    }

    public void moveTo(int x, int y) {
        if (player == Player.PLAYER1 || player == Player.ONLINE_PLAYER1) {
            int boundedX = Math.min(Math.max(x, -100), 900);
            int boundedY = Math.min(Math.max(y, 0), 800);
            setX(boundedX);
            setY(boundedY);
        } else if (player == Player.PLAYER2 || player == Player.ONLINE_PLAYER2) {
            int boundedX = Math.min(Math.max(x, 0), 1000);
            int boundedY = Math.min(Math.max(y, 0), 800);
            setX(boundedX);
            setY(boundedY);
        }
    }

    public void updateAnimation() {
        if (currentFrame >= movelist.getMove(state.ordinal()).getFrameCount()) currentFrame = 0;
        setTextureRegion(movelist.getMove(state.ordinal()).getFrame(currentFrame).getSprite());
        setHurtboxes(adjustBoxesForFighterPosition(movelist.getMove(state.ordinal()).getFrame(currentFrame).getHurtboxes()));
        setHitboxes(adjustBoxesForFighterPosition(movelist.getMove(state.ordinal()).getFrame(currentFrame).getHitboxes()));
    }

    @Override
    public void update() {
        Command command = inputHandler.handleInput();
        command.execute(this);
        if (multiplayer != null && (player == Player.PLAYER1 || player == Player.PLAYER2)) {
            multiplayer.sendCommand(command);
        }
        updateAnimation();
    }

    public void updateIdle() {
        state = State.NEUTRAL;
        currentFrame++;
        updateAnimation();
    }

    public void updateRoundEnd() {
        if (health <= 0) {
            currentFrame++;
            int maxFrame = movelist.getMove(state.getId()).getFrameCount();
            if (currentFrame < maxFrame) {
                int xAxisMovement = movelist.getMove(state.getId()).getFrame(currentFrame).getXAxisMovement();
                int yAxisMovement = movelist.getMove(state.getId()).getFrame(currentFrame).getYAxisMovement();
                this.moveTo(this.getX() + xAxisMovement, this.getY() + yAxisMovement);
                updateAnimation();
            }
        } else {
            update();
        }
    }

    public void resetToDefault() {
        currentFrame = 0;
        health = 3;
        this.moveTo(initialX, initialY);
        isBlockStunnedHigh = false;
        isBlockStunnedMid = false;
        isBlockStunnedLow = false;
        isHitStunnedHigh = false;
        isHitStunnedMid = false;
        isHitStunnedLow = false;
        hurtboxes = movelist.getMove(0).getFrame(0).getHurtboxes();
        hitboxes = movelist.getMove(0).getFrame(0).getHitboxes();
        state = State.NEUTRAL;
        setTextureRegion(movelist.getMove(state.ordinal()).getFrame(currentFrame).getSprite());
        if (multiplayer != null && (player == Player.PLAYER1 || player == Player.PLAYER2)) multiplayer.sendCommand(new MoveFighterCommand(this, State.NEUTRAL, movelist.getMove(0), 0));
    }

    public void resetToInitial() {
        currentFrame = 0;
        health = 3;
        this.moveTo(initialX, initialY);
        isBlockStunnedHigh = false;
        isBlockStunnedMid = false;
        isBlockStunnedLow = false;
        isHitStunnedHigh = false;
        isHitStunnedMid = false;
        isHitStunnedLow = false;
        hurtboxes = movelist.getMove(0).getFrame(0).getHurtboxes();
        hitboxes = movelist.getMove(0).getFrame(0).getHitboxes();
        state = State.NEUTRAL;
        setTextureRegion(movelist.getMove(state.ordinal()).getFrame(currentFrame).getSprite());
        roundsWon = 0;
    }

    private List<Rectangle> adjustBoxesForFighterPosition(List<Rectangle> boxes) {
        List<Rectangle> newBoxes = new ArrayList<>();
        for (Rectangle box : boxes) {
            Rectangle newBox = new Rectangle(box.getX() + this.getX(), box.getY() + this.getY(), box.width, box.height);
            newBoxes.add(newBox);
        }
        return newBoxes;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        if (health < 0) {
            this.health = 0;
        } else if (health > 3) {
            this.health = 3;
        } else {
            this.health = health;
        }
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }

    public void setInputHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Movelist getMovelist() {
        return movelist;
    }

    public void setMovelist(Movelist movelist) {
        this.movelist = movelist;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        this.inputHandler.setPlayer(player);
    }

    public List<Rectangle> getHurtboxes() {
        return hurtboxes;
    }

    public void setHurtboxes(List<Rectangle> hurtboxes) {
        this.hurtboxes = hurtboxes;
    }

    public List<Rectangle> getHitboxes() {
        return hitboxes;
    }

    public void setHitboxes(List<Rectangle> hitboxes) {
        this.hitboxes = hitboxes;
    }

    public boolean isBlockStunnedHigh() {
        return isBlockStunnedHigh;
    }

    public void setBlockStunnedHigh(boolean blockStunnedHigh) {
        isBlockStunnedHigh = blockStunnedHigh;
    }

    public boolean isBlockStunnedMid() {
        return isBlockStunnedMid;
    }

    public void setBlockStunnedMid(boolean blockStunnedMid) {
        isBlockStunnedMid = blockStunnedMid;
    }

    public boolean isBlockStunnedLow() {
        return isBlockStunnedLow;
    }

    public void setBlockStunnedLow(boolean blockStunnedLow) {
        isBlockStunnedLow = blockStunnedLow;
    }

    public boolean isHitStunnedHigh() {
        return isHitStunnedHigh;
    }

    public void setHitStunnedHigh(boolean hitStunnedHigh) {
        isHitStunnedHigh = hitStunnedHigh;
    }

    public boolean isHitStunnedMid() {
        return isHitStunnedMid;
    }

    public void setHitStunnedMid(boolean hitStunnedMid) {
        isHitStunnedMid = hitStunnedMid;
    }

    public boolean isHitStunnedLow() {
        return isHitStunnedLow;
    }

    public void setHitStunnedLow(boolean hitStunnedLow) {
        isHitStunnedLow = hitStunnedLow;
    }

    public Multiplayer getMultiplayer() {
        return multiplayer;
    }

    public void setMultiplayer(Multiplayer multiplayer) {
        this.multiplayer = multiplayer;
        this.inputHandler.setMultiplayer(multiplayer);
    }

    public int getRoundsWon() {
        return roundsWon;
    }

    public void setRoundsWon(int roundsWon) {
        this.roundsWon = roundsWon;
    }

    public float getMaxHealth() {
        return maxHealth;
    }
}
