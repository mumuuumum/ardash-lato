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

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import ardash.lato.actors3.AbyssCollider;
import ardash.lato.actors3.CliffLeft;
import ardash.lato.actors3.CliffRight;
import ardash.lato.actors3.TerrainItem;
import ardash.lato.terrain.TerrainSeg.TSType;

/**
 * 
 */
public class Canyon extends Section {
	public Canyon() {
		add (new Vector2(0,0), new Vector2(10,3), Interpolation.smooth);
		add (new Vector2(10,-30), new Vector2(25,-30), Interpolation.smooth, TSType.ABYSS);
		add (new Vector2(25,-5),new Vector2(40,-7), Interpolation.smooth);
	// TODO add cliff-sides
		// TODO add fog in bottom
		// TODO 
		
//		surroundingItems.add(new AbyssCollider(10, -40, 25, -5));
		// note: we can't use the collider to show fog
		surroundingItems.add(new AbyssCollider(9, -30, 17, 24.5f));
//		surroundingItems.add(new AbyssMist(9-35, -30-10, 17+35*2, 24.5f+10*2));
//		AbyssMist am2 = new AbyssMist(9-35, -30-10, 17+35*2, 24.5f+10*2);
//		am2.setZ(am2.getZ()+1);
//		surroundingItems.add(am2);
//		
		// cliffs can't be attached perfectly to the edge, because the share renderer moves it slightly, especially when removing old items
		TerrainItem cliffLeft = new CliffLeft(3.90f,-27.695f); 
		surroundingItems.add(cliffLeft);
		TerrainItem cliffRight = new CliffRight(21.50f,-35.4595f); 
		surroundingItems.add(cliffRight);
		// TODO don't do culling the t segments when a canyon is on the screen
		
		// mist in the abyss
		
		
//		surroundingItems.add( new Coin());
	}
}
