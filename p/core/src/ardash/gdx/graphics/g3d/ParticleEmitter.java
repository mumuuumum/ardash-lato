/*******************************************************************************
 * Copyright (C) 2023 Andreas Redmer <ar-lato@abga.be>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package ardash.gdx.graphics.g3d;

import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pools;

import ardash.gdx.scenes.scene3d.Actor3D;
import ardash.lato.actors.particles.SnowParticle;

public class ParticleEmitter extends Actor3D {

	public enum ParticleEmitterType {
		SNOW, COINSPLASH
	};
	
	private ModelBuilder mb;
	private ParticleEmitterType type;
	private boolean isStarted;

	public ParticleEmitter(ParticleEmitterType type) {
		super();
		this.type = type;
		this.isStarted = false;
		this.mb = new ModelBuilder();
		setName("ParticleEmitter");
		setTag(Tag.CENTER);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (isStarted)
			spawnNewParticles();
	}

	private void spawnNewParticles() {
		for (int i = 0 ; i<5 ; i++) {
			Particle p;
			if (type == ParticleEmitterType.SNOW) {
				p = Pools.get(SnowParticle.class).obtain();
				p.init();
			}
			else {
				throw new RuntimeException("not implemented");
			}
			final float dx = MathUtils.random(-0.3f, 0.3f);
			final float dy = MathUtils.random(-0.3f, 0.3f);
			p.setPosition(getX()+dx, getY()+dy, getZ());
			this.getStage().addActor(p);
		}
	}
	

	public void startEmitting() {
		isStarted = true;
	}

	public void stopEmitting() {
		isStarted = false;
	}

}
