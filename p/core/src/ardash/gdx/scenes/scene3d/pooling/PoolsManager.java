package ardash.gdx.scenes.scene3d.pooling;

import com.badlogic.gdx.utils.Pools;

import ardash.lato.actors3.Farmhouse;

public class PoolsManager {
	public static void init() {
		Pools.get(Farmhouse.class, 10);
		System.out.println("poolsinit");
	}
	
	public static void printStatusOutput() {
		System.out.println("Farmhouse: " + Pools.get(Farmhouse.class).getFree());
	}
}
