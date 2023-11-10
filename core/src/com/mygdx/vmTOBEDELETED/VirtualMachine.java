package com.mygdx.vmTOBEDELETED;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.entities.Fighter;
import com.mygdx.moves.Frame;
import com.mygdx.moves.Move;

import java.util.Stack;

public class VirtualMachine {
    private final Stack<Integer> stack = new Stack<>();

    private final Fighter[] fighters = new Fighter[2];

    public VirtualMachine(Fighter fighter1, Fighter fighter2) {
        fighters[0] = fighter1;
        fighters[1] = fighter2;
    }

    public void execute(int[] bytecode) {
        for (int i = 0; i < bytecode.length; i++) {
            Instruction instruction = Instruction.getInstruction(bytecode[i]);
            switch (instruction) {
                // Read the next byte from the bytecode
                case INST_LITERAL: {
                    int value = bytecode[++i];
                    stack.push(value);
                    break;
                }
                // Set health of a fighter
                case INST_SET_HEALTH: {
                    int health = stack.pop();
                    int fighter = stack.pop();
                    fighters[fighter].setHealth(health);
                    break;
                }
                // Get health of a fighter and push to stack
                case INST_GET_HEALTH: {
                    int fighter = stack.pop();
                    stack.push(fighters[fighter].getHealth());
                    break;
                }
                // Pop two values from stack, add and push result
                case INST_ADD: {
                    int b = stack.pop();
                    int a = stack.pop();
                    stack.push(a + b);
                    break;
                }
                // Pop two values from stack, subtract and push result
                case INST_SUB: {
                    int b = stack.pop();
                    int a = stack.pop();
                    stack.push(a - b);
                    break;
                }
                // Set instruction pointer to specified address
                case INST_GO_TO: {
                    i = stack.pop();
                    break;
                }
                // Add a move to the fighter's list of moves
/*                case INST_ADD_MOVE: {
                    int damage = stack.pop();
                    int spriteSheetIndex = stack.pop();
                    int fighter = stack.pop();
                    Texture spriteSheet = new Texture("\\spriteSheets\\Fighter1\\" + spriteSheetIndex + ".png");
                    Move move = new Move(spriteSheet, damage);
                    fighters[fighter].addMove(move);
                    break;
                }
                // Add a frame to the fighter's move
                case INST_ADD_FRAME: {
                    int yHitboxOffset = stack.pop();
                    int xHitboxOffset = stack.pop();
                    int hitboxHeight = stack.pop();
                    int hitboxWidth = stack.pop();
                    int yHurtboxMovement = stack.pop();
                    int xHurtboxMovement = stack.pop();
                    int hurtboxHeight = stack.pop();
                    int hurtboxWidth = stack.pop();
                    int sprite = stack.pop();
                    int move = stack.pop();
                    int fighter = stack.pop();
                    Frame frame = new Frame(fighters[fighter].getMove(move).getSpriteSheet(), sprite, hurtboxWidth, hurtboxHeight, xHurtboxMovement, yHurtboxMovement, hitboxWidth, hitboxHeight, xHitboxOffset, yHitboxOffset);
                    fighters[fighter].getMove(move).addFrame(frame);
                    break;
                }*/
            }
        }
    }
}
