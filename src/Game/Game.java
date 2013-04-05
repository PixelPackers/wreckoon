package Game;

// TODO delete
import java.util.ArrayList;

import org.jbox2d.*;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Settings;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Game extends BasicGame {
	// linux setup
	// online editor und anschliessssssend pull test
	// neuer linux test mit neuem project setup
	/*/
	private static int				screenWidth		= 800;
	private static int				screenHeight	= 600;
	private static boolean			fullScreen		= false;
	/*/
	private static int				screenWidth		= 1366;
	private static int				screenHeight	= 768;
	private static boolean			fullScreen		= true;
	//*/
	
	private World					world;
	
	private ArrayList<GameObject>	staticObjects	= new ArrayList<GameObject>();
	private ArrayList<GameObject>	crates			= new ArrayList<GameObject>();
	private Player					player;
	
	private final float				WORLD_SCALE		= 100f;
	private float					zoom			= 0.3f;
	private final float				ZOOM_STEP		= 0.01f;
	
	private Camera					cam				= new Camera(0, screenHeight);
	private static final float		CAM_SPEED		= 5f;
	
	public Game() {
		super("The Raccooning");
	}
	
	@Override
	public void init(GameContainer gc) throws SlickException {
		// world = new World(gravity, doSleep);
		world = new World(new Vec2(0f, -30f), false);
		
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(0f, -9f);
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(50f, 10f);
		// groundBody.createFixture(groundBox, 0);
		FixtureDef groundFixtureDef = new FixtureDef();
		groundFixtureDef.shape = groundBox;
		
		GameObject ground = new GameObject(groundBodyDef, groundFixtureDef, world, "images/crate.png");
		staticObjects.add(ground);
		
		// walls
		BodyDef wallBodyDef = new BodyDef();
		wallBodyDef.position.set(0f, 0f);
		PolygonShape wallShape = new PolygonShape();
		wallShape.setAsBox(10f, 50f);
		FixtureDef wallFixtureDef = new FixtureDef();
		wallFixtureDef.shape = wallShape;
		GameObject wall = new GameObject(wallBodyDef, wallFixtureDef, world, "images/crate.png");
		staticObjects.add(wall);
		
		// groundBodyDef.position.set(10, 0);
		// wall = new GameObject(groundBodyDef, groundFixtureDef, world, "images/crate.png");
		// wallShape = new PolygonShape();
		// groundBox.setAsBox(1, 50);
		// groundBody.createFixture(groundBox, 0);
		// staticObjects.add(wall);
		
		// Dynamic Body
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(4f, 4f);
		CircleShape playerShape = new CircleShape();
		playerShape.m_radius = 1f;
		// TODO get the fucking polygon shape to work!
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = playerShape;
		fixtureDef.density = 11f;
		fixtureDef.friction = 0.3f;
		
		// Player
		player = new Player(bodyDef, fixtureDef, world, "images/player.png");
		
		PolygonShape dynamicBox = new PolygonShape();
		dynamicBox.setAsBox(0.15f, 0.15f);
		fixtureDef.shape = dynamicBox;

		fixtureDef.density = 1f;
		fixtureDef.friction = 0.5f;
		
		// Crate
		float min_size = 0.1f;
		float max_size = 0.3f;
		
		for (int i = 0; i < 10; ++i) {
			for (int j = 0; j < 10; ++j) {
				float size = (float) Math.random()*max_size + min_size;
				

				CircleShape c = new CircleShape();
				c.m_radius = size;
				fixtureDef.shape = c;
				
				
				bodyDef.position.set(max_size * i, max_size * j);
				GameObject crate = new GameObject(bodyDef, fixtureDef, world, "images/player.png");
				crates.add(crate);
			}
		}


		fixtureDef.density = 2f;
		fixtureDef.friction = 0.8f;
		
		min_size = 0.1f;
		max_size = 0.3f;
		
		for (int i = 0; i < 10; ++i) {
			for (int j = 0; j < 10; ++j) {
				float size = (float) Math.random()*max_size + min_size;
				

				PolygonShape p = new PolygonShape();
				p.setAsBox(size, size);
				fixtureDef.shape = dynamicBox;
				fixtureDef.shape = p;

				
				
				bodyDef.position.set(max_size * i, max_size * j);
				GameObject crate = new GameObject(bodyDef, fixtureDef, world, "images/crate.png");
				crates.add(crate);
			}
		}
	}
	
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		Input input = gc.getInput();
		
		if (input.isKeyPressed(Input.KEY_SPACE)) {
			player.getBody().setLinearVelocity(new Vec2(player.getBody().getLinearVelocity().x, 20));
		}
		if (input.isKeyDown(Input.KEY_LEFT)) {
			// player.getBody().setLinearVelocity(new Vec2(-2, player.getBody().getLinearVelocity().y));
			player.accelerate(true);
		}
		if (input.isKeyDown(Input.KEY_RIGHT)) {
			// player.getBody().setLinearVelocity(new Vec2(2, player.getBody().getLinearVelocity().y));
			player.accelerate(false);
		}
		
		if (input.isKeyDown(Input.KEY_E)) {
			zoom += ZOOM_STEP;
		}
		if (input.isKeyDown(Input.KEY_R)) {
			zoom -= ZOOM_STEP;
		}
		
		// if (input.isKeyDown(Input.KEY_W)) {
		// cam.move(0, CAM_SPEED);
		// }
		// if (input.isKeyDown(Input.KEY_A)) {
		// cam.move(CAM_SPEED, 0);
		// }
		// if (input.isKeyDown(Input.KEY_S)) {
		// cam.move(0, -CAM_SPEED);
		// }
		// if (input.isKeyDown(Input.KEY_D)) {
		// cam.move(-CAM_SPEED, 0);
		// }
		cam.follow(player.getBody().getPosition().x, player.getBody().getPosition().y, 10);
		
		// world.step(timeStep, velocityIterations, positionIterations);
		world.step(delta / 1000f, 6, 2);
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		g.translate(cam.getX() * zoom * WORLD_SCALE + screenWidth / 2f, cam.getY() * zoom * WORLD_SCALE + screenHeight * 2f / 3f);
		g.scale(zoom * WORLD_SCALE, zoom * WORLD_SCALE);
		for (GameObject staticObj : staticObjects) {
			staticObj.draw();
		}
		
		for (GameObject crate : crates) {
			crate.draw();
		}
		
		player.draw();
	}
	
	public static void main(String[] args) throws SlickException {
		AppGameContainer game = new AppGameContainer(new Game());
		game.setDisplayMode(screenWidth, screenHeight, fullScreen);
		game.setMultiSample(16);
		game.setTargetFrameRate(60);
		game.setVSync(true);
		game.setShowFPS(false);
		game.start();
	}
	
	// delete me please
}
