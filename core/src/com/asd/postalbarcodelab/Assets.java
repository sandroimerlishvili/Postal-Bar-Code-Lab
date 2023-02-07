package com.asd.postalbarcodelab;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/*

This class handles all external files that are in postalBarCodeLab/assets

It is beneficial to use a separate class and AssetManager in order to avoid
having to access each file everytime it is initialized into a variable
in the other classes of the game (saves lots of memory)

 */

public class Assets {

    // All of these strings are name identifiers for the files that are to be loaded

    public static AssetManager manager = new AssetManager();

    // textures

    public static final String background = "textures/PostalBackground.jpg";

    // music

    public static final String lobbyMusic = "audio/lobbyMusic.mp3";

    // sounds

    public static final String clickSound = "audio/clickSound.mp3";
    public static final String errorSound = "audio/errorSound.mp3";

    // ui skin (these files contained in assets/uiskin handle the "stage" elements such as buttons and textFields that are used throughout the program

    public static final AssetDescriptor<Skin> SKIN = new AssetDescriptor<Skin>("uiskin/glassy-ui.json", Skin.class, new SkinLoader.SkinParameter("uiskin/glassy-ui.atlas"));

    // this method loads all the textures (pictures) into memory

    public static void loadTextures() {

        // textures

        manager.load(background, Texture.class);


        // uiSkin

        manager.load(SKIN);

    }

    // this method loads all the audio into memory

    public static void loadAudio() {

        manager.load(lobbyMusic, Music.class);
        manager.load(clickSound, Sound.class);
        manager.load(errorSound, Sound.class);

    }

    // this method destroys the asset manager when needed

    public static void dispose() {

        manager.dispose();

    }

}

