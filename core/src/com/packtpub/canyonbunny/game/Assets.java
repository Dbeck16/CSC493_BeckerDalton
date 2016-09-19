package com.packtpub.canyonbunny.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.packtpub.libgdx.canyonbunny.util.Constants;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
/**
 * Assets manager for keeping tracks of textures.
 * @author Dalton
 *
 */

public class Assets implements Disposable, AssetErrorListener
{


	//Sets the TAG for this class for console output
	public static final String TAG = Assets.class.getName();
	//creates a single Assets instance called instance
	public static final Assets instance = new Assets();

	private AssetManager assetManager;

	//Singleton: prevent instantation from other classes
	private Assets(){}


	public AssetMain main;
	public AssetDesk desk;

	//initializes the asset manager for this class
	public void init(AssetManager assetManager)
	{
		this.assetManager = assetManager;
		// set asset manager error handler
		assetManager.setErrorListener(this);

		// load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);

		// start loading assets and wait until finished
		assetManager.finishLoading();
		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames())
		{
			Gdx.app.debug(TAG, "asset: " + a);
		}


		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);

		//enable texture filtering for pixel smoothing
		for(Texture t : atlas.getTextures())
		{
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}

		//create the game resource objects.
		desk = new AssetDesk(atlas);
		main = new AssetMain(atlas);
	}


	//error methods are used to print errors to the console whenever we run into an error.1
	@Override
	public void error(AssetDescriptor asset, Throwable throwable)
	{
		Gdx.app.error(TAG,  "Couldn't load asset '" + asset.fileName +"'", (Exception)throwable);
	}

	//error methods are used to print errors to the console whenever we run into an error.
	public void error(String filename, Class type, Throwable throwable)
	{
		Gdx.app.error(TAG,  "Couldn't load asset '" + filename + "'", (Exception) throwable);
	}

	//Deletes the assetManager when called.
	@Override
	public void dispose()
	{
		assetManager.dispose();
		System.out.println("Disposed");
	}

	public class AssetMain
	{
		public final AtlasRegion main;

		public AssetMain (TextureAtlas atlas)
		{
			main = atlas.findRegion("Main");
		}

	}
	public class AssetDesk
	{
		public final AtlasRegion desk;

		public AssetDesk (TextureAtlas atlas)
		{
			desk = atlas.findRegion("desk");
		}

	}
/**	//sub class for the Bunny head
	public class AssetBunny
	{
		public final AtlasRegion head;

		public AssetBunny (TextureAtlas atlas)
		{
			head = atlas.findRegion("bunny_head");
		}

	}

	//subclass for the the rock and edges
	public class AssetRock
	{
		 public final AtlasRegion edge;
		 public final AtlasRegion middle;
		 public AssetRock (TextureAtlas atlas)
		 {
			 edge = atlas.findRegion("rock_edge");
			 middle = atlas.findRegion("rock_middle");
		 }
	}

	//subclass for building the gold coin texture
	public class AssetGoldCoin
	{
		 public final AtlasRegion goldCoin;
		 public AssetGoldCoin (TextureAtlas atlas)
		 {
			 goldCoin = atlas.findRegion("item_gold_coin");
		 }
	}

	//builds the texture for feathers
	public class AssetFeather
	{
		 public final AtlasRegion feather;
		 public AssetFeather (TextureAtlas atlas)
		 {
			 feather = atlas.findRegion("item_feather");
		 }
	}

	//builds all the decorations for levels.
	public class AssetLevelDecoration
	{
		 public final AtlasRegion cloud01;
		 public final AtlasRegion cloud02;
		 public final AtlasRegion cloud03;
		 public final AtlasRegion mountainLeft;
		 public final AtlasRegion mountainRight;
		 public final AtlasRegion waterOverlay;
		 public AssetLevelDecoration (TextureAtlas atlas)
		 {
			 cloud01 = atlas.findRegion("cloud01");
			 cloud02 = atlas.findRegion("cloud02");
			 cloud03 = atlas.findRegion("cloud03");
			 mountainLeft = atlas.findRegion("mountain_left");
			 mountainRight = atlas.findRegion("mountain_right");
			 waterOverlay = atlas.findRegion("water_overlay");
		 }
	}
*/
}
