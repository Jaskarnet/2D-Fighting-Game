package com.mygdx.utils.json;

import com.badlogic.gdx.math.Rectangle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SpriteData {

    private List<Box> hurtboxes;
    private List<Box> hitboxes;
    private int index;

    public SpriteData() {
        hurtboxes = new ArrayList<>();
        hitboxes = new ArrayList<>();
    }

    public SpriteData(List<Box> hurtboxes, List<Box> hitboxes, int index) {
        this.hurtboxes = hurtboxes;
        this.hitboxes = hitboxes;
        this.index = index;
    }


    public List<Box> getHurtboxes() {
        return hurtboxes;
    }

    public List<Box> getHitboxes() {
        return hitboxes;
    }

    public void addHurtbox(Box hurtbox) {
        hurtboxes.add(hurtbox);
    }

    public void addHurtbox(MyRectangle hurtbox) {
        hurtboxes.add(new Box(hurtbox));
    }

    public void addHitbox(Box hitbox) {
        hitboxes.add(hitbox);
    }

    public void addHitbox(MyRectangle hitbox) {
        hitboxes.add(new Box(hitbox));
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Box hurtbox : hurtboxes) {
            sb.append("hurtbox - " + hurtbox.getBox().getMinX() + " " + hurtbox.getBox().getMinY() + "\n");
        }
        return sb.toString();
    }
}

