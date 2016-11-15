package com.packtpub.canyonbunny.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.packtpub.libgdx.canyonbunny.util.Constants;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.packtpub.libgdx.canyonbunny.util.GamePreferences;

/**
 * Renderer for the game
 * @author Dalton Becker
 *
 */
public class WorldRenderer implements Disposable
{
	private OrthographicCamera camera; //creates an orthographic camera
	private SpriteBatch batch; //Used for creating our sprites
	private WorldController worldController;	//instance of world controller
	private OrthographicCamera cameraGUI;	//Orthograpic camera for GUI.

	/**
	 * Constructor for WorldRenderer
	 * @param worldController
	 */
	public WorldRenderer (WorldController worldController)
	{
		this.worldController = worldController;
		init();

	}

	/**
	 * init function for WorldRenderer
	 */
	private void init()
	{
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.update();
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_WIDTH);
		cameraGUI.position.set(0,0,0);
		cameraGUI.setToOrtho(true); //flip y-axis
		cameraGUI.update();
	}

	/**
	 * Renders all objects
	 */
	public void render()
	{
		renderWorld(batch);
		renderGUI(batch);
	}

	/**
	 * Handles resizing the window
	 * @param width
	 * @param height
	 */
	public void resize (int width, int height)
	{
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
		camera.update();
		cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
		cameraGUI.viewportWidth = Constants.VIEWPORT_GUI_WIDTH;
		cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight/2,0);
		cameraGUI.update();
	}

	/**
	 * Renders the instance of the world
	 * @param batch
	 */
	private void renderWorld(SpriteBatch batch)
	{
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		batch.end();
	}


	/**
	 * dispose function for freeing up memory
	 */
	@Override
	public void dispose()
	{
		batch.dispose();
	}

	/**
	 * creates a GUI score
	 * @param batch
	 */
	private void renderGuiScore(SpriteBatch batch)
	{
		float x = -15;
		float y = -15;
		batch.draw(Assets.instance.goldCoin.goldCoin, x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
		Assets.instance.fonts.defaultBig.draw(batch, "" + worldController.score, x + 75, y + 35);
	}
	/**
	 * GUI render function for showing how many lives are left
	 * @param batch
	 */
	private void renderGuiExtraLive (SpriteBatch batch)
	{
		float x = cameraGUI.viewportWidth - 50 - Constants.LIVES_START * 50;
		float y = -15;
		for (int i = 0; i < Constants.LIVES_START; i++) {
			if (worldController.lives <= i)
			{
				batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
			}
			batch.draw(Assets.instance.bunny.head,x + i * 50, y, 50, 50, 120, 100, 0.35f, -0.35f, 0);
			batch.setColor(1, 1, 1, 1);
		}
	}

	/**
	 * Renders the GUI Fps counter
	 * @param batch
	 */
	private void renderGuiFpsCounter (SpriteBatch batch)
	{
		float x = cameraGUI.viewportWidth - 55;
		float y = cameraGUI.viewportHeight - 15;
		int fps = Gdx.graphics.getFramesPerSecond();
		BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;
		if (fps >= 45)
		{
			// 45 or more FPS show up in green
			fpsFont.setColor(0, 1, 0, 1);
		}
		else if (fps >= 30)
		{
			// 30 or more FPS show up in yellow
			fpsFont.setColor(1, 1, 0, 1);
		}
		else
		{
			// less than 30 FPS show up in red
			fpsFont.setColor(1, 0, 0, 1);
		}
		fpsFont.draw(batch, "FPS: " + fps, x, y);
		fpsFont.setColor(1, 1, 1, 1); // white
	}
	/**
	 * Creates our GUI all at once
	 * @param batch
	 */
	private void renderGUI(SpriteBatch batch)
	{
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		//draw collected gold coins icon and text
		renderGuiScore(batch);
		//draw Extra lives icon and text
		renderGuiExtraLive(batch);
		//draw fps counter
		if(GamePreferences.instance.showFpsCounter)
			renderGuiFpsCounter(batch);
		//draw feather power up
		renderGuiFeatherPowerup(batch);
		//draw game over
		renderGuiGameOverMessage(batch);
		batch.end();
	}

	/**
	 * Creates a game over message when game ends
	 * @param batch
	 */
	private void renderGuiGameOverMessage (SpriteBatch batch)
	{
		 float x = cameraGUI.viewportWidth / 2;
		 float y = cameraGUI.viewportHeight / 2;
		 if (worldController.isGameOver())
		 {
			 BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
			 fontGameOver.setColor(1, 0.75f, 0.25f, 1);
			 fontGameOver.draw(batch, "GAME OVER", x,y,0,Align.center,false);
			 fontGameOver.setColor(1, 1, 1, 1);
		 }
	}

	/**
	 * Renders Gui for to show when and how much longer feather is active
	 * @param batch
	 */
	private void renderGuiFeatherPowerup (SpriteBatch batch)
	{
		 float x = -15;
		 float y = 30;
		 float timeLeftFeatherPowerup = worldController.level.bunnyHead.timeLeftFeatherPowerup;
		 if (timeLeftFeatherPowerup > 0)
		 {
			 // Start icon fade in/out if the left power-up time
			 // is less than 4 seconds. The fade interval is set
			 // to 5 changes per second.
			 if (timeLeftFeatherPowerup < 4)
			 {
				 	if (((int)(timeLeftFeatherPowerup * 5) % 2) != 0)
				 	{
				 		batch.setColor(1, 1, 1, 0.5f);
				 	}
			 }
			 batch.draw(Assets.instance.feather.feather, x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
			 batch.setColor(1, 1, 1, 1);
			 Assets.instance.fonts.defaultSmall.draw(batch, "" + (int)timeLeftFeatherPowerup, x + 60, y + 57);
		 }
	}
}
