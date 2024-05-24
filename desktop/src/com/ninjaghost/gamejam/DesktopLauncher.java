package com.ninjaghost.gamejam;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.ninjaghost.gamejam.ChronoFloraGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.useVsync(true);
		config.setForegroundFPS(60);
		config.setTitle("ChronoFlora 0.5-alpha");
		config.setWindowedMode(640, 480);
		new Lwjgl3Application(new ChronoFloraGame(), config);
	}
}
