package com.mygdx.engine;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.commands.AttackCommand;
import com.mygdx.commands.Command;
import com.mygdx.commands.Direction;
import com.mygdx.commands.MoveFighterCommand;
import com.mygdx.entities.Fighter;
import com.mygdx.entities.State;

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
        //System.out.println("fighter1: " + fighter1.getInputHandler().getCommandHistory().getSize() + " commandHistorySize: " + fighter1.getInputHandler().getCommandHistory().getSize());
        //System.out.println("fighter2: " + fighter2.getInputHandler().getCommandHistory().getSize() + " commandHistorySize: " + fighter2.getInputHandler().getCommandHistory().getSize());
        Command command1 = fighter1.getInputHandler().getCommandHistory().get(fighter1.getInputHandler().getCommandHistory().getSize() - 1);
        Command command2 = fighter2.getInputHandler().getCommandHistory().get(fighter2.getInputHandler().getCommandHistory().getSize() - 1);
        Direction direction1 = getDirection(command1);
        Direction direction2 = getDirection(command2);
        State state1 = fighter1.getState();
        State state2 = fighter2.getState();


        // HITBOX1 X HITBOX2
        for (Rectangle hitbox1 : hitboxes1) {
            for (Rectangle hitbox2 : hitboxes2) {
                if (hitbox1.overlaps(hitbox2)) {
                    fighter2.setHealth(fighter2.getHealth() - fighter1.getMovelist().getMove(state1.ordinal()).getDamage());
                    fighter1.setHealth(fighter2.getHealth() - fighter2.getMovelist().getMove(state1.ordinal()).getDamage());
                    if (state1 == State.HIGH_ATTACK) {
                        fighter2.setHitStunnedHigh(true);
                        fighter2.setHealth(fighter2.getHealth() - 3);
                    }
                    if (state1 == State.MID_ATTACK) {
                        fighter2.setHitStunnedMid(true);
                        fighter2.setHealth(fighter2.getHealth() - 2);
                    }
                    if (state1 == State.LOW_ATTACK) {
                        fighter2.setHitStunnedLow(true);
                        fighter2.setHealth(fighter2.getHealth() - 1);
                    }
                    if (state2 == State.HIGH_ATTACK) {
                        fighter1.setHitStunnedHigh(true);
                        fighter1.setHealth(fighter1.getHealth() - 3);
                    }
                    if (state2 == State.MID_ATTACK) {
                        fighter1.setHitStunnedMid(true);
                        fighter1.setHealth(fighter1.getHealth() - 2);
                    }
                    if (state2 == State.LOW_ATTACK) {
                        fighter1.setHitStunnedLow(true);
                        fighter1.setHealth(fighter1.getHealth() - 1);
                    }
                }
            }
        }

        // HITBOX1 X HURTBOX2
        for (Rectangle hitbox1 : hitboxes1) {
            for (Rectangle hurtbox2 : hurtboxes2) {
                if (hitbox1.overlaps(hurtbox2)) {
/*                    System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAA " + state1 + " " + state2 + " " + direction1 + " " + direction2);*/
                    if (state2 == State.HIGH_ATTACK || state2 == State.MID_ATTACK || state2 == State.LOW_ATTACK) {
                        if (state1 == State.HIGH_ATTACK) {
                            fighter2.setHitStunnedHigh(true);
                            fighter2.setHealth(fighter2.getHealth() - 3);
                        }
                        if (state1 == State.MID_ATTACK) {
                            fighter2.setHitStunnedMid(true);
                            fighter2.setHealth(fighter2.getHealth() - 2);
                        }
                        if (state1 == State.LOW_ATTACK) {
                            fighter2.setHitStunnedLow(true);
                            fighter2.setHealth(fighter2.getHealth() - 1);
                        }
                    }
                    else {
                        if (state1 == State.HIGH_ATTACK && state2 != State.CROUCHING && (direction2 == Direction.NEUTRAL || direction2 == Direction.RIGHT))
                            fighter2.setBlockStunnedHigh(true);
                        else if (state1 == State.HIGH_ATTACK && state2 != State.CROUCHING) fighter2.setHitStunnedHigh(true);
                        if (state1 == State.MID_ATTACK && state2 != State.CROUCHING && (direction2 == Direction.NEUTRAL || direction2 == Direction.RIGHT))
                            fighter2.setBlockStunnedMid(true);
                        else if (state1 == State.MID_ATTACK) fighter2.setHitStunnedMid(true);
                        if (state1 == State.LOW_ATTACK && state2 == State.CROUCHING && (direction2 == Direction.NEUTRAL || direction2 == Direction.RIGHT))
                            fighter2.setBlockStunnedLow(true);
                        else if (state1 == State.LOW_ATTACK) fighter2.setHitStunnedLow(true);
                    }
                }
            }
        }

        // HURTBOX1 X HITBOX2
        for (Rectangle hitbox2 : hitboxes2) {
            for (Rectangle hurtbox1 : hurtboxes1) {
                if (hitbox2.overlaps(hurtbox1)) {
                    if (state1 == State.HIGH_ATTACK || state1 == State.MID_ATTACK || state1 == State.LOW_ATTACK) {
                        if (state2 == State.HIGH_ATTACK) {
                            fighter1.setHitStunnedHigh(true);
                            fighter1.setHealth(fighter2.getHealth() - 3);
                        }
                        if (state2 == State.MID_ATTACK) {
                            fighter1.setHitStunnedMid(true);
                            fighter1.setHealth(fighter2.getHealth() - 2);
                        }
                        if (state2 == State.LOW_ATTACK) {
                            fighter1.setHitStunnedLow(true);
                            fighter1.setHealth(fighter2.getHealth() - 1);
                        }
                    }
                    else {
                        if (state2 == State.HIGH_ATTACK && state1 != State.CROUCHING && (direction1 == Direction.NEUTRAL || direction1 == Direction.LEFT))
                            fighter1.setBlockStunnedHigh(true);
                        else if (state2 == State.HIGH_ATTACK && state1 != State.CROUCHING) fighter1.setHitStunnedHigh(true);
                        if (state2 == State.MID_ATTACK && state1 != State.CROUCHING && (direction1 == Direction.NEUTRAL || direction1 == Direction.LEFT))
                            fighter1.setBlockStunnedMid(true);
                        else if (state2 == State.MID_ATTACK) fighter1.setHitStunnedMid(true);
                        if (state2 == State.LOW_ATTACK && state1 == State.CROUCHING && (direction1 == Direction.NEUTRAL || direction1 == Direction.LEFT))
                            fighter1.setBlockStunnedLow(true);
                        else if (state2 == State.LOW_ATTACK) fighter1.setHitStunnedLow(true);
                    }
                }
            }
        }


