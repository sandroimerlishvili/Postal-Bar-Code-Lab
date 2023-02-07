package com.asd.postalbarcodelab;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/*

This class is the "main" class for the whole program, it handles switching between
different visual environments (screens) which each handle a different aspect of the game

*/

public class PostalBarCodeLab extends Game {

	// screens

	protected MainScreen mainScreen;
	protected BarCodeScreen barCodeScreen;

	// screen values

	public final static int MAINMENU = 0;
	public final static int BARCODE = 1;

	// audio

	protected Music lobbyMusic;
	protected Sound clickSound;
	protected Sound errorSound;

	// code

	String code;

	// this method loads the assets into memory and creates the initial mainScreen

	public void create() {

		Assets.loadTextures();
		Assets.loadAudio();
		Assets.manager.finishLoading();

		mainScreen = new MainScreen(this);
		setScreen(mainScreen);


	}

	// this method handles the operation of changing between screens and disposing them (setting to null) to conserve memory

	public void changeScreen(int screen) {

		switch(screen) {

			case MAINMENU:

				barCodeScreen = null;
				mainScreen = new MainScreen(this);
				this.setScreen(mainScreen);
				break;

			case BARCODE:

				if (barCodeScreen == null) {

					mainScreen = null;
					barCodeScreen = new BarCodeScreen(this, code);

				}

				this.setScreen(barCodeScreen);
				break;

		}

	}

	// This method disposes the asset manager as well as calls the super method (in the Game class)

	@Override
	public void dispose() {

		Assets.dispose();
		super.dispose();

	}



}
