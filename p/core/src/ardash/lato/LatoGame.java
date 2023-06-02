/*******************************************************************************
 * Copyright (C) 2020-2023 Andreas Redmer <ar-lato@abga.be>
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

package ardash.lato;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import ardash.gdx.scenes.scene3d.pooling.PoolsManager;
import ardash.lato.screens.LoadingScreen;

public class LatoGame extends Game {
	public GameManager gm;

    @Override
    public void create () {
    	PoolsManager.init();
    	gm = new GameManager(this);
    	setScreen(new LoadingScreen(gm));
    }

    @Override
    public void setScreen(Screen screen) {
    	super.setScreen(screen);
    	System.gc();
    }
    
    @Override
    public void dispose () {
    	super.dispose();
    	screen.dispose();
		A.dispose();
	}
}
