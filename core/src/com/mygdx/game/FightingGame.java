package com.mygdx.game;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.commands.Player;
import com.mygdx.entities.Fighter;
import com.mygdx.screens.LoadingScreen;
import com.mygdx.screens.MainMenuScreen;
import com.mygdx.utils.GameAssetManager;

public class FightingGame extends Game {
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 700;
    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;
    public GameAssetManager assetManager;
    public Fighter player1, player2;
    public Multiplayer multiplayer;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        assetManager = new GameAssetManager();
        assetManager.loadAssets();
        this.setScreen(new LoadingScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        System.out.println("~dispose(FightingGame)");
        this.getScreen().dispose();
        batch.dispose();
        shapeRenderer.dispose();
        assetManager.dispose();
    }
}
