package com.mygdx.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.commands.Player;
import com.mygdx.entities.Fighter;
import com.mygdx.game.FightingGame;
import com.mygdx.screens.MainMenuScreen;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LoadingScreen implements Screen {
    private FightingGame game;
    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;

    private float progress;
    private float barWidth = Gdx.graphics.getWidth() * 0.8f;
    private float barHeight = 25;
    private float barX = Gdx.graphics.getWidth() * 0.1f;
    private float barY = Gdx.graphics.getHeight() * 0.1f;

    List<String> tips = Arrays.asList(
            "Remember, each character has only 3 health points. Every attack counts!",
            "Mix up the heights of your attacks to confuse your opponent and break through their defense.",
            "High attacks are the fastest, perfect for surprising your opponent.",
            "Mid attacks are slower than high ones but harder to dodge, use them wisely.",
            "Low attacks are the slowest, but they can catch an opponent off-guard if they're standing in defense.",
            "Crouch to avoid high attacks and block low ones.",
            "Stay standing to block mid attacks.",
            "Blocking a high attack puts the attacker in a better position, be ready to continue defending.",
            "After blocking a mid attack, you're in a better position, capitalize on it!",
            "Blocking a low attack gives you enough time to counter with a high attack.",
            "Watch the opponent's blockstun animations to know when you can take the initiative.",
            "Understanding the situation on block and on hit can be crucial for the outcome of the fight.",
            "Use block punishment to turn the tides of battle after a successful defense.",
            "A single high attack can knock out an opponent in one hit, it's a real head-turner!",
            "Mid attacks deal 2 damage points, the 'bread and butter' of your offensive strategy.",
            "Low attacks deal 1 damage point. Not the strongest, but definitely the trickster of the trio.",
            "If you're hit, the attacker has the upper hand. Keep blocking or go for a quick high to turn the tables!"
    );

    private String currentTip;
    private Random random = new Random();

    public LoadingScreen(FightingGame game) {
        this.game = game;
        this.batch = game.batch;
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
        this.font.setColor(Color.WHITE);
        pickRandomTip();
    }

    private void pickRandomTip() {
        currentTip = tips.get(random.nextInt(tips.size()));
    }

    private void drawProgressBar() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);
        shapeRenderer.setColor(Color.ORANGE);
        shapeRenderer.rect(barX, barY, progress * barWidth, barHeight);
        shapeRenderer.end();
    }

    private void drawTip() {
        batch.begin();
        font.draw(batch, currentTip, barX, barY + barHeight + 20);
        batch.end();
    }

    @Override
    public void show() {
        game.assetManager.loadAssets();
    }

    @Override
    public void render(float delta) {
        progress = game.assetManager.manager.getProgress() * 2;
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        drawProgressBar();
        drawTip();
        if (game.assetManager.manager.update()) {
            game.player1 = new Fighter(250, 20, Player.PLAYER1, Input.Keys.A, Input.Keys.D, Input.Keys.S, Input.Keys.F, 600, game.assetManager.manager);
            game.player2 = new Fighter(650, 20, Player.PLAYER2, Input.Keys.RIGHT, Input.Keys.LEFT, Input.Keys.DOWN, Input.Keys.CONTROL_RIGHT, 600, game.assetManager.manager);
            dispose();
            game.setScreen(new MainMenuScreen(game));
        }
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
        System.out.println("~dispose(LoadingScreen)");
        font.dispose();
    }
}
