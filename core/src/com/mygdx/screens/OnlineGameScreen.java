package com.mygdx.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.commands.Player;
import com.mygdx.engine.Collision;
import com.mygdx.entities.Entity;
import com.mygdx.entities.Fighter;
import com.mygdx.game.FightingGame;
import com.mygdx.game.GameState;
import com.mygdx.game.Multiplayer;

import java.util.ArrayList;
import java.util.Collection;

public class OnlineGameScreen implements Screen {
    FightingGame game;
    private Collection<Entity> entities;
    private Fighter player1, player2;
    private Collision collision;
    private GameState gameState;
    private Multiplayer multiplayer;

    public OnlineGameScreen(FightingGame game, Multiplayer multiplayer, Fighter player1, Fighter player2) {
        this.game = game;
        this.multiplayer = multiplayer;
        this.player1 = player1;
        this.player2 = player2;
        gameState = GameState.ONLINE_GAME;
        entities = new ArrayList<>();
        entities.add(player1);
        entities.add(player2);
        collision = new Collision(player1, player2);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //clearing
        ScreenUtils.clear(1, (float) 0.54, 0, 1);

        //logic
        for (Entity entity : entities) {
            entity.update();
        }
        collision.update();

        //rendering
        game.batch.begin();
        for (Entity entity : entities) {
            game.batch.draw(entity.getTextureRegion(), entity.getX(), entity.getY());
        }
        game.batch.end();

        //draw hitboxes/hurtboxes for testing
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Rectangle box : player1.getHurtboxes()) {
            game.shapeRenderer.setColor(Color.BLACK);
            game.shapeRenderer.rect(box.x, box.y, box.width, box.height);
        }
        for (Rectangle box : player2.getHurtboxes()) {
            game.shapeRenderer.setColor(Color.BLACK);
            game.shapeRenderer.rect(box.x, box.y, box.width, box.height);
        }
        for (Rectangle box : player1.getHitboxes()) {
            game.shapeRenderer.setColor(Color.RED);
            game.shapeRenderer.rect(box.x, box.y, box.width, box.height);
        }
        for (Rectangle box : player2.getHitboxes()) {
            game.shapeRenderer.setColor(Color.RED);
            game.shapeRenderer.rect(box.x, box.y, box.width, box.height);
        }
        game.shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
