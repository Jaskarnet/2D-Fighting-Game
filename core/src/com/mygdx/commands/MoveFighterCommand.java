package com.mygdx.commands;

import com.mygdx.entities.Entity;
import com.mygdx.entities.Fighter;
import com.mygdx.entities.State;
import com.mygdx.moves.Frame;
import com.mygdx.moves.Move;

public class MoveFighterCommand implements Command {
    private int stateBeforeId, stateId;
    private int moveId;
    private int currentFrame;
    private int xBefore, yBefore;
    private int x, y;
    private int frameCount;

    public MoveFighterCommand() {
    }

    public MoveFighterCommand(Fighter fighter, State state, Move move, int currentFrame) {
        this.stateBeforeId = fighter.getState().ordinal();
        this.stateId = state.ordinal();
        this.moveId = fighter.getMovelist().getMoveIndex(move);
        this.currentFrame = currentFrame;
        Frame frame = move.getFrame(currentFrame);
        this.x = fighter.getX() + frame.getXAxisMovement();
        this.y = fighter.getY() + frame.getYAxisMovement();
        this.xBefore = fighter.getX();
        this.yBefore = fighter.getY();
        this.frameCount = move.getFrameCount();
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

    @Override
    public String toString() {
        return "M";
    }

    public int getXBefore() {
        return xBefore;
    }

    public int getYBefore() {
        return yBefore;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }
    public int getFrameCount() {
        return frameCount;
    }
}
