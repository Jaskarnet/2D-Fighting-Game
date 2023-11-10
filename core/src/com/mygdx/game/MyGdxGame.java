package com.mygdx.game;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.commands.Player;
import com.mygdx.engine.Collision;
import com.mygdx.entities.Entity;
import com.mygdx.entities.Fighter;
import java.util.ArrayList;
import java.util.Collection;

public class MyGdxGame extends ApplicationAdapter {
    private Collection<Entity> entities;
    private Fighter player1, player2;
    private SpriteBatch batch;
    private Collision collision;
    private ShapeRenderer shapeRenderer;

    @Override
    public void create() {
        player1 = new Fighter(200, 20, Player.PLAYER1, Input.Keys.A, Input.Keys.D, Input.Keys.S, Input.Keys.R, 600);
        player2 = new Fighter(600, 20, Player.PLAYER2, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.DOWN, Input.Keys.CONTROL_RIGHT, 600);
        entities = new ArrayList<>();
        entities.add(player1);
        entities.add(player2);
        batch = new SpriteBatch();
        collision = new Collision(player1, player2);
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render() {
        //clearing
        ScreenUtils.clear(1, (float) 0.54, 0, 1);

        //logic
        for (Entity entity : entities) {
            entity.update();
        }
        collision.update();

        //rendering
        batch.begin();
        for (Entity entity : entities) {
            batch.draw(entity.getTextureRegion(), entity.getX(), entity.getY());
        }
        batch.end();
        for (Rectangle box : player1.getHurtboxes()) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.rect(box.x, box.y, box.width, box.height);
            shapeRenderer.end();
        }
        for (Rectangle box : player2.getHurtboxes()) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.rect(box.x, box.y, box.width, box.height);
            shapeRenderer.end();
        }
        for (Rectangle box : player1.getHitboxes()) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(box.x, box.y, box.width, box.height);
            shapeRenderer.end();
        }
        for (Rectangle box : player2.getHitboxes()) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(box.x, box.y, box.width, box.height);
            shapeRenderer.end();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
