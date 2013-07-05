package Game;

import java.util.Iterator;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.contacts.Contact;
import org.newdawn.slick.SlickException;

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
			
//			+ dynamic objects
			for (GameObject gameObject : game.getDynamicObjects()){
				if (	gameObject.getBody().getFixtureList() == contact.getFixtureB() 
					||	gameObject.getBody().getFixtureList() == contact.getFixtureA()){
					
					gameObject.getBody().setLinearVelocity( new Vec2(0,-10) );
//					break;
				}
			}
			
//			+ enemies
			for( Enemy enemy : game.getEnemies()){
				if( enemy.getFixture() == contact.getFixtureA() ||
					enemy.getFixture() == contact.getFixtureB()
				){
					enemy.throwBack();
				}
			}
		} 
		
		// eye laser
		if (	(contact.getFixtureA() == game.getPlayer().getLaser().getFixture()
			|| 	contact.getFixtureB() == game.getPlayer().getLaser().getFixture())) {
			
			for( Enemy enemy : game.getEnemies()){
				if( enemy.getFixture() == contact.getFixtureA() ||
					enemy.getFixture() == contact.getFixtureB()
				){
					if(game.getPlayer().isLaserActive() ){
						enemy.throwBack();
						enemy.die();
					} else {

						game.getPlayer().getLaser().getLaserContacts().add(enemy);
					}
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
		
//		player contact
		if( game.getPlayer().getFixture() == contact.getFixtureA() || game.getPlayer().getFixture() == contact.getFixtureB() ){
		
//			+ enemy contact
			for( Enemy enemy : game.getEnemies()){
				if ( !enemy.isDead() && !enemy.isDizzy() && !game.getPlayer().isGroundPounding() ){
					if (enemy.getFixture() == contact.getFixtureA() || enemy.getFixture() == contact.getFixtureB() ){
						game.getPlayer().die(true);
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
			for (Spike spike : game.getSpikes() ){
				if (spike.getFixture() == contact.getFixtureA() || spike.getFixture() == contact.getFixtureB() ) {
					game.getPlayer().die(false);
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
//			Iterator iterator = game.getBolts().iterator();
			Iterator iterator = game.getDropItems().iterator();
			while (iterator.hasNext()){
				DropItem dropItem = (DropItem) iterator.next();
				if (dropItem.isCollectable()) {
					
					if (dropItem.getFixture() == contact.getFixtureA() || dropItem.getFixture() == contact.getFixtureB() ) {
						dropItem.collect();
						iterator.remove();
						game.getObjectsToRemove().add(dropItem);
					}
					
				}
			}
			

////			+ nuts	
//			iterator = game.getNuts().iterator();
//			while (iterator.hasNext()){
//				Nut nut = (Nut) iterator.next();
//
//				if (nut.isCollectable()) {
//					if (nut.getFixture() == contact.getFixtureA() || nut.getFixture() == contact.getFixtureB() ) {
//						nut.collect();
//						iterator.remove();
//						game.getObjectsToRemove().add(nut);
//					}
//				}
//			}
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
				for (Spike spike : game.getSpikes() ){

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
		
		
		// eye laser
		if (	(contact.getFixtureA() == game.getPlayer().getLaser().getFixture()
			|| 	contact.getFixtureB() == game.getPlayer().getLaser().getFixture())) {
			
			for( Enemy enemy : game.getEnemies()){
				if( enemy.getFixture() == contact.getFixtureA() ||
					enemy.getFixture() == contact.getFixtureB()
				){
					if( !game.getPlayer().isLaserActive() ){
						game.getPlayer().getLaser().getLaserContacts().remove(enemy);
					}
				}
			}
			
			for (GameObject gameObject : game.getDynamicObjects()){
				if (	gameObject.getBody().getFixtureList() == contact.getFixtureB() 
					||	gameObject.getBody().getFixtureList() == contact.getFixtureA()){
					
					gameObject.getBody().setLinearVelocity( new Vec2(0,120) );
//					break;
				}
			}
			
			Iterator iterator = game.getDropItems().iterator();
			while (iterator.hasNext()){
				DropItem dropItem = (DropItem) iterator.next();
				
				if( dropItem instanceof Shred ){
					Shred shred = (Shred) dropItem;
					try {
						shred.grilled();
					} catch (SlickException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
		}
		
	}
	
	@Override	public void postSolve(Contact contact, ContactImpulse contactImpulse) {
		
	}
	@Override	public void preSolve(Contact contact, Manifold manifold) {
		
////		player contact
////		for( Enemy enemy : game.getEnemies()){									
////			
////			if( enemy.getFixture() == contact.getFixtureA() || enemy.getFixture() == contact.getFixtureB() ){
//			if( game.getPlayer().getFixture() == contact.getFixtureA() || game.getPlayer().getFixture() == contact.getFixtureB() ){
//	
//	//			+ bolts	
//				Iterator iterator = game.getBolts().iterator();
//				while (iterator.hasNext()){
//					Bolt bolt = (Bolt) iterator.next();
//					if (bolt.getFixture() == contact.getFixtureA() || bolt.getFixture() == contact.getFixtureB() ) {
//						bolt.getFixture().setSensor(true);
//					}
//				}
//				
//	
//	//			+ nuts	
//				iterator = game.getNuts().iterator();
//				while (iterator.hasNext()){
//	
//					Nut nut = (Nut) iterator.next();
//	
//					if (nut.getFixture() == contact.getFixtureA() || nut.getFixture() == contact.getFixtureB() ) {
//						nut.getFixture().setSensor(true);
//					}
//				}
//			}
////		}
	}
}
