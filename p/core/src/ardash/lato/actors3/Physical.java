package ardash.lato.actors3;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import ardash.gdx.scenes.scene3d.Actor3D;

/**
 * An annotation interface to attach to Actor3D classes. Enables an actor to become part of the physical world (Performer, Rock, Ramp, ...)
 * @author Andreas Redmer
 */
public interface Physical {
	
	default void enablePhysics() {
		World world = getWorld();
		
		// First we create a body definition
		BodyDef bodyDef = new BodyDef();
		// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
		bodyDef.type = BodyType.DynamicBody;
		// Set our body's starting position in the world
		bodyDef.position.set(getX(), getY());
		bodyDef.fixedRotation=true;
//		bodyDef.d

		// Create our body in the world using our body definition
		Body body = world.createBody(bodyDef);
		body.setUserData(this);
		body.setLinearDamping(0.10f);
//		body.getMassData().mass=50f;
		body.setGravityScale(1f);
		
		// Create a circle shape and set its radius to 6
		CircleShape circle = new CircleShape();
		circle.setRadius(getWidth()/2f);

		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 02.5f; 
		fixtureDef.friction = 0.1f; //0.94f;
		fixtureDef.restitution = 0.000001f; // no bouncing

		// Create our fixture and attach it to the body
		Fixture fixture = body.createFixture(fixtureDef);
		fixture.setUserData(this);

		// Remember to dispose of any shapes after you're done with them!
		// BodyDef and FixtureDef don't need disposing, but shapes do.
		circle.dispose();
		
		((Actor3D)this).userData = body;
	}
	
	default Body getBody()
	{
		return (Body)((Actor3D)this).userData;
	}

	float getX();
	float getY();
	float getWidth();


	World getWorld();
	
	boolean isPhysicsEnabled();

//	default World getPhys()
//	{
//		getStage();
//	}
//
//	Stage3D getStage();

}
