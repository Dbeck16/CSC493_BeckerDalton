package com.packtpub.libgdx.canyonbunny.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.packtpub.libgdx.canyonbunny.game.Assets;
/**
 * Building class for scenery
 * @author Dalton
 *
 */
public class Buildings extends AbstractGameObject
{
	private TextureRegion Building;

	private int length;
	/**
	 * constructor for buildings class
	 * @param length
	 */
	public Buildings (int length)
	{
		this.length = length;
		init();
	}
	/**
	 * initilializes the buildings
	 */
	private void init()
	{
		dimension.set(20, 4);

		 Building = Assets.instance.levelDecoration.buildings;

		origin.x = -dimension.x * 2;
		length += dimension.x *2;
	}
	/**
	 * Draws our lovely buildings
	 * @param batch
	 */
	private void drawBuildings(SpriteBatch batch)
	{
		TextureRegion reg = null;

		float xRel = dimension.x;
		float yRel = dimension.y;

		//Mountains span the whole level
		int mountainLength = 0;

		mountainLength += MathUtils.ceil(length / (2 * dimension.x));

		for (int i = 0; i < mountainLength; i++)
		{
			//mountain left
			reg = Building;
			batch.draw(reg.getTexture(), origin.x + xRel + dimension.x,
					position.y - dimension.y, origin.x, origin.y,
					dimension.x, dimension.y, scale.x, scale.y + yRel, rotation,
					reg.getRegionX(), reg.getRegionY(),reg.getRegionWidth(),
					reg.getRegionHeight(), false, false);
			xRel += dimension.x;
		}
		//reset color to white
	}
/**
 * Renders the buildings
 */
	@Override
	public void render(SpriteBatch batch)
	{
		//distant mountains (dark grey)
		drawBuildings(batch);
	}
}