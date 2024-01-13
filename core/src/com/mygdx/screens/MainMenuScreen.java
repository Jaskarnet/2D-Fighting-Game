package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class MainMenuScreen implements Screen {

    private final FightingGame game;
    private final Stage stage;
    private final Skin skin;
    private Texture backgroundTexture;
    private TextButton playOfflineButton;
    private TextButton hostOnlineGameButton;
    private TextButton joinOnlineGameButton;
    private TextButton exitButton;
    BitmapFont font;

    public MainMenuScreen(FightingGame game) {
        backgroundTexture = new Texture(Gdx.files.internal("background.jpg"));
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        FileHandle fileHandle = Gdx.files.internal("uiskin.json");
        String fullPath = fileHandle.file().getAbsolutePath();
        System.out.println("Full path to uiskin.json: " + fullPath);
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.font = new BitmapFont();
        font.getData().setScale(2f);

        createUI();
    }

    private void createUI() {
        TextButtonStyle buttonStyle = new TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.up = skin.getDrawable("default-round");
        buttonStyle.down = skin.getDrawable("default-round-down");

        playOfflineButton = new TextButton("Play Offline", buttonStyle);
        hostOnlineGameButton = new TextButton("Host Online Game", buttonStyle);
        joinOnlineGameButton = new TextButton("Join Online Game", buttonStyle);
        exitButton = new TextButton("Exit", buttonStyle);

        float buttonWidth = 300f;
        float buttonHeight = 60f;

        playOfflineButton.setSize(buttonWidth, buttonHeight);
        hostOnlineGameButton.setSize(buttonWidth, buttonHeight);
        joinOnlineGameButton.setSize(buttonWidth, buttonHeight);
        exitButton.setSize(buttonWidth, buttonHeight);

        // Set button listeners
        playOfflineButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Play Offline button clicked!");
                dispose();
                game.setScreen(new OfflineGameScreen(game));
            }
        });

        hostOnlineGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Host Online Game button clicked!");
                dispose();
                game.setScreen(new HostOnlineGameScreen(game));
            }
        });

        joinOnlineGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Join Online Game button clicked!");
                dispose();
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
        table.left();
        table.setFillParent(true);
        table.defaults().pad(10);
        table.add(playOfflineButton).size(buttonWidth, buttonHeight).padBottom(10).row();
        table.add(hostOnlineGameButton).size(buttonWidth, buttonHeight).padBottom(10).row();
        table.add(joinOnlineGameButton).size(buttonWidth, buttonHeight).padBottom(10).row();
        table.add(exitButton).size(buttonWidth, buttonHeight).row();
        stage.addActor(table);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, (float) 0.54, 0, 1);
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();
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
        if (Gdx.input.getInputProcessor() == stage) {
            Gdx.input.setInputProcessor(null);
        }
    }

    @Override
    public void dispose() {
        System.out.println("~dispose(MainMenuGameScreen)");
        backgroundTexture.dispose();
        stage.dispose();
        skin.dispose();
    }
}
