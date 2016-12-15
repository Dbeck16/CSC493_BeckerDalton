package com.packtpub.libgdx.canyonbunny.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
/**
 *
 * @author Dalton
 *
 */

public class GamePreferences
{
	public static final String TAG = GamePreferences.class.getName();

	public static final GamePreferences instance = new GamePreferences();

	public boolean sound;
	public boolean music;
	public float volSound;
	public float volMusic;
	public int charSkin;
	public boolean showFpsCounter;


	private Array hs;

	private Preferences prefs;

	//singleton: to precent instantation of our preferences
	// singleton: prevent instantiation from other classes
	/**
	 * Constructor for Game Preferences
	 */
	private GamePreferences ()
	{
		prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
		hs = new Array<Integer>();
		hs.setSize(4);
		hs.set(0, 0);
		hs.set(1, 0);
		hs.set(2, 0);
	}

	/**
	 * Loads preferences for options
	 */
	public void load()
	{
		sound = prefs.getBoolean("sound", true);

		music = prefs.getBoolean("music", true);

		volSound = MathUtils.clamp(prefs.getFloat("volSound", 0.5f), 0.0f, 1.0f);

		volMusic = MathUtils.clamp(prefs.getFloat("volMusic", 0.5f), 0.0f, 1.0f);

		showFpsCounter = prefs.getBoolean("showFpsCounter", false);

	}
	/**
	 * allows the user to change preferences and save them
	 */
	public void save()
	{
		prefs.putBoolean("sound", sound);

		prefs.putBoolean("music", music);

		prefs.putFloat("volSound", volSound);

		prefs.putFloat("volMusic", volMusic);

		prefs.putInteger("charSkin", charSkin);

		prefs.putBoolean("showFpsCounter", showFpsCounter);

		prefs.flush();
	}

	public void addHS(int i)
	{
		hs.set(3, i);
		hs.sort();
		hs.reverse();

		if(hs.get(3) != null)
		{
			hs.set(3, 0);
		}
	}

	public Object getHS(int i)
	{
		return hs.get(i);
	}



}
