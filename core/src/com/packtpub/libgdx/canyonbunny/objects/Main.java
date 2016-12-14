package com.packtpub.libgdx.canyonbunny.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.packtpub.libgdx.canyonbunny.game.Assets;
import com.packtpub.libgdx.canyonbunny.util.AudioManager;
import com.packtpub.libgdx.canyonbunny.util.CharacterSkin;
import com.packtpub.libgdx.canyonbunny.util.Constants;
import com.packtpub.libgdx.canyonbunny.util.GamePreferences;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
/**
 * Not to be confused with main function. This is our main character
 * @author Dalton
 *
 */
public class Main extends AbstractGameObject
{
	public ParticleEffect dustParticles = new ParticleEffect();
	public static final String TAG = Main.class.getName();
	private final float JUMP_TIME_MAX = 2.0f; //max jump time
	private final float JUMP_TIME_MIN = 0.1f; //min jump time
	private final float JUMP_TIME_OFFSET_FLYING = JUMP_TIME_MAX - 0.009f; //offset

	public enum VIEW_DIRECTION { LEFT, RIGHT }//enum holding direction of bunny

	public enum JUMP_STATE	//enum holding states of bunny
	{
		GROUNDED, FALLING, JUMP_RISING, JUMP_FALLING
	}
	private TextureRegion regMain;

	public VIEW_DIRECTION viewDirection;
	public float timeJumping;
	public JUMP_STATE jumpState;
	public boolean hasBeerPowerup;
	public float timeLeftBeerPowerup;

	/**
	 * Main constructor
	 */
	public Main ()
	{
		init();
	}
	/**
	 * Initialized Main
	 */
	public void init ()
	{
		 dimension.set(1, 1);
		 regMain = Assets.instance.main.main;
		 // Center image on game object
		 origin.set(dimension.x / 2, dimension.y / 2);
		 // Bounding box for collision detection
		 bounds.set(0, 0, dimension.x/2, dimension.y);
		 // Set physics values
		 terminalVelocity.set(3.0f, 4.0f);
		 friction.set(12.0f, 0.0f);
		 acceleration.set(0.0f, -25.0f);
		 // View direction
		 viewDirection = VIEW_DIRECTION.RIGHT;
		 // Jump state
		 jumpState = JUMP_STATE.FALLING;
		 timeJumping = 0;
		 // Power-ups
		 hasBeerPowerup = false;
		 timeLeftBeerPowerup = 0;
		 //particles
		 dustParticles.load(Gdx.files.internal("../core/assets/particles/dust.pfx"), Gdx.files.internal("../core/assets/particles"));
	}
	/**
	 * sets Main to jumping state
	 * @param jumpKeyPressed
	 */
	public void setJumping (boolean jumpKeyPressed)
	{
		switch (jumpState)
		{
			case GROUNDED: // Character is standing on a platform
				if (jumpKeyPressed)
				{
					AudioManager.instance.play(Assets.instance.sounds.jump);
					// Start counting jump time from the beginning
					timeJumping = 0;
					jumpState = JUMP_STATE.JUMP_RISING;
				}
				else if (velocity.x != 0)
				{
					//Gdx.app.log(TAG, "starting particles");
					dustParticles.setPosition(position.x + dimension.x / 2, position.y+0.1f);
					dustParticles.start();
				}
				else if (velocity.x == 0)
				{
					dustParticles.allowCompletion();
				}
				break;

			case JUMP_RISING: // Rising in the air

					if (!jumpKeyPressed)
						jumpState = JUMP_STATE.JUMP_FALLING;
					break;

			case FALLING:// Falling down

			case JUMP_FALLING: // Falling down after jump
					if (jumpKeyPressed && hasBeerPowerup)
					{
						AudioManager.instance.play(
								Assets.instance.sounds.jumpWithFeather, 1,
								MathUtils.random(1.0f, 1.1f));
						 timeJumping = JUMP_TIME_OFFSET_FLYING;
						 jumpState = JUMP_STATE.JUMP_RISING;
					}
					break;
			}

		};
	/**
	 * sets main to power mode
	 * @param pickedUp
	 */
	public void setBeerPowerup (boolean pickedUp)
	{
		hasBeerPowerup = pickedUp;
		if (pickedUp)
		{
			timeLeftBeerPowerup = Constants.ITEM_BEER_POWERUP_DURATION;
		}
	};
	/**
	 * checks if main has Beer power up
	 * @return
	 */
	public boolean hasBeerPowerup ()
	{
		return hasBeerPowerup && timeLeftBeerPowerup > 0;
	};

	/**
	 * update method for our bunny
	 */
	@Override
	public void update (float deltaTime)
	{
		super.update(deltaTime);
		if(body != null)
		{
			body.setLinearVelocity(velocity);
			position.set(body.getPosition());
		}
		if(velocity.x != 0)
		{
			viewDirection = velocity.x < 0 ? VIEW_DIRECTION.LEFT : VIEW_DIRECTION.RIGHT;

		}

		if(velocity.x!= 0)
		{
			if(timeLeftBeerPowerup > 0)
			{
				terminalVelocity.set(1.5f, 2.0f);
				timeLeftBeerPowerup -= deltaTime;
				if(timeLeftBeerPowerup < 0)
				{
					//disable power-up
					timeLeftBeerPowerup = 0;
					setBeerPowerup(false);
					terminalVelocity.set(3.0f, 4.0f);
				}
			}
		}
		dustParticles.update(deltaTime);

	}

	/**
	 * Render method for the bunny head
	 */
	@Override
	public void render(SpriteBatch batch)
	{
		TextureRegion reg = null;
		dustParticles.draw(batch);
		//apply skin color
				batch.setColor(CharacterSkin.values()[GamePreferences.instance.charSkin].getColor());
		//set a special color when game object has a feather powerup
		if (hasBeerPowerup)
		{
			batch.setColor(1.0f,0.8f,0.0f,1.0f);
		}
		//draw image
		reg = regMain;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x,
				 origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
				 reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
				 reg.getRegionHeight(), viewDirection == VIEW_DIRECTION.LEFT,
				 false);

		//reset color to white
		batch.setColor(1,1,1,1);
	}
	/**
	 * updates the bunnies y motion
	 */
	@Override
	protected void updateMotionY (float deltaTime)
	{
		switch (jumpState)
		{
		case GROUNDED:
			jumpState = JUMP_STATE.FALLING;
			if(velocity.x != 0)
			{
				dustParticles.setPosition(position.x + dimension.x/2, position.y);
				dustParticles.start();
			}
			break;

		case JUMP_RISING:
			// Keep track of jump time
			timeJumping += deltaTime;
			// Jump time left?
			if (timeJumping <= JUMP_TIME_MAX)
			{
				// Still jumping
				velocity.y = terminalVelocity.y;
			}
			break;
		case FALLING:
			break;
		case JUMP_FALLING:
			// Add delta times to track jump time
			timeJumping += deltaTime;
			// Jump to minimal height if jump key was pressed too short
			if (timeJumping > 0 && timeJumping <= JUMP_TIME_MIN)
			{
				// Still jumping
				velocity.y = terminalVelocity.y;
			}
		}
		if (jumpState != JUMP_STATE.GROUNDED)
		{
			dustParticles.allowCompletion();
			super.updateMotionY(deltaTime);

		}
	}
}