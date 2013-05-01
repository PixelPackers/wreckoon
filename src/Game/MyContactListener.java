package Game;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;

public class MyContactListener implements ContactListener{

//	private static int collisionCounter = 0;
	private Player player;
	
	public MyContactListener(Player player) {
		this.player = player;
	}
	
	@Override
	public void beginContact(Contact contact) {
		
		int i=0;
		for( Fixture sensorFixture : player.getSensorList() ){
			
			if (contact.getFixtureA() == sensorFixture||
				contact.getFixtureB() == sensorFixture
			){
				System.out.println("sensor" + i + " hit");
			}

			++i;
		}
//		System.out.println(++collisionCounter);
		
	}
	@Override	public void endContact(Contact contact) {
		
		// TODO iswalljumpalbe wieder weg
		// is on solid ground weg
		
	}
	@Override	public void postSolve(Contact contact, ContactImpulse contactImpulse) {}
	@Override	public void preSolve(Contact contact, Manifold manifold) {}
}
