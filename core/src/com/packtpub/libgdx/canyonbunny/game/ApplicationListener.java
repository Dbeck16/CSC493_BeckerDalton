package com.packtpub.libgdx.canyonbunny.game;

/**
 *
 * @author Dalton Becker
 *	Interface for holding all CanyonBunnyMain
 */
public interface ApplicationListener
{
	//Function for adjusting camera on resize of windows
	void resize(int width, int height);

	//*Futile* for mobile users if they close the app
	void pause();
	//*futile* for mobile users when they resume the app
	void resume();

	//Gets rid of resources upon closing
	void dispose();

	//Used to redraw the screen
	void render();

	//
	void create();

}
