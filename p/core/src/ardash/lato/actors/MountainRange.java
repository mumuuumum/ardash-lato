package ardash.lato.actors;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Logger;

import ardash.lato.Assets;
import ardash.lato.Assets.SceneTexture;

public class MountainRange extends Group implements StageAccessor{

	/**
	 * speed in units per sec
	 */
	private float speed = 0f;
	
	/**
	 * numberof mountains in the mountain range
	 */
	private final int numPieces;
	private final float distanceBetweenPieces = 100f;

	public MountainRange(int numPieces) {
		this.numPieces =numPieces;
	}

	@Override
	public void init() {
		for (int i=0; i< numPieces; i++)
		{
			Image img = new Image(getAssets().getSTexture(SceneTexture.MOUNT));
			img.rotateBy(45f);
			img.moveBy(distanceBetweenPieces*i + MathUtils.random(-10f, 10f), MathUtils.random(-10f, 10f));
			addActor(img);
		}
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
//		int i = 0;
		for (int i=0; i< numPieces; i++)
		{
			final Actor child = getChild(i);
			child.moveBy(-speed*delta, 0);
			
			// all mountains will eventually move beyond -0. If that happens too far, send them back to the end.
			if (child.getX() < -child.getWidth()/2f )
			{
				child.moveBy(distanceBetweenPieces*numPieces , 0);
			}
			
		}
		
//		System.out.println(child.getX());
//		System.out.println(child.getWidth());
	}
}
