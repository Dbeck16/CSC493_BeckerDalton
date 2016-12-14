package com.packtpub.libgdx.canyonbunny.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class AbstractGameObject
{
	public Vector2 velocity;
	public Vector2 terminalVelocity;
	public Vector2 friction;
	public Vector2 acceleration;
	public Rectangle bounds;
	public Vector2 position;
	public Vector2 dimension;
    public Vector2 origin;
    public Vector2 scale;
    public float rotation;
    public Body body;
    public float stateTime;
    public Animation animation;


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
    	velocity = new Vector2();
    	terminalVelocity = new Vector2(1, 1);
    	friction = new Vector2();
    	acceleration = new Vector2();
    	bounds = new Rectangle();
    }

    public void setAnimation(Animation animation)
    {
    	this.animation = animation;
    	stateTime = 0;
    }

    /**
     * updates objects as they change
     * @param deltaTime
     */
    public void update(float deltaTime)
    {
    	stateTime += deltaTime;
    	if(body == null)
    	{
    		updateMotionX(deltaTime);
    		updateMotionY(deltaTime);
    		position.x += velocity.x * deltaTime;
    		position.y += velocity.y * deltaTime;
    	}
    	else
    	{
    		position.set(body.getPosition());
    		rotation = body.getAngle() * MathUtils.radiansToDegrees;
    	}
    }
    /**
     * Renders images
     * @param batch
     */
    public abstract void render (SpriteBatch batch);
    /**
     * updates how we move horizontally
     * @param deltaTime
     */
    protected void updateMotionX (float deltaTime)
    {
    	if (velocity.x != 0)
    	{
    		//apply friction
    		if(velocity.x > 0 )
    		{
    			Math.max(velocity.x - friction.x * deltaTime, 0);
    		}
    		else
    		{
    			velocity.x = Math.min(velocity.x + friction.x * deltaTime,  0);
    		}
    	}
    	// Apply acceleration
    	 velocity.x += acceleration.x * deltaTime;
    	 // Make sure the object's velocity does not exceed the
    	 // positive or negative terminal velocity
    	 velocity.x = MathUtils.clamp(velocity.x,
    	 -terminalVelocity.x, terminalVelocity.x);
    }
    /**
     * updates how we move vertically
     * @param deltaTime
     */
    protected void updateMotionY(float deltaTime)
    {
    	 if (velocity.y != 0)
    	 {
    		 // Apply friction
    		 if (velocity.y > 0)
    		 {
    			 velocity.y = Math.max(velocity.y - friction.y * deltaTime, 0);
    		 }
    		 else
    		 {
    			 velocity.y = Math.min(velocity.y + friction.y *
    					 deltaTime, 0);
    		 }
    	 }
    		 // Apply acceleration
    		 velocity.y += acceleration.y * deltaTime;
    		 // Make sure the object's velocity does not exceed the
    		 // positive or negative terminal velocity
    		 velocity.y = MathUtils.clamp(velocity.y, -
    		terminalVelocity.y, terminalVelocity.y);
    }



}

