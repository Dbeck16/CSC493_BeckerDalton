package com.packtpub.libgdx.canyonbunny.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.canyonbunny.game.Assets;

/**
 * GoldCoin class
 * @author Dalton
 *
 */
public class GoldCoin extends AbstractGameObject
{
	private TextureRegion regGoldCoin;
	public boolean collected; //Keeps track of if we collected a coin

	/**
	 * coin constructor
	 */
	public GoldCoin ()
	{
		init();
	}
	/**
	 * initializes our coin
	 */
	private void init ()
	{
		dimension.set(0.5f, 0.5f);
		regGoldCoin = Assets.instance.goldCoin.goldCoin;
		// Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		collected = false;
	}
	/**
	 * renders a coin
	 */
	public void render (SpriteBatch batch)
	{
		if (collected) return;
		TextureRegion reg = null;
		reg = regGoldCoin;
		batch.draw(reg.getTexture(), position.x, position.y,
				origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
				rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
 }
/**
 * gives a score value when we collect a coin
 * @return
 */
 public int getScore()
 	{
	 	return 100;
 	}
}
