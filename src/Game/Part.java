package Game;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

public class Part {
	
	private final int			BOLT_VALUE		= 500;
	
	private static final float	radius			= 0.5f;
	private World				world;
	private Game				game;
	private float				x;
	private float				y;
	private Image				image			= Images.getInstance().getImage("images/part.png");
	private boolean				collected		= false;
	
	private float				floatingHeight	= 0f;
	private float				floatingSpeed	= 0.005f;
	private int					i				= 0;
	
	private Body				body;
	private Fixture				fixture;
	private FixtureDef			fixtureDef		= new FixtureDef();
	
	public Part(World world, Game game, float x, float y) throws SlickException {
		
		this.world = world;
		this.game = game;
		this.x = x;
		this.y = y;
		
		CircleShape circleShape = new CircleShape();
		circleShape.m_radius = radius * 0.75f;
		
		this.fixtureDef.shape = circleShape;
		this.fixtureDef.isSensor = true;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.STATIC;
		// bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(x, y);
		
		this.body = world.createBody(bodyDef);
		this.fixture = this.body.createFixture(fixtureDef);
	}
	
	public void collect() {
		if (!this.collected) {
			// MusicManager.getInstance().itemCollected();
			this.world.destroyBody(this.body);
			this.game.getRidOfPart(this);
			// this.game.getPlayer().increaseBoltCounter(BOLT_VALUE);
			this.game.getPlayer().boltsCollected(BOLT_VALUE);
			
			this.collected = true;
			// show counter
			// make counter dissapear?
		}
	}
	
	public void draw(Graphics g, boolean debugView) {
		
		if (collected) {
			return;
		}
		
		if (debugView) {
			this.drawOutline(g);
		} else {
			this.drawImage();
		}
	}
	
	public void drawImage() {
		image.draw(this.body.getPosition().x - radius, this.body.getPosition().y - radius, radius * 2f, radius * 2f);
	}
	
	public void drawOutline(Graphics g) {
		g.drawArc(this.body.getPosition().x - this.radius * 0.5f, this.body.getPosition().y - this.radius * 0.5f, this.radius, this.radius,
				0f, 360f);
	}
	
	public Body getBody() {
		return body;
	}
	
	public Fixture getFixture() {
		return fixture;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public boolean isCollected() {
		return collected;
	}
	
	public void update() {
		
		// make floating movement
		this.floatingHeight = (float) (this.floatingSpeed * Math.sin(Math.toRadians(++i % 360f)));
		this.body.setTransform(new Vec2(this.body.getPosition().x, this.body.getPosition().y + this.floatingHeight), 0);
		
	}
}
