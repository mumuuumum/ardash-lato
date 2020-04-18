package ardash.gdx.scenes.scene3d.shape;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

import ardash.gdx.scenes.scene3d.Group3D;

/**
 * In image in a group that allows it to be displaced locally. 
 * Implements additional functionality for Origin (rotation center).
 * Put the image into 2 boxes. The offset translates the image to the origin of box 1. All rotations are applied to box1 only.
 * Box 2 moves it back to the previous position. (Box means coordinate system). Box2 == this
 * @author z
 *
 */
public class OffsetImage3D extends Group3D{
	
	private final Image3D image;
	private final Group3D box1;
	public OffsetImage3D(Image3D wrappedImage) {
		this.image = wrappedImage;
		setScale(1,1,1);
		box1 = new Group3D();
		box1.addActor(image);
		addActor(box1);
		setName("box2:"+image.getName());
	}
	
	public Image3D getImage() {
		return image;
	}
	
	public void setOriginX(float x) {
		image.setPosition(x, image.getY());
	}

	public void setOriginY(float y) {
		image.setPosition(image.getX(), y);
	}
	
//	@Override
//	public void rotate(float amountYaw, float amountPitch, float amountRoll) {
//		image.rotate(amountYaw, amountPitch, amountRoll);
//	}
//	
//	@Override
//	public float getRotation() {
//		return image.getRotation();
//	}
//	
//	@Override
//		public void setRotation(float f) {
//			// TODO Auto-generated method stub
//			image.setRotation(f);
//		}
//	
//	@Override
//	public void setRotation(float newYaw, float newPitch, float newRoll) {
//		// TODO Auto-generated method stub
//		image.setRotation(newYaw, newPitch, newRoll);
//	}
	
	@Override
		public void draw(ModelBatch modelBatch, Environment environment, Tag tag) {
			super.draw(modelBatch, environment, tag);
		}

}
