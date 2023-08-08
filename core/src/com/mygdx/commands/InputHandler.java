package com.mygdx.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.entities.Fighter;
import com.mygdx.utils.CircularBuffer;

public class InputHandler {
    private Fighter fighter;
    private CircularBuffer<Command> commandHistory;
    private int commandHistorySize;
    private int leftButton, rightButton, undoButton;
    private boolean recordingInput;

    public InputHandler(Fighter fighter, Player player, int leftButton, int rightButton, int undoButton, int commandHistorySize) {
        this.fighter = fighter;
        this.commandHistorySize = commandHistorySize;
        commandHistory = new CircularBuffer<>(commandHistorySize);

        for (int i = 0; i < commandHistorySize; i++) {

        }
        this.leftButton = leftButton;
        this.rightButton = rightButton;
        this.undoButton = undoButton;
        this.recordingInput = true;
    }

    public Command handleInput() {
        Command command;

        if (Gdx.input.isKeyPressed(undoButton)) {
            recordingInput = false;
            System.out.println(commandHistory.toString());
            command = commandHistory.get(commandHistory.popCurrent());
            command.undo();
            return CommandFactory.doNothingCommand();
        }

        if (Gdx.input.isKeyPressed(leftButton) && Gdx.input.isKeyPressed(rightButton)) {
            command = CommandFactory.doNothingCommand();
            if (recordingInput) commandHistory.add(command);
            return command;
        } else if (Gdx.input.isKeyPressed(leftButton)) {
            recordingInput = true;
            command = CommandFactory.moveFighterCommandLeft(fighter, fighter.getX(), fighter.getY());
            if (recordingInput) commandHistory.add(command);
            return command;
        } else if (Gdx.input.isKeyPressed(rightButton)) {
            recordingInput = true;
            command = CommandFactory.moveFighterCommandRight(fighter, fighter.getX(), fighter.getY());
            if (recordingInput) commandHistory.add(command);
            return command;
        }
        command = CommandFactory.doNothingCommand();
        if (recordingInput) commandHistory.add(command);
        return command;
    }

    public Fighter getFighter() {
        return this.fighter;
    }

    public void setFighter(Fighter fighter) {
        this.fighter = fighter;
    }

    public int getLeftButton() {
        return leftButton;
    }

    public void setLeftButton(int leftButton) {
        this.leftButton = leftButton;
    }

    public int getRightButton() {
        return rightButton;
    }

    public void setRightButton(int rightButton) {
        this.rightButton = rightButton;
    }

    public boolean isRecordingInput() {
        return recordingInput;
    }

    public void setRecordingInput(boolean recordingInput) {
        this.recordingInput = recordingInput;
    }

    public CircularBuffer<Command> getCommandHistory() {
        return commandHistory;
    }

    public void setCommandHistory(CircularBuffer<Command> commandHistory) {
        this.commandHistory = commandHistory;
    }
}
