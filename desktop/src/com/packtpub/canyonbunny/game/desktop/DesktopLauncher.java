package com.packtpub.canyonbunny.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.packtpub.canyonbunny.game.CanyonBunnyMain;

/**
 * The starter class for the game.
 * @author Dalton Becker
 *
 */

public class DesktopLauncher
{
	// Keeps track of whether or not to rebuild the texture atlas.
	private static boolean rebuildAtlas = false;
	// Checks if it should (re)draw debug outlines in the texture atlas
	private static boolean drawDebugOutline = true;

	public static void main(String[] arg)
	{
		// Checks if it should rebuild the texture atlas
		if (rebuildAtlas)
		{
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.duplicatePadding = false;
			settings.debug = drawDebugOutline;

			TexturePacker.process(settings, "assets-raw/images/", "../core/assets/images/", "canyonbunny.pack");
		}

		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();

		// sets the title of the game
		cfg.title = "CanyonBunny";

		// Sets the width and height of the window
		cfg.width = 800;
		cfg.height = 480;

		new LwjglApplication(new CanyonBunnyMain(), cfg);

	}
}
