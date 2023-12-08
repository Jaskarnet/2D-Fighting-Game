package com.mygdx.commands;

import com.mygdx.entities.Entity;
import com.mygdx.entities.Fighter;

public interface Command {
    void execute(Entity entity);
    void undo(Entity entity);
}
