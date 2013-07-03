package Game;

import java.util.Iterator;

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
			
			if ( ( contact.getFixtureA() == sensor.getFixture() && !contact.getFixtureB().isSensor() ) 
			|| ( contact.getFixtureB() == sensor.getFixture() && !contact.getFixtureA().isSensor() ) ) {
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
		
		// eye laser
		if (	contact.getFixtureA() == game.getPlayer().getFixtureLaser()
			|| 	contact.getFixtureB() == game.getPlayer().getFixtureLaser() ) {
			
			for( Enemy enemy : game.getEnemies()){
				if( enemy.getFixture() == contact.getFixtureA() ||
					enemy.getFixture() == contact.getFixtureB()
				){
					enemy.throwBack();
					enemy.die();
				}
			}
			
			for (GameObject gameObject : game.getDynamicObjects()){
				if (	gameObject.getBody().getFixtureList() == contact.getFixtureB() 
					||	gameObject.getBody().getFixtureList() == contact.getFixtureA()){
					
					gameObject.getBody().setLinearVelocity( new Vec2(0,120) );
//							break;
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
				if ( ( contact.getFixtureA() == sensor.getFixture() && !contact.getFixtureB().isSensor() ) 
				|| ( contact.getFixtureB() == sensor.getFixture() && !contact.getFixtureA().isSensor() ) ) {
					sensor.increaseCollidingCounter();
				}
			}
		}
		
//		 player contact
		if( game.getPlayer().getFixture() == contact.getFixtureA() || game.getPlayer().getFixture() == contact.getFixtureB() ){
		
//			+ enemy contact
			for( Enemy enemy : game.getEnemies()){
				if ( !enemy.isDead() && !game.getPlayer().isGroundPounding() ){
					if (enemy.getFixture() == contact.getFixtureA() || enemy.getFixture() == contact.getFixtureB() ){
						game.getPlayer().die();
						break;
					}
				}
			}
			
//			+ item
			for (Part part : game.getParts()){
				if(!part.isCollected()){
					if(part.getFixture() == contact.getFixtureA() || part.getFixture() == contact.getFixtureB() ){
						part.collect();
					}
				}
			}
			
//			+ generator
			if(game.getGenerator().getFixture() == contact.getFixtureA() || game.getGenerator().getFixture() == contact.getFixtureB() ){
				game.getPlayer().setAbleToGetLaser(true);
			}
			
//			+ spikes
			for (Spikes spike : game.getSpikes() ){
				if (spike.getFixture() == contact.getFixtureA() || spike.getFixture() == contact.getFixtureB() ) {
					game.getPlayer().die();
				}
			}
			
			
//			+ conveyor
			for (Conveyor conveyor : game.getConveyor() ){
				if (conveyor.getFixture() == contact.getFixtureA() || conveyor.getFixture() == contact.getFixtureB() ) {
					game.getPlayer().setConveyorSpeed(conveyor.getSpeed());
				}
				break;
			}

//			+ bolts	
			Iterator iterator = game.getBolts().iterator();
			while (iterator.hasNext()){
				Bolt bolt = (Bolt) iterator.next();
				if (bolt.isCollectable()) {
					
					if (bolt.getFixture() == contact.getFixtureA() || bolt.getFixture() == contact.getFixtureB() ) {
						bolt.collect();
						iterator.remove();
						game.getObjectsToRemove().add(bolt);
					}
					
				}
			}
			

//			+ nuts	
			iterator = game.getNuts().iterator();
			while (iterator.hasNext()){
				Nut nut = (Nut) iterator.next();

				if (nut.isCollectable()) {
					if (nut.getFixture() == contact.getFixtureA() || nut.getFixture() == contact.getFixtureB() ) {
						nut.collect();
						iterator.remove();
						game.getObjectsToRemove().add(nut);
					}
				}
			}
		}
		
		// enemy
		for( Enemy enemy : game.getEnemies()){
			if ( !enemy.isDead() ){
				
//				 + missile
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
				
//				+ spikes
				for (Spikes spike : game.getSpikes() ){

					if (enemy.getFixture() == contact.getFixtureA() || enemy.getFixture() == contact.getFixtureB() ){
						if (spike.getFixture() == contact.getFixtureA() || spike.getFixture() == contact.getFixtureB() ) {
							enemy.die();
						}
					}
					
				}
				
			} // not dead end
			
//			+ conveyor
			for (Conveyor conveyor : game.getConveyor() ){
				if (enemy.getFixture() == contact.getFixtureA() || enemy.getFixture() == contact.getFixtureB() ){
					if (conveyor.getFixture() == contact.getFixtureA() || conveyor.getFixture() == contact.getFixtureB() ) {
						enemy.setConveyorSpeed(conveyor.getSpeed());
					}						
				}
			}
		}
	}
	
	@Override	
	public void endContact(Contact contact) {
			
		for( MySensor sensor : game.getPlayer().getSensorList() ){	
			if ( ( contact.getFixtureA() == sensor.getFixture() && !contact.getFixtureB().isSensor() ) 
			|| ( contact.getFixtureB() == sensor.getFixture() && !contact.getFixtureA().isSensor() ) ) {
				sensor.decreaseCollidingCounter();
			}
		}

		for( Enemy enemy : game.getEnemies()){
			for( MySensor sensor : enemy.getSensorList() ){	
				if ( ( contact.getFixtureA() == sensor.getFixture() && !contact.getFixtureB().isSensor() ) 
				|| ( contact.getFixtureB() == sensor.getFixture() && !contact.getFixtureA().isSensor() ) ) {
					sensor.decreaseCollidingCounter();
				}
			}
		}
	

//		 player contact
		if( game.getPlayer().getFixture() == contact.getFixtureA() || game.getPlayer().getFixture() == contact.getFixtureB() ){
						
//			+ generator
			if(game.getGenerator().getFixture() == contact.getFixtureA() || game.getGenerator().getFixture() == contact.getFixtureB() ){
				game.getPlayer().setAbleToGetLaser(false);
			}
			
//			+ conveyor
			for (Conveyor conveyor : game.getConveyor() ){
				if (conveyor.getFixture() == contact.getFixtureA() || conveyor.getFixture() == contact.getFixtureB() ) {
					game.getPlayer().setConveyorSpeed(0);
				}
				break;
			}
		}
		
		
		
		// enemy
		for( Enemy enemy : game.getEnemies()){									
//			+ conveyor
			for (Conveyor conveyor : game.getConveyor() ){
				if (enemy.getFixture() == contact.getFixtureA() || enemy.getFixture() == contact.getFixtureB() ){
					if (conveyor.getFixture() == contact.getFixtureA() || conveyor.getFixture() == contact.getFixtureB() ) {
						enemy.setConveyorSpeed( 0 );
					}						
				}
			}	
		}
		
	}
	
	@Override	public void postSolve(Contact contact, ContactImpulse contactImpulse) {}
	@Override	public void preSolve(Contact contact, Manifold manifold) {}
}
