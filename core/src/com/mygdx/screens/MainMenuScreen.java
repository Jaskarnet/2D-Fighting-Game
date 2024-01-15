package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
    private Music gameMusic;
    private Sound hoverSound;

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
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("dreamscape.mp3"));
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.2f);
        hoverSound = Gdx.audio.newSound(Gdx.files.internal("hover.mp3"));
        createUI();
    }

    private void createUI() {
        TextButtonStyle playOfflineStyle = new TextButtonStyle();
        playOfflineStyle.font = font;
        playOfflineStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Buttons/Play offline/default.png"))));
        playOfflineStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Buttons/Play offline/clicked.png"))));
        playOfflineStyle.over = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Buttons/Play offline/hover.png"))));

        TextButtonStyle hostOnlineStyle = new TextButtonStyle();
        hostOnlineStyle.font = font;
        hostOnlineStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Buttons/Host online game/default.png"))));
        hostOnlineStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Buttons/Host online game/clicked.png"))));
        hostOnlineStyle.over = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Buttons/Host online game/hover.png"))));

        TextButtonStyle joinOnlineStyle = new TextButtonStyle();
        joinOnlineStyle.font = font;
        joinOnlineStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Buttons/Join online game/default.png"))));
        joinOnlineStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Buttons/Join online game/clicked.png"))));
        joinOnlineStyle.over = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Buttons/Join online game/hover.png"))));

        TextButtonStyle exitStyle = new TextButtonStyle();
        exitStyle.font = font;
        exitStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Buttons/Exit/default.png"))));
        exitStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Buttons/Exit/clicked.png"))));
        exitStyle.over = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Buttons/Exit/hover.png"))));

        playOfflineButton = new TextButton("", playOfflineStyle);
        hostOnlineGameButton = new TextButton("", hostOnlineStyle);
        joinOnlineGameButton = new TextButton("", joinOnlineStyle);
        exitButton = new TextButton("", exitStyle);

        float buttonWidth = 400f;
        float buttonHeight = 80f;

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

        ClickListener hoverSoundListener = new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) { // -1 pointer means the mouse entered without a touch down
                    hoverSound.play(0.3f); // Set the volume as needed
                }
            }
        };
        playOfflineButton.addListener(hoverSoundListener);
        hostOnlineGameButton.addListener(hoverSoundListener);
        joinOnlineGameButton.addListener(hoverSoundListener);
        exitButton.addListener(hoverSoundListener);
        // Add buttons to the stage
        Table table = new Table();
        table.right();
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
        gameMusic.play();
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
        if (gameMusic != null) gameMusic.dispose();
        backgroundTexture.dispose();
        stage.dispose();
        skin.dispose();
    }
}
