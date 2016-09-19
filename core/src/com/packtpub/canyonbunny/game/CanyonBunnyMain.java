package com.packtpub.canyonbunny.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.packtpub.canyonbunny.game.Assets;
import com.badlogic.gdx.assets.AssetManager;
/**
 *Main code for game. (Not the actual main function)
 *Carries most of the of the commands for reacting to different states
 * @author Dalton Becker
 *
 */
public class CanyonBunnyMain implements ApplicationListener, com.badlogic.gdx.ApplicationListener
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

		//Initialize controller and renderer
		worldController = new WorldController();
		worldRenderer = new WorldRenderer(worldController);
		//Game world is active on start
		paused = false;

	}
	
	//Constantly redrawing game to show updates
	@Override
	public void render()
	{
		//Do not update game world when paused
		if(!paused)
		{
		//Update game world by the time that has passed
		//since last rendered frame
		worldController.update(Gdx.graphics.getDeltaTime());
		}
		//Sets the clear color to: Cornflower Blue
		Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xed/255.0f, 0xff/255.0f);
		//Clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//Rendered game world to screen
		worldRenderer.render();
	}

	//used when resizing the window. Handled in world renderer
	@Override
	public void resize (int width, int height)
	{

	}

	//pauses the game
	@Override
	public void pause()
	{
		paused = true;
	}

	//resumes game after being paused
	@Override
	public void resume()
	{
		Assets.instance.init(new AssetManager());
		paused = false;
	}
	//disposes of unneccessary instances after completion
	@Override
	public void dispose()
	{
		worldRenderer.dispose();
		Assets.instance.dispose();
	}

}
