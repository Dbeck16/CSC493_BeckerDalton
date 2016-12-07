package com.packtpub.libgdx.canyonbunny.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.packtpub.libgdx.canyonbunny.game.Assets;
/**
 * Abstract game screen for our game
 * @author Dalton
 *
 */
public abstract class AbstractGameScreen implements Screen
{
	protected Game game;


	public AbstractGameScreen(Game game)
	{
		this.game = game;
	}


	public abstract void render (float deltaTime); //render method
	public abstract void resize(int width, int height); //handles what to do when window is resized
	public abstract void show(); //shows menus
	public abstract void hide(); //Hides menus
	public abstract void pause(); // pauses games

	public void resume()
	{
		Assets.instance.init(new AssetManager()); //resumes game
	}
/**
 * Disposes of assets that were created
 */
	public void dispose()
	{
		Assets.instance.dispose();
	}
}
