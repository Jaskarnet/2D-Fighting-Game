package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.FightingGame;
import com.mygdx.screens.HostOnlineGameScreen;
import com.mygdx.screens.OfflineGameScreen;

public class MainMenuScreen implements Screen {

    private final FightingGame game;
    private final Stage stage;
    private final Skin skin;

    private TextButton playOfflineButton;
    private TextButton hostOnlineGameButton;
    private TextButton joinOnlineGameButton;
    private TextButton exitButton;

    public MainMenuScreen(FightingGame game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        FileHandle fileHandle = Gdx.files.internal("uiskin.json");
        String fullPath = fileHandle.file().getAbsolutePath();
        System.out.println("Full path to uiskin.json: " + fullPath);
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        createUI();
    }

    private void createUI() {
        playOfflineButton = new TextButton("Play Offline", skin);
        hostOnlineGameButton = new TextButton("Host Online Game", skin);
        joinOnlineGameButton = new TextButton("Join Online Game", skin);
        exitButton = new TextButton("Exit", skin);

        // Set button listeners
        playOfflineButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Play Offline button clicked!");
                game.setScreen(new OfflineGameScreen(game));
            }
        });

        hostOnlineGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Host Online Game button clicked!");
                game.setScreen(new HostOnlineGameScreen(game));
            }
        });

        joinOnlineGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Join Online Game button clicked!");
                game.setScreen(new JoinOnlineGameScreen(game));
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Exit button clicked!");
                Gdx.app.exit(); // Close the application
            }
        });

        // Add buttons to the stage
        Table table = new Table();
        table.setFillParent(true);
        table.defaults().pad(10);
        table.add(playOfflineButton).row();
        table.add(hostOnlineGameButton).row();
        table.add(joinOnlineGameButton).row();
        table.add(exitButton).row();

        stage.addActor(table);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, (float) 0.54, 0, 1);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // Implement if needed
    }

    @Override
    public void resume() {
        // Implement if needed
    }

    @Override
    public void hide() {
        // Implement if needed
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
