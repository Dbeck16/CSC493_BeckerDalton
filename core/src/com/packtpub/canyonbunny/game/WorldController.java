package com.packtpub.canyonbunny.game;
import com.badlogic.gdx.graphics.Pixmap;import com.badlogic.gdx.math.Rectangle;
import com.packtpub.libgdx.canyonbunny.objects.Main;
import com.packtpub.libgdx.canyonbunny.objects.Main.JUMP_STATE;
import com.packtpub.libgdx.canyonbunny.objects.Diploma;
import com.packtpub.libgdx.canyonbunny.objects.Beer;
import com.packtpub.libgdx.canyonbunny.objects.Tiles;
import com.packtpub.libgdx.canyonbunny.screens.MenuScreen;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.canyonbunny.util.CameraHelper;
import com.packtpub.libgdx.canyonbunny.objects.Tiles;
import com.packtpub.libgdx.canyonbunny.util.Constants;

/**
 *
 * @author Dalton Becker
 *
 */

public class WorldController extends InputAdapter
{
	private static final String TAG = WorldController.class.getName(); //Tag to print in console for WorldController

	public CameraHelper cameraHelper; //creates a camera helper class

	public Level level;	//keeps track of what level
	public int lives;	//keeps track of how many lives you have left
	public int score;	//keeps track of score in game

	private Game game;

	/**
	 * initializes current level(level 1 in this case)
	 */
	private void initLevel()
	{
		score = 0;
		level = new Level(Constants.LEVEL_01);
		cameraHelper.setTarget(level.main);
	}

	/**
	 * WorldController Constructor
	 */
	public WorldController(Game game)
	{
		this.game = game;
		init();
	}
	/**
	 * initialization function for WorldController
	 */
	private void init()
	{
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		timeLeftGameOverDelay = 0;
		initLevel();
	}

	/**
	 * Used for creating test squares
	 * @param width
	 * @param height
	 * @return
	 */
	private Pixmap createProceduralPixmap(int width, int height)
	{
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		// Fill square with red color at 50% opacity
		pixmap.setColor(1, 0, 0, 0.5f);
		pixmap.fill();

		//Draw a yellow colored X shape onsquare
		pixmap.setColor(1, 1, 0, 1);
		pixmap.drawLine(0, 0, width, height);
		pixmap.drawLine(width, 0, 0, height);

		//Draw a cyan colored border around square
		pixmap.setColor(0, 1, 1, 1);
		pixmap.drawRectangle(0, 0, width, height);

		return pixmap;
	}

