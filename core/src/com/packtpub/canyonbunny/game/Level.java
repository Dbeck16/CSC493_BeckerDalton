package com.packtpub.canyonbunny.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.canyonbunny.objects.AbstractGameObject;
import com.packtpub.libgdx.canyonbunny.objects.Clouds;
import com.packtpub.libgdx.canyonbunny.objects.Mountains;
import com.packtpub.libgdx.canyonbunny.objects.Rock;
import com.packtpub.libgdx.canyonbunny.objects.WaterOverlay;
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
		ROCK(0, 255, 0), //green
		PLAYER_SPAWNPOINT(255, 255, 255), //white
		ITEM_FEATHER(255, 0, 255), // purple
		ITEM_GOLD_COIN(255, 255, 0); // yellow

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
	public Array<Rock> rocks;

	//decoration
	public Clouds clouds;
	public Mountains mountains;
	public WaterOverlay waterOverlay;

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
		rocks = new Array<Rock>(); //array for holding all of the rocks

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
				else if (BLOCK_TYPE.ROCK.sameColor(currentPixel))
				{
					if (lastPixel != currentPixel)
					{
						obj = new Rock();
						float heightIncreaseFactor = 0.25f;
						offsetHeight = -2.5f;
						obj.position.set(pixelX, baseHeight * obj.dimension.y * heightIncreaseFactor + offsetHeight);
						rocks.add((Rock)obj);
					}
					else
					{
						rocks.get(rocks.size -1).increaseLength(1);
					}
				}
				//player spawn point
				else if(BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel))
				{
				}
				//feather
				else if(BLOCK_TYPE.ITEM_FEATHER.sameColor(currentPixel))
				{
				}
				else if( BLOCK_TYPE.ITEM_GOLD_COIN.sameColor(currentPixel))
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
		clouds = new Clouds(pixmap.getWidth());
		clouds.position.set(0, 2);
		mountains = new Mountains(pixmap.getWidth());
		mountains.position.set(-1, -1);
		waterOverlay = new WaterOverlay(pixmap.getWidth());
		waterOverlay.position.set(0, -3.75f);

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
		//Draw Mountains
		mountains.render(batch);

		//Draw Rocks
		for(Rock rock : rocks)
			rock.render(batch);
		//Draw Water Overlay
		waterOverlay.render(batch);

		//Draw clouds
		clouds.render(batch);
	}

}
