package com.packtpub.libgdx.canyonbunny.util;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.packtpub.libgdx.canyonbunny.objects.AbstractGameObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;


/**
 * Basic function for the camera.
 * @author Dalton Becker
 *
 */
public class CameraHelper
{
	private static final String TAG = CameraHelper.class.getName();
	private final float MAX_ZOOM_IN = 0.25f; //max zoom in
	private final float MAX_ZOOM_OUT = 10.0f; //max zoom out

	private Vector2 position; //keeps track of camera position
	private float zoom;	//keeps track of how far zoomed in/out you are

	private AbstractGameObject target; //Keeps track of the target

	/**
	 * CameraHelper Constructor
	 */
	public CameraHelper()
	{
		position = new Vector2();
		zoom = 1.0f;
	}


	/**
	 *update method for cameraHelper
	 * @param deltaTime
	 */
	public void update (float deltaTime)
	{
		if (!hasTarget()) return;

		position.x = target.position.x + target.origin.x;
		position.y = target.position.y + target.origin.y;
	}


	/**
	 * Sets the position
	 * @param x
	 * @param y
	 */
	public void setPosition(float x, float y)
	{
		this.position.set(x, y);
	}

	/**
	 * gets position of the vector
	 * @return
	 */
	public Vector2 getPosition()
	{
		return position;
	}

	/**
	 * Adds zoom to the camera
	 * @param amount
	 */
	public void addZoom(float amount)
	{
		setZoom(zoom + amount);
	}

	/**
	 * sets the current zoom
	 * @param zoom
	 */
	public void setZoom(float zoom)
	{
		this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
	}
	/**
	 * gets the current zoom
	 *
	 * @return
	 */
	public float getZoom()
	{
		return zoom;
	}
	/**
	 * Sets a target for the camera
	 * @param target
	 */
	public void setTarget(AbstractGameObject target)
	{
		this.target = target;
	}
	/**
	 * Getter for which target is being followed
	 * @return current target
	 */
	public AbstractGameObject getTarget()
	{
		return target;
	}
	/**
	 * checks if we have a target
	 * @return if we have a target
	 */
	public boolean hasTarget()
	{
		return target != null;
	}
	/**
	 *
	 * @param target
	 * @return
	 */
	public boolean hasTarget(AbstractGameObject target)
	{
		return hasTarget() && this.target.equals(target);
	}
	/**
	 * updates camera information
	 * @param camera
	 */
	public void applyTo (OrthographicCamera camera)
	{
		camera.position.x = position.x;
		camera.position.y = position.y;

		camera.zoom = zoom;
		camera.update();
	}
	/**
	 *
	 * @return what the current TAG is
	 */
	public static String getTag()
	{
		return TAG;
	}

}