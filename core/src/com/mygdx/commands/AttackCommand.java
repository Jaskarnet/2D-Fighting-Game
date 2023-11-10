package com.mygdx.commands;

import com.mygdx.entities.Fighter;
import com.mygdx.entities.State;
import com.mygdx.moves.Frame;
import com.mygdx.moves.Move;

public class AttackCommand implements Command {
    private Fighter fighter;
    State stateBefore, state;
    Move move;
    Frame frame;
    int currentFrame;
    int xBefore, yBefore;
    int x, y;

    public AttackCommand(Fighter fighter, State state, Move move, int currentFrame) {
        this.fighter = fighter;
        this.stateBefore = fighter.getState();
        this.state = state;
        this.move = move;
        this.currentFrame = currentFrame;
        this.frame = move.getFrame(currentFrame);
        this.x = fighter.getX() + frame.getXAxisMovement();
        this.y = fighter.getY() + frame.getYAxisMovement();
        this.xBefore = fighter.getX();
        this.yBefore = fighter.getY();
    }

    @Override
    public void execute() {
        fighter.setState(state);
        fighter.moveTo(x, y);
    }

    @Override
    public void undo() {
        fighter.setState(stateBefore);
        fighter.moveTo(xBefore, yBefore);
    }
}
