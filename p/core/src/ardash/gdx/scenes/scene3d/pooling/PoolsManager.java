package ardash.gdx.scenes.scene3d.pooling;

import com.badlogic.gdx.utils.Pools;

import ardash.lato.actors.particles.SnowParticle;
import ardash.lato.actors3.Coin;
import ardash.lato.actors3.Farmhouse;

public class PoolsManager {
	public static void init() {
		Pools.get(Farmhouse.class, 100);
		Pools.get(SnowParticle.class, 200);
		Pools.get(Coin.class, 100);
		System.out.println("poolsinit");
	}
	
	public static void printStatusOutput() {
		System.out.println("Farmhouse: " + Pools.get(Farmhouse.class).getFree() + " peak: " + Pools.get(Farmhouse.class).peak);
		System.out.println("SnowParticle: " + Pools.get(SnowParticle.class).getFree() + " peak: " + Pools.get(SnowParticle.class).peak);
		System.out.println("Coin: " + Pools.get(Coin.class).getFree() + " peak: " + Pools.get(Coin.class).peak);
	}
}
