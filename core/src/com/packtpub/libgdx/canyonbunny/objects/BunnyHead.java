package com.packtpub.libgdx.canyonbunny.objects;

import com.badlogic.gdx.Gdx;
import com.packtpub.libgdx.canyonbunny.util.CharacterSkin;
import com.packtpub.libgdx.canyonbunny.util.GamePreferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.canyonbunny.game.Assets;
import com.packtpub.libgdx.canyonbunny.util.Constants;

public class BunnyHead extends AbstractGameObject
{
	public static final String TAG = BunnyHead.class.getName();
	private final float JUMP_TIME_MAX = 0.3f; //max jump time
	private final float JUMP_TIME_MIN = 0.1f; //min jump time
	private final float JUMP_TIME_OFFSET_FLYING = JUMP_TIME_MAX - 0.018f; //offset

	public enum VIEW_DIRECTION { LEFT, RIGHT }//enum holding direction of bunny

	public enum JUMP_STATE	//enum holding states of bunny
	{
		GROUNDED, FALLING, JUMP_RISING, JUMP_FALLING
	}
	private TextureRegion regHead;

	public VIEW_DIRECTION viewDirection;
	public float timeJumping;
	public JUMP_STATE jumpState;
	public boolean hasFeatherPowerup;
	public float timeLeftFeatherPowerup;

	/**
	 * bunny head constructor
	 */
	public BunnyHead ()
	{
		init();
	}
	/**
	 * Initialized bunnyHead
	 */
	public void init ()
	{
		 dimension.set(1, 1);
		 regHead = Assets.instance.bunny.head;
		 // Center image on game object
		 origin.set(dimension.x / 2, dimension.y / 2);
		 // Bounding box for collision detection
		 bounds.set(0, 0, dimension.x, dimension.y);
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
		 hasFeatherPowerup = false;
		 timeLeftFeatherPowerup = 0;
	}
	/**
	 * sets bunnyHead to jumping state
	 * @param jumpKeyPressed
	 */
	public void setJumping (boolean jumpKeyPressed)
	{
		switch (jumpState)
		{
			case GROUNDED: // Character is standing on a platform
				if (jumpKeyPressed)
				{
					// Start counting jump time from the beginning
					timeJumping = 0;
					jumpState = JUMP_STATE.JUMP_RISING;
				}
				break;
				case JUMP_RISING: // Rising in the air
					if (!jumpKeyPressed)
						jumpState = JUMP_STATE.JUMP_FALLING;
					break;
				case FALLING:// Falling down
				case JUMP_FALLING: // Falling down after jump
					if (jumpKeyPressed && hasFeatherPowerup)
					{
						 timeJumping = JUMP_TIME_OFFSET_FLYING;
						 jumpState = JUMP_STATE.JUMP_RISING;
					}
					break;
			}

		};
	/**
	 * sets bunnyHead to feather mode
	 * @param pickedUp
	 */
	public void setFeatherPowerup (boolean pickedUp)
	{
		hasFeatherPowerup = pickedUp;
		if (pickedUp)
		{
			timeLeftFeatherPowerup = Constants.ITEM_FEATHER_POWERUP_DURATION;
		}
	};
	/**
	 * checks if bunny has feather power up
	 * @return
	 */
	public boolean hasFeatherPowerup ()
	{
		return hasFeatherPowerup && timeLeftFeatherPowerup > 0;
	};

	/**
	 * update method for our bunny
	 */
	@Override
	public void update (float deltaTime)
	{
		super.update(deltaTime);
		if(velocity.x != 0)
		{
			viewDirection = velocity.x < 0 ? VIEW_DIRECTION.LEFT : VIEW_DIRECTION.RIGHT;

		}
		if(velocity.x!= 0)
		{
			if(timeLeftFeatherPowerup > 0)
			{
				timeLeftFeatherPowerup -= deltaTime;
				if(timeLeftFeatherPowerup < 0)
				{
					//disable power-up
					timeLeftFeatherPowerup = 0;
					setFeatherPowerup(false);
				}
			}
		}
	}

	/**
	 * Render method for the bunny head
	 */
	@Override
	public void render(SpriteBatch batch)
	{
		TextureRegion reg = null;
		//set a special color when game object has a feather powerup
		if (hasFeatherPowerup)
		{
			batch.setColor(1.0f,0.8f,0.0f,1.0f);
		}
		//apply skin color
		batch.setColor(CharacterSkin.values()[GamePreferences.instance.charSkin].getColor());
		//draw image
		reg = regHead;
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
			super.updateMotionY(deltaTime);
	}
}
