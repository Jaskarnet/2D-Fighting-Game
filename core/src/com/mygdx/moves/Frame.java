package com.mygdx.moves;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.entities.Fighter;
import com.mygdx.utils.json.Box;



import java.util.ArrayList;
import java.util.List;

public class Frame {
    TextureRegion sprite;

    int xAxisMovement, yAxisMovement;
    List<Rectangle> hurtboxes;
    List<Rectangle> hitboxes;


    public Frame(Texture spriteSheet, int sprite, int xAxisMovement, int yAxisMovement, List<Box> hurtboxes, List<Box> hitboxes) {
        this.sprite = new TextureRegion(spriteSheet, 0, 300 * sprite, 300, 300);
        this.xAxisMovement = xAxisMovement;
        this.yAxisMovement = yAxisMovement;
        this.hurtboxes = boxListToRectangleList(hurtboxes);
        this.hitboxes = boxListToRectangleList(hitboxes);
    }

    private List<Rectangle> boxListToRectangleList(List<Box> boxes) {
        List<Rectangle> newBoxes = new ArrayList<>();
        for (Box box : boxes) {
            Rectangle rectangle = new Rectangle();
            rectangle.x = (int) box.getBox().getMinX() * 3;
            rectangle.y = (int) box.getBox().getMinY() * 3;
            rectangle.width = (int) box.getBox().getWidth() * 3;
            rectangle.height = (int) box.getBox().getHeight() * 3;
            newBoxes.add(rectangle);
        }
        return newBoxes;
    }

    public TextureRegion getSprite() {
        return sprite;
    }

    public void setSprite(TextureRegion sprite) {
        this.sprite = sprite;
    }


    public int getXAxisMovement() {
        return xAxisMovement;
    }

    public void setXAxisMovement(int xAxisMovement) {
        this.xAxisMovement = xAxisMovement;
    }

    public int getYAxisMovement() {
        return yAxisMovement;
    }

    public void setYAxisMovement(int yAxisMovement) {
        this.yAxisMovement = yAxisMovement;
    }

    public List<Rectangle> getHurtboxes() {
        return hurtboxes;
    }

    public void setHurtboxes(List<Rectangle> hurtboxes) {
        this.hurtboxes = hurtboxes;
    }

    public List<Rectangle> getHitboxes() {
        return hitboxes;
    }

    public void setHitboxes(List<Rectangle> hitboxes) {
        this.hitboxes = hitboxes;
    }
}
