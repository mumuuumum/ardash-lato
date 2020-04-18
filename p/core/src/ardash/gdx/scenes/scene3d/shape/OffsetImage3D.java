package ardash.gdx.scenes.scene3d.shape;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

import ardash.gdx.scenes.scene3d.Group3D;

/**
 * In image in a group that allows it to be displaced locally. 
 * Implements additional functionality for Origin (rotation center).
 * @author z
 *
 */
public class OffsetImage3D extends Group3D{
	
	private final Image3D image;
	public OffsetImage3D(Image3D wrappedImage) {
		this.image = wrappedImage;
		setScale(1,1,1);
		addActor(image);
		setName("offset:"+image.getName());
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
	
	@Override
		public void draw(ModelBatch modelBatch, Environment environment, Tag tag) {
			// TODO Auto-generated method stub
			super.draw(modelBatch, environment, tag);
		}

}
