package com.mygdx.engine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.commands.InputHandler;
import com.mygdx.commands.Player;
import com.mygdx.entities.Fighter;
import com.mygdx.game.FightingGame;
import com.mygdx.utils.GameAssetManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;
//TODO: zrealizować poniższy test
//Test ma za zadanie sprawdzic czy dojdzie do kolizji tj. czy klasa Colission zmieni flage u którejś postaci ze stunem
//inaczej mówiąc czy hitboxy są poprawnie sprawdzane np. jak postać jest w klatce o 17 indeksie i w odpowiednim miejscu
//i wykonujac high attack to czy trafi drugiego fightera ktory jest w odpowiednim miejscu i idzie do przodu

public class CollisionTest extends ApplicationAdapter {
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
        GameAssetManager gameAssetManager = new GameAssetManager();
        gameAssetManager.loadAssets();
        while (!gameAssetManager.manager.update()) {
            // oczekiwanie aż wszystkie zasoby sie zaladują
        }
        fighter = new Fighter(initialX, initialY, Player.PLAYER1, 0, 0, 0, 0, 10, gameAssetManager.manager);
        fighter.setInputHandler(mockInputHandler);
    }
}
