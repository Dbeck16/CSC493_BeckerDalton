package com.packtpub.canyonbunny.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.canyonbunny.objects.AbstractGameObject;
import com.packtpub.libgdx.canyonbunny.objects.Sky;
import com.packtpub.libgdx.canyonbunny.objects.Tiles;
import com.packtpub.libgdx.canyonbunny.objects.Buildings;
/**
 *
 * @author Dalton
 *
 */

public class Level
{
	public static final String TAG = Level.class.getName(); //uses TAG to print lines in console


	public enum BLOCK_TYPE //hold values for level being loaded in.. using rgba values it can determine what each color does
	{
		EMPTY(0, 0, 0), //black
		TILE(0, 255, 0), //green
		PLAYER_SPAWNPOINT(255, 255, 255), //white
		ITEM_BEER(255, 0, 255), // purple
		ITEM_PAPER(255, 255, 0); // yellow

		private int color;

		private BLOCK_TYPE (int r, int g, int b)
		{
			color = r << 24 | g << 16 | b << 8 | 0xff;
		}

		/**
		 * Returns if something is the same color
		 * @param color
		 * @return Boolean for whether the colors are the same
		 */
		public boolean sameColor (int color)
		{
			return this.color == color;
		}
		/**
		 * getter for current color on the level
		 * @return the color at a pixel on the level
		 */
		public int getColor()
		{
			return color;
		}
	}
	//objects
	public Array<Tiles> tiles;

	//decoration
	public Sky sky;
	public Buildings buildings;

	/**
	 * Constructor for Level class
	 * @param filename
	 */
	public Level (String filename)
	{
		init(filename);
	}

/**
 * Initializes Level class
 * @param filename
 */
	private void init (String filename)
	{
		tiles = new Array<Tiles>(); //array for holding all of the rocks

		//load image file that respresents the level data
		Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
		//scan pixels from top-left to bottom right
		int lastPixel = -1;
		for(int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++)
		{
			for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++)
			{
				AbstractGameObject obj = null;
				float offsetHeight = 0;
				//height grows from bottom to top
				float baseHeight = pixmap.getHeight() - pixelY;
				//get color of current pixel as 32-bit RGBA value
				int currentPixel = pixmap.getPixel(pixelX, pixelY);
				//find matching color value to identify block type at (x,y) point and creat the corresponding game object if there is a match.

				//empty space
				if (BLOCK_TYPE.EMPTY.sameColor(currentPixel))
				{
					//LEAVE IT FRIGGIN BLANK
				}

				//rock
				else if (BLOCK_TYPE.TILE.sameColor(currentPixel))
				{
					if (lastPixel != currentPixel)
					{
						obj = new Tiles();
						float heightIncreaseFactor = 0.25f;
						offsetHeight = -2.5f;
						obj.position.set(pixelX, baseHeight * obj.dimension.y * heightIncreaseFactor + offsetHeight);
						tiles.add((Tiles)obj);
					}
					else
					{
						tiles.get(tiles.size -1).increaseLength(1);
					}
				}
				//player spawn point
				else if(BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel))
				{
				}
				//feather
				else if(BLOCK_TYPE.ITEM_BEER.sameColor(currentPixel))
				{
				}
				else if( BLOCK_TYPE.ITEM_PAPER.sameColor(currentPixel))
				{
				}
				else
				{
					int r = 0xff & (currentPixel >>> 24); //red color channel
					int g = 0xff & (currentPixel >>> 24); //green color channel
					int b = 0xff & (currentPixel >>> 24); //blue color channel
					int a = 0xff & (currentPixel >>> 24); //alpha color channel
					Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<"	+ pixelY + ">: r<" + r+ "> g<" + g + "> b<" + b + "> a<" + a + ">");
				}
				lastPixel = currentPixel;
			}
		}
		//decoration
		sky = new Sky(pixmap.getWidth());
		sky.position.set(0, 0);
		buildings = new Buildings(pixmap.getWidth());
		buildings.position.set(-1, -1);
		//free memory
		pixmap.dispose();
		Gdx.app.debug(TAG, "level '" + filename + "' loaded");
	}

	/**
	 * Renders all of the level objects
	 * @param batch
	 */
	public void render (SpriteBatch batch)
	{
		sky.render(batch);
		//Draw Mountains
		buildings.render(batch);

		//Draw Rocks
		for(Tiles tile : tiles)
			tile.render(batch);
		//Draw Water Overlay
	}

}