package com.mygdx.entities;

import java.util.HashMap;
import java.util.Map;

public enum State {
    NEUTRAL(0),
    GOING_BACK(1),
    GOING_FORWARD(2),
    CROUCHING(3),
    HIGH_ATTACK(4),
    MID_ATTACK(5),
    LOW_ATTACK(6),
    BLOCK_STUNNED_HIGH(7),
    BLOCK_STUNNED_MID(8),
    BLOCK_STUNNED_LOW(9),
    HIT_STUNNED_HIGH(10),
    HIT_STUNNED_MID(11),
    HIT_STUNNED_LOW(12),
    BLOCK_STUNNED_UNKNOWN(13),
    HIT_STUNNED_UNKNOWN(14);

    private final int id;
    private static final Map<Integer, State> idToStateMap = new HashMap<>();

    static {
        for (State state : values()) {
            idToStateMap.put(state.id, state);
        }
    }

    State(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static State fromId(int id) {
        return idToStateMap.get(id);
    }
}
