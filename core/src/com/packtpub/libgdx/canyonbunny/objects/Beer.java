package com.packtpub.libgdx.canyonbunny.objects;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.canyonbunny.game.Assets;
/**
 * Beer class
 * @author Dalton
 *
 */

public class Beer extends AbstractGameObject
{
	public boolean collected; //boolean for if beer is collected

	private TextureRegion regBeer;
	/**
	 * beer constructor
	 */
	public Beer()
	{
		init();

	}

	/**
	 * renders a brew
	 */
	@Override
	public void render(SpriteBatch batch)
	{
		if (collected) return;
		TextureRegion reg = null;
		reg = regBeer;

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

		regBeer = Assets.instance.beer.beer;

		//set bounding box for collision detection
		bounds.set(0,0,dimension.x,dimension.y);

		collected = false;
	}

	/**
	 *
	 * @return the amount of points gained for touching a feather.
	 */
	public int getScore()
	{
		return 250;
	}

}