package com.packtpub.canyonbunny.game;

public interface ApplicationListener
{

	void resize(int width, int height);

	void pause();

	void resume();

	void dispose();

	void render();

	void create();

}
