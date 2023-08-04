package com.mygdx.moves;

public class Move {
    int frames;
    int startup;
    int activeFrames;
    int recoveryFrames;

    public Move(int frames, int startup, int activeFrames, int recoveryFrames) {
        this.frames = frames;
        this.startup = startup;
        this.activeFrames = activeFrames;
        this.recoveryFrames = recoveryFrames;
    }
}
