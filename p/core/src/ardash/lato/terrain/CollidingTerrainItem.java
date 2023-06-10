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

import ardash.lato.actors3.TerrainItem;

/**
 * Indicates a Terrain Item that can collide with the Performer (like stones, ramps, coins).
 */
public interface CollidingTerrainItem extends TerrainItem{
	
	default void act(float delta) {
		// by doing collision detection here we reduce the work and the code needed in Performer (ie. if there is no Stone, nothing need to be done))
		detectCollision();
	}
	void detectCollision();
	void onCollision();	

}
