package Game;

import java.util.ArrayList;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Game extends BasicGame {

//	 /*/
	private static int screenWidth = 800;
	private static int screenHeight = 600;
	private static boolean fullScreen = false;
	
	/*/
	 private static int screenWidth = 1920;
	 private static int screenHeight = 1080;
	 private static boolean fullScreen = true;
	 //*/
	private boolean debugView = true;
	
	private World world;

	private ArrayList<GameObject> 	staticObjects 	= new ArrayList<GameObject>();
	private ArrayList<GameObject> 	balls 			= new ArrayList<GameObject>();
//	private ArrayList<Body> 		jsonObjects 	= new ArrayList<Body>();
	private Player player;
	private GameObjectPolygon polygon;

	// private Image[][] worldImages = new Image[8][8];
	private Image[] trashpile = new Image[5];

	private float zoom = 30f;
	private final float ZOOM_STEP = 1f;

	private Camera cam = new Camera(0, screenHeight);

	public Game() {
		super("The Raccooning");
	}

	@Override
	public void init(GameContainer gc) throws SlickException {

//		for (int y = 0; y < worldImages.length; ++y) { 
//			for (int x = 0; x < worldImages[0].length; ++x) {
//				worldImages[y][x] = new	Image("images/world_x" + x % 2 + "_y" + y % 2 + ".png");
//				worldImages[y][x] = new Image("images/test" + (x + 1) + " (" + (y +	1) + ").png"); } 
//		}
		
		for (int i = 0; i <= 4; ++i) {
			trashpile[i] = new Image("images/background" + (i + 1) + ".png");
		}

		// world = new World(gravity, doSleep);
		world = new World(new Vec2(0f, -30f), false);

		

		float testWidth = 3f; 
		float testHeight = 3f;
		float space = 20f;
		int max = 5;
		for(int i=0; i<max; ++i){
			for(int j=0; j<max; ++j){
				if(j==i)
				staticObjects.add(new GameObjectBox(world,  space + i*testWidth,  j*testHeight, testWidth, testHeight, 0.5f,0.5f, 0f, "images/crate.png", BodyType.STATIC));
			}
		}
		
		
		// ground
		staticObjects.add(new GameObjectBox(world, 0f, -9f, 50f, 10f, 0.5f, 0.5f, 0f, "images/crate.png", BodyType.STATIC));

		// walls
		staticObjects.add(new GameObjectBox(world,  22f,  0f, 1f, 10f, 0.5f, 0.5f, 0f, "images/crate.png", BodyType.STATIC));
		staticObjects.add(new GameObjectBox(world, -25f, 0f, 1f, 20f, 0.5f, 0.5f, 0f, "images/crate.png", BodyType.STATIC));
		staticObjects.add(new GameObjectBox(world,  25f, 0f, 1f, 20f, 0.5f, 0.5f, 0f, "images/crate.png", BodyType.STATIC));

		// P O L Y G O N
		Vec2[] points = new Vec2[3];
		points[0] = new Vec2(0f, 0f);
		points[1] = new Vec2(6f, 0f);
		points[2] = new Vec2(-2f, 2f);
		polygon = new GameObjectPolygon(world, -5f, 0f, points, 0.9f, 0.5f, 0.3f, "images/player.png", BodyType.DYNAMIC);

		staticObjects.add( new GameObjectCircle(world, 2f, -4f, 1.5f, 0.9f, 0.5f, 0.7f, "images/player.png", BodyType.STATIC) );
//		// JSON Loader
//		String jsonFileAsString = "";
//		try {
//			jsonFileAsString = readFile("etc/world");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		JSONObject jsonObject = new Gson().fromJson(jsonFileAsString, JSONObject.class);
//		jsonObject.createShapes(world, jsonObjects);
//		
		player = new Player(world, 2f, 4f);
		world.setContactListener(new MyContactListener(player));
	}

//	private String readFile(String file) throws IOException {
//		BufferedReader reader = new BufferedReader(new FileReader(file));
//		String line = null;
//		StringBuilder stringBuilder = new StringBuilder();
//		String ls = System.getProperty("line.separator");
//
//		while ((line = reader.readLine()) != null) {
//			stringBuilder.append(line);
//			stringBuilder.append(ls);
//		}
//
//		reader.close();
//		return stringBuilder.toString();
//	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		
		processInput(gc);
		
		player.update();

		cam.follow(player.getBody().getPosition().x, player.getBody().getPosition().y, 10);

		// world.step(timeStep, velocityIterations, positionIterations);
		world.step(delta / 1000f, 18, 6);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		
		g.pushTransform();
		drawBackground(g);

		g.translate(cam.getX() * zoom + screenWidth / 2f, cam.getY() * zoom + screenHeight * 2f / 3f);
		g.scale(zoom, zoom);

		for (GameObject staticObj : staticObjects) {
			staticObj.draw(g, debugView);
		}

//		for (Body body : jsonObjects) {
//			Fixture fixture = body.getFixtureList();
//			// System.out.println(f.m_shape);
//			while (fixture != null) {
//				Polygon polygon = new Polygon();
//				PolygonShape polygonShape = (PolygonShape) fixture.getShape();
//
//				Vec2[] verts = polygonShape.getVertices();
//				for (int i = 0; i < polygonShape.getVertexCount(); ++i) {
//					Vec2 worldPoint = body.getWorldPoint(verts[i]);
//					polygon.addPoint(worldPoint.x, -worldPoint.y);
//					// p.addPoint(b.getPosition().x+ verts[i].x,
//					// -(b.getPosition().y +verts[i].y));
//				}
//				g.pushTransform();
//				g.draw(polygon);
//
//				g.popTransform();
//				fixture = fixture.getNext();
//			}
//		}

		for (GameObject ball : balls) {
			ball.draw(g, debugView);
		}

		player.draw(g, debugView);
		polygon.draw(g, debugView);

		// GUI
		g.popTransform();

		// scale pixel size : box2d:size
		g.drawString("10px", 0, 50);
		g.drawRect(50, 50, 10, 1);
		g.drawRect(50, 55, 10 * zoom, 1);
		g.setColor(Color.white);
		g.drawString("Count: " + world.getBodyCount(), 0, 0);
		
		g.drawString("PlayerSpeed: " + player.getCurrentVelocity().toString(), 200, 10);
		

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
	
	
	
	
	
	
	

	public void drawBackground(Graphics g) {
		// FIXME clean this crap up.
		// *
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
	}
	
	public void processInput(GameContainer gc) throws SlickException{
		Input input = gc.getInput();

		if (input.isKeyPressed(Input.KEY_SPACE) || input.isKeyPressed(Input.KEY_W) || input.isKeyPressed(Input.KEY_UP)) {
			player.jump();
		}
		if (input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_A)) {
			player.accelerate(true);
		}
		if (input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D)) {
			player.accelerate(false);
		}

		if (input.isKeyPressed(Input.KEY_DOWN) || input.isKeyDown(Input.KEY_S)) {
			player.groundPound();
		}

		// TODO Kamera Smoothness muss auch angepasst werden, je nach Zoom
		if (input.isKeyDown(Input.KEY_E)) {
			if (zoom < 200) {
				zoom += ZOOM_STEP;
			}
		}
		if (input.isKeyDown(Input.KEY_R)) {
			if (zoom - ZOOM_STEP > ZOOM_STEP) {
				zoom -= ZOOM_STEP;
			}
		}

		if (input.isKeyPressed(Input.KEY_X)) {
			if (!balls.isEmpty()) {
				// GameObject o = crates.get(0);
				// o.getBody().setLinearVelocity( new Vec2(0, 14 ) );
				// world.destroyBody( o.getBody() );
				// crates.remove(o);
			}
			for (GameObject o : balls) {

				float oX = o.getBody().getPosition().x;
				float pX = player.getBody().getPosition().x;
				float distance = Math.abs(oX - pX);
				float space = 5f;

				if (distance < space) {
					o.getBody().setLinearVelocity(new Vec2(o.getBody().m_linearVelocity.x, (space - distance) * 1.5f));
				}
			}
			world.setGravity(new Vec2(0f, -1f));
		}
		if (input.isKeyDown(Input.KEY_Y)) {
			for(int i=0; i < 10; ++i){
				if (!balls.isEmpty()) {
					GameObject o = balls.get(0);
					world.destroyBody(o.getBody());
					balls.remove(o);
				} else {
					break;
				}
			}

			world.setGravity(new Vec2(0f, -30f));
		}
		if (input.isKeyDown(Input.KEY_C)) {
			float max_size = 0.5f;
			float min_size = 0.05f;
			float size = (float) Math.random() * max_size + min_size;

			CircleShape c = new CircleShape();
			c.m_radius = size;
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = c;
			fixtureDef.density = 1f;
			fixtureDef.friction = 0.5f;

			for (int i = 0; i < 4; ++i) {
				GameObjectCircle ball = new GameObjectCircle(world, player.getBody().getPosition().x, player.getBody().getPosition().y - size
						* 2, size, 1f, 0.5f, 0f, "images/player.png", BodyType.DYNAMIC);
				balls.add(ball);
			}
		}

		if (input.isKeyDown(Input.KEY_V)) {
			if (!balls.isEmpty()) {
				GameObject o = balls.get(10);
				o.getBody().setTransform(new Vec2(player.getBody().getPosition().x + 2, player.getBody().getPosition().y + 1) // vec2
																																// end
						, 0); // transform end
								// gravity f�r objekt deaktivieren?

			}
		}
		
		if (input.isKeyPressed(Input.KEY_ENTER)) {
			debugView = !debugView;			
		}
		
		
		// TODO crappy, weils keine keyUp() methode gibt. die reihenfolge muss auch so erhalten bleiben, sonsts is immer false
		if(!input.isKeyPressed(input.KEY_LSHIFT) && !input.isKeyDown(input.KEY_RSHIFT)){
			player.setRunning(false);
		}
		if (input.isKeyDown(input.KEY_LSHIFT) || input.isKeyDown(input.KEY_RSHIFT)){
			player.setRunning(true);
		}

		
	}
}
