package com.mygdx.engine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.commands.Command;
import com.mygdx.commands.CommandFactory;
import com.mygdx.commands.InputHandler;
import com.mygdx.commands.Player;
import com.mygdx.entities.Fighter;
import com.mygdx.entities.State;
import com.mygdx.game.FightingGame;
import com.mygdx.utils.GameAssetManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CollisionTest extends ApplicationAdapter {
    Collision collision;
    private Fighter attacker, receiver;
    private InputHandler attackerMockInputHandler, receiverMockInputHandler;
    private int attackerInitialX, attackerInitialY, receiverInitialX, receiverInitialY;
    @BeforeAll
    public static void init() {
        HeadlessApplicationConfiguration conf = new HeadlessApplicationConfiguration();
        new HeadlessApplication(new FightingGame(), conf);
        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = mock(GL20.class);
    }
    @BeforeEach
    void setUp() {
        // Początkowe rozmieszczenie umieszcza postacie tuż obok siebie
        attackerInitialX = 0;
        attackerInitialY = 0;
        receiverInitialX = 300;
        receiverInitialY = 0;
        GameAssetManager gameAssetManager = new GameAssetManager();
        gameAssetManager.loadAssets();
        while (!gameAssetManager.manager.update()) {
            // oczekiwanie aż wszystkie zasoby sie zaladują
        }
        attacker = new Fighter(attackerInitialX, attackerInitialY, Player.PLAYER1, 0, 0, 0, 0, 10, gameAssetManager.manager);
        attackerMockInputHandler = spy(new InputHandler(attacker, Player.PLAYER1, 0, 0, 0, 0, 10));
        attacker.setInputHandler(attackerMockInputHandler);
        receiver = new Fighter(receiverInitialX, receiverInitialY, Player.PLAYER2, 0, 0, 0, 0, 10, gameAssetManager.manager);
        receiverMockInputHandler = spy(new InputHandler(receiver, Player.PLAYER2, 0, 0, 0, 0, 10));
        receiver.setInputHandler(receiverMockInputHandler);
        collision = new Collision(attacker, receiver);
    }

    @Test
    void testCollisionDetectionAndAftermaths() {
        // Symulacja otrzymania komendy AttackFighterCommand od InputHandler'a
        doAnswer(invocation -> {
            Command command = CommandFactory.AttackCommandHigh(attacker);
            attackerMockInputHandler.getCommandHistory().add(command);
            attacker.setCurrentFrame(attacker.getCurrentFrame() + 1);
            return command;
        }).when(attackerMockInputHandler).handleInput();
        // Symulacja funkcjonalności InputHandler.handleInput() ograniczającej się
        // do zwracania komendy MoveFighterCommand lub HitStunCommand w przypadku,
        // gdy Collision wykryje otrzymanie ataku high
        doAnswer(invocation -> {
            Command command = CommandFactory.moveFighterCommandForward(receiver);
            int currentFrame = receiver.getCurrentFrame() + 1;
            if (receiver.isHitStunnedHigh()) {
                if (receiver.getState() != State.HIT_STUNNED_HIGH) {
                    receiver.setCurrentFrame(0);
                    command = CommandFactory.hitStunCommandHigh(receiver);
                    receiverMockInputHandler.getCommandHistory().add(command);
                    return command;
                } else if (receiver.getState() == State.HIT_STUNNED_HIGH && currentFrame >= receiver.getMovelist().getMove(State.HIT_STUNNED_HIGH.ordinal()).getFrameCount()) {
                    receiver.setHitStunnedHigh(false);
                } else {
                    receiver.setCurrentFrame(currentFrame);
                    command = CommandFactory.hitStunCommandHigh(receiver);
                    receiverMockInputHandler.getCommandHistory().add(command);
                    return command;
                }
            }
            receiverMockInputHandler.getCommandHistory().add(command);
            receiver.setCurrentFrame(currentFrame);
            return command;
        }).when(receiverMockInputHandler).handleInput();

        // Symulacja upływanego czasu, gdzie "i" oznacza numer aktualnej klatki
        for (int i = 0; i <= 14; i++) {
            attacker.update();
            receiver.update();
            collision.update();
            if (i <= 12) {
                // Sprawdzanie czy receiver nie dostał za wcześnie atakiem
                assertFalse(receiver.isHitStunnedHigh());
            } else if (i == 13) {
                // W 14 klatce collision powinien wykryć kolizję i zmienić flagę isHitStunnedHigh na true
                assertTrue(receiver.isHitStunnedHigh());
            } else if (i >= 14) {
                // W 15 klatce receiver powinien odczuć skutki dostania atakiem high
                int damage = attacker.getMovelist().getMove(State.HIGH_ATTACK.getId()).getDamage();
                assertEquals(receiver.getMaxHealth() - damage, receiver.getHealth());
                assertEquals(State.HIT_STUNNED_HIGH, receiver.getState());
            }
        }
    }
}
