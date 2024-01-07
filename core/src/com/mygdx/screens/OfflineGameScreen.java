package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.commands.Player;
import com.mygdx.engine.Collision;
import com.mygdx.entities.Entity;
import com.mygdx.entities.Fighter;
import com.mygdx.entities.State;
import com.mygdx.game.FightingGame;
import com.mygdx.game.GameState;
import com.mygdx.utils.ImageFlipper;
import com.mygdx.utils.json.MoveFileReader;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import java.util.ArrayList;
import java.util.Collection;

public class OfflineGameScreen implements Screen {
    FightingGame game;
    private Collection<Entity> entities;
    private Fighter player1, player2;
    private Collision collision;
    private GameState gameState;
    private Texture backgroundTexture;
    private float countdownTime = 5f;
    private boolean isCountdownActive = true;
    private float fightMessageTime = 0.5f;
    private boolean isFightMessageActive = false;
    private BitmapFont countdownFont;
    private GlyphLayout layout;
    private boolean isWinnerMessageActive = false;
    private float winnerMessageTime = 2.0f;
    private String winnerMessage = "";


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
        backgroundTexture = new Texture(Gdx.files.internal("sunset.jpg"));
        countdownFont = new BitmapFont();
        countdownFont.setColor(Color.WHITE);
        countdownFont.getData().setScale(10);
        layout = new GlyphLayout();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        checkWinCondition();
        updateCountdown(delta);
        updateFightMessage(delta);
        updateWinnerMessage(delta);
        clearScreen();
        updateEntities();
        renderGame();
        drawCountdown();
        drawFightMessage();
        drawWinnerMessage();
        //drawHitboxesAndHurtboxes();
    }

    private void checkWinCondition() {
        if (player1.getRoundsWon() >= 3 && player2.getRoundsWon() >= 3 && player1.getState() == State.HIT_STUNNED_HIGH && player2.getState() == State.HIT_STUNNED_HIGH) {
            winnerMessage = "Draw!";
            isWinnerMessageActive = true;
        } else if (player1.getRoundsWon() >= 3 && player2.getState() == State.HIT_STUNNED_HIGH) {
            winnerMessage = "Player1 wins!";
            isWinnerMessageActive = true;
        } else if (player2.getRoundsWon() >= 3 && player1.getState() == State.HIT_STUNNED_HIGH) {
            winnerMessage = "Player2 wins!";
            isWinnerMessageActive = true;
        } else if (player1.getState() == State.HIT_STUNNED_HIGH || player2.getState() == State.HIT_STUNNED_HIGH) {
            winnerMessage = String.format("%d - %d", player1.getRoundsWon(), player2.getRoundsWon());
            isWinnerMessageActive = true;
        }
    }

    private void updateWinnerMessage(float delta) {
        if (isWinnerMessageActive) {
            winnerMessageTime -= delta;
            if (winnerMessageTime <= 0) {
                isWinnerMessageActive = false;
                winnerMessageTime = 2.0f;
                if (player1.getRoundsWon() >= 3 || player2.getRoundsWon() >= 3) game.setScreen(new MainMenuScreen(game));
                else startNewRound();
            }
        }
    }

    private void drawWinnerMessage() {
        if (isWinnerMessageActive) {
            drawTextCentered(winnerMessage);
        }
    }

    private void startNewRound() {
        player1.resetToDefault();
        player2.resetToDefault();
        isCountdownActive = true;
    }

    private void updateCountdown(float delta) {
        if (isCountdownActive) {
            countdownTime -= delta;
            if (countdownTime <= 0) {
                isCountdownActive = false;
                isFightMessageActive = true;
                countdownTime = 3;
            }
        }
    }

    private void updateFightMessage(float delta) {
        if (isFightMessageActive) {
            fightMessageTime -= delta;
            if (fightMessageTime <= 0) {
                isFightMessageActive = false;
                fightMessageTime = 0.5f;
            }
        }
    }

    private void clearScreen() {
        ScreenUtils.clear(1, 1, 1, 1);
    }

    private void updateEntities() {
        if (isCountdownActive || isFightMessageActive) {
            player1.updateIdle();
            player2.updateIdle();
        } else if (isWinnerMessageActive) {
            player1.updateRoundEnd();
            player2.updateRoundEnd();
        } else {
            for (Entity entity : entities) {
                entity.update();
            }
            collision.update();
        }
    }

    private void renderGame() {
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        for (Entity entity : entities) {
            game.batch.draw(entity.getTextureRegion(), entity.getX(), entity.getY());
        }
        game.batch.end();
    }

    private void drawCountdown() {
        if (isCountdownActive) {
            drawTextCentered(String.format("%.0f", Math.ceil(countdownTime)));
        }
    }

    private void drawFightMessage() {
        if (isFightMessageActive) {
            drawTextCentered("FIGHT!");
        }
    }

    private void drawTextCentered(String text) {
        layout.setText(countdownFont, text);
        float x = Gdx.graphics.getWidth() / 2f - layout.width / 2;
        float y = Gdx.graphics.getHeight() / 2f + layout.height / 2;
        game.batch.begin();
        countdownFont.draw(game.batch, text, x, y);
        game.batch.end();
    }

    private void drawHitboxesAndHurtboxes() {
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Rysowanie hurtboxów gracza 1
        for (Rectangle box : player1.getHurtboxes()) {
            game.shapeRenderer.setColor(Color.BLACK);
            game.shapeRenderer.rect(box.x, box.y, box.width, box.height);
        }

        // Rysowanie hurtboxów gracza 2
        for (Rectangle box : player2.getHurtboxes()) {
            game.shapeRenderer.setColor(Color.BLACK);
            game.shapeRenderer.rect(box.x, box.y, box.width, box.height);
        }

        // Rysowanie hitboxów gracza 1
        for (Rectangle box : player1.getHitboxes()) {
            game.shapeRenderer.setColor(Color.RED);
            game.shapeRenderer.rect(box.x, box.y, box.width, box.height);
        }

        // Rysowanie hitboxów gracza 2
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
        backgroundTexture.dispose();
        countdownFont.dispose();
    }
}
