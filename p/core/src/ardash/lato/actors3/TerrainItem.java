package ardash.lato.actors3;

import ardash.gdx.scenes.scene3d.Actor3D.Tag;

public interface TerrainItem {

	void moveBy(float x, float y);

	Tag getTag();

	float getZ();

	void setTag(Tag tag);

	boolean remove();

	void translate(float x, float y, float z);

}
