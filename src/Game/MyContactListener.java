package Game;

import java.io.ObjectInputStream.GetField;
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
		
		// player sensors
		for( MySensor sensor : game.getPlayer().getSensorList() ){
			
			if (contact.getFixtureA() == sensor.getFixture()||
				contact.getFixtureB() == sensor.getFixture()
			){
				sensor.increaseCollidingCounter();
			}
		}
		
		// tailwhip hit
		if (	contact.getFixtureA() == game.getPlayer().getTailFixture()
			|| 	contact.getFixtureB() == game.getPlayer().getTailFixture() ) {
			
			for (GameObject gameObject : game.getBalls()){
				if (	gameObject.getBody().getFixtureList() == contact.getFixtureB() 
					||	gameObject.getBody().getFixtureList() == contact.getFixtureA()){
					
					gameObject.getBody().setLinearVelocity( new Vec2(0,20) );
//					break;
				}
			}
			
			for( Enemy enemy : game.getEnemies()){
				if( enemy.getFixture() == contact.getFixtureA() ||
					enemy.getFixture() == contact.getFixtureB()
				){
					enemy.die();
				}
			}
		} 
		
		// groundpounding

		if(game.getPlayer().isGroundPounding() ){

			if (	contact.getFixtureA() == game.getPlayer().getSensorGroundCollision().getFixture()
				|| 	contact.getFixtureB() == game.getPlayer().getSensorGroundCollision().getFixture() ) {
			
				for( Enemy enemy : game.getEnemies()){
					if( enemy.getFixture() == contact.getFixtureA() ||
						enemy.getFixture() == contact.getFixtureB()
					){
						enemy.die();
					}
				}
				
			}
		}
		
		// enemies sensors
		for( Enemy enemy : game.getEnemies()){
			for( MySensor sensor : enemy.getSensorList() ){	
				if (contact.getFixtureA() == sensor.getFixture()||
					contact.getFixtureB() == sensor.getFixture()
				){
					sensor.increaseCollidingCounter();
				}
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

		for( Enemy enemy : game.getEnemies()){
			for( MySensor sensor : enemy.getSensorList() ){	
				if (contact.getFixtureA() == sensor.getFixture()||
					contact.getFixtureB() == sensor.getFixture()
				){
					sensor.decreaseCollidingCounter();
				}
			}
		}
	
	}
	
	@Override	public void postSolve(Contact contact, ContactImpulse contactImpulse) {}
	@Override	public void preSolve(Contact contact, Manifold manifold) {}
}
