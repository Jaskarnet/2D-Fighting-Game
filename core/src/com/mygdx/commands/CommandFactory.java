package com.mygdx.commands;

import com.mygdx.entities.Fighter;

public class CommandFactory {
    static MoveFighterCommand moveFighterCommandLeft(Fighter fighter, int x, int y) {
        return new MoveFighterCommand(fighter, x - 1, y);
    }

    static MoveFighterCommand moveFighterCommandRight(Fighter fighter, int x, int y) {
        return new MoveFighterCommand(fighter, x + 1, y);
    }

    static DoNothingCommand doNothingCommand() {
        return new DoNothingCommand();
    }
}
