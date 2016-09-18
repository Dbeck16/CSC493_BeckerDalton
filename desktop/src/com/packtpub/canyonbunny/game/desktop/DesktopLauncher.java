package com.packtpub.canyonbunny.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.packtpub.canyonbunny.game.CanyonBunnyMain;

/**
 *
 * @author Dalton Becker
  *
 */
public class DesktopLauncher
{
	public static void main (String[] arg)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new CanyonBunnyMain(), config);
		config.width = 800;
		config.height = 400;
	}
}