	/**
	 * used to update the game
	 * @param deltaTime
	 */
	public void update(float deltaTime)
	{
		handleDebugInput(deltaTime);
		handleInputGame(deltaTime);
		cameraHelper.update(deltaTime);
		if (timeLeftGameOverDelay < 0) backToMenu();
		level.update(deltaTime);
		testCollisions();
		if(isGameOver())
		{
			timeLeftGameOverDelay -= deltaTime;
			if(timeLeftGameOverDelay < 0) init();
		}
		else
		{
			handleInputGame(deltaTime);
		}
		if(!isGameOver() && isPlayerInWater())
		{
			lives--;
			if(isGameOver())
			{
				timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
			}
			else
			{
				initLevel();
			}
		}
	}
	/**
	 * handles key input events. Currently using keypresses to move camera
	 * @param deltaTime
	 */
	private void handleDebugInput(float deltaTime)
	{
		if(Gdx.app.getType() != ApplicationType.Desktop) return;

		//camera Controls
		float camMoveSpeed = 5 * deltaTime;
		float camMoveSpeedAccelerationFactor = 5;
		if(!cameraHelper.hasTarget(level.main));
		{
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camMoveSpeed *= camMoveSpeedAccelerationFactor;

			if (Gdx.input.isKeyPressed(Keys.LEFT)) moveCamera(-camMoveSpeed, 0);

			if (Gdx.input.isKeyPressed(Keys.RIGHT)) moveCamera(camMoveSpeed, 0);

			if (Gdx.input.isKeyPressed(Keys.UP)) moveCamera(0, camMoveSpeed);

			if (Gdx.input.isKeyPressed(Keys.DOWN)) moveCamera(0, -camMoveSpeed);

			if (Gdx.input.isKeyPressed(Keys.BACKSPACE))cameraHelper.setPosition(0, 0);
		}
		//Camera Controls (zoom)
		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camZoomSpeed *= camZoomSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Keys.COMMA)) cameraHelper.addZoom(camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.PERIOD)) cameraHelper.addZoom(-camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.SLASH)) cameraHelper.setZoom(1);
	}
	/**
	 * Keeps track of moving the camera
	 * @param x
	 * @param y
	 */
	private void moveCamera(float x, float y)
	{
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}
	/**
	 * key up function for resetting the game
	 */
	@Override
	public boolean keyUp(int keycode)
	{
		//Reset game world
		if (keycode == Keys.R){
			init();
			Gdx.app.debug(TAG,  "Game world resetted");
		}
		else if(keycode == Keys.ENTER)
		{
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null: level.main);
			Gdx.app.debug(TAG,  "Camera follow enabled" + cameraHelper.hasTarget());
		}
		return false;
	}
	// Rectangles for collision detection
		private Rectangle r1 = new Rectangle();
		private Rectangle r2 = new Rectangle();

		/**
		 * Handles what our bunny does when it collides with a rock
		 * @param rock
		 */
		private void onCollisionMainWithTile(Tiles tile)
		{
			Main main = level.main;
			float heightDifference = Math.abs(main.position.y - (tile.position.y + tile.bounds.height));
			if(heightDifference > 0.25f)
			{
				boolean hitRightEdge = main.position.x > (tile.position.x + tile.bounds.width / 2.0f);
				if (hitRightEdge)
				{
					main.position.x = tile.position.x + tile.bounds.width;
				}
				else
				{
					main.position.x = tile.position.x - main.bounds.width;
				}
				return;
			}
			switch (main.jumpState)
			{
				case GROUNDED:
					break;
				case FALLING:
				case JUMP_FALLING:
				main.position.y = tile.position.y + main.bounds.height + main.origin.y;
				main.jumpState = JUMP_STATE.GROUNDED;
				break;
				case JUMP_RISING:
					main.position.y = tile.position.y + main.bounds.height + main.origin.y;
					break;
			}



		}
		/**
		 * handles what our bunny does when it touches a Diploma
		 * @param goldcoin
		 */
		private void onCollisionMainWithDiploma(Diploma diploma)
		{
			diploma.collected = true;
			score+= diploma.getScore();
			Gdx.app.log(TAG,  "Diploma collect");
		}

		/**
		 * handles what our bunny does when it touches a feather
		 * @param feather
		 */
		private void onCollisionMainWithBeer(Beer beer)
		{
			beer.collected = true;
			score+= beer.getScore();
			level.main.setBeerPowerup(true);
			Gdx.app.log(TAG, "Beer Collected");
		}

		/**
		 * Tests if we collided with something
		 */
		private void testCollisions()
		{
			r1.set(level.main.position.x, level.main.position.y,
					level.main.bounds.width, level.main.bounds.height);
		 // Test collision: Bunny Head <-> Rocks
			for (Tiles tile : level.tiles)
			{
				r2.set(tile.position.x, tile.position.y, tile.bounds.width,
						tile.bounds.height);
				if (!r1.overlaps(r2)) continue;
				onCollisionMainWithTile(tile);
				// IMPORTANT: must do all collisions for valid
				// edge testing on rocks.
			}
			// Test collision: Bunny Head <-> Gold Coins
		 for (Diploma diploma: level.diploma)
		 {
			 if (diploma.collected) continue;
			 r2.set(diploma.position.x, diploma.position.y,
					 diploma.bounds.width, diploma.bounds.height);
			 if (!r1.overlaps(r2)) continue;
			 onCollisionMainWithDiploma(diploma);
			 break;
		 }
		 // Test collision: Bunny Head <-> Feathers
		 for (Beer beer : level.beer)
		 {
			 if (beer.collected) continue;
			 r2.set(beer.position.x, beer.position.y,
					 beer.bounds.width, beer.bounds.height);
			 if (!r1.overlaps(r2)) continue;
			 onCollisionMainWithBeer(beer);
			 break;
		 }
		}

		/**
		 * Handles what our game does when a key is pressed
		 * @param deltaTime
		 */
		private void handleInputGame (float deltaTime)
		{
			 if (cameraHelper.hasTarget(level.main))
			 {
				 // Player Movement
				 if (Gdx.input.isKeyPressed(Keys.LEFT))
				 {
					 level.main.velocity.x = -level.main.terminalVelocity.x;
				 }
				 else if (Gdx.input.isKeyPressed(Keys.RIGHT))
				 {
					 level.main.velocity.x =	level.main.terminalVelocity.x;
				 }
			 }
			else
			{
			 // Execute auto-forward movement on non-desktop platform
				if (Gdx.app.getType() != ApplicationType.Desktop)
				{
					level.main.velocity.x = level.main.terminalVelocity.x;
				}
			 }
				 // Bunny Jump
				 if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE))
				 {
					 level.main.setJumping(true);
				 }
				 else
				 {
					 level.main.setJumping(false);
				 }

		}
		private float timeLeftGameOverDelay;//holds how much time the delay is before starting a new game

		//returns if the game is over
		public boolean isGameOver()
		{
			return lives < 0;
		}

		/**
		 * returns if the player hit da water
		 * @return
		 */
		public boolean isPlayerInWater()
		{
			return level.main.position.y < -5;
		}
		private void backToMenu()
		{
			//switch to menu screen
			game.setScreen(new MenuScreen(game));
		}


	}