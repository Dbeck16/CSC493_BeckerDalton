package com.packtpub.libgdx.canyonbunny.game;


import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.canyonbunny.util.AudioManager;
import com.packtpub.libgdx.canyonbunny.util.CameraHelper;
import com.packtpub.libgdx.canyonbunny.objects.Rock;
import com.packtpub.libgdx.canyonbunny.util.Constants;
import com.badlogic.gdx.math.Rectangle;
import com.packtpub.libgdx.canyonbunny.objects.BunnyHead;
import com.packtpub.libgdx.canyonbunny.objects.BunnyHead
.JUMP_STATE;
import com.packtpub.libgdx.canyonbunny.objects.Feather;
import com.packtpub.libgdx.canyonbunny.objects.GoldCoin;
import com.packtpub.libgdx.canyonbunny.objects.Rock;
import com.badlogic.gdx.Game;
import com.packtpub.libgdx.canyonbunny.screens.MenuScreen;
import com.packtpub.libgdx.canyonbunny.util.AudioManager;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.packtpub.libgdx.canyonbunny.objects.Carrot;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.Input.Peripheral;
/**
 *
 * @author Dalton Becker
 *
 */

public class WorldController extends InputAdapter implements Disposable
{
	private static final String TAG = WorldController.class.getName(); //Tag to print in console for WorldController

	public CameraHelper cameraHelper; //creates a camera helper class

	public float scoreVisual;
	public float livesVisual;
	public Level level;	//keeps track of what level
	public int lives;	//keeps track of how many lives you have left
	public int score;	//keeps track of score in game
	private boolean goalReached;
	public World b2world;
	private boolean accelerometerAvailable;