/*        for (Rectangle hitbox1 : hitboxes1) {
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
        }*/


        // HURTBOX1 X HURTBOX2
        for (Rectangle hurtbox1 : hurtboxes1) {
            for (Rectangle hurtbox2 : hurtboxes2) {
                if (hurtbox1.overlaps(hurtbox2)) {
                    //System.out.println(direction1 + " " + direction2);
                    if (direction1 == Direction.RIGHT && direction2 == Direction.LEFT) {
                        fighter1.moveTo(((MoveFighterCommand) command1).getXBefore(), fighter1.getY());
                        fighter2.moveTo(((MoveFighterCommand) command2).getXBefore(), fighter2.getY());
                    } else if (direction1 == Direction.RIGHT && direction2 == Direction.NEUTRAL) {
                        fighter2.moveTo(fighter2.getX() + 5, fighter2.getY());
                    } else if (direction1 == Direction.NEUTRAL && direction2 == Direction.LEFT) {
                        fighter1.moveTo(fighter1.getX() - 5, fighter1.getY());
                    } else if (state1 == State.HIGH_ATTACK || state1 == State.MID_ATTACK || state1 == State.LOW_ATTACK) {
                        if (state2 == State.HIGH_ATTACK || state2 == State.MID_ATTACK || state2 == State.LOW_ATTACK || state2 == State.GOING_FORWARD) {
                            fighter1.moveTo(((AttackCommand) command1).getXBefore(), fighter1.getY());
                            fighter2.moveTo(((AttackCommand) command2).getXBefore(), fighter2.getY());
                        } else {
                            fighter2.moveTo(fighter2.getX() + 5, fighter2.getY());
                        }
                    } else if (state2 == State.HIGH_ATTACK || state2 == State.MID_ATTACK || state2 == State.LOW_ATTACK) {
                        if (state1 == State.HIGH_ATTACK || state1 == State.MID_ATTACK || state1 == State.LOW_ATTACK || state1 == State.GOING_FORWARD) {
                            fighter2.moveTo(((AttackCommand) command2).getXBefore(), fighter2.getY());
                            fighter1.moveTo(((AttackCommand) command1).getXBefore(), fighter1.getY());
                        } else {
                            fighter1.moveTo(fighter1.getX() - 3, fighter1.getY());
                        }
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
