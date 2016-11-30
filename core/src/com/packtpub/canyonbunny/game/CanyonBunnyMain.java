package com.packtpub.canyonbunny.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.packtpub.canyonbunny.game.Assets;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.Game;
import com.packtpub.libgdx.canyonbunny.screens.MenuScreen;
import com.packtpub.libgdx.canyonbunny.util.AudioManager;
import com.packtpub.libgdx.canyonbunny.util.GamePreferences;

/**
 *Main code for game. (Not the actual main function)
 *Carries most of the of the commands for reacting to different states
 * @author Dalton Becker
 *
 */
public class CanyonBunnyMain extends Game
{
	private static final String TAG = CanyonBunnyMain.class.getName();

	private WorldController worldController; //instance of a worldController
	private WorldRenderer worldRenderer;	//instance of a worldrenderer
	private boolean paused;	//keeps track of whether or not the game is paused

	//Creates the board controllers and all other instances
	@Override
	public void create()
	{
		//set Libgdx log level to DEBUG
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		//loads the assets
		Assets.instance.init(new AssetManager());

		// Load preferences for audio settings and start playing music
		GamePreferences.instance.load();
		AudioManager.instance.play(Assets.instance.music.song01);


		//start game at menu screen
		setScreen(new MenuScreen(this));

	}

}
