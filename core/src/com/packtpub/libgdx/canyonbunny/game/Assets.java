package com.packtpub.libgdx.canyonbunny.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.packtpub.libgdx.canyonbunny.util.Constants;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

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
	public AssetOrbs orbs;
	public AssetSounds sounds; //sounds assets.
	public AssetMusic music; //music assets


	//initializes the asset manager for this class
	public void init(AssetManager assetManager)
	{
		this.assetManager = assetManager;
		// set asset manager error handler
		assetManager.setErrorListener(this);

		// load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		// load sounds
		assetManager.load("../core/assets/sounds/jump.wav", Sound.class);
		assetManager.load("../core/assets/sounds/jump_with_feather.wav", Sound.class);
		assetManager.load("../core/assets/sounds/pickup_coin.wav", Sound.class);
		assetManager.load("../core/assets/sounds/pickup_feather.wav", Sound.class);
		assetManager.load("../core/assets/sounds/live_lost.wav", Sound.class);
		assetManager.load("../core/assets/sounds/pickup_orb.wav", Sound.class);
		assetManager.load("../core/assets/sounds/goal_sound.wav", Sound.class);


		// load music
		assetManager.load("../core/assets/music/keith303_-_brand_new_highscore.mp3", Music.class);

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
		orbs = new AssetOrbs(atlas);
		levelDecoration = new AssetLevelDecoration(atlas);
		fonts = new AssetFonts();
		sounds = new AssetSounds(assetManager);
		music = new AssetMusic(assetManager);

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
		 public final AtlasRegion goal;


		 public AssetLevelDecoration (TextureAtlas atlas)
		 {
			 buildings = atlas.findRegion("Buildings");
			 sky = atlas.findRegion("Sky");
			 goal = atlas.findRegion("goal");
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
		public final Animation animScroll;
		public AssetScroll(TextureAtlas atlas)
		{
			scroll = atlas.findRegion("Scroll");

			Array<AtlasRegion> regions = atlas.findRegions("anim_scroll");
			AtlasRegion region = regions.first();
			for (int i = 0; i < 10; i++)
				regions.insert(0, region);
			animScroll = new Animation (1.0f / 5.0f, regions, Animation.PlayMode.LOOP);
		}
	}

	public class AssetOrbs
	{
		public final AtlasRegion orbs;
		public final Animation animOrbs;
		public AssetOrbs(TextureAtlas atlas)
		{
			orbs = atlas.findRegion("orb_01");

			Array<AtlasRegion> regions = null;
			AtlasRegion region = null;

			regions = atlas.findRegions("orb");
			region = regions.first();
			for (int i = 0; i < 2; i++)
				regions.insert(0, region);
			animOrbs = new Animation (1.0f/10.f, regions, Animation.PlayMode.LOOP_PINGPONG);
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
	/**
	 * sub class for handling game sounds
	 * @author Dalton
	 *
	 */
	public class AssetSounds
	{
		public final Sound jump;
		public final Sound jumpWithFeather;
		public final Sound pickupCoin;
		public final Sound pickupFeather;
		public final Sound liveLost;
		public final Sound PickupOrb;
		public final Sound Goal;
		public AssetSounds (AssetManager am)
		{
			jump = am.get("../core/assets/sounds/jump.wav", Sound.class);
			jumpWithFeather = am.get("../core/assets/sounds/jump_with_feather.wav",Sound.class);
			pickupCoin = am.get("../core/assets/sounds/pickup_coin.wav", Sound.class);
			pickupFeather = am.get("../core/assets/sounds/pickup_feather.wav",Sound.class);
			PickupOrb = am.get("../core/assets/sounds/pickup_orb.wav", Sound.class);
			Goal = am.get("../core/assets/sounds/goal_sound.wav", Sound.class);
			liveLost = am.get("../core/assets/sounds/live_lost.wav", Sound.class);
		}
	}

	/**
	 * sub class for handling music
	 * @author Dalton
	 *
	 */
	public class AssetMusic
	{
		public final Music song01;
		public AssetMusic (AssetManager am)
		{
			song01 = am.get("../core/assets/music/keith303_-_brand_new_highscore.mp3",	Music.class);
		}
	}

}
