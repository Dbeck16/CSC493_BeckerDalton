package com.packtpub.libgdx.canyonbunny.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.packtpub.libgdx.canyonbunny.game.Assets;

/**
 * diploma class
 * @author Dalton
 *
 */
public class Diploma extends AbstractGameObject
{
	private TextureRegion regDiploma;
	public boolean collected; //Keeps track of if we collected a coin

	/**
	 * diploma constructor
	 */
	public Diploma ()
	{
		init();
	}
	/**
	 * initializes our diploma
	 */
	private void init ()
	{
		dimension.set(0.5f, 0.5f);

		setAnimation(Assets.instance.scroll.animScroll);
		stateTime = MathUtils.random(0.0f, 1.0f);

		regDiploma = Assets.instance.scroll.scroll;
		// Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		collected = false;
	}
	/**
	 * renders a diploma
	 */
	public void render (SpriteBatch batch)
	{
		if (collected) return;
		TextureRegion reg = null;
		reg = animation.getKeyFrame(stateTime, true);
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