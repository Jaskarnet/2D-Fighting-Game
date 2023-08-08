package com.mygdx.commands;

import com.mygdx.entities.Fighter;

public class MoveFighterCommand implements Command {
    private Fighter fighter;
    int xBefore, yBefore;
    int x, y;

    public MoveFighterCommand(Fighter fighter, int x, int y) {
        this.fighter = fighter;
        this.xBefore = 0;
        this.yBefore = 0;
        this.x = x;
        this.y = y;
    }


    @Override
    public void execute() {
        xBefore = this.fighter.getX();
        yBefore = this.fighter.getY();
        fighter.moveTo(x, y);
    }

    @Override
    public void undo() {
        System.out.println("undoing: moving from x=" + x + " to xBefore=" + xBefore);
        fighter.moveTo(xBefore, yBefore);
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
}
