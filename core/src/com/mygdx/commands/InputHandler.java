package com.mygdx.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.engine.Collision;
import com.mygdx.entities.Fighter;
import com.mygdx.entities.State;
import com.mygdx.game.GameState;
import com.mygdx.game.Multiplayer;
import com.mygdx.moves.Move;
import com.mygdx.utils.CircularBuffer;

import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class InputHandler {
    private Fighter fighter;
    private CircularBuffer<Command> commandHistory;
    private int commandHistorySize;
    private int forwardButton, backwardButton, undoButton, crouchButton, attackButton;
    private boolean recordingInput;
    private Player player;
    private Multiplayer multiplayer;



    public InputHandler(Fighter fighter, Player player, int forwardButton, int backwardButton, int crouchButton, int attackButton, int commandHistorySize) {
        this.fighter = fighter;
        this.player = player;
        this.commandHistorySize = commandHistorySize;
        commandHistory = new CircularBuffer<>(commandHistorySize);

        for (int i = 0; i < commandHistorySize; i++) {

        }
        this.forwardButton = forwardButton;
        this.backwardButton = backwardButton;
        this.crouchButton = crouchButton;
        this.attackButton = attackButton;
        this.recordingInput = true;

    }

    public Command handleInput() {
        int currentFrame = fighter.getCurrentFrame();
        Command command = null;

        // Online
        if (player == Player.ONLINE_PLAYER1 || player == Player.ONLINE_PLAYER2) {
            if (multiplayer.getCommandQueue().size() > 0) {
                command = multiplayer.getCommandQueue().getLast();
                if (command instanceof HitStunCommand) {
                    currentFrame = ((HitStunCommand) command).getCurrentFrame();
                } else if (command instanceof BlockStunCommand) {
                    currentFrame = ((BlockStunCommand) command).getCurrentFrame();
                } else if (command instanceof AttackCommand) {
                    currentFrame = ((AttackCommand) command).getCurrentFrame();
                } else if (command instanceof DoNothingCommand) {
                    currentFrame = ((DoNothingCommand) command).getCurrentFrame();
                } else if (command instanceof CrouchCommand) {
                    currentFrame = ((CrouchCommand) command).getCurrentFrame();
                } else if (command instanceof MoveFighterCommand) {
                    currentFrame = ((MoveFighterCommand) command).getCurrentFrame();
                }

                fighter.setCurrentFrame(currentFrame);
                if (recordingInput) commandHistory.add(command);
                return command;
            }
        }

        currentFrame = fighter.getCurrentFrame() + 1;
        /*if (Gdx.input.isKeyPressed(undoButton)) {
            recordingInput = false;
            //System.out.println(commandHistory.toString());
            command = commandHistory.get(commandHistory.popCurrent());
            command.undo();
            return CommandFactory.doNothingCommand(fighter);
        }*/

        // GETTING_HIT_HIGH
        if (fighter.isHitStunnedHigh()) {
            if (fighter.getState() != State.HIT_STUNNED_HIGH) {
                fighter.setCurrentFrame(0);
                command = CommandFactory.hitStunCommandHigh(fighter);
                if (recordingInput) commandHistory.add(command);
                return command;
            } else if (fighter.getState() == State.HIT_STUNNED_HIGH && currentFrame >= fighter.getMovelist().getMove(State.HIT_STUNNED_HIGH.ordinal()).getFrameCount()) {
                fighter.setHitStunnedHigh(false);
            } else {
                fighter.setCurrentFrame(currentFrame);
                command = CommandFactory.hitStunCommandHigh(fighter);
                if (recordingInput) commandHistory.add(command);
                return command;
            }
        }

        // GETTING_HIT_MID
        if (fighter.isHitStunnedMid()) {
            if (fighter.getState() != State.HIT_STUNNED_MID) {
                fighter.setCurrentFrame(0);
                command = CommandFactory.hitStunCommandMid(fighter);
                if (recordingInput) commandHistory.add(command);
                return command;
            } else if (fighter.getState() == State.HIT_STUNNED_MID && currentFrame >= fighter.getMovelist().getMove(State.HIT_STUNNED_MID.ordinal()).getFrameCount()) {
                fighter.setHitStunnedMid(false);
            } else {
                fighter.setCurrentFrame(currentFrame);
                command = CommandFactory.hitStunCommandMid(fighter);
                if (recordingInput) commandHistory.add(command);
                return command;
            }
        }

        // GETTING_HIT_LOW
        if (fighter.isHitStunnedLow()) {
            if (fighter.getState() != State.HIT_STUNNED_LOW) {
                fighter.setCurrentFrame(0);
                command = CommandFactory.hitStunCommandLow(fighter);
                if (recordingInput) commandHistory.add(command);
                return command;
            } else if (fighter.getState() == State.HIT_STUNNED_LOW && currentFrame >= fighter.getMovelist().getMove(State.HIT_STUNNED_LOW.ordinal()).getFrameCount()) {
                fighter.setHitStunnedLow(false);
            } else {
                fighter.setCurrentFrame(currentFrame);
                command = CommandFactory.hitStunCommandLow(fighter);
                if (recordingInput) commandHistory.add(command);
                return command;
            }
        }

        // BLOCKING_HIGH
        if (fighter.isBlockStunnedHigh()) {
            if (fighter.getState() != State.BLOCK_STUNNED_HIGH) {
                fighter.setCurrentFrame(0);
                command = CommandFactory.blockStunCommandHigh(fighter);
                if (recordingInput) commandHistory.add(command);
                return command;
            } else if (fighter.getState() == State.BLOCK_STUNNED_HIGH && currentFrame >= fighter.getMovelist().getMove(State.BLOCK_STUNNED_HIGH.ordinal()).getFrameCount()) {
                fighter.setBlockStunnedHigh(false);
            } else {
                fighter.setCurrentFrame(currentFrame);
                command = CommandFactory.blockStunCommandHigh(fighter);
                if (recordingInput) commandHistory.add(command);
                return command;
            }
        }

        // BLOCKING_MID
        if (fighter.isBlockStunnedMid()) {
            if (fighter.getState() != State.BLOCK_STUNNED_MID) {
                fighter.setCurrentFrame(0);
                command = CommandFactory.blockStunCommandMid(fighter);
                if (recordingInput) commandHistory.add(command);
                return command;
            } else if (fighter.getState() == State.BLOCK_STUNNED_MID && currentFrame >= fighter.getMovelist().getMove(State.BLOCK_STUNNED_MID.ordinal()).getFrameCount()) {
                fighter.setBlockStunnedMid(false);
            } else {
                fighter.setCurrentFrame(currentFrame);
                command = CommandFactory.blockStunCommandMid(fighter);
                if (recordingInput) commandHistory.add(command);
                return command;
            }
        }

        // BLOCKING_LOW
        if (fighter.isBlockStunnedLow()) {
            if (fighter.getState() != State.BLOCK_STUNNED_LOW) {
                fighter.setCurrentFrame(0);
                command = CommandFactory.blockStunCommandLow(fighter);
                if (recordingInput) commandHistory.add(command);
                return command;
            } else if (fighter.getState() == State.BLOCK_STUNNED_LOW && currentFrame >= fighter.getMovelist().getMove(State.BLOCK_STUNNED_LOW.ordinal()).getFrameCount()) {
                fighter.setBlockStunnedLow(false);
            } else {
                fighter.setCurrentFrame(currentFrame);
                command = CommandFactory.blockStunCommandLow(fighter);
                if (recordingInput) commandHistory.add(command);
                return command;
            }
        }

        // ATTACK
        if (fighter.getState() == State.HIGH_ATTACK || fighter.getState() == State.MID_ATTACK || fighter.getState() == State.LOW_ATTACK) {
            int frameCount = fighter.getMovelist().getMove(fighter.getState().ordinal()).getFrameCount();
            fighter.setCurrentFrame(currentFrame);
            if (currentFrame < frameCount) {
                if (fighter.getState() == State.HIGH_ATTACK) {
                    command = CommandFactory.AttackCommandHigh(fighter);
                } else if (fighter.getState() == State.MID_ATTACK) {
                    command = CommandFactory.AttackCommandMid(fighter);
                } else if (fighter.getState() == State.LOW_ATTACK) {
                    command = CommandFactory.AttackCommandLow(fighter);
                }

                if (recordingInput) commandHistory.add(command);
                return command;
            }
        }

        if (Gdx.input.isKeyPressed(attackButton)) {
            fighter.setCurrentFrame(0);
            if (Gdx.input.isKeyPressed(forwardButton)) {
                command = CommandFactory.AttackCommandHigh(fighter);
            } else if (Gdx.input.isKeyPressed(backwardButton)) {
                command = CommandFactory.AttackCommandLow(fighter);
            } else {
                command = CommandFactory.AttackCommandMid(fighter);
            }
            if (recordingInput) commandHistory.add(command);
            return command;
        }


        // NEUTRAL
        if (Gdx.input.isKeyPressed(forwardButton) && Gdx.input.isKeyPressed(backwardButton)) {
            if (fighter.getState() != State.NEUTRAL || currentFrame >= fighter.getMovelist().getMove(State.NEUTRAL.ordinal()).getFrameCount()) {
                fighter.setCurrentFrame(0);
            } else {
                fighter.setCurrentFrame(currentFrame);
            }
            command = CommandFactory.doNothingCommand(fighter);
            if (recordingInput) commandHistory.add(command);
            return command;
        }

        // CROUCHING
        if (Gdx.input.isKeyPressed(crouchButton)) {
            if (fighter.getState() != State.CROUCHING || currentFrame >= fighter.getMovelist().getMove(State.CROUCHING.ordinal()).getFrameCount()) {
                fighter.setCurrentFrame(0);
            } else {
                fighter.setCurrentFrame(currentFrame);
            }
            command = CommandFactory.crouchCommand(fighter);
            if (recordingInput) commandHistory.add(command);
            return command;
        }

        // GOING_BACKWARD
        if (Gdx.input.isKeyPressed(backwardButton)) {
            if (fighter.getState() != State.GOING_BACK || currentFrame >= fighter.getMovelist().getMove(State.GOING_BACK.ordinal()).getFrameCount()) {
                fighter.setCurrentFrame(0);
            } else {
                fighter.setCurrentFrame(currentFrame);
            }
            command = CommandFactory.moveFighterCommandBackward(fighter);
            if (recordingInput) commandHistory.add(command);
            return command;
        }

        // GOING_FORWARD
        if (Gdx.input.isKeyPressed(forwardButton)) {
            if (fighter.getState() != State.GOING_FORWARD || currentFrame >= fighter.getMovelist().getMove(State.GOING_FORWARD.ordinal()).getFrameCount()) {
                fighter.setCurrentFrame(0);
            } else {
                fighter.setCurrentFrame(currentFrame);
            }
            command = CommandFactory.moveFighterCommandForward(fighter);
            if (recordingInput) commandHistory.add(command);
            return command;
        }

        // NEUTRAL (NOT PRESSING ANYTHING)
        fighter.setCurrentFrame(currentFrame);
        if (currentFrame >= fighter.getMovelist().getMove(State.NEUTRAL.ordinal()).getFrameCount()) {
            fighter.setCurrentFrame(0);
        }
        command = CommandFactory.doNothingCommand(fighter);
        if (recordingInput) commandHistory.add(command);
        return command;
    }

    private Command getMovementCommand() {
        Command command;
        if (Gdx.input.isKeyPressed(forwardButton) && Gdx.input.isKeyPressed(backwardButton)) {
            command = CommandFactory.doNothingCommand(fighter);
            if (recordingInput) commandHistory.add(command);
            return command;
        } else if (Gdx.input.isKeyPressed(crouchButton)) {
            command = CommandFactory.crouchCommand(fighter);
            if (recordingInput) commandHistory.add(command);
            return command;
        } else if (Gdx.input.isKeyPressed(forwardButton)) {
            recordingInput = true;
            command = CommandFactory.moveFighterCommandForward(fighter);
            if (recordingInput) commandHistory.add(command);
            return command;
        } else if (Gdx.input.isKeyPressed(backwardButton)) {
            recordingInput = true;
            command = CommandFactory.moveFighterCommandBackward(fighter);
            if (recordingInput) commandHistory.add(command);
            return command;
        } else {
            command = CommandFactory.doNothingCommand(fighter);
            if (recordingInput) commandHistory.add(command);
            return command;
        }
    }

    public Fighter getFighter() {
        return this.fighter;
    }

    public void setFighter(Fighter fighter) {
        this.fighter = fighter;
    }

    public int getForwardButton() {
        return forwardButton;
    }

    public void setForwardButton(int forwardButton) {
        this.forwardButton = forwardButton;
    }

    public int getBackwardButton() {
        return backwardButton;
    }

    public void setBackwardButton(int backwardButton) {
        this.backwardButton = backwardButton;
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

    public void setMultiplayer(Multiplayer multiplayer) {
        this.multiplayer = multiplayer;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
