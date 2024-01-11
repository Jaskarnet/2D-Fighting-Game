package com.mygdx.engine;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.commands.*;
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
        List<Rectangle> hitboxes1 = fighter1.getHitboxes();
        List<Rectangle> hitboxes2 = fighter2.getHitboxes();
        List<Rectangle> hurtboxes1 = fighter1.getHurtboxes();
        List<Rectangle> hurtboxes2 = fighter2.getHurtboxes();

        // Check for collisions
        State fighter1State = checkHitboxHurtboxCollisions(hitboxes2, hurtboxes1, fighter2, fighter1);
        State fighter2State = checkHitboxHurtboxCollisions(hitboxes1, hurtboxes2, fighter1, fighter2);
        resolveHurtboxHurtboxCollisions(hurtboxes1, hurtboxes2);
        applyStates(fighter1State, fighter2State);

        // Check health and update state
        checkHealthAndSetState();
    }

    public static Direction getDirection(Command command) {
        if (command instanceof MoveFighterCommand) {
            int xBefore = ((MoveFighterCommand) command).getXBefore();
            int x = ((MoveFighterCommand) command).getX();
            return (xBefore < x) ? Direction.RIGHT : Direction.LEFT;
        }
        return Direction.NEUTRAL;
    }

    //returns defender state to be applied after collision
    private State checkHitboxHurtboxCollisions(List<Rectangle> hitboxes, List<Rectangle> hurtboxes, Fighter attacker, Fighter defender) {
        for (Rectangle hitbox : hitboxes) {
            for (Rectangle hurtbox : hurtboxes) {
                if (hitbox.overlaps(hurtbox)) {
                    return processAttack(attacker, defender);
                }
            }
        }
        return null;
    }

    private State processAttack(Fighter attacker, Fighter defender) {
        int attState = attacker.getState().getId();
        int defState = defender.getState().getId();
        if (attState == State.HIGH_ATTACK.getId()) {
            if (defState >= 2 && defState != 3) return State.HIT_STUNNED_HIGH;
            else if (defState == 3) return null;
            else return State.BLOCK_STUNNED_HIGH;
        } else if (attState == State.MID_ATTACK.getId()) {
            if (defState >= 2) return State.HIT_STUNNED_MID;
            else return State.BLOCK_STUNNED_MID;
        } else if (attState == State.LOW_ATTACK.getId()) {
            if (defState != 3) return State.HIT_STUNNED_LOW;
            else return State.BLOCK_STUNNED_LOW;
        }
        return null;
    }

    private void resolveHurtboxHurtboxCollisions(List<Rectangle> hurtboxes1, List<Rectangle> hurtboxes2) {
        Command command1 = fighter1.getInputHandler().getCommandHistory().get(fighter1.getInputHandler().getCommandHistory().getSize() - 1);
        Command command2 = fighter2.getInputHandler().getCommandHistory().get(fighter2.getInputHandler().getCommandHistory().getSize() - 1);
        Direction direction1 = getDirection(command1);
        Direction direction2 = getDirection(command2);
        State state1 = fighter1.getState();
        State state2 = fighter2.getState();
        for (Rectangle hurtbox1 : hurtboxes1) {
            for (Rectangle hurtbox2 : hurtboxes2) {
                if (hurtbox1.overlaps(hurtbox2)) {
                    //System.out.println(direction1 + " " + direction2);
                    if (direction1 == Direction.RIGHT && direction2 == Direction.LEFT) {
                        if (fighter1.getPlayer() == Player.PLAYER1) fighter1.moveTo(((MoveFighterCommand) command1).getXBefore(), fighter1.getY());
                        if (fighter2.getPlayer() == Player.PLAYER2) fighter2.moveTo(((MoveFighterCommand) command2).getXBefore(), fighter2.getY());
                    } else if (direction1 == Direction.RIGHT && direction2 == Direction.NEUTRAL) {
                        if (fighter2.getPlayer() == Player.PLAYER2) fighter2.moveTo(fighter2.getX() + 5, fighter2.getY());
                    } else if (direction1 == Direction.NEUTRAL && direction2 == Direction.LEFT) {
                        if (fighter2.getPlayer() == Player.PLAYER1) fighter1.moveTo(fighter1.getX() - 5, fighter1.getY());
                    } else if (state1 == State.HIGH_ATTACK || state1 == State.MID_ATTACK || state1 == State.LOW_ATTACK) {
                        if (state2 == State.HIGH_ATTACK || state2 == State.MID_ATTACK || state2 == State.LOW_ATTACK || state2 == State.GOING_FORWARD) {
                            if (fighter1.getPlayer() == Player.PLAYER1) fighter1.moveTo(((AttackCommand) command1).getXBefore(), fighter1.getY());
                            if (fighter2.getPlayer() == Player.PLAYER2) fighter2.moveTo(((AttackCommand) command2).getXBefore(), fighter2.getY());
                        } else {
                            if (fighter2.getPlayer() == Player.PLAYER2) fighter2.moveTo(fighter2.getX() + 5, fighter2.getY());
                        }
                    } else if (state2 == State.HIGH_ATTACK || state2 == State.MID_ATTACK || state2 == State.LOW_ATTACK) {
                        if (state1 == State.HIGH_ATTACK || state1 == State.MID_ATTACK || state1 == State.LOW_ATTACK || state1 == State.GOING_FORWARD) {
                            if (fighter2.getPlayer() == Player.PLAYER2) fighter2.moveTo(((AttackCommand) command2).getXBefore(), fighter2.getY());
                            if (fighter1.getPlayer() == Player.PLAYER1) fighter1.moveTo(((AttackCommand) command1).getXBefore(), fighter1.getY());
                        } else {
                            if (fighter1.getPlayer() == Player.PLAYER1) fighter1.moveTo(fighter1.getX() - 3, fighter1.getY());
                        }
                    }

                }
            }
        }
    }

    private void applyStates(State fighter1State, State fighter2State) {
        int damage1 = getDamage(fighter1);
        int damage2 = getDamage(fighter2);
        if (fighter1State != null) {
            if (fighter1.getPlayer() == Player.PLAYER1) applyState(fighter1State, damage2, fighter1);
        }
        if (fighter2State != null) {
            if (fighter2.getPlayer() == Player.PLAYER2) applyState(fighter2State, damage1, fighter2);
        }
    }

    private void applyState(State defenderState, int attackerDamage, Fighter defender) {
        if (defenderState == State.BLOCK_STUNNED_HIGH) defender.setBlockStunnedHigh(true);
        if (defenderState == State.BLOCK_STUNNED_MID) defender.setBlockStunnedMid(true);
        if (defenderState == State.BLOCK_STUNNED_LOW) defender.setBlockStunnedLow(true);
        if (defenderState == State.HIT_STUNNED_HIGH) {
            defender.setHealth(defender.getHealth() - attackerDamage);
        }
        if (defenderState == State.HIT_STUNNED_MID) {
            defender.setHitStunnedMid(true);
            defender.setHealth(defender.getHealth() - attackerDamage);
        }
        if (defenderState == State.HIT_STUNNED_LOW) {
            defender.setHitStunnedLow(true);
            defender.setHealth(defender.getHealth() - attackerDamage);
        }
    }

    private int getDamage(Fighter fighter) {
        return fighter.getMovelist().getMove(fighter.getState().getId()).getDamage();
    }

    private void checkHealthAndSetState() {
        if (fighter1.getHealth() <= 0 && fighter1.getState() != State.HIT_STUNNED_HIGH) {
            handleFighterDefeated(fighter1, fighter2);
        }
        if (fighter2.getHealth() <= 0 && fighter2.getState() != State.HIT_STUNNED_HIGH) {
            handleFighterDefeated(fighter2, fighter1);
        }
    }

    private void handleFighterDefeated(Fighter defeated, Fighter winner) {
        if (!defeated.isHitStunnedHigh()) {
            if (defeated.getPlayer() == Player.PLAYER1 || defeated.getPlayer() == Player.PLAYER2) defeated.setHitStunnedHigh(true);
        }
    }
}
