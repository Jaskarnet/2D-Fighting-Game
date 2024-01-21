package com.mygdx.entities;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.commands.CommandFactory;
import com.mygdx.commands.InputHandler;
import com.mygdx.commands.Player;
import com.mygdx.game.FightingGame;
import com.mygdx.moves.Move;
import com.mygdx.utils.GameAssetManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        // Sprawdzenie początkowego stanu postaci
        assertEquals(0, fighter.getCurrentFrame());
        assertEquals(initialX, fighter.getX());
        assertEquals(initialY, fighter.getY());
        assertEquals(State.NEUTRAL, fighter.getState());
        Move idleMove = fighter.getMovelist().getMove(State.NEUTRAL.getId());
        List<Rectangle> initialHurtboxes = idleMove.getFrame(0).getHurtboxes();
        List<Rectangle> initialHitboxes = idleMove.getFrame(0).getHitboxes();
        TextureRegion initialSprite = idleMove.getFrame(0).getSprite();
        assertEquals(initialHurtboxes, fighter.getHurtboxes());
        assertEquals(initialHitboxes, fighter.getHitboxes());
        assertEquals(initialSprite, fighter.getTextureRegion());

        // Symulacja otrzymania komendy MoveFighterCommand od InputHandler'a
        when(mockInputHandler.handleInput()).thenReturn(CommandFactory.moveFighterCommandForward(fighter));

        // Aktualizacja logiki postaci
        fighter.update();

        // Sprawdzenie, czy aktualna klatka animacji to 0
        assertEquals(0, fighter.getCurrentFrame());

        // Sprawdzenie, czy postać poruszyła się o oczekiwaną odległość
        Move goingForwardMove = fighter.getMovelist().getMove(State.GOING_FORWARD.getId());
        int xAxisMovement = goingForwardMove.getFrame(0).getXAxisMovement();
        int yAxisMovement = goingForwardMove.getFrame(0).getYAxisMovement();
        assertEquals(initialX + xAxisMovement, fighter.getX());
        assertEquals(initialY + yAxisMovement, fighter.getY());

        // Sprawdzenie, czy stan postaci zmienił się na GOING_FORWARD
        assertEquals(State.GOING_FORWARD, fighter.getState());

        // Sprawdzenie, czy hurtboxy i hitboxy są zgodne z aktualną klatką ruchu
        List<Rectangle> hurtboxes = goingForwardMove.getFrame(0).getHurtboxes();
        for (Rectangle hurtbox : hurtboxes) {
            // Jako że położenie hurtboxów jest względne (wobec postaci)
            // to należy jeszcze dodać przebytą drogę
            hurtbox.x = hurtbox.x + xAxisMovement;
        }
        List<Rectangle> hitboxes = goingForwardMove.getFrame(0).getHitboxes();
        for (Rectangle hitbox : hitboxes) {
            // Jako że położenie hitboxów jest względne (wobec postaci)
            // to należy jeszcze dodać przebytą drogę
            hitbox.x = hitbox.x + xAxisMovement;
        }
        assertEquals(hurtboxes ,fighter.getHurtboxes());
        assertEquals(hitboxes ,fighter.getHitboxes());

        // Sprawdzenie czy postac zmienilła wyświetlaną teksturę
        TextureRegion sprite = goingForwardMove.getFrame(0).getSprite();
        assertEquals(sprite, fighter.getTextureRegion());
    }
}