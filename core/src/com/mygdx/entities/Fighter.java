package com.mygdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.commands.*;
import com.mygdx.moves.Frame;
import com.mygdx.moves.Move;
import com.mygdx.moves.Movelist;
import com.mygdx.engine.Collision;

import java.util.ArrayList;
import java.util.List;

import static com.mygdx.engine.Collision.getDirection;

public class Fighter extends Entity {
    InputHandler inputHandler;
    // state bedzie mogl sie zmienić po collision.update() w głównej pętli i bedzie trzeba dodać animation.update
    State state;
    private int currentFrame;
    int health;
    boolean isCrouching;
    List<Rectangle> hurtboxes, hitboxes;
    Movelist movelist;
    Player player;

    public Fighter(int x, int y, Player player, int leftButton, int rightButton, int downButton, int attackButton, int commandHistorySize) {
        super(x, y);
        inputHandler = new InputHandler(this, player, leftButton, rightButton, downButton, attackButton, commandHistorySize);
        this.player = player;
        health = 3;
        this.isCrouching = false;
        movelist = new Movelist(player);
        hurtboxes = movelist.getMove(0).getFrame(0).getHurtboxes();
        hitboxes = movelist.getMove(0).getFrame(0).getHitboxes();
        state = State.NEUTRAL;
        setTextureRegion(movelist.getMove(state.ordinal()).getFrame(currentFrame).getSprite());
    }

    public void moveTo(int x, int y) {
        int boundedX = Math.min(Math.max(x, 0), 720);
        int boundedY = Math.min(Math.max(y, 0), 800);
        setX(boundedX);
        setY(boundedY);
    }

/*    public void playAnimation(int move, int frame) {
        if (move == this.currentMove)  {
            this.currentFrame = this.currentFrame + 1;
        } else if (state == State.CANCELABLE) {
            this.currentMove = move;
            this.currentFrame = 0;
        } else {

        }
    }*/

    void updateState(Command command) {
        Direction direction = getDirection(command);
        if (direction == Direction.LEFT) System.out.println("left");
        if (direction == Direction.RIGHT) System.out.println("right");


        //NEUTRAL
        if (command instanceof DoNothingCommand) {
            if (state == State.NEUTRAL) {
                currentFrame++;
            } else if (state == State.GOING_BACK || state == State.GOING_FORWARD) {
                state = State.NEUTRAL;
                currentFrame = 0;
            }
        }


        //MOVE_FORWARD & MOVE_BACKWARD
        if (command instanceof MoveFighterCommand) {
            if ((player == Player.PLAYER1 && direction == Direction.RIGHT) || (player == Player.PLAYER2 && direction == Direction.LEFT)) {
                if (state == State.GOING_FORWARD) {
                    currentFrame++;
                } else if (state == State.NEUTRAL || state == State.GOING_BACK) {
                    state = State.GOING_FORWARD;
                    currentFrame = 0;
                }
            } else if ((player == Player.PLAYER1 && direction == Direction.LEFT) || (player == Player.PLAYER2 && direction == Direction.RIGHT)) {
                if (state == State.GOING_BACK) {
                    currentFrame++;
                } else if (state == State.NEUTRAL || state == State.GOING_FORWARD) {
                    state = State.GOING_BACK;
                    currentFrame = 0;
                }
            }
        }

        //ATTACK
        if (command instanceof AttackCommand) {
          /*  if (state == State.NEUTRAL || state == State.CROUCHING || state == State.GOING_BACK || state == State.GOING_FORWARD) {

            }*/
        }


        //
        if (currentFrame >= movelist.getMove(state.ordinal()).getFrameCount()) {
            state = State.NEUTRAL;
            currentFrame = 0;
        }

        setTextureRegion(movelist.getMove(state.ordinal()).getFrame(currentFrame).getSprite());

        setHurtboxes(adjustBoxesForFighterPosition(movelist.getMove(state.ordinal()).getFrame(currentFrame).getHurtboxes()));

        setHitboxes(adjustBoxesForFighterPosition(movelist.getMove(state.ordinal()).getFrame(currentFrame).getHitboxes()));
    }

    @Override
    public void update() {
        Command command = inputHandler.handleInput();
        command.execute();
        updateState(command);
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
        this.health = health;
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

    public boolean isCrouching() {
        return isCrouching;
    }

    public void setCrouching(boolean crouching) {
        isCrouching = crouching;
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
}
