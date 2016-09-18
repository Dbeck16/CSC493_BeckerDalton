package com.packtpub.canyonbunny.game;

/**
 *
 * @author Dalton Becker
 *
 */
public interface ApplicationListener
{

	void resize(int width, int height);

	void pause();

	void resume();

	void dispose();

	void render();

	void create();

}
