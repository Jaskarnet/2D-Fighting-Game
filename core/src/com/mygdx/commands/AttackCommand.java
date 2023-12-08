package com.mygdx.commands;

import com.mygdx.entities.Entity;
import com.mygdx.entities.Fighter;
import com.mygdx.entities.State;
import com.mygdx.moves.Frame;
import com.mygdx.moves.Move;

public class AttackCommand implements Command {
    private int stateBeforeId, stateId;
    private int moveId;
    private int currentFrame;
    private int xBefore, yBefore;
    private int x, y;

    public AttackCommand() {
    }

    public AttackCommand(Fighter fighter, State state, Move move, int currentFrame) {
        this.stateBeforeId = fighter.getState().ordinal();
        this.stateId = state.ordinal();
        this.moveId = fighter.getMovelist().getMoveIndex(move);
        this.currentFrame = currentFrame;
        Frame frame = move.getFrame(currentFrame);
        this.x = fighter.getX() + frame.getXAxisMovement();
        this.y = fighter.getY() + frame.getYAxisMovement();
        this.xBefore = fighter.getX();
        this.yBefore = fighter.getY();
    }

    @Override
    public void execute(Entity entity) {
        if (entity instanceof Fighter) {
            Fighter fighter = (Fighter) entity;
            fighter.setState(State.fromId(stateId));
            fighter.moveTo(x, y);
        }
    }

    @Override
    public void undo(Entity entity) {
        if (entity instanceof Fighter) {
            Fighter fighter = (Fighter) entity;
            fighter.setState(State.fromId(stateBeforeId));
            fighter.moveTo(xBefore, yBefore);
        }
    }

    public int getXBefore() {
        return xBefore;
    }

    public void setXBefore(int xBefore) {
        this.xBefore = xBefore;
    }

    public int getYBefore() {
        return yBefore;
    }

    public void setYBefore(int yBefore) {
        this.yBefore = yBefore;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }
}
