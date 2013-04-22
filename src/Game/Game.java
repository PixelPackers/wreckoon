package Game;

// TODO delete
import java.util.ArrayList;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
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
import org.newdawn.slick.geom.Polygon;

public class Game extends BasicGame {
	
	// Sifu's Kommentar
	
	/*/
	private static int				screenWidth		= 800;
	private static int				screenHeight	= 600;
	private static boolean			fullScreen		= false;
	/*/
	private static int				screenWidth		= 1920;
	private static int				screenHeight	= 1080;
	private static boolean			fullScreen		= true;
	//*/
	
	private World					world;
	
	private ArrayList<GameObject>	staticObjects	= new ArrayList<GameObject>();
	private ArrayList<GameObject>	crates			= new ArrayList<GameObject>();
	private Player					player;
	private PolygonShape 			polyPolyShape;
	private BodyDef 				polyBodyDef;
	
	//private Image[][] worldImages = new Image[8][8];
	private Image[] trashpile = new Image[5];
	
	private float					zoom			= 30f;
	private final float				ZOOM_STEP		= 1f;
	
	private Camera					cam				= new Camera(0, screenHeight);
	private static final float		CAM_SPEED		= 5f;
	
	public Game() {
		super("The Raccooning");
	}
	
	@Override
	public void init(GameContainer gc) throws SlickException {
		
		/*for (int y = 0; y < worldImages.length; ++y) {
			for (int x = 0; x < worldImages[0].length; ++x) {
				//worldImages[y][x] = new Image("images/world_x" + x % 2 + "_y" + y % 2 + ".png");
				worldImages[y][x] = new Image("images/test" + (x + 1) + " (" + (y + 1) + ").png");
			}
		}*/
		trashpile[0] = new Image("images/background1.png");
		trashpile[1] = new Image("images/background2.png");
		trashpile[2] = new Image("images/background3.png");
		trashpile[3] = new Image("images/background4.png");
		trashpile[4] = new Image("images/background5.png");
		
		// git, bitte funtionier
		
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
		wallBodyDef.position.set(17f, 0f);
		PolygonShape wallShape = new PolygonShape();
		wallShape.setAsBox(1f, 10f);
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
		
		// P O L Y G O N
		polyBodyDef = new BodyDef();
		polyBodyDef.position.set(-5f,4f);
		
		Body poly = new Body(polyBodyDef, world);
		
		polyPolyShape = new PolygonShape();

			Vec2[] points = new Vec2[8];
			points[0] = new Vec2( 0f,	0f);
			points[1] = new Vec2( 22f,	0f);
			points[2] = new Vec2( -6f,	12f);
			points[3] = new Vec2( -8f,	8f);
			points[4] = new Vec2( -8f,	7f);
			points[5] = new Vec2( -7f,	4f);
			points[6] = new Vec2( -5f,	2f);
			points[7] = new Vec2( -2f,	0f);
		polyPolyShape.set(points, 8);
		//*/
		FixtureDef polyFixDef = new FixtureDef();
		polyFixDef.shape = polyPolyShape;
		
		world.createBody(polyBodyDef);
		poly.createFixture(polyFixDef);
		
		
		
		
		
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
				
				
				bodyDef.position.set(max_size * i * 10, max_size * j * 10);
				GameObject crate = new GameObject(bodyDef, fixtureDef, world, "images/player.png");
				crates.add(crate);
			}
		}


		fixtureDef.density = 2f;
		fixtureDef.friction = 0.8f;
		
		min_size = 0.1f;
		max_size = 0.3f;
		//*
		for (int i = 0; i < 10; ++i) {
			for (int j = 0; j < 10; ++j) {
				float size = (float) Math.random()*max_size + min_size;
				

				PolygonShape p = new PolygonShape();
				p.setAsBox(size, size);
				fixtureDef.shape = dynamicBox;
				fixtureDef.shape = p;

				
				
				bodyDef.position.set(max_size * i * 10, max_size * j * 10);
				GameObject crate = new GameObject(bodyDef, fixtureDef, world, "images/crate.png");
				crates.add(crate);
			}
		}//*/
	}
	
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		Input input = gc.getInput();
		
		if (input.isKeyPressed(Input.KEY_SPACE) || input.isKeyPressed(Input.KEY_W) || input.isKeyPressed(Input.KEY_UP) ) {
			player.getBody().setLinearVelocity(new Vec2(player.getBody().getLinearVelocity().x, 20));
		}
		if (input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_A) ) {
			// player.getBody().setLinearVelocity(new Vec2(-2, player.getBody().getLinearVelocity().y));
			player.accelerate(true);
		}
		if (input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D) ) {
			// player.getBody().setLinearVelocity(new Vec2(2, player.getBody().getLinearVelocity().y));
			player.accelerate(false);
		}
		
		if (input.isKeyPressed(Input.KEY_DOWN) || input.isKeyDown(Input.KEY_S) ) {
			player.getBody().setLinearVelocity(new Vec2(player.getBody().getLinearVelocity().x, -50));
		}
		
		// TODO Kamera Smoothness muss auch angepasst werden, je nach Zoom
		if (input.isKeyDown(Input.KEY_E)) {
			if (zoom < 200){
				zoom += ZOOM_STEP;
			}
		}
		if (input.isKeyDown(Input.KEY_R)) {
			if(zoom -ZOOM_STEP > ZOOM_STEP){
				zoom -= ZOOM_STEP;
			}
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
		// FIXME clean this crap up. create method: drawBackground().
		//*
		g.pushTransform();
			g.translate(cam.getX() * 0.475f * zoom + screenWidth / 2f, cam.getY() * 0.475f * zoom + screenHeight * 2f / 3f);
			g.scale(zoom, zoom);
			trashpile[2].draw(13, -18, 40f, 20f);
		g.popTransform();
	
	
		g.pushTransform();
			g.translate(cam.getX() * 0.575f * zoom + screenWidth / 2f, cam.getY() * 0.575f * zoom + screenHeight * 2f / 3f);
			g.scale(zoom, zoom);
			trashpile[4].draw(6, -18, 40f, 20f);
		g.popTransform();
		
		g.pushTransform();
			g.translate(cam.getX() * 0.675f * zoom + screenWidth / 2f, cam.getY() * 0.675f * zoom + screenHeight * 2f / 3f);
			g.scale(zoom, zoom);
			trashpile[3].draw(23, -18, 40f, 20f);
		g.popTransform();
	
		g.pushTransform();
			g.translate(cam.getX() * 0.75f * zoom + screenWidth / 2f, cam.getY() * 0.75f * zoom + screenHeight * 2f / 3f);
			g.scale(zoom, zoom);
			trashpile[0].draw(-7, -29, 30f, 15f);
		g.popTransform();

		g.pushTransform();
			g.translate(cam.getX() * 0.875f * zoom + screenWidth / 2f, cam.getY() * 0.875f * zoom + screenHeight * 2f / 3f);
			g.scale(zoom, zoom);
			trashpile[1].draw(15, -18, 40f, 20f);
		g.popTransform();
		


		/*/
		for(int i=0; i<trashpile.length; ++i){
			g.pushTransform();
				if (i%2 == 0){
					g.translate(cam.getX() * 0.75f * zoom + screenWidth / 2f, cam.getY() * 0.75f * zoom + screenHeight * 2f / 3f);
					g.scale(zoom, zoom);
					trashpile[i].draw(-7 + (10*i), -29, 30f, 15f);
				}
				else {
					g.translate(cam.getX() * 0.875f * zoom + screenWidth / 2f, cam.getY() * 0.875f * zoom + screenHeight * 2f / 3f);
					g.scale(zoom, zoom);
					trashpile[i].draw(3 + (15*i), -18, 40f, 20f);
				}					
			g.popTransform();
		}
		//*/
				
		g.translate(cam.getX() * zoom + screenWidth / 2f, cam.getY() * zoom + screenHeight * 2f / 3f);
		g.scale(zoom, zoom);
		
		for (GameObject staticObj : staticObjects) {
			staticObj.draw();
		}
		
		/*for (int y = 0; y < worldImages.length; ++y) {
			for (int x = 0; x < worldImages[0].length; ++x) {
				worldImages[y][x].draw(x * 19, y * 10, 19, 10);
			}
		}*/
		
		for (GameObject crate : crates) {
			crate.draw();
		}
		
		player.draw();
		
		
		// my poly		
		Polygon p = new Polygon();
		Vec2[] verts = polyPolyShape.getVertices();
		for (Vec2 v : verts){
			p.addPoint(polyBodyDef.position.x+ v.x, -(polyBodyDef.position.y +v.y));
		}
		g.draw(p);
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
	
}
