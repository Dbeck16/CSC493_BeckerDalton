package com.packtpub.libgdx.canyonbunny.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.packtpub.canyonbunny.game.Assets;


public class Sky extends AbstractGameObject
{
	private TextureRegion Sky;

	private int length;

	public Sky (int length)
	{
		this.length = length;
		init();
	}

	private void init()
	{
		dimension.set(10, 2);

		Sky = Assets.instance.levelDecoration.sky;

		origin.x = -dimension.x * 2;
		length += dimension.x *2;
	}

	private void drawSky(SpriteBatch batch)
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
			reg = Sky;
			batch.draw(reg.getTexture(), origin.x + xRel + dimension.x,
					position.y - dimension.y, origin.x, origin.y + yRel/3,
					dimension.x, dimension.y, scale.x, scale.y * dimension.y + yRel, rotation,
					reg.getRegionX(), reg.getRegionY(),reg.getRegionWidth(),
					reg.getRegionHeight(), false, true);
			xRel += dimension.x;
		}
		//reset color to white
	}

	@Override
	public void render(SpriteBatch batch)
	{
		//distant mountains (dark grey)
		drawSky(batch);
	}
}