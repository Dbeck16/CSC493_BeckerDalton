package com.packtpub.libgdx.canyonbunny.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.canyonbunny.game.Assets;


public class Rock extends AbstractGameObject
{
	private TextureRegion regEdge; //holds reg edge
	private TextureRegion regMiddle; //holds reg middle

	private int length; //keeps track of the length of the rock
	/**
	 * Rock constructor
	 */
	public Rock()
	{
		init();
	}
	/**
	 * Rocks initialization function
	 */
	private void init()
	{
		dimension.set(1,1.5f);

		regEdge = Assets.instance.rock.edge;
		regMiddle = Assets.instance.rock.middle;

		//start length of this rock
		setLength(1);
	}
	/**
	 * Sets the length of the rock
	 * @param length
	 */
	public void setLength(int length)
	{
		this.length = length;
	}
	/**
	 * Gives the ability to increase the length of the rock
	 * @param amount
	 */
	public void increaseLength(int amount)
	{
		setLength(length += amount);
	}
	/**
	 * Renders the rock
	 */
	@Override
	public void render(SpriteBatch batch)
	{
		TextureRegion reg = null;

		float relX = 0;
		float relY = 0;

		//Draw left edge
		reg = regEdge;
		relX -= dimension.x/4;

		batch.draw(reg.getTexture(), position.x + relX, position.y +
			relY, origin.x, origin.y, dimension.x / 4, dimension.y,
			scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
			reg.getRegionWidth(), reg.getRegionHeight(), false, false);

		//Draw middle
		relX = 0;
		reg = regMiddle;
		for(int i = 0; i < length; i++)
		{
			batch.draw(reg.getTexture(), position.x + relX,
					position.y + relY, origin.x, origin.y, dimension.x,
					dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),
					reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
					false, false);
			relX+=dimension.x;
		}

		//Draw right edge
		reg = regEdge;
		batch.draw(reg.getTexture(), position.x + relX,
				position.y + relY, origin.x + dimension.x / 8, origin.y, dimension.x / 4,
				dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),
				reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
				true, false);
	}

}
