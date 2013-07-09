package Game;

import java.util.Iterator;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.contacts.Contact;
import org.newdawn.slick.SlickException;

public class MyContactListener implements ContactListener {
	
	private Game	game;
	
	float			minKillingSpeed	= 25;
	
	public MyContactListener(Game game) {
		this.game = game;
	}
	
	@Override
	public void beginContact(Contact contact) {
		
		// player sensors
		for (MySensor sensor : game.getPlayer().getSensorList()) {
			
			if ((contact.getFixtureA() == sensor.getFixture() && !contact.getFixtureB().isSensor())
					|| (contact.getFixtureB() == sensor.getFixture() && !contact.getFixtureA().isSensor())) {
				sensor.increaseCollidingCounter();
			}
		}
		
		// tailwhip hit
		if (contact.getFixtureA() == game.getPlayer().getTailFixture() || contact.getFixtureB() == game.getPlayer().getTailFixture()) {
			
			// tailwhip + dynamic objects
			for (DropItem dropItem : game.getDropItems()) {
				if (dropItem.getBody().getFixtureList() == contact.getFixtureB()
						|| dropItem.getBody().getFixtureList() == contact.getFixtureA()) {
					dropItem.throwback();
					// break;
				}
			}
			
			// tailwhip + enemies
			for (Enemy enemy : game.getEnemies()) {
				if (enemy.getFixture() == contact.getFixtureA() || enemy.getFixture() == contact.getFixtureB()) {
					enemy.throwBack(true);
				}
			}
		}
		
		// eye laser
		if ((contact.getFixtureA() == game.getPlayer().getLaser().getFixture() || contact.getFixtureB() == game.getPlayer().getLaser()
				.getFixture())) {
			
			// laser + enemies
			for (Enemy enemy : game.getEnemies()) {
				if (enemy.getFixture() == contact.getFixtureA() || enemy.getFixture() == contact.getFixtureB()) {
					if (game.getPlayer().isLaserActive()) {
						enemy.laserHit();
					} else {
						game.getPlayer().getLaser().getLaserContacts().add(enemy);
					}
				}
			}
			
			// laser + gameObject
			for (GameObject gameObject : game.getDynamicObjects()) {
				if (gameObject.getBody().getFixtureList() == contact.getFixtureB()
						|| gameObject.getBody().getFixtureList() == contact.getFixtureA()) {
					
					gameObject.getBody().setLinearVelocity(new Vec2(0, 120));
					// break;
				}
			}
			
			// laser + dropitems
			Iterator iterator = game.getDropItems().iterator();
			while (iterator.hasNext()) {
				DropItem dropItem = (DropItem) iterator.next();
				
				if (dropItem.getFixture() == contact.getFixtureB() || dropItem.getFixture() == contact.getFixtureA()) {
					
					if (dropItem instanceof Shred) {
						Shred shred = (Shred) dropItem;
						
						if (game.getPlayer().isLaserActive()) {
							try {
								shred.grilled();
							} catch (SlickException e) {
								e.printStackTrace();
							}
						} else {
							game.getPlayer().getLaser().getLaserContacts().add(shred);
						}
					}
					if (game.getPlayer().isLaserActive()) {
						dropItem.throwback();
					}
				}
				
			}
			
		}
		
		// groundpounding
		if (game.getPlayer().isGroundPounding()) {
			if (contact.getFixtureA() == game.getPlayer().getSensorGroundCollision().getFixture()
					|| contact.getFixtureB() == game.getPlayer().getSensorGroundCollision().getFixture()) {
				
				// groundpound + enemy
				for (Enemy enemy : game.getEnemies()) {
					if (enemy.getFixture() == contact.getFixtureA() || enemy.getFixture() == contact.getFixtureB()) {
						enemy.die();
					}
				}
				
				// groundpound + boden
				if (contact.getFixtureA().getFilterData().categoryBits == 33 || contact.getFixtureB().getFilterData().categoryBits == 33) {
					game.getPlayer().stopGroundpounding();
				}
			}
			
		}
		
		// enemies sensors
		for (Enemy enemy : game.getEnemies()) {
			for (MySensor sensor : enemy.getSensorList()) {
				if ((contact.getFixtureA() == sensor.getFixture() && !contact.getFixtureB().isSensor())
						|| (contact.getFixtureB() == sensor.getFixture() && !contact.getFixtureA().isSensor())) {
					sensor.increaseCollidingCounter();
				}
			}
		}
		
		for (Tire tire : game.getTires()) {
			if (contact.getFixtureA() == tire.getFixture() || contact.getFixtureB() == tire.getFixture()) {
				float bouncyness = (float) Math.max(1.3f, 1f + (contact.getFixtureA().m_body.getLinearVelocity().length() + contact
						.getFixtureB().m_body.getLinearVelocity().length()) / 23f);
				if (bouncyness > 1.15f)
					tire.bounce(bouncyness);
			}
		}
		
		// player contact
		if (playerContact(contact)) {
			
			// player + enemy contact
			for (Enemy enemy : game.getEnemies()) {
				if (!enemy.isDead() && !enemy.isDizzy() && !game.getPlayer().isGroundPounding()) {
					if (enemy.getFixture() == contact.getFixtureA() || enemy.getFixture() == contact.getFixtureB()) {
						game.getPlayer().die(true);
						break;
					}
				}
			}
			
			// player + item
			if (!game.getPlayer().isDead())
				for (Part part : game.getParts()) {
					if (!part.isCollected()) {
						if (part.getFixture() == contact.getFixtureA() || part.getFixture() == contact.getFixtureB()) {
							part.collect();
						}
					}
				}
			
			// player + generator
			for (Generator gen : Game.getGenerators()) {
				if (gen.getFixture() == contact.getFixtureA() || gen.getFixture() == contact.getFixtureB()) {
					game.getPlayer().setGenerator(gen);
					break;
				}
			}
			
			// player + spikes
			for (Spike spike : game.getSpikes()) {
				if (spike.getFixture() == contact.getFixtureA() || spike.getFixture() == contact.getFixtureB()) {
					game.getPlayer().die(false);
				}
			}
			
			// player + conveyor
			for (Conveyor conveyor : game.getConveyors()) {
				if (conveyor.getFixture() == contact.getFixtureA() || conveyor.getFixture() == contact.getFixtureB()) {
					game.getPlayer().setConveyorSpeed(conveyor.getSpeed());
				}
			}
			
			// player + dropItems
			Iterator iterator = game.getDropItems().iterator();
			while (iterator.hasNext()) {
				
				DropItem dropItem = (DropItem) iterator.next();
				
				if (dropItem.getFixture() == contact.getFixtureA() || dropItem.getFixture() == contact.getFixtureB()) {
					
					if (dropItem.isCollectable()) {
						dropItem.collect();
						iterator.remove();
						game.getObjectsToRemove().add(dropItem);
					} else {
						game.getPlayer().getDropItemsToCollect().add(dropItem);
					}
					
				}
			}
			
		}
		
		// enemy
		for (Enemy enemy : game.getEnemies()) {
			if (!enemy.isDead()) {
				
				// enemy + missile
				// for(GameObject dynamicObject : game.getDynamicObjects() ){
				// if (enemy.getFixture() == contact.getFixtureA() ||
				// enemy.getFixture() == contact.getFixtureB() ){
				// if (dynamicObject.getFixture() == contact.getFixtureA() ||
				// dynamicObject.getFixture() == contact.getFixtureB() ){
				// if (Math.abs(dynamicObject.getBody().getLinearVelocity().x) >
				// minKillingSpeed ||
				// Math.abs(dynamicObject.getBody().getLinearVelocity().y) >
				// minKillingSpeed ){
				// enemy.die();
				// break;
				// }
				// }
				// }
				// }
				
				// enemy + spikes
				for (Spike spike : game.getSpikes()) {
					
					if (enemy.getFixture() == contact.getFixtureA() || enemy.getFixture() == contact.getFixtureB()) {
						if (spike.getFixture() == contact.getFixtureA() || spike.getFixture() == contact.getFixtureB()) {
							enemy.die();
						}
					}
					
				}
				
				// enemy + enemy
				for (Enemy enemy2 : game.getEnemies()) {
					if (enemy == enemy2) {
						continue;
					}
					//
					if (enemy.getFixture() == contact.getFixtureA() || enemy.getFixture() == contact.getFixtureB()) {
						if (enemy2.getFixture() == contact.getFixtureA() || enemy2.getFixture() == contact.getFixtureB()) {
							
							if (enemy.dizzy && enemy.isOriginalHit()) {
								enemy2.throwBack(false);
								// enemy.setOriginalHit(false);
							}
							if (enemy2.dizzy && enemy2.isOriginalHit()) {
								enemy.throwBack(false);
								// enemy2.setOriginalHit(false);
							}
						}
					}
				}
				
			} // not dead end
			
			// enemy + conveyor
			for (Conveyor conveyor : game.getConveyors()) {
				if (enemy.getFixture() == contact.getFixtureA() || enemy.getFixture() == contact.getFixtureB()) {
					if (conveyor.getFixture() == contact.getFixtureA() || conveyor.getFixture() == contact.getFixtureB()) {
						enemy.setConveyorSpeed(conveyor.getSpeed());
					}
				}
			}
		}
	}
	
