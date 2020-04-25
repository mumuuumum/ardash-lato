package ardash.lato;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

import ardash.lato.actors.Performer;

public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener {
	private static final float MIN_SPEED = 9.3f;
	private static final float MAX_SPEED = 29.3f;
	private float lastSpeed;

	@Override
	public void beginContact(Contact contact) {
		final Object uda = contact.getFixtureA().getUserData();
		final Object udb = contact.getFixtureB().getUserData();
		if (uda instanceof Performer) {
			Performer p = (Performer) uda;
			p.currentContacts++;
//			adjustDirection(contact.getFixtureA().getBody());
			saveLastSpeed(contact.getFixtureA().getBody());
		}
		if (udb instanceof Performer) {
			Performer p = (Performer) udb;
			p.currentContacts++;
//			adjustDirection(contact.getFixtureB().getBody());
			saveLastSpeed(contact.getFixtureB().getBody());
		}
	}

	private void saveLastSpeed(Body body) {
		final Vector2 linearVelocity = body.getLinearVelocity();
		final float linearSpeed = linearVelocity.len();
		final float angle = linearVelocity.angle();
		lastSpeed = linearSpeed;
	}
	
	private void adjustDirection(Body body) {
		final Vector2 linearVelocity = body.getLinearVelocity();
		final float linearSpeed = linearVelocity.len();
		final float angle = linearVelocity.angle();
		if (linearSpeed > MAX_SPEED)
		{
			linearVelocity.setLength(MAX_SPEED);
			body.setLinearVelocity(linearVelocity);
		}
		else if (linearSpeed < MIN_SPEED)
		{
			linearVelocity.setLength(MIN_SPEED);
			body.setLinearVelocity(linearVelocity);
		}
		if (90 < angle && angle < 270)
		{
			// bounced off a hill and moved backward now
			linearVelocity.rotate(-90f);
			body.setLinearVelocity(linearVelocity);
//			body.velo
		}
		
	}

	@Override
	public void endContact(Contact contact) {
		final Object uda = contact.getFixtureA().getUserData();
		final Object udb = contact.getFixtureB().getUserData();
		if (uda instanceof Performer) {
			Performer p = (Performer) uda;
			p.currentContacts--;
		}
		if (udb instanceof Performer) {
			Performer p = (Performer) udb;
			p.currentContacts--;
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
//		adjustDirection(contact.getFixtureA().getBody());
//		adjustDirection(contact.getFixtureB().getBody());
//		contact.setTangentSpeed(0f);
//		final Vector2 v1 = contact.getFixtureA().getBody().getLinearVelocity();
//		v1.rotate(90f);
//		contact.getFixtureA().getBody().setLinearVelocity(v1);
//		final Vector2 v2 = contact.getFixtureB().getBody().getLinearVelocity();
//		v2.rotate(90f);
//		contact.getFixtureA().getBody().setLinearVelocity(v2);
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		adjustDirection(contact.getFixtureA().getBody());
		adjustDirection(contact.getFixtureB().getBody());
//		impulse.getNormalImpulses()
//		contact.setTangentSpeed(0f);
//		final Vector2 v1 = contact.getFixtureA().getBody().getLinearVelocity();
//		v1.rotate(90f);
//		contact.getFixtureA().getBody().setLinearVelocity(v1);
//		final Vector2 v2 = contact.getFixtureB().getBody().getLinearVelocity();
//		v2.rotate(90f);
//		contact.getFixtureA().getBody().setLinearVelocity(v2);
	}

}
