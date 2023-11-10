package com.mygdx.engine;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.commands.Command;
import com.mygdx.commands.Direction;
import com.mygdx.commands.MoveFighterCommand;
import com.mygdx.entities.Fighter;

import java.util.List;

public class Collision {
    Fighter fighter1, fighter2;

    public Collision(Fighter fighter1, Fighter fighter2) {
        this.fighter1 = fighter1;
        this.fighter2 = fighter2;
    }

    public void update() {
        List<Rectangle> hurtboxes1 = fighter1.getHurtboxes();
        List<Rectangle> hitboxes1 = fighter1.getHitboxes();
        List<Rectangle> hurtboxes2 = fighter2.getHurtboxes();
        List<Rectangle> hitboxes2 = fighter2.getHitboxes();

        for (Rectangle hitbox1 : hitboxes1) {
            for (Rectangle hitbox2 : hitboxes2) {
                if (hitbox1.overlaps(hitbox2)) {
                    fighter2.setHealth(fighter2.getHealth() - fighter1.getMovelist().getMove(fighter1.getState().ordinal()).getDamage());
                }
            }
        }

        // TODO change this somehow
        for (Rectangle hitbox1 : hitboxes1) {
            for (Rectangle hitbox2 : hitboxes2) {
                for (Rectangle hurtbox1 : hurtboxes1) {
                    for (Rectangle hurtbox2 : hurtboxes2) {
                        if (hitbox1.overlaps(hurtbox2) && hitbox2.overlaps(hurtbox1)) {
                            // Handle collision between hitbox
                        }
                    }
                }
            }
        }

        for (Rectangle hitbox1 : hitboxes1) {
            for (Rectangle hurtbox2 : hurtboxes2) {
                if (hitbox1.overlaps(hurtbox2)) {

                }
            }
        }

        for (Rectangle hitbox2 : hitboxes2) {
            for (Rectangle hurtbox1 : hurtboxes1) {
                if (hitbox2.overlaps(hurtbox1)) {

                }
            }
        }


        for (Rectangle hurtbox1 : hurtboxes1) {
            for (Rectangle hurtbox2 : hurtboxes2) {
                if (hurtbox1.overlaps(hurtbox2)) {
                    Command command1 = fighter1.getInputHandler().getCommandHistory().get(fighter1.getInputHandler().getCommandHistory().getSize() - 1);
                    Command command2 = fighter2.getInputHandler().getCommandHistory().get(fighter2.getInputHandler().getCommandHistory().getSize() - 1);
                    Direction direction1 = getDirection(command1);

                    Direction direction2 = getDirection(command2);
                    System.out.println(direction1 + " " + direction2);
                    if (direction1 == Direction.RIGHT && direction2 == Direction.LEFT) {
                        fighter1.moveTo(((MoveFighterCommand) command1).getXBefore(), fighter1.getY());
                        fighter2.moveTo(((MoveFighterCommand) command2).getXBefore(), fighter2.getY());
                    } else if (direction1 == Direction.RIGHT && direction2 == Direction.NEUTRAL) {
                        fighter2.moveTo(fighter2.getX() + 5, fighter2.getY());
                    } else if (direction1 == Direction.NEUTRAL && direction2 == Direction.LEFT) {
                        fighter1.moveTo(fighter1.getX() - 5, fighter1.getY());
                    }

                }
            }
        }

    }

    public static Direction getDirection(Command command) {
        if (command instanceof MoveFighterCommand) {
            int xBefore = ((MoveFighterCommand) command).getXBefore();
            int x = ((MoveFighterCommand) command).getX();
            return (xBefore < x) ? Direction.RIGHT : Direction.LEFT;
        }
        return Direction.NEUTRAL;
    }


}