	/**
	 * initializes current level(level 1 in this case)
	 */
	private void initLevel()
	{
		score = 0;
		scoreVisual = score;
		goalReached = false;
		level = new Level(Constants.LEVEL_01);
		cameraHelper.setTarget(level.bunnyHead);
		initPhysics();
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
		accelerometerAvailable = Gdx.input.isPeripheralAvailable(
				Peripheral.Accelerometer);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		initLevel();
		livesVisual = lives;
		timeLeftGameOverDelay = 0;
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
		cameraHelper.update(deltaTime);
		level.update(deltaTime);
		testCollisions();
		b2world.step(deltaTime, 8, 3);
		if(isGameOver())
		{
			timeLeftGameOverDelay -= deltaTime;
			if(timeLeftGameOverDelay < 0) backToMenu();
		}
		else
		{
			handleInputGame(deltaTime);
		}
		if(!isGameOver() && isPlayerInWater())
		{
			AudioManager.instance.play(Assets.instance.sounds.liveLost);
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
		level.mountains.updateScrollPosition(cameraHelper.getPosition());
		if (livesVisual > lives)
			livesVisual = Math.max(lives,  livesVisual - 1 * deltaTime);
		if (scoreVisual < score)
		{
			scoreVisual = Math.min(score, scoreVisual + 250 * deltaTime);
		}
	}
	/**
	 * handles key input events. Currently using keypresses to move camera
	 * @param deltaTime
	 */
	private void handleDebugInput(float deltaTime)
	{
		if(Gdx.app.getType() != ApplicationType.Desktop) return;

		if(!cameraHelper.hasTarget(level.bunnyHead))
		{
			//camera Controls(move)
			float camMoveSpeed = 5 * deltaTime;
			float camMoveSpeedAccelerationFactor = 5;

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
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null: level.bunnyHead);
			Gdx.app.debug(TAG,  "Camera follow enabled" + cameraHelper.hasTarget());
		}
		else if(keycode == Keys.ESCAPE || keycode == Keys.BACK)
		{
			backToMenu();
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
	private void onCollisionBunnyHeadWithRock(Rock rock)
	{
		BunnyHead bunnyHead = level.bunnyHead;
		float heightDifference = Math.abs(bunnyHead.position.y - (rock.position.y + rock.bounds.height));
		if(heightDifference > 0.25f)
		{
			boolean hitRightEdge = bunnyHead.position.x > (rock.position.x + rock.bounds.width / 2.0f);
			if (hitRightEdge)
			{
				bunnyHead.position.x = rock.position.x + rock.bounds.width;
			}
			else
			{
				bunnyHead.position.x = rock.position.x - bunnyHead.bounds.width;
			}
			return;
		}
		switch (bunnyHead.jumpState)
		{
			case GROUNDED:
				break;
			case FALLING:
			case JUMP_FALLING:
			bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y;
			bunnyHead.jumpState = JUMP_STATE.GROUNDED;
			break;
			case JUMP_RISING:
				bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y;
				break;
		}



	}
	/**
	 * handles what our bunny does when it touches a coind
	 * @param goldcoin
	 */
	private void onCollisionBunnyWithGoldCoin(GoldCoin goldcoin)
	{
		goldcoin.collected = true;
		AudioManager.instance.play(Assets.instance.sounds.pickupCoin);
		score+= goldcoin.getScore();
		Gdx.app.log(TAG,  "Gold coin collect");
	}

	/**
	 * handles what our bunny does when it touches a feather
	 * @param feather
	 */
	private void onCollisionBunnyWithFeather(Feather feather)
	{
		feather.collected = true;
		AudioManager.instance.play(Assets.instance.sounds.pickupFeather);
		score+= feather.getScore();
		level.bunnyHead.setFeatherPowerup(true);
		Gdx.app.log(TAG, "Feather Collected");
	}

	/**
	 * Tests if we collided with something
	 */
	private void testCollisions ()
	{
		r1.set(level.bunnyHead.position.x, level.bunnyHead.position.y,
				level.bunnyHead.bounds.width, level.bunnyHead.bounds.height);
	 // Test collision: Bunny Head <-> Rocks
		for (Rock rock : level.rocks)
		{
			r2.set(rock.position.x, rock.position.y, rock.bounds.width,
					rock.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionBunnyHeadWithRock(rock);
			// IMPORTANT: must do all collisions for valid
			// edge testing on rocks.
		}
		// Test collision: Bunny Head <-> Gold Coins
	 for (GoldCoin goldcoin : level.goldCoins)
	 {
		 if (goldcoin.collected) continue;
		 r2.set(goldcoin.position.x, goldcoin.position.y,
				 goldcoin.bounds.width, goldcoin.bounds.height);
		 if (!r1.overlaps(r2)) continue;
		 onCollisionBunnyWithGoldCoin(goldcoin);
		 break;
	 }
	 // Test collision: Bunny Head <-> Feathers
	 for (Feather feather : level.feathers)
	 {
		 if (feather.collected) continue;
		 r2.set(feather.position.x, feather.position.y,
				 feather.bounds.width, feather.bounds.height);
		 if (!r1.overlaps(r2)) continue;
		 onCollisionBunnyWithFeather(feather);
		 break;
	 }

	// Test collision: Bunny Head <-> Goal
	 if (!goalReached)
	 {
		 r2.set(level.goal.bounds);
		 r2.x += level.goal.position.x;
		 r2.y += level.goal.position.y;
		 if (r1.overlaps(r2)) onCollisionBunnyWithGoal();
	 }
	}

	/**
	 * Handles what our game does when a key is pressed
	 * @param deltaTime
	 */
	private void handleInputGame (float deltaTime)
	{
		 if (cameraHelper.hasTarget(level.bunnyHead))
		 {
			 // Player Movement
			 if (Gdx.input.isKeyPressed(Keys.LEFT))
			 {
				 level.bunnyHead.velocity.x = -level.bunnyHead.terminalVelocity.x;
			 }
			 else if (Gdx.input.isKeyPressed(Keys.RIGHT))
			 {
				 level.bunnyHead.velocity.x =	level.bunnyHead.terminalVelocity.x;
			 }
			 else
			 {
				// Use accelerometer for movement if available
				 if (accelerometerAvailable) {
					// normalize accelerometer values from [-10, 10] to [-1, 1]
					// which translate to rotations of [-90, 90] degrees
					float amount = Gdx.input.getAccelerometerY() / 10.0f;
					amount *= 90.0f;
					// is angle of rotation inside dead zone?
					if (Math.abs(amount) <Constants.ACCEL_ANGLE_DEAD_ZONE) {
					amount = 0;
					} else {
					// use the defined max angle of rotation instead of
					// the full 90 degrees for maximum velocity
					amount /= Constants.ACCEL_MAX_ANGLE_MAX_MOVEMENT;
					}
					level.bunnyHead.velocity.x =
					level.bunnyHead.terminalVelocity.x * amount;
					}
					// Execute auto-forward movement on non-desktop platform
					else if (Gdx.app.getType() != ApplicationType.Desktop) {

				}
			 }
		 }
		else
		{
		 // Execute auto-forward movement on non-desktop platform
			if (Gdx.app.getType() != ApplicationType.Desktop)
			{
				level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
			}
		 }
			 // Bunny Jump
			 if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE))
			 {
				 level.bunnyHead.setJumping(true);
			 }
			 else
			 {
				 level.bunnyHead.setJumping(false);
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
		return level.bunnyHead.position.y < -5;
	}

	private Game game;

	private void backToMenu()
	{
		//switch to menu screen
		game.setScreen(new MenuScreen(game));
	}

	private void initPhysics ()
	{
		if (b2world != null) b2world.dispose();
		b2world = new World(new Vector2(0, -9.81f), true);

		// Rocks
		Vector2 origin = new Vector2();
		for (Rock rock : level.rocks)
		{
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.KinematicBody;
			bodyDef.position.set(rock.position);
			Body body = b2world.createBody(bodyDef);
			rock.body = body;
			PolygonShape polygonShape = new PolygonShape();
			origin.x = rock.bounds.width / 2.0f;
			origin.y = rock.bounds.height / 2.0f;
			polygonShape.setAsBox(rock.bounds.width / 2.0f,
				rock.bounds.height / 2.0f, origin, 0);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			body.createFixture(fixtureDef);
			polygonShape.dispose();
		}

	}

	private void spawnCarrots (Vector2 pos, int numCarrots, float radius)
	{
		float carrotShapeScale = 0.5f;

		// create carrots with box2d body and fixture
		for (int i = 0; i<numCarrots; i++)
		{
			Carrot carrot = new Carrot();
			// calculate random spawn position, rotation, and scale
			float x = MathUtils.random(-radius, radius);
			float y = MathUtils.random(5.0f, 15.0f);
			float rotation = MathUtils.random(0.0f, 360.0f)
				* MathUtils.degreesToRadians;
			float carrotScale = MathUtils.random(0.5f, 1.5f);
			carrot.scale.set(carrotScale, carrotScale);
			// create box2d body for carrot with start position
			// and angle of rotation
			BodyDef bodyDef = new BodyDef();
			bodyDef.position.set(pos);
			bodyDef.position.add(x, y);
			bodyDef.angle = rotation;
			Body body = b2world.createBody(bodyDef);
			body.setType(BodyType.DynamicBody);
			carrot.body = body;

			// create rectangular shape for carrot to allow
			// interactions (collisions) with other objects
			PolygonShape polygonShape = new PolygonShape();
			float halfWidth = carrot.bounds.width / 2.0f * carrotScale;
			float halfHeight = carrot.bounds.height /2.0f * carrotScale;
			polygonShape.setAsBox(halfWidth * carrotShapeScale,
					halfHeight * carrotShapeScale);

			// set physics attributes
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			fixtureDef.density = 50;
			fixtureDef.restitution = 0.5f;
			fixtureDef.friction = 0.5f;
			body.createFixture(fixtureDef);
			polygonShape.dispose();

			// finally, add new carrot to list for updating/rendering
			level.carrots.add(carrot);
		}
	}
	private void onCollisionBunnyWithGoal ()
	{
		goalReached = true;
		timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_FINISHED;
		Vector2 centerPosBunnyHead =
				new Vector2(level.bunnyHead.position);
		centerPosBunnyHead.x += level.bunnyHead.bounds.width;
		spawnCarrots(centerPosBunnyHead, Constants.CARROTS_SPAWN_MAX,
				Constants.CARROTS_SPAWN_RADIUS);
	}

	@Override
	public void dispose()
	{
		if (b2world != null) b2world.dispose();
	}
}
