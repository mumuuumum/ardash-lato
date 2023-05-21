package ardash.lato;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.PerformanceCounter;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;

import ardash.gdx.scenes.scene3d.Actor3D;
import ardash.gdx.scenes.scene3d.Stage3D;
import ardash.lato.actors.WaveDrawer;
import ardash.lato.actors3.TerrainItem;
import ardash.lato.terrain.Section;
import ardash.lato.terrain.TerrainManager.TerrainListener;

public class LatoStage3D extends Stage3D implements TerrainListener {

	private static int sc = 0;
    protected PerformanceCounter pcact = Actor3D.getGameManager().performanceCounters.add("s3d act "+sc);
    protected PerformanceCounter pcdra = Actor3D.getGameManager().performanceCounters.add("s3d dra "+sc++);
    public World world = null;
    Box2DDebugRenderer worldRenderer;
	private double worldAccumulator = 0;
	public static final float DRAW_STEPS=WaveDrawer.DRAW_STEPS;


	public LatoStage3D(Viewport v) {
		super(v);
	}

	public LatoStage3D(Viewport v, ShaderProvider shaderProvider) {
		super(v, shaderProvider);
	}

	@Override
	public void onNewSectionCreated(Section s) {
		List<TerrainItem> items = s.getSurroundingItems();
		
		// add the new actors (itmes surrounding the terrain)
		for (TerrainItem a : items) {
			addActor(a);
		}
		
		// and remove the old stuff (use same offset as in wavedrawer)
		float border = getRoot().getGameManager().getGameScreen().performer.getX()-WaveDrawer.PASSED_TERRAIN;
		List<TerrainItem> canBeDeleted = new LinkedList<TerrainItem>();
		for (Actor3D a : getRoot().getChildren()) {
			if (a instanceof TerrainItem) {
				if (a.getX()<border)
					canBeDeleted.add((TerrainItem)a);
			}
		}
		
		for (TerrainItem a : canBeDeleted) {
			a.remove();
		}

		// add new parts to the physical world
		float firstX = s.first().x;
		float lastX = s.last().x;
		List<Float> pp = new ArrayList<Float>(100);
		for (float x = firstX; x<lastX-DRAW_STEPS ; x+=DRAW_STEPS)
		{
//			float toX = x-DRAW_STEPS;
//			float toY = terrainSegmentList.heightAt(toX);
			
			
			float y = s.heightAt(x);
			pp.add(x);
			pp.add(y);
			
//			float[] fa = {x,y, toX,toY, toX,y-500f, x, y-500f};
//			sr.polygon(fa);
//			counter ++;
		}
		pp.add(lastX);
		pp.add(s.heightAt(lastX));

		for (int i=0 ; i<pp.size()-2 ; i+=2)
		{
			// Create our body definition
			BodyDef groundBodyDef = new BodyDef();  
			// Set its world position
			groundBodyDef.position.set(new Vector2(0, 0));  

			// Create a body from the definition and add it to the world
			Body groundBody = world.createBody(groundBodyDef);  

			// Create a polygon shape
			PolygonShape groundBox = new PolygonShape();
			float x1 = pp.get(i+0);
			float y1 = pp.get(i+1);
			float x2 = pp.get(i+2);
			float y2 = pp.get(i+3);
			float x3 = x2;
			float y3 = y2-50;
			float x4 = x1;
			float y4 = y3;
			// build poly counter clockwise for box2d
			float[] verts = {x1,y1,x4,y4,x3,y3,x2,y2};
			groundBox.set(verts );
			// Create a fixture from our polygon shape and add it to our ground body  
			final Fixture fixture = groundBody.createFixture(groundBox, 0.0f); 
			fixture.setRestitution(0f);
			fixture.setFriction(0.0099f);
			// Clean up after ourselves
			groundBox.dispose();
			groundBody.setUserData(new Float(x1));
		}
		
		// delete old world-ground
		Array<Body> bodies = new Array<Body>(world.getBodyCount());
		world.getBodies(bodies);
		for (Body a : bodies) {
			if (a.getUserData() instanceof Float) {
				Float xx = (Float)a.getUserData();
				if (xx <border)
					world.destroyBody(a);
//					canBeDeleted.add((TerrainItem)a);
			}
		}


		
	}

	private void addActor(TerrainItem a) {
		if (a.getTag() == null)
			throw new RuntimeException("surrounding item must have a tag");
		this.addActor((Actor3D)a);		
	}

	@Override
	public void draw() {
    	pcdra.start();
		super.draw();
        pcdra.stop();
	}
	
	@Override
	public void draw(boolean in3grounds) {
    	pcdra.start();
		super.draw(in3grounds);
        pcdra.stop();
//        if (worldRenderer != null)
//        	worldRenderer.render(world, getCamera().combined);
	}
	
	@Override
	public void act(float delta) {
    	pcact.start();
    	if (world != null)
    		doPhysicsStep(delta);
		super.act(delta);
		pcact.stop();
	}
	
	public void enablePhysics() {
		world = new World(new Vector2(0f, -9.80665f), true);
		worldRenderer = new Box2DDebugRenderer(true, false, false, true, true, true);
		world.setContactListener(new ContactListener());
	}

	private void doPhysicsStep(float deltaTime) {
	    // fixed time step
	    // max frame time to avoid spiral of death (on slow devices)
	    float frameTime = Math.min(deltaTime, 0.25f);
	    worldAccumulator += frameTime;
	    float TIME_STEP = 1f/60f;
		while (worldAccumulator >= TIME_STEP ) {
	        world.step(TIME_STEP, 60, 20);
//	        world.step(TIME_STEP, 6, 2);
	        worldAccumulator -= TIME_STEP;
	    }
	}
	
	@Override
	public void dispose() {
		super.dispose();
		Disposables.gracefullyDisposeOf(world, worldRenderer);
	}
}
