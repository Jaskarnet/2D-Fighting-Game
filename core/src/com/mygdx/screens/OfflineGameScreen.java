package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.commands.Player;
import com.mygdx.engine.Collision;
import com.mygdx.entities.Entity;
import com.mygdx.entities.Fighter;
import com.mygdx.game.FightingGame;
import com.mygdx.game.GameState;

import java.util.ArrayList;
import java.util.Collection;

public class OfflineGameScreen implements Screen {
    FightingGame game;
    private Collection<Entity> entities;
    private Fighter player1, player2;
    private Collision collision;
    private GameState gameState;

    public OfflineGameScreen(FightingGame game) {
        this.game = game;
        gameState = GameState.OFFLINE_GAME;
        /*MoveFileReader moveFileReader = new MoveFileReader();
        moveFileReader.copyAndReverseMoveInfo("moves/Fighter1/moveinfo", "moves/Fighter2/moveinfo");
        ImageFlipper.flipImagesHorizontally("moves/Fighter1/spritesheets", "moves/Fighter2/spritesheets");*/

        player1 = new Fighter(250, 20, Player.PLAYER1, Input.Keys.A, Input.Keys.D, Input.Keys.S, Input.Keys.R, 600);
        player2 = new Fighter(650, 20, Player.PLAYER2, Input.Keys.RIGHT, Input.Keys.LEFT, Input.Keys.DOWN, Input.Keys.CONTROL_RIGHT, 600);

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
