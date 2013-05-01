package Game;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

public class MyContactListener implements ContactListener{

	private Player player;
	
	public MyContactListener(Player player) {
		this.player = player;
	}
	
	@Override
	public void beginContact(Contact contact) {	
		
		for( MySensor sensor : player.getSensorList() ){
			
			if (contact.getFixtureA() == sensor.getFixture()||
				contact.getFixtureB() == sensor.getFixture()
			){
				sensor.increaseCollidingCounter();
			}
		}
		
	}
	
	@Override	
	public void endContact(Contact contact) {
			
		for( MySensor sensor : player.getSensorList() ){	
			if (contact.getFixtureA() == sensor.getFixture()||
				contact.getFixtureB() == sensor.getFixture()
			){
				sensor.decreaseCollidingCounter();
			}
		}
		
	}
	@Override	public void postSolve(Contact contact, ContactImpulse contactImpulse) {}
	@Override	public void preSolve(Contact contact, Manifold manifold) {}
}
