package com.packtpub.libgdx.canyonbunny.screens;

import com.badlogic.gdx.Game;
import com.packtpub.libgdx.canyonbunny.game.WorldController;
import com.packtpub.libgdx.canyonbunny.game.WorldRenderer;
import com.packtpub.libgdx.canyonbunny.util.GamePreferences;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
/**
 * Handles most of the game using controller and renderer
 * @author Dalton
 *
 */
public class GameScreen extends AbstractGameScreen
{

	private static final String TAG = GameScreen.class.getName();

	private WorldController worldController;
	private WorldRenderer worldRenderer;

	private boolean paused;
	/*
	 * Constructor for Game Screen
	 */
	public GameScreen(Game game)
	{
		// TODO Auto-generated constructor stub
		super(game);
	}

	/**
	 * Show function displays the menu
	 */
	@Override
	public void show()
	{
		GamePreferences.instance.load();
		worldController = new WorldController(game);
		worldRenderer = new WorldRenderer(worldController);
		Gdx.input.setCatchBackKey(true);
	}

	/**
	 * Renders the screen
	 */
	@Override
	public void render(float deltaTime)
	{
		//Do not pdate game world when paused
		if(!paused)
		{
			//update game world by the time that has passed since last rendered frame
			worldController.update(deltaTime);
		}
		//sets the clear screen color to cornflower blue
		Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xed/ 255.0f, 0xff/255.0f);
		//clears teh screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//renders the game world to screen
		worldRenderer.render();
	}
	/**
	 * handles window resizing
	 */
	@Override
	public void resize(int width, int height)
	{
		worldRenderer.resize(width, height);
	}
	/**
	 * pauses game
	 */
	@Override
	public void pause()
	{
		paused = true;
	}
	/**
	 * resumes game
	 */
	@Override
	public void resume()
	{
		super.resume();
		paused = false;
	}
	/**
	 * Hides stuff
	 */
	@Override
	public void hide()
	{
		worldController.dispose();
		worldRenderer.dispose();
		Gdx.input.setCatchBackKey(false);
	}
	/**
	 * disposes assets
	 */
	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub

	}

}
