package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.commands.Player;
import com.mygdx.entities.Fighter;
import com.mygdx.game.FightingGame;
import com.mygdx.game.Multiplayer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HostOnlineGameScreen implements Screen {
    FightingGame game;
    Multiplayer multiplayer;
    private Skin skin;
    private Stage stage;
    private Label messageLabel;
    private Label encodedIpLabel;
    private TextButton returnButton;
    private TextButton copyButton;
    private Fighter player1, player2;


    public HostOnlineGameScreen(FightingGame game) {
        this.game = game;
        multiplayer = new Multiplayer(game);
        multiplayer.initializeServer();
        player1 = new Fighter(250, 20, Player.PLAYER1, Input.Keys.A, Input.Keys.D, Input.Keys.S, Input.Keys.R, 600, multiplayer);
        player2 = new Fighter(650, 20, Player.ONLINE_PLAYER2, 600, multiplayer);
    }


    @Override
    public void show() {
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ScreenViewport());

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Create labels and buttons
        messageLabel = new Label("Share this code with your friend:", skin);
        encodedIpLabel = new Label("", skin);
        returnButton = new TextButton("Return to Main Menu", skin);
        copyButton = new TextButton("Copy", skin);

        // Add labels and buttons to the table
        table.add(messageLabel).expandX().top().padTop(20).row();
        table.add(encodedIpLabel).expandX().top().padTop(10).row();
        table.add(copyButton).padTop(10).row();
        table.add(returnButton).padTop(30).row();

        // Set label alignments
        messageLabel.setAlignment(Align.center);
        encodedIpLabel.setAlignment(Align.center);

        // Add click listener to the copy button
        copyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Copy the encoded IP to the clipboard
                copyToClipboard(encodedIpLabel.getText().toString());
            }
        });

        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new OnlineGameScreen(game, multiplayer, player1, player2));
            }
        });

        // Get the encoded IP and display it
        String encodedIp = multiplayer.encodeIpAndPort(multiplayer.getIpAddress(), multiplayer.getServerPort());
        encodedIpLabel.setText(encodedIp);

        // Add the stage to the input processors
        Gdx.input.setInputProcessor(stage);
    }

    private void copyToClipboard(String text) {
        Gdx.app.getClipboard().setContents(text);
        System.out.println("Text copied to clipboard: " + text);
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

    }

    @Override
    public void dispose() {

        stage.dispose();
    }
}
