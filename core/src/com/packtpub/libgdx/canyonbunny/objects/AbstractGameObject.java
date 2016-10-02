package com.packtpub.libgdx.canyonbunny.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;


public abstract class AbstractGameObject
{
	public Vector2 position;
	public Vector2 dimension;
    public Vector2 origin;
    public Vector2 scale;
    public float rotation;
    /**
     * abstract class for all game objects
     */
    public AbstractGameObject()
    {
    	position = new Vector2(); //keeps track of position
    	dimension = new Vector2(1,1);	//keeps track of dimension
    	origin = new Vector2();	//keeps track of the origin
    	scale = new Vector2(1,1);	//keeps track of the scale of objects
    	rotation = 0;	//keeps track of any rotation
    }

    /**
     * updates objects as they change
     * @param deltaTime
     */
    public void update(float deltaTime)
    {

    }
    /**
     * Renders images
     * @param batch
     */
    public abstract void render (SpriteBatch batch);
}
