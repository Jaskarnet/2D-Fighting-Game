package com.mygdx.commands;

import com.mygdx.entities.Fighter;

public class DoNothingCommand implements Command {

    public DoNothingCommand() {
    }

    @Override
    public void execute() {

    }

    @Override
    public void undo() {
    }

    @Override
    public String toString() {
        return "-";
    }
}
