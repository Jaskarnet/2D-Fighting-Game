package com.mygdx.commands;

import com.mygdx.entities.Fighter;

public interface Command {
    void execute();
    void undo();
}
