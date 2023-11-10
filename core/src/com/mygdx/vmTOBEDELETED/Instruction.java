package com.mygdx.vmTOBEDELETED;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public enum Instruction {
    INST_LITERAL,
    INST_SET_HEALTH,
    INST_GET_HEALTH,
    INST_ADD,
    INST_SUB,
    INST_GO_TO,
    INST_GET_HURTBOX,
    INST_SET_HURTBOX,
    INST_GET,
    INST_ADD_FRAME,
    INST_ADD_MOVE;

    public static Instruction getInstruction(int bytecode) {
        if (bytecode >= 0 && bytecode < values().length) {
            return values()[bytecode];
        }
        throw new IllegalArgumentException("Invalid bytecode: " + bytecode);
    }

    public static int[] getBytecode(InputStream inputStream) {
        try (Scanner scanner = new Scanner(inputStream)) {
            List<Integer> bytecodeList = new ArrayList<>();
            while (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                bytecodeList.add(value);
            }
            int[] bytecodeArray = new int[bytecodeList.size()];
            for (int i = 0; i < bytecodeList.size(); i++) {
                bytecodeArray[i] = bytecodeList.get(i);
            }
            return bytecodeArray;
        }
    }

}