package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.commands.Player;
import com.mygdx.entities.Fighter;
import com.mygdx.game.FightingGame;
import com.mygdx.game.Multiplayer;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class JoinOnlineGameScreen implements Screen {
    FightingGame game;
    Multiplayer multiplayer;
    private Skin skin;
    private Stage stage;
    private TextField inviteCodeField;
    private TextButton connectButton;
    private TextButton returnButton;
    private Fighter player1, player2;

    public JoinOnlineGameScreen(FightingGame game) {
        this.game = game;
        multiplayer = new Multiplayer(game);
        player2 = new Fighter(650, 20, Player.PLAYER2, Input.Keys.RIGHT, Input.Keys.LEFT, Input.Keys.DOWN, Input.Keys.CONTROL_RIGHT, 600, multiplayer);
        player1 = new Fighter(250, 20, Player.ONLINE_PLAYER1, 600, multiplayer);
    }

    @Override
    public void show() {
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ScreenViewport());

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Create UI elements
        inviteCodeField = new TextField("", skin);
        connectButton = new TextButton("Connect", skin);
        returnButton = new TextButton("Return to Main Menu", skin);

        // Add UI elements to the table
        table.add(inviteCodeField).padBottom(10).row();
        table.add(connectButton).padBottom(10).row();
        table.add(returnButton).padBottom(10).row();

        // Add click listener to the connect button
        connectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                connectToServer();
            }
        });

        // Add click listener to the return button
        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                multiplayer.stopServer();
                game.setScreen(new MainMenuScreen(game));
            }
        });

        // Set the stage as the input processor
        Gdx.input.setInputProcessor(stage);
    }

    private void connectToServer() {
        String[] decodedInviteLink = multiplayer.decodeIpAndPort(inviteCodeField.getText());
        String ipAddress = decodedInviteLink[0];
        int portNumber = Integer.parseInt(decodedInviteLink[1]);

        // Now you have the IP address as a string and port number as an integer
        System.out.println("IP Address: " + ipAddress);
        System.out.println("Port Number: " + portNumber);
        multiplayer.initializeClient(ipAddress, portNumber);
        game.setScreen(new OnlineGameScreen(game, multiplayer, player1, player2));
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
        // Dispose of resources when the screen is hidden
    }

    @Override
    public void dispose() {

        stage.dispose();
    }
}
