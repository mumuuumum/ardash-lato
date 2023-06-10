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
package ardash.lato.terrain;

import ardash.gdx.scenes.scene3d.Actor3D.Tag;
import ardash.gdx.scenes.scene3d.Camera3D;

public class DummyTerrainItem implements CollidingTerrainItem{

	@Override
	public void moveBy(float x, float y) {
		// nothing
	}

	@Override
	public Tag getTag() {
		return null;
	}

	@Override
	public float getX() {
		return 0;
	}

	@Override
	public float getZ() {
		return 0;
	}

	@Override
	public void setTag(Tag tag) {
		// nothing
	}

	@Override
	public boolean remove() {
		return false;
	}

	@Override
	public void translate(float x, float y, float z) {
		// nothing
	}
	
	@Override
	public boolean isCulled(Camera3D cam) {
		return true;
	}

	@Override
	public void detectCollision() {
		// nothing
	}

	@Override
	public void onCollision() {
		// nothing
	}

}
