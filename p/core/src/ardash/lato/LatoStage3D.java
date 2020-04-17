package ardash.lato;

import java.util.List;

import com.badlogic.gdx.utils.viewport.Viewport;

import ardash.gdx.scenes.scene3d.Actor3D;
import ardash.gdx.scenes.scene3d.Stage3D;
import ardash.lato.terrain.Section;
import ardash.lato.terrain.TerrainManager.TerrainListener;

public class LatoStage3D extends Stage3D implements TerrainListener{
	
	enum Type {FOREGROUND, BACKGROUND}
	public final Type type;
	
	public LatoStage3D(Type t, Viewport v) {
		super(v);
		this.type = t;
	}

	@Override
	public void onNewSectionCreated(Section s) {
		List<Actor3D> items = type== Type.FOREGROUND ? s.getForegroundItems() : s.getBackgroundItems();
		for (Actor3D a : items) {
			addActor(a);
		}
	}

}