	@Override
	public void endContact(Contact contact) {
		
		for (MySensor sensor : game.getPlayer().getSensorList()) {
			if ((contact.getFixtureA() == sensor.getFixture() && !contact.getFixtureB().isSensor())
					|| (contact.getFixtureB() == sensor.getFixture() && !contact.getFixtureA().isSensor())) {
				sensor.decreaseCollidingCounter();
			}
		}
		
		for (Enemy enemy : game.getEnemies()) {
			for (MySensor sensor : enemy.getSensorList()) {
				if ((contact.getFixtureA() == sensor.getFixture() && !contact.getFixtureB().isSensor())
						|| (contact.getFixtureB() == sensor.getFixture() && !contact.getFixtureA().isSensor())) {
					sensor.decreaseCollidingCounter();
				}
			}
		}
		
		// player contact
		if (playerContact(contact)) {
			
			// player + generator
			for (Generator gen : Game.getGenerators()) {
				if (gen.getFixture() == contact.getFixtureA() || gen.getFixture() == contact.getFixtureB()) {
					game.getPlayer().setGenerator(null);
					break;
				}
			}
			
			// player + conveyor
			for (Conveyor conveyor : game.getConveyors()) {
				if (conveyor.getFixture() == contact.getFixtureA() || conveyor.getFixture() == contact.getFixtureB()) {
					game.getPlayer().setConveyorSpeed(-conveyor.getSpeed());
				}
			}
			
			// player + dropItems
			Iterator iterator = game.getDropItems().iterator();
			while (iterator.hasNext()) {
				
				DropItem dropItem = (DropItem) iterator.next();
				
				if (dropItem.getFixture() == contact.getFixtureA() || dropItem.getFixture() == contact.getFixtureB()) {
					
					if (!dropItem.isCollectable()) {
						game.getPlayer().getDropItemsToCollect().remove(dropItem);
					} else {
						dropItem.collect();
						// kA warum das nicht gebraucht wird, aber bitte... es
						// funkt
						// game.getObjectsToRemove().add(dropItem);
						iterator.remove();
					}
				}
			}
		}
		
		// enemy
		for (Enemy enemy : game.getEnemies()) {
			// enemy + conveyor
			for (Conveyor conveyor : game.getConveyors()) {
				if (enemy.getFixture() == contact.getFixtureA() || enemy.getFixture() == contact.getFixtureB()) {
					if (conveyor.getFixture() == contact.getFixtureA() || conveyor.getFixture() == contact.getFixtureB()) {
						enemy.setConveyorSpeed(-conveyor.getSpeed());
					}
				}
			}
		}
		
		// eye laser
		if ((contact.getFixtureA() == game.getPlayer().getLaser().getFixture() || contact.getFixtureB() == game.getPlayer().getLaser()
				.getFixture())) {
			
			for (Enemy enemy : game.getEnemies()) {
				if (enemy.getFixture() == contact.getFixtureA() || enemy.getFixture() == contact.getFixtureB()) {
					if (game.getPlayer().isLaserActive()) {
						enemy.laserHitEnd();
					} else {
						game.getPlayer().getLaser().getLaserContacts().remove(enemy);
					}
				}
			}
			
			for (GameObject gameObject : game.getDynamicObjects()) {
				if (gameObject.getBody().getFixtureList() == contact.getFixtureB()
						|| gameObject.getBody().getFixtureList() == contact.getFixtureA()) {
					
					gameObject.getBody().setLinearVelocity(new Vec2(0, 120));
					// break;
				}
			}
			// shreds grill party end
			Iterator iterator = game.getDropItems().iterator();
			while (iterator.hasNext()) {
				DropItem dropItem = (DropItem) iterator.next();
				
				if (dropItem instanceof Shred) {
					Shred shred = (Shred) dropItem;
					if (shred.getFixture() == contact.getFixtureB() || shred.getFixture() == contact.getFixtureA()) {
						
						if (!game.getPlayer().isLaserActive()) {
							game.getPlayer().getLaser().getLaserContacts().remove(shred);
						}
					}
				}
			}
			
		}
		
	}
	
