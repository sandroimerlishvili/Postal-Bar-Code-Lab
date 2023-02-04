package com.asd.postalbarcodelab;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {

    public static AssetManager manager = new AssetManager();

    // textures

    public static final String background = "textures/PostalBackground.jpg";

    // music

    public static final String lobbyMusic = "audio/lobbyMusic.mp3";

    // sounds

    public static final String clickSound = "audio/clickSound.mp3";
    public static final String errorSound = "audio/errorSound.mp3";

    // ui skin

    public static final AssetDescriptor<Skin> SKIN = new AssetDescriptor<Skin>("uiskin/glassy-ui.json", Skin.class, new SkinLoader.SkinParameter("uiskin/glassy-ui.atlas"));

    public static void loadTextures() {

        // textures

        manager.load(background, Texture.class);


        // uiSkin

        manager.load(SKIN);

    }

    public static void loadAudio() {

        manager.load(lobbyMusic, Music.class);
        manager.load(clickSound, Sound.class);
        manager.load(errorSound, Sound.class);

    }

    public static void dispose() {

        manager.dispose();

    }

}

