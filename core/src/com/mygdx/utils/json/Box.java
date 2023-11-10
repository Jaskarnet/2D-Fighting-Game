package com.mygdx.utils.json;



public class Box {
    private MyRectangle box;

    public Box() {}

    public Box(MyRectangle box) {
        this.box = box;
    }

    public void setBox(MyRectangle box) {
        this.box = box;
    }

    public MyRectangle getBox() {
        return box;
    }
}

