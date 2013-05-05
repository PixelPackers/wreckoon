package Game;

import java.util.ArrayList;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

public class MyContactListener implements ContactListener{

	private World world;
	private Player player;
	private ArrayList<GameObject> dynamicObjects;
	
	public MyContactListener(World world, Player player, ArrayList<GameObject> dynamicObjects) {
		this.world = world;
		this.player = player;
		this.dynamicObjects = dynamicObjects;
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
		
		if (contact.getFixtureA() == player.getTailFixture()) {
			System.out.println("hit_A");
			
			for (GameObject gameObject : dynamicObjects){
				if ( gameObject.getBody().getFixtureList() == contact.getFixtureB() ){
					gameObject.getBody().setLinearVelocity( new Vec2(0,20) );
					break;
				}
			}
		}
		if (contact.getFixtureB() == player.getTailFixture()) {
			System.out.println("hit_B");
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
