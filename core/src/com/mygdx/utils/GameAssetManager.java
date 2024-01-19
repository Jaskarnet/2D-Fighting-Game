package com.mygdx.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class GameAssetManager {
    public final AssetManager manager = new AssetManager();

    //main menu
    public static final String backgroundImagePath = "background.jpg";
    public static final String mainMenuMusicPath = "dreamscape.mp3";
    public static final String hoverSoundPath = "hover.mp3";
    public static final String skinPath = "uiskin.json";
    public static final String fontPath = "default.fnt";
    public static final String playOfflineButtonUpPath = "Buttons/Play offline/default.png";
    public static final String playOfflineButtonDownPath = "Buttons/Play offline/clicked.png";
    public static final String playOfflineButtonOverPath = "Buttons/Play offline/hover.png";
    public static final String hostOnlineButtonUpPath = "Buttons/Host online game/default.png";
    public static final String hostOnlineButtonDownPath = "Buttons/Host online game/clicked.png";
    public static final String hostOnlineButtonOverPath = "Buttons/Host online game/hover.png";
    public static final String joinOnlineButtonUpPath = "Buttons/Join online game/default.png";
    public static final String joinOnlineButtonDownPath = "Buttons/Join online game/clicked.png";
    public static final String joinOnlineButtonOverPath = "Buttons/Join online game/hover.png";
    public static final String exitButtonUpPath = "Buttons/Exit/default.png";
    public static final String exitButtonDownPath = "Buttons/Exit/clicked.png";
    public static final String exitButtonOverPath = "Buttons/Exit/hover.png";

    //game
    public static final String p1MovelistPath = "p1movelist.png";
    public static final String p2MovelistPath = "p2movelist.png";
    public static final String gameMusicPath = "risk.mp3";
    public static final String threeSoundPath = "three.mp3";
    public static final String twoSoundPath = "two.mp3";
    public static final String oneSoundPath = "one.mp3";
    public static final String walk1SoundPath = "walk1.mp3";
    public static final String walk2SoundPath = "walk2.mp3";


    public void loadAssets() {
        //main menu
        manager.load(backgroundImagePath, Texture.class);
        manager.load(mainMenuMusicPath, Music.class);
        manager.load(hoverSoundPath, Sound.class);
        manager.load(skinPath, Skin.class);
        manager.load(fontPath, BitmapFont.class);
        manager.load(playOfflineButtonUpPath, Texture.class);
        manager.load(playOfflineButtonDownPath, Texture.class);
        manager.load(playOfflineButtonOverPath, Texture.class);
        manager.load(hostOnlineButtonUpPath, Texture.class);
        manager.load(hostOnlineButtonDownPath, Texture.class);
        manager.load(hostOnlineButtonOverPath, Texture.class);
        manager.load(joinOnlineButtonUpPath, Texture.class);
        manager.load(joinOnlineButtonDownPath, Texture.class);
        manager.load(joinOnlineButtonOverPath, Texture.class);
        manager.load(exitButtonUpPath, Texture.class);
        manager.load(exitButtonDownPath, Texture.class);
        manager.load(exitButtonOverPath, Texture.class);
        //game
        loadAllTexturesInDirectory("moves/Fighter1/spritesheets");
        loadAllTexturesInDirectory("moves/Fighter2/spritesheets");
        loadAllTexturesInDirectory("sunset");
        manager.load(p1MovelistPath, Texture.class);
        manager.load(p2MovelistPath, Texture.class);
        manager.load(gameMusicPath, Music.class);
        manager.load(threeSoundPath, Sound.class);
        manager.load(twoSoundPath, Sound.class);
        manager.load(oneSoundPath, Sound.class);
        manager.load(walk1SoundPath, Sound.class);
        manager.load(walk2SoundPath, Sound.class);
    }

    private void loadAllTexturesInDirectory(String directoryPath) {
        FileHandle dirHandle = Gdx.files.internal(directoryPath);
        for (FileHandle entry : dirHandle.list()) {
            if (entry.extension().equals("png") || entry.extension().equals("jpg")) {
                manager.load(directoryPath + "/" + entry.name(), Texture.class);
            }
        }
    }

    public void dispose() {
        manager.dispose();
    }
}

