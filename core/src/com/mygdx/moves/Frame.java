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


    public Frame(Texture spriteSheet, int spriteIndex, int xAxisMovement, int yAxisMovement, List<Box> hurtboxes, List<Box> hitboxes) {
        int spriteWidth = 300;
        int spriteHeight = 300;
        int textureWidth = spriteSheet.getWidth();
        int textureHeight = spriteSheet.getHeight();
        int columns = textureWidth / spriteWidth; // assuming the width of the spritesheet is divisible by 300

        // Check if the spriteSheet's file path contains 'Fighter2'
        boolean isFighter2 = spriteSheet.toString().contains("Fighter2");

        // Calculate x, y coordinate for TextureRegion based on spriteIndex
        int x, y;
        if (isFighter2) {
            // For Fighter2, calculate x from the right side of the spritesheet
            int row = spriteIndex / columns;
            int column = columns - 1 - (spriteIndex % columns); // Reversed column index for Fighter2
            x = column * spriteWidth;
            y = row * spriteHeight;
        } else {
            // For other fighters, calculate x from the left side of the spritesheet
            x = (spriteIndex % columns) * spriteWidth;
            y = (spriteIndex / columns) * spriteHeight;
        }

        this.sprite = new TextureRegion(spriteSheet, x, y, spriteWidth, spriteHeight);
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
