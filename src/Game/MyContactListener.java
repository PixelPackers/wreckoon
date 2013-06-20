package Game;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.contacts.Contact;

public class MyContactListener implements ContactListener{

	private Game game;

	float minKillingSpeed = 25;
	
	public MyContactListener(Game game) {
		this.game = game;
	}
	
	@Override
	public void beginContact(Contact contact) {
		
		// player sensors
		for( MySensor sensor : game.getPlayer().getSensorList() ){
			
			if (contact.getFixtureA() == sensor.getFixture() ||
				contact.getFixtureB() == sensor.getFixture()
			){
				sensor.increaseCollidingCounter();
			}
		}
		
		// tailwhip hit
		if (	contact.getFixtureA() == game.getPlayer().getTailFixture()
			|| 	contact.getFixtureB() == game.getPlayer().getTailFixture() ) {
			
			for (GameObject gameObject : game.getDynamicObjects()){
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
					enemy.throwBack();
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
		
//		 player contat
		if( game.getPlayer().getFixture() == contact.getFixtureA() || game.getPlayer().getFixture() == contact.getFixtureB() ){
		
//			+ enemy contact
			for( Enemy enemy : game.getEnemies()){
				if ( !enemy.isDead() ){
					if (enemy.getFixture() == contact.getFixtureA() || enemy.getFixture() == contact.getFixtureB() ){
						game.getPlayer().die();
						break;
					}
				}
			}
			
//			+ item
			for (Part part : game.getParts()){
				if(part.getFixture() == contact.getFixtureA() || part.getFixture() == contact.getFixtureB() ){
					part.collected();
				}
			}
		}
		
		// enemy + missile
		for( Enemy enemy : game.getEnemies()){
			if ( !enemy.isDead() ){
				for(GameObject dynamicObject : game.getDynamicObjects() ){
					if (enemy.getFixture() == contact.getFixtureA() || enemy.getFixture() == contact.getFixtureB() ){
						if (dynamicObject.getFixture() == contact.getFixtureA() || dynamicObject.getFixture() == contact.getFixtureB() ){
							if (Math.abs(dynamicObject.getBody().getLinearVelocity().x) > minKillingSpeed || 
									Math.abs(dynamicObject.getBody().getLinearVelocity().y) > minKillingSpeed ){
								enemy.die();
								break;
							}
							
						}
					}
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
