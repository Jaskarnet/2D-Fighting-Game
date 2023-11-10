package com.mygdx.commands;

import com.mygdx.entities.Fighter;
import com.mygdx.entities.State;

public class CommandFactory {
    static MoveFighterCommand moveFighterCommandLeft(Fighter fighter, int x, int y) {
        return new MoveFighterCommand(fighter, x - 5, y);
    }

    static MoveFighterCommand moveFighterCommandRight(Fighter fighter, int x, int y) {
        return new MoveFighterCommand(fighter, x + 5, y);
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

    static DoNothingCommand doNothingCommand() {
        return new DoNothingCommand();
    }
}
