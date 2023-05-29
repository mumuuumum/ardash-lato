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

import java.util.Collection;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.PerformanceCounters;

import ardash.lato.terrain.TerrainManager;
import ardash.lato.terrain.TerrainSeg;
import net.dermetfan.gdx.assets.AnnotationAssetManager;

public class GameManager {
	
	public static final boolean DEBUG_VIEW = false;
	
	public final LatoGame game;
	public final Assets assets;
	public final AnnotationAssetManager am;
	public TerrainManager tm;
	public PerformanceCounters performanceCounters = new PerformanceCounters();

	/**
	 * Indicates if forward movement is going on. User must tap initially to start and movement will end after crash.
	 */
	private boolean started;

	public GameManager(LatoGame game) {
		this.game = game;
		this.assets = new Assets();
		this.am = assets.manager;
		assets.loadAll();
		this.tm = new TerrainManager();
		reset();
	}
	
	private void reset() {
		tm.reset();
		started = false;
	}

	public Screen getScreen() {
		return game.getScreen();
	}

	public GameScreen getGameScreen() {
		return (GameScreen)game.getScreen();
	}
	
	public void setStarted(boolean started) {
		this.started = started;
	}
	
	private boolean isStarted()
	{
		return started;
	}

}
