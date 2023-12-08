package com.mygdx.commands;

import com.mygdx.entities.Entity;

public class CreateHostClientCommand implements Command {
    String inviteCode;

    @Override
    public void execute(Entity entity) {

    }

    @Override
    public void undo(Entity entity) {

    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
