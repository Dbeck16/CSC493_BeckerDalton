package com.packtpub.libgdx.canyonbunny.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.packtpub.libgdx.canyonbunny.game.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

/**
 * orbs class
 * @author Dalton
 *
 */
public class Orbs extends AbstractGameObject
{
	private TextureRegion regorbs;
	public boolean collected; //Keeps track of if we collected a coin

	/**
	 * orbs constructor
	 */
	public Orbs ()
	{
		init();
	}
	/**
	 * initializes our orbs
	 */
	private void init ()
	{
		dimension.set(0.5f, 0.5f);

		setAnimation(Assets.instance.orbs.animOrbs);
		stateTime = MathUtils.random(0.0f, 1.0f);

		// Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		collected = false;

	}
	@Override
	public void update(float deltaTime)
	{
    	stateTime += deltaTime;
    	if(body == null)
    	{

    		updateMotionX(deltaTime);
    		updateMotionY(deltaTime);

    		position.x += velocity.x * deltaTime;
    		position.y += velocity.y * deltaTime;
    	}
    	else
    	{
    		position.set(body.getPosition());
    		rotation = body.getAngle() * MathUtils.radiansToDegrees;
    	}
	}
	/**
	 * renders a orbs
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
	 	return 1000;
 	}
}