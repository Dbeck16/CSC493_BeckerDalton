package com.packtpub.libgdx.canyonbunny.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.libgdx.canyonbunny.game.Assets;
/**
 * Feather class
 * @author Dalton
 *
 */

public class Feather extends AbstractGameObject
{
	public boolean collected;

	private TextureRegion regFeather;
	/**
	 * feather constructor
	 */
	public Feather()
	{
		init();

	}

	/**
	 * renders a feather
	 */
	@Override
	public void render(SpriteBatch batch)
	{
		if (collected) return;
		TextureRegion reg = null;
		reg = regFeather;

		batch.draw(reg.getTexture(), position.x, position.y,
				origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
				rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}
	/**
	 * initializes an instance of feather
	 */
	private void init()
	{
		dimension.set(0.5f, 0.5f);

		regFeather = Assets.instance.feather.feather;

		//set bounding box for collision detection
		bounds.set(0,0,dimension.x,dimension.y);

		collected = false;
	}

	public int getScore()
	{
		return 250;
	}

}
