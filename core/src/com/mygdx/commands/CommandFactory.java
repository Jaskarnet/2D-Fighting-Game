package com.mygdx.commands;

import com.mygdx.entities.Fighter;
import com.mygdx.entities.State;

public class CommandFactory {
    public static MoveFighterCommand moveFighterCommandForward(Fighter fighter) {
        return new MoveFighterCommand(fighter, State.GOING_FORWARD, fighter.getMovelist().getMove(State.GOING_FORWARD.ordinal()), fighter.getCurrentFrame());
    }

    static MoveFighterCommand moveFighterCommandBackward(Fighter fighter) {
        return new MoveFighterCommand(fighter, State.GOING_BACK, fighter.getMovelist().getMove(State.GOING_BACK.ordinal()), fighter.getCurrentFrame());
    }

    static AttackCommand AttackCommandHigh(Fighter fighter) {
        return new AttackCommand(fighter, State.HIGH_ATTACK, fighter.getMovelist().getMove(State.HIGH_ATTACK.ordinal()), fighter.getCurrentFrame());
    }

    static AttackCommand AttackCommandMid(Fighter fighter) {
        return new AttackCommand(fighter, State.MID_ATTACK, fighter.getMovelist().getMove(State.MID_ATTACK.ordinal()), fighter.getCurrentFrame());
    }

    static AttackCommand AttackCommandLow(Fighter fighter) {
        return new AttackCommand(fighter, State.LOW_ATTACK, fighter.getMovelist().getMove(State.LOW_ATTACK.ordinal()), fighter.getCurrentFrame());
    }

    static CrouchCommand crouchCommand(Fighter fighter) {
        return new CrouchCommand(fighter, State.CROUCHING, fighter.getMovelist().getMove(State.CROUCHING.ordinal()), fighter.getCurrentFrame());
    }

    static BlockStunCommand blockStunCommandHigh(Fighter fighter) {
        return new BlockStunCommand(fighter, State.BLOCK_STUNNED_HIGH, fighter.getMovelist().getMove(State.BLOCK_STUNNED_HIGH.ordinal()), fighter.getCurrentFrame());
    }

    static BlockStunCommand blockStunCommandMid(Fighter fighter) {
        return new BlockStunCommand(fighter, State.BLOCK_STUNNED_MID, fighter.getMovelist().getMove(State.BLOCK_STUNNED_MID.ordinal()), fighter.getCurrentFrame());
    }

    static BlockStunCommand blockStunCommandLow(Fighter fighter) {
        return new BlockStunCommand(fighter, State.BLOCK_STUNNED_LOW, fighter.getMovelist().getMove(State.BLOCK_STUNNED_LOW.ordinal()), fighter.getCurrentFrame());
    }

    static HitStunCommand hitStunCommandHigh(Fighter fighter) {
        return new HitStunCommand(fighter, State.HIT_STUNNED_HIGH, fighter.getMovelist().getMove(State.HIT_STUNNED_HIGH.ordinal()), fighter.getCurrentFrame());
    }

    static HitStunCommand hitStunCommandMid(Fighter fighter) {
        return new HitStunCommand(fighter, State.HIT_STUNNED_MID, fighter.getMovelist().getMove(State.HIT_STUNNED_MID.ordinal()), fighter.getCurrentFrame());
    }

    static HitStunCommand hitStunCommandLow(Fighter fighter) {
        return new HitStunCommand(fighter, State.HIT_STUNNED_LOW, fighter.getMovelist().getMove(State.HIT_STUNNED_LOW.ordinal()), fighter.getCurrentFrame());
    }

    static DoNothingCommand doNothingCommand(Fighter fighter) {
        return new DoNothingCommand(fighter, State.NEUTRAL, fighter.getMovelist().getMove(State.NEUTRAL.ordinal()), fighter.getCurrentFrame());
    }
}
