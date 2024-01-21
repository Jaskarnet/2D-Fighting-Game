package com.mygdx.entities;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.commands.CommandFactory;
import com.mygdx.commands.InputHandler;
import com.mygdx.commands.Player;
import com.mygdx.game.FightingGame;
import com.mygdx.moves.Move;
import com.mygdx.moves.Movelist;
import com.mygdx.utils.GameAssetManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FighterTest extends ApplicationAdapter {
    private Fighter fighter;
    private InputHandler mockInputHandler;
    private int initialX, initialY;

    @BeforeAll
    public static void init() {
        HeadlessApplicationConfiguration conf = new HeadlessApplicationConfiguration();
        new HeadlessApplication(new FightingGame(), conf);
        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = mock(GL20.class);
    }
    @BeforeEach
    void setUp() {
        initialX = 0;
        initialY = 0;
        mockInputHandler = mock(InputHandler.class);
        GameAssetManager gameAssetManager = new GameAssetManager();
        gameAssetManager.loadAssets();
        while (!gameAssetManager.manager.update()) {
            // oczekiwanie aż wszystkie zasoby sie zaladują
        }
        fighter = new Fighter(initialX, initialY, Player.PLAYER1, 0, 0, 0, 0, 10, gameAssetManager.manager);
        fighter.setInputHandler(mockInputHandler);
    }

    @Test
    void testUpdateWithMoveForwardCommand() {
        // Symulacja otrzymania komendy MoveFighterCommand przez InputHandler
        when(mockInputHandler.handleInput()).thenReturn(CommandFactory.moveFighterCommandForward(fighter));

        //Sprawdzenie początkowego stanu postaci
        assertEquals(State.NEUTRAL, fighter.getState());

        // Aktualizacja logiki postaci
        fighter.update();

        // Sprawdzenie, czy postać poruszyła się o 5 do przodu
        assertEquals(initialX + 5, fighter.getX());

        // Sprawdzenie, czy stan postaci zmienił się na GOING_FORWARD
        assertEquals(State.GOING_FORWARD, fighter.getState());

        // Sprawdzenie, czy aktualna klatka animacji to 0
        assertEquals(0, fighter.getCurrentFrame());

        // Sprawdzenie, czy hurtboxy i hitboxy są zgodne z zerową klatką ruchu
        Move goingForwardMove = fighter.getMovelist().getMove(State.GOING_FORWARD.getId());
        List<Rectangle> hurtboxes = goingForwardMove.getFrame(0).getHurtboxes();
        for (Rectangle hurtbox : hurtboxes) {
            // Dodanie wartosci równej odległości którą przebył fighter
            hurtbox.x = hurtbox.x + 5;
        }
        List<Rectangle> hitboxes = goingForwardMove.getFrame(0).getHitboxes();
        for (Rectangle hitbox : hitboxes) {
            // Dodanie wartosci równej odległości którą przebył fighter
            hitbox.x = hitbox.x + 5;
        }
        assertEquals(hurtboxes ,fighter.getHurtboxes());
        assertEquals(hitboxes ,fighter.getHitboxes());
    }
}