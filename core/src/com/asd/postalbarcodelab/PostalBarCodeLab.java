package com.asd.postalbarcodelab;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

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


	public void create() {

		Assets.loadTextures();
		Assets.loadAudio();
		Assets.manager.finishLoading();

		mainScreen = new MainScreen(this);
		setScreen(mainScreen);


	}

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

	@Override
	public void dispose() {

		Assets.dispose();
		super.dispose();

	}



}
