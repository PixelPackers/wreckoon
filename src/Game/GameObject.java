package Game;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public abstract class GameObject {

	private Body 			body;
	private BodyDef 		bodyDef;
	private FixtureDef 		fixtureDef;
	private Fixture 		fixture;

	private Image 		img;
	
	public GameObject(World world, float posX, float posY, float density, float friction, float restitution, String imgPath, BodyType bodyType)
			throws SlickException {
		this(world, posX, posY, density, friction, restitution, imgPath, bodyType, false);
	}

	public GameObject(World world, float posX, float posY, float density, float friction, float restitution, String imgPath, BodyType bodyType,
			boolean isSensor) throws SlickException {
		this.bodyDef = new BodyDef();
		this.bodyDef.type = bodyType;
		this.bodyDef.position.set(posX, posY);
		
		this.fixtureDef = new FixtureDef();
		this.fixtureDef.density = density;
		this.fixtureDef.friction = friction;
		this.fixtureDef.restitution = restitution;
		this.fixtureDef.isSensor = isSensor;

		if(imgPath != null)
			this.img = Images.getInstance().getImage(imgPath);
	}

	protected void getReadyToRumble(World world) {
		this.body = world.createBody(bodyDef);
		this.fixture = this.body.createFixture(fixtureDef);
	}

	public void draw(Graphics g, boolean debugView){
		if(debugView /*|| this.img == null*/)
			this.drawOutline(g);
		else 
			this.drawImage();
	}

	public abstract void drawImage();
	public abstract void drawOutline(Graphics g);

	public Body getBody() {
		return body;
	}
	
	public BodyDef getBodyDef() {
		return bodyDef;
	}

	public FixtureDef getFixtureDef() {
		return fixtureDef;
	}

	public Fixture getFixture() {
		return fixture;
	}
	
	public Image getImage() {
		return this.img;
	}

	public void setImage(Image img) {
		this.img = img;
	}
	
	
}