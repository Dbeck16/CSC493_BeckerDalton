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


public class Level
{
	public static final String TAG = Level.class.getName();
	public enum BLOCK_TYPE
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

		public boolean sameColor (int color)
		{
			return this.color == color;
		}
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

	public Level (String filename)
	{
		init(filename);
	}

	private void init (String filename) {}
	public void render (SpriteBatch batch){}

}
