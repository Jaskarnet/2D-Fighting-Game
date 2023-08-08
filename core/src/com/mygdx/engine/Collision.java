package com.mygdx.engine;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.commands.Command;
import com.mygdx.commands.Direction;
import com.mygdx.commands.DoNothingCommand;
import com.mygdx.commands.MoveFighterCommand;
import com.mygdx.entities.Fighter;

import java.util.Collection;
import java.util.Iterator;

public class Collision {
    Fighter fighter1, fighter2;

    public Collision(Fighter fighter1, Fighter fighter2) {
        this.fighter1 = fighter1;
        this.fighter2 = fighter2;
    }

    public void update() {
        Rectangle hurtbox1 = fighter1.getHurtbox();
        Rectangle hitbox1 = fighter1.getHitbox();
        Rectangle hurtbox2 = fighter2.getHurtbox();
        Rectangle hitbox2 = fighter2.getHitbox();

        if (hurtbox1.overlaps(hurtbox2)) {
            Command command1 = fighter1.getInputHandler().getCommandHistory().getLast(fighter1.getInputHandler().getCommandHistory().getSize() - 1);
            Command command2 = fighter2.getInputHandler().getCommandHistory().getLast(fighter2.getInputHandler().getCommandHistory().getSize() - 1);
            Direction direction1 = getDirection(command1);

            Direction direction2 = getDirection(command2);
            System.out.println(direction1 + " " + direction2);
            if (direction1 == Direction.RIGHT && direction2 == Direction.LEFT) {
                fighter1.moveTo(((MoveFighterCommand) command1).getXBefore(), fighter1.getY());
                fighter2.moveTo(((MoveFighterCommand) command2).getXBefore(), fighter2.getY());
            } else if (direction1 == Direction.RIGHT && direction2 == Direction.NEUTRAL) {
                fighter2.moveTo(fighter1.getX() + 64, fighter2.getY());
            } else if (direction1 == Direction.NEUTRAL && direction2 == Direction.LEFT) {
                fighter1.moveTo(fighter2.getX() - 64, fighter2.getY());
            }

        }

        /*if (hitbox1.overlaps(hitbox2)) {
            // Handle collision between hitboxes
        }*/
    }

    private Direction getDirection(Command command) {
        if (command instanceof MoveFighterCommand) {
            int xBefore = ((MoveFighterCommand) command).getXBefore();
            int x = ((MoveFighterCommand) command).getX();
            return (xBefore < x) ? Direction.RIGHT : Direction.LEFT;
        }
        return Direction.NEUTRAL;
    }


}
