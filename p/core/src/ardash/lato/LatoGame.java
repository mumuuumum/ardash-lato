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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;

import ardash.gdx.scenes.scene3d.pooling.PoolsManager;
import ardash.lato.screens.LoadingScreen;

public class LatoGame extends Game {
	public GameManager gm;
//	Assets assets;
    //create paths
    private CatmullRomSpline<Vector2> path1; 
    private CatmullRomSpline<Vector2> path2;
    private ShapeRenderer sr;
    

    @Override
    public void create () {
    	PoolsManager.init();
    	gm = new GameManager(this);
    	setScreen(new LoadingScreen(gm));

//        // set up random control points
//        int width = Gdx.graphics.getWidth();
//        int height = Gdx.graphics.getHeight();
//        int points = 8;
//        Vector2[] controlPoints = new Vector2[points];
//        for (int i = 0; i < points; i++) {
//           int x = (int) (Math.random() * width) ;
//           int y = (int) (Math.random() * height);
//           Vector2 point = new Vector2(x, y);
//           controlPoints[i] = point;
//        }
//        
//        controlPoints[0].set(50, 400);
//        controlPoints[1].set(100, 350);
//        controlPoints[2].set(150, 360);
//        controlPoints[3].set(200, 300);
//        controlPoints[4].set(250, 310);
//        controlPoints[5].set(300, 250);
//        controlPoints[6].set(350, 260);
//        controlPoints[7].set(400, 200);
//
//        // set up the curves
//        path1 = new CatmullRomSpline<Vector2>(controlPoints, false);
//        path2 = new CatmullRomSpline<Vector2>(controlPoints, false);
//
//        // setup ShapeRenderer
//        sr = new ShapeRenderer();
//        sr.setAutoShapeType(true);
    }




//    @Override
//    public void render () {
//        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        sr.begin();
//        sr.setColor(Color.WHITE);
//
//        //draw path1
//        for(int i = 0; i < 100; ++i){
//            float t = i /100f;
//            // create vectors to store start and end points of this section of the curve
//            Vector2 st = new Vector2();
//            Vector2 end = new Vector2();
//            // get the start point of this curve section
//            path1.valueAt(st,t);
//            // get the next start point(this point's end)
//            path1.valueAt(end, t-0.01f);
//            // draw the curve
//            sr.line(st.x, st.y, end.x, end.y);
//
//        }
//
//        //same as above but for catmullrom
//        sr.setColor(Color.RED);
//        for(int i = 0; i < 100; ++i){
//            float t = i /100f;
//            Vector2 st = new Vector2();
//            Vector2 end = new Vector2();
//            path2.valueAt(st,t);
//            path2.valueAt(end, t-0.01f);
//            sr.line(st.x, st.y, end.x, end.y);
//
//        }
//
//        sr.end();
//    }

    @Override
    public void dispose () {
		A.dispose();
	}
}
