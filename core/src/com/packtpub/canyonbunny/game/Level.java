package com.packtpub.canyonbunny.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.canyonbunny.objects.AbstractGameObject;
import com.packtpub.libgdx.canyonbunny.objects.Sky;
import com.packtpub.libgdx.canyonbunny.objects.Tiles;
import com.packtpub.libgdx.canyonbunny.objects.Buildings;
import com.packtpub.libgdx.canyonbunny.objects.Beer;
import com.packtpub.libgdx.canyonbunny.objects.Diploma;
import com.packtpub.libgdx.canyonbunny.objects.Main;

/**
 *
 * @author Dalton
 *
 */

public class Level
{
	public static final String TAG = Level.class.getName(); //uses TAG to print lines in console
	public Main main;
	public Array<Beer> beer;
	public Array<Diploma> diploma;


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
		//player character
		main = null;

		//objects
		tiles = new Array<Tiles>(); //array for holding all of the rocks
		beer = new Array<Beer>();
		diploma = new Array<Diploma>();

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
					obj = new Main();
					offsetHeight = -3.0f;
					obj.position.set(pixelX,baseHeight * obj.dimension.y + offsetHeight);
					main = (Main)obj;

				}
				//feather
				else if(BLOCK_TYPE.ITEM_BEER.sameColor(currentPixel))
				{
					obj = new Beer();
					offsetHeight = -1.5f;
					obj.position.set(pixelX,baseHeight * obj.dimension.y
							+ offsetHeight);
					beer.add((Beer)obj);
				}
				else if( BLOCK_TYPE.ITEM_PAPER.sameColor(currentPixel))
				{
					obj = new Diploma();
					offsetHeight = -1.5f;
					obj.position.set(pixelX,baseHeight * obj.dimension.y
							+ offsetHeight);
					diploma.add((Diploma)obj);
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

		//draw player
		main.render(batch);

		//draw beers
		for(Beer beer : beer)
			beer.render(batch);

		//draw diplomas
		for(Diploma diploma : diploma)
			diploma.render(batch);

	}
	/**
	 * updates everything loaded in the level when called
	 * @param deltaTime
	 */
	public void update(float deltaTime)
	{
		main.update(deltaTime);
		for (Tiles tile : tiles)

			tile.update(deltaTime);

		for(Beer beer : beer)

			beer.update(deltaTime);

		for(Diploma diploma : diploma)

			diploma.update(deltaTime);

	}

}