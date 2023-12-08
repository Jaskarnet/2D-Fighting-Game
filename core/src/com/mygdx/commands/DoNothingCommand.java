package com.mygdx.commands;

import com.mygdx.entities.Entity;
import com.mygdx.entities.Fighter;
import com.mygdx.entities.State;
import com.mygdx.moves.Frame;
import com.mygdx.moves.Move;

public class DoNothingCommand implements Command {
    private int stateBeforeId, stateId;
    private int moveId;
    private int currentFrame;
    private int xBefore, yBefore;
    private int x, y;

    public DoNothingCommand() {

    }

    public DoNothingCommand(Fighter fighter, State state, Move move, int currentFrame) {
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

    @Override
    public String toString() {
        return "-";
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }
}
