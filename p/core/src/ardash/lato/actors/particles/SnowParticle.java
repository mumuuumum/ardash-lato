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
package ardash.lato.actors.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Pools;

import ardash.gdx.graphics.g3d.Particle;
import ardash.gdx.scenes.scene3d.actions.Actions3D;
import ardash.gdx.scenes.scene3d.actions.AlphaAction;
import ardash.gdx.scenes.scene3d.actions.FreeActorAction;
import ardash.lato.actions.GravityAction;

public class SnowParticle extends Particle implements Poolable {	

	public SnowParticle() {
		super();
		rotateBy(45f);
		setScale(0.2f);
		init();
	}
	
	@Override
	public boolean remove() {
		
		// remove() also is called on add(), but we only want to free the actor when it is actually being removed from a parent.
		if (hasParent())
			Pools.get(SnowParticle.class).free(this);
		boolean b = super.remove();
		return b;
	}
	
	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		super.act(delta);
//		addAction(Actions3D. color(Color.RED, 0.5f));
	}
	
	@Override
	public void reset() {
	}

	public void init() {
		// TODO Auto-generated method stub
		clearActions();
		setColor(Color.WHITE);
		final SnowParticle thisActor = this;
		final float dx = MathUtils.random(0.2f, 0.5f);
		final float dy = MathUtils.random(0.3f, 1.8f);
		final float du = MathUtils.random(0.3f, 0.5f);
		
		// action sequence to move up and down
//		addAction(Actions3D.sequence(
//				Actions3D.moveBy(-dx, dy, 0f, du),
//				new GravityAction()
//				));
		// throw-up and gravity parallel
		addAction(Actions3D.moveBy(-dx, dy, 0f, du));
		addAction(new GravityAction());
		
		// action sequence to get removed after lifetime
		AlphaAction fadeOutAction = Actions3D.fadeOut(0.4f);
//		fadeOutAction.setColor(Color.WHITE.cpy());
		addAction(Actions3D.sequence(
				Actions3D.delay(0.1f),
				fadeOutAction,
				Actions3D.removeActor(this)
//				Actions3D.freeActor(this)
				)); 
		
	}

}
