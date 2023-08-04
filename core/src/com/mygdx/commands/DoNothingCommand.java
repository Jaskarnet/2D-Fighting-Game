package com.mygdx.commands;

public class DoNothingCommand implements Command {
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
