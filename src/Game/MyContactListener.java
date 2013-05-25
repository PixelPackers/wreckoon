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

	private Game game;
	
	public MyContactListener(Game game) {
		this.game = game;
	}
	
	@Override
	public void beginContact(Contact contact) {
		
		for( MySensor sensor : game.getPlayer().getSensorList() ){
			
			if (contact.getFixtureA() == sensor.getFixture()||
				contact.getFixtureB() == sensor.getFixture()
			){
				sensor.increaseCollidingCounter();
			}
		}
		
		if (contact.getFixtureA() == game.getPlayer().getTailFixture()) {
			System.out.println("hit_A");
			
			for (GameObject gameObject : game.getBalls()){
				if ( gameObject.getBody().getFixtureList() == contact.getFixtureB() ){
					gameObject.getBody().setLinearVelocity( new Vec2(0,20) );
					break;
				}
			}
		}
		if (contact.getFixtureB() == game.getPlayer().getTailFixture()) {
			System.out.println("hit_B");
		}
		
		
		for( MySensor sensor : game.getEnemy().getSensorList() ){	
			if (contact.getFixtureA() == sensor.getFixture()||
				contact.getFixtureB() == sensor.getFixture()
			){
				sensor.increaseCollidingCounter();
			}
		}
		
	}
	
	@Override	
	public void endContact(Contact contact) {
			
		for( MySensor sensor : game.getPlayer().getSensorList() ){	
			if (contact.getFixtureA() == sensor.getFixture()||
				contact.getFixtureB() == sensor.getFixture()
			){
				sensor.decreaseCollidingCounter();
			}
		}
		for( MySensor sensor : game.getEnemy().getSensorList() ){	
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