	private boolean playerContact(Contact contact) {
		return game.getPlayer().getFixture() == contact.getFixtureA() || game.getPlayer().getFixture() == contact.getFixtureB()
				|| game.getPlayer().getWheel().getFixture() == contact.getFixtureA()
				|| game.getPlayer().getWheel().getFixture() == contact.getFixtureB();
	}
	
	@Override
	public void postSolve(Contact contact, ContactImpulse contactImpulse) {
		
	}
	
	@Override
	public void preSolve(Contact contact, Manifold manifold) {
		
		// // player contact
		// // for( Enemy enemy : game.getEnemies()){
		// //
		// // if( enemy.getFixture() == contact.getFixtureA() ||
		// enemy.getFixture() == contact.getFixtureB() ){
		// if( game.getPlayer().getFixture() == contact.getFixtureA() ||
		// game.getPlayer().getFixture() == contact.getFixtureB() ){
		//
		// // + bolts
		// Iterator iterator = game.getBolts().iterator();
		// while (iterator.hasNext()){
		// Bolt bolt = (Bolt) iterator.next();
		// if (bolt.getFixture() == contact.getFixtureA() || bolt.getFixture()
		// == contact.getFixtureB() ) {
		// bolt.getFixture().setSensor(true);
		// }
		// }
		//
		//
		// // + nuts
		// iterator = game.getNuts().iterator();
		// while (iterator.hasNext()){
		//
		// Nut nut = (Nut) iterator.next();
		//
		// if (nut.getFixture() == contact.getFixtureA() || nut.getFixture() ==
		// contact.getFixtureB() ) {
		// nut.getFixture().setSensor(true);
		// }
		// }
		// }
		// // }
	}
}
