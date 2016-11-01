package com.packtpub.libgdx.canyonbunny.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.canyonbunny.game.Assets;

/**
 * Tiles class for the platform in game
 * @author Dalton
 *
 */
public class Tiles extends AbstractGameObject
{
	private TextureRegion regTile; //region of our tile

	private int length; //how long the tiles are

	/**
	 * Tile constructor
	 */
	public Tiles()
	{
		init();
	}

	/**
	 * initializes our tiles
	 */

	private void init()
	{
		dimension.set(1,1.5f);


		regTile = Assets.instance.tile.tile;

		//start length of this rock
		setLength(1);
	}
	/**
	 * method for changing the lengths of tiles
	 * @param length
	 */
	public void setLength(int length)
	{
		this.length = length;
		//update the bounding box for collision detection.
		bounds.set(0,0,dimension.x *length,dimension.y);
	}
	
	/**
	 * ability to increase lengths of tiles
	 * @param amount
	 */
	public void increaseLength(int amount)
	{
		setLength(length += amount);
	}
/**
 * renders our tiles
 */
	@Override
	public void render(SpriteBatch batch)
	{
		TextureRegion reg = null;

		float relX = 0;
		float relY = 0;


		//Draw middle
		relX = 0;
		reg = regTile;
		for(int i = 0; i < length; i++)
		{
			batch.draw(reg.getTexture(), position.x + relX,
					position.y + relY, origin.x, origin.y, dimension.x,
					dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),
					reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
					false, false);
			relX+=dimension.x;
		}
	}

}