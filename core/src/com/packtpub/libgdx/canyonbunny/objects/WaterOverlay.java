package com.packtpub.libgdx.canyonbunny.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.canyonbunny.game.Assets;
/**
 * WaterOverlay class for creating our water
 * @author Dalton
 *
 */
public class WaterOverlay extends AbstractGameObject
{
	private TextureRegion regWaterOverlay;
	private float length;
	/**
	 * WaterOverlay Constructor
	 * @param length
	 */
	public WaterOverlay (float length)
	{
		this.length = length;
		init();
	}

	/**
	 * Init funcion for the water overlay
	 */
	private void init()
	{
		dimension.set(length * 10, 3);
		regWaterOverlay = Assets.instance.levelDecoration.waterOverlay;

		origin.x = -dimension.x/2;
	}

	/**
	 * Renders the WaterOverlay
	 */
	@Override
	public void render(SpriteBatch batch)
	{
		TextureRegion reg = null;
		reg = regWaterOverlay;
		batch.draw(reg.getTexture(), position.x + origin.x, position.y + origin.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}
}
