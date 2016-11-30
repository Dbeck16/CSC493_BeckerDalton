package com.packtpub.canyonbunny.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.packtpub.libgdx.canyonbunny.util.Constants;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

	public AssetFonts fonts;
	private AssetManager assetManager;

	//Singleton: prevent instantation from other classes
	private Assets(){}


	public AssetMain main;
	public AssetLevelDecoration levelDecoration;
	public AssetTiles tile;
	public AssetBeer beer;
	public AssetScroll scroll;

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
		main = new AssetMain(atlas);
		tile = new AssetTiles(atlas);
		beer = new AssetBeer(atlas);
		scroll = new AssetScroll(atlas);
		levelDecoration = new AssetLevelDecoration(atlas);
		fonts = new AssetFonts();
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
		fonts.defaultSmall.dispose();
		fonts.defaultNormal.dispose();
		fonts.defaultBig.dispose();
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
	public class AssetTiles
	{
		public final AtlasRegion tile;
		public AssetTiles(TextureAtlas atlas)
		{
			tile = atlas.findRegion("Tile");
		}
	}

	public class AssetLevelDecoration
	{
		 public final AtlasRegion buildings;
		 public final AtlasRegion sky;

		 public AssetLevelDecoration (TextureAtlas atlas)
		 {
			 buildings = atlas.findRegion("Buildings");
			 sky = atlas.findRegion("Sky");
		 }
	}
	public class AssetBeer
	{
		public final AtlasRegion beer;
		public AssetBeer(TextureAtlas atlas)
		{
			beer = atlas.findRegion("Beer");
		}
	}
	public class AssetScroll
	{
		public final AtlasRegion scroll;
		public AssetScroll(TextureAtlas atlas)
		{
			scroll = atlas.findRegion("Scroll");
		}
	}
	public class AssetFonts
	{
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;

		public AssetFonts ()
		{
		// create three fonts using Libgdx's 15px bitmap font
			defaultSmall = new BitmapFont(
				Gdx.files.internal("../core/assets/images/arial-15.fnt"), true);
			defaultNormal = new BitmapFont(
				Gdx.files.internal("../core/assets/images/arial-15.fnt"), true);
			defaultBig = new BitmapFont(
				Gdx.files.internal("../core/assets/images/arial-15.fnt"), true);

			// set font sizes

			defaultSmall.getData().setScale(0.75f);
			defaultNormal.getData().setScale(1.0f);
			defaultBig.getData().setScale(2.0f);

			// enable linear texture filtering for smooth fonts
			defaultSmall.getRegion().getTexture().setFilter(
				TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture().setFilter(
				TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture().setFilter(
				TextureFilter.Linear, TextureFilter.Linear);
			}

	}
}
