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
import com.mygdx.commands.StartGameCommand;
import com.mygdx.entities.Fighter;
import com.mygdx.game.FightingGame;
import com.mygdx.game.Multiplayer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HostOnlineGameScreen implements Screen {
    FightingGame game;
    private Skin skin;
    private Stage stage;
    private Label messageLabel;
    private Label encodedIpLabel;
    private TextButton returnButton;
    private TextButton copyButton;
    private Label connectedPlayerLabel;
    private TextButton startGameButton;


    public HostOnlineGameScreen(FightingGame game) {
        this.game = game;
        game.multiplayer = new Multiplayer();
        game.multiplayer.initializeServer();
        game.player1.setMultiplayer(game.multiplayer);
        game.player1.setPlayer(Player.PLAYER1);
        game.player2.setMultiplayer(game.multiplayer);
        game.player2.setPlayer(Player.ONLINE_PLAYER2);
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
        connectedPlayerLabel = new Label("No players connected", skin);
        startGameButton = new TextButton("Start Game", skin);
        startGameButton.setDisabled(true);

        // Add labels and buttons to the table
        table.add(messageLabel).expandX().top().padTop(20).row();
        table.add(encodedIpLabel).expandX().top().padTop(10).row();
        table.add(copyButton).padTop(10).row();
        table.add(connectedPlayerLabel).padTop(20).row();
        table.add(startGameButton).padTop(20).row();
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
                dispose();
                game.setScreen(new MainMenuScreen(game));
            }
        });

        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!startGameButton.isDisabled()) {
                    game.multiplayer.sendCommand(new StartGameCommand());
                    // Możesz również tutaj przejść do ekranu gry dla hosta
                    game.setScreen(new OnlineGameScreen(game));
                }
            }
        });

        // Get the encoded IP and display it
        String encodedIp = game.multiplayer.encodeIpAndPort(game.multiplayer.getIpAddress(), game.multiplayer.getServerPort());
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
        if (game.multiplayer.isConnected()) {
            // Get the address of the connected client
            connectedPlayerLabel.setText("Player joined.");
            startGameButton.setDisabled(false); // Make the start game button clickable
        }
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
        System.out.println("~dispose(HostOnlineGameScreen)");
        if (game.multiplayer != null) {
            game.multiplayer.closeServer();
        }
        stage.dispose();
        skin.dispose();
    }
}
