package ardash.lato.actors3;

import ardash.gdx.scenes.scene3d.Actor3D.Tag;

/**
 * A marker interface to mark items that are spread around the terrain and created by the Terraingenerator.
 * All methods in here, are implemented by Actor3D.
 * @author z
 *
 */
public interface TerrainItem {

	void moveBy(float x, float y);

	Tag getTag();

	float getZ();

	void setTag(Tag tag);

	boolean remove();

	void translate(float x, float y, float z);

}
