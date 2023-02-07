package com.asd.postalbarcodelab;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.asd.postalbarcodelab.PostalBarCodeLab;

/*

This class creates a new Lwjgl3 application using the main
PostalBarCodeLab class and configuration which is shown below

*/

public class DesktopLauncher {
	public static void main (String[] arg) {

		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
		config.useVsync(true);
		config.setForegroundFPS(60);
		config.setTitle("Sandro Imerlishvili - Postal Bar Code Lab");

		new Lwjgl3Application(new PostalBarCodeLab(), config);

	}
}
