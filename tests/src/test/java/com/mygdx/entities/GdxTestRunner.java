package test.java.com.mygdx.entities;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.commands.Player;
import com.mygdx.entities.Fighter;
import com.mygdx.game.FightingGame;
import com.mygdx.moves.Move;
import com.mygdx.moves.Movelist;
import com.mygdx.utils.GameAssetManager;
import com.mygdx.utils.json.FrameRangeData;
import com.mygdx.utils.json.TempData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GdxTestRunner extends ApplicationAdapter {
    private Files mockFiles;
    private FileHandle mockFileHandle;
    private Movelist mockMovelist;

    @BeforeAll
    public static void init() {
        HeadlessApplicationConfiguration conf = new HeadlessApplicationConfiguration();
        new HeadlessApplication(new FightingGame(), conf);
        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = mock(GL20.class);
    }

    @Test
    public void createFighter_ShouldNotBeNull() {
        GameAssetManager gameAssetManager = new GameAssetManager();
        gameAssetManager.loadAssets();
        while (!gameAssetManager.manager.update()) {
            //
        }
        // Utwórz instancję klasy Fighter z mockowanymi zależnościami
        Fighter fighter = new Fighter(0, 0, Player.PLAYER1, 0, 0, 0, 0, 10, gameAssetManager.manager);

        assertNotNull(fighter);
    }
}
