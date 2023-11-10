package com.mygdx.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.entities.Fighter;
import com.mygdx.entities.State;
import com.mygdx.moves.Move;
import com.mygdx.utils.CircularBuffer;

public class InputHandler {
    private Fighter fighter;
    private CircularBuffer<Command> commandHistory;
    private int commandHistorySize;
    private int leftButton, rightButton, undoButton, downButton, attackButton;
    private boolean recordingInput;
    private Player player;

    public InputHandler(Fighter fighter, Player player, int leftButton, int rightButton, int downButton, int attackButton, int commandHistorySize) {
        this.fighter = fighter;
        this.player = player;
        this.commandHistorySize = commandHistorySize;
        commandHistory = new CircularBuffer<>(commandHistorySize);

        for (int i = 0; i < commandHistorySize; i++) {

        }
        this.leftButton = leftButton;
        this.rightButton = rightButton;
        this.downButton = downButton;
        this.attackButton = attackButton;
        this.recordingInput = true;
    }

    public Command handleInput() {
        Command command;
        int currentFrame = fighter.getCurrentFrame();
        currentFrame++;

        if (Gdx.input.isKeyPressed(undoButton)) {
            recordingInput = false;
            System.out.println(commandHistory.toString());
            command = commandHistory.get(commandHistory.popCurrent());
            command.undo();
            return CommandFactory.doNothingCommand();
        }

        // DURING_ATTACK
        if (fighter.getState() == State.HIGH_ATTACK || fighter.getState() == State.MID_ATTACK || fighter.getState() == State.LOW_ATTACK) {
            fighter.setCurrentFrame(currentFrame);
            if (currentFrame < fighter.getMovelist().getMove(fighter.getState().ordinal()).getFrameCount()) {
                if (fighter.getState() == State.HIGH_ATTACK) {
                    command = CommandFactory.AttackCommandHigh(fighter);
                    if (recordingInput) commandHistory.add(command);
                    return command;
                } else if (fighter.getState() == State.MID_ATTACK) {
                    command = CommandFactory.AttackCommandMid(fighter);
                    if (recordingInput) commandHistory.add(command);
                    return command;
                } else if (fighter.getState() == State.LOW_ATTACK) {
                    command = CommandFactory.AttackCommandLow(fighter);
                    if (recordingInput) commandHistory.add(command);
                    return command;
                }
            }
        }


        // STARTING_ATTACK
        if (Gdx.input.isKeyPressed(attackButton)) {
            fighter.setCurrentFrame(0);
            if (Gdx.input.isKeyPressed(rightButton) && player == Player.PLAYER1 || Gdx.input.isKeyPressed(leftButton) && player == Player.PLAYER2) {
                command = CommandFactory.AttackCommandMid(fighter);
                if (recordingInput) commandHistory.add(command);
                return command;
            } else if (Gdx.input.isKeyPressed(downButton)) {
                command = CommandFactory.AttackCommandLow(fighter);
                if (recordingInput) commandHistory.add(command);
                return command;
            } else {
                command = CommandFactory.AttackCommandHigh(fighter);
                if (recordingInput) commandHistory.add(command);
                return command;
            }
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
