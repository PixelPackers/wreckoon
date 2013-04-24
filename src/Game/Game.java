package Game;

// TODO delete
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
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
import org.newdawn.slick.geom.Polygon;

import com.google.gson.Gson;

public class Game extends BasicGame {
	
	// Sifu's Kommentar
	
//	/*/
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
	private ArrayList<Body>	jsonObjects		= new ArrayList<Body>();
	private Player					player;
	private PolygonShape 			polyPolyShape;
	private BodyDef 				polyBodyDef;
	
	//private Image[][] worldImages = new Image[8][8];
	private Image[] trashpile = new Image[5];
	
	private float					zoom			= 30f;
	private final float				ZOOM_STEP		= 1f;
	
	private Camera					cam				= new Camera(0, screenHeight);
	private static final float		CAM_SPEED		= 5f;

	float deleteMeRotation = 0;
	float deleteMeX = 0;
	float deleteMeY = 0;
	
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
		{
			BodyDef wallBodyDef = new BodyDef();
			wallBodyDef.position.set(22f, 0f);
			PolygonShape wallShape = new PolygonShape();
			wallShape.setAsBox(1f, 10f);
			FixtureDef wallFixtureDef = new FixtureDef();
			wallFixtureDef.shape = wallShape;
			GameObject wall = new GameObject(wallBodyDef, wallFixtureDef, world, "images/crate.png");
			staticObjects.add(wall);
		}
		{
			BodyDef wallBodyDef = new BodyDef();
			wallBodyDef.position.set(-45f, 20f);
			PolygonShape wallShape = new PolygonShape();
			wallShape.setAsBox(1f, 20f);
			FixtureDef wallFixtureDef = new FixtureDef();
			wallFixtureDef.shape = wallShape;
			GameObject wall = new GameObject(wallBodyDef, wallFixtureDef, world, "images/crate.png");
			staticObjects.add(wall);
		}
		{
			BodyDef wallBodyDef = new BodyDef();
			wallBodyDef.position.set(45f, 20f);
			PolygonShape wallShape = new PolygonShape();
			wallShape.setAsBox(1f, 20f);
			FixtureDef wallFixtureDef = new FixtureDef();
			wallFixtureDef.shape = wallShape;
			GameObject wall = new GameObject(wallBodyDef, wallFixtureDef, world, "images/crate.png");
			staticObjects.add(wall);
		}
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

			Vec2[] points = new Vec2[7];
			points[0] = new Vec2(  0f,	0f);
			points[1] = new Vec2( 22f,	0f);
			points[2] = new Vec2( -6f, 12f);
			points[3] = new Vec2( -8f,	8f);
			points[4] = new Vec2( -8f,	7f);
			points[5] = new Vec2( -7f,	4f);
			points[6] = new Vec2( -5f,	2f);
//			points[7] = new Vec2( -2.5f,2f);
		polyPolyShape.set(points, 7);
		//*/
		FixtureDef polyFixDef = new FixtureDef();
		polyFixDef.shape = polyPolyShape;
		
		world.createBody(polyBodyDef);
		poly.createFixture(polyFixDef);
			
		// JSON Loader
		String test = "";
		try {
			test = readFile("etc/world");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONObject testObj = new Gson().fromJson(test, JSONObject.class);
		
		for (int i = 0; i < testObj.rigidBodies.size(); ++i) {
			BodyDef jsonBodyDef = new BodyDef();
			jsonBodyDef.type = BodyType.DYNAMIC;
			jsonBodyDef.position.set(18f * i, 25f);
			Body jsonBody;
			jsonBody = world.createBody(jsonBodyDef);
			
			for (int j = 0; j < testObj.rigidBodies.get(i).polygons.size(); ++j) {
				FixtureDef jsonFixtureDef = new FixtureDef();
				PolygonShape jsonShape = new PolygonShape();
				Vec2[] jsonPoints = new Vec2[100];
				for (int k = 0; k < testObj.rigidBodies.get(i).polygons.get(j).size(); ++k) {
					jsonPoints[k] = testObj.rigidBodies.get(i).polygons.get(j).get(k).mul(1);
				}
				jsonShape.set(jsonPoints, testObj.rigidBodies.get(i).polygons.get(j).size());
				jsonFixtureDef.shape = jsonShape;
				jsonFixtureDef.density = 0.5f;
				jsonBody.createFixture(jsonFixtureDef);
			}
			jsonObjects.add(jsonBody);
			
		}
		
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
		
		/*/ Crate
		float min_size = 0.1f;
		float max_size = 0.3f;
		
		for (int i = 0; i < 5; ++i) {
			for (int j = 0; j < 5; ++j) {
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
		for (int i = 0; i < 5; ++i) {
			for (int j = 0; j < 5; ++j) {
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
	
	private String readFile( String file ) throws IOException {
	    BufferedReader reader = new BufferedReader( new FileReader (file));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");

	    while( ( line = reader.readLine() ) != null ) {
	        stringBuilder.append( line );
	        stringBuilder.append( ls );
	    }

	    reader.close();
	    return stringBuilder.toString();
	}
	
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		Input input = gc.getInput();
		
		if (input.isKeyPressed(Input.KEY_SPACE) || input.isKeyPressed(Input.KEY_W) || input.isKeyPressed(Input.KEY_UP) ) {
			player.getBody().setLinearVelocity(new Vec2(player.getBody().getLinearVelocity().x, 20));
		}
		if (input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_A) ) {
			
			if(input.isKeyDown(Input.KEY_LSHIFT) || input.isKeyDown(Input.KEY_RSHIFT)){
				deleteMeX -=10;
			} else {
				// player.getBody().setLinearVelocity(new Vec2(-2, player.getBody().getLinearVelocity().y));
				player.accelerate(true);
			}
		}
		if (input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D) ) {

			if(input.isKeyDown(Input.KEY_LSHIFT) || input.isKeyDown(Input.KEY_RSHIFT)){
				deleteMeX +=10;
			} else {
				// player.getBody().setLinearVelocity(new Vec2(2, player.getBody().getLinearVelocity().y));
				player.accelerate(false);
			}
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
		
		if (input.isKeyPressed(Input.KEY_X)) {
			if( !crates.isEmpty() ){
//				GameObject o = crates.get(0);
//				o.getBody().setLinearVelocity( new Vec2(0, 14 ) );
//				world.destroyBody( o.getBody() );
//				crates.remove(o);
			}
			for ( GameObject o : crates){
				
				float oX = o.getBody().getPosition().x;
				float pX = player.getBody().getPosition().x;
				float distance = Math.abs(oX - pX);
				float space = 5f;
				
				if(distance < space){
					o.getBody().setLinearVelocity( new Vec2( o.getBody().m_linearVelocity.x, (space-distance)*1.5f ) ) ;
				}
			}
			world.setGravity( new Vec2(0f,-1f));
		}
		if (input.isKeyDown(Input.KEY_Y)) {
			if( !crates.isEmpty() ){
				GameObject o = crates.get(0);
				world.destroyBody( o.getBody() );
				crates.remove(o);
			}

			world.setGravity( new Vec2(0f,-30f));
		}
		if (input.isKeyDown(Input.KEY_C)) {
			float max_size = 0.5f;
			float min_size=0.05f;
			float size = (float) Math.random()* max_size + min_size;

			CircleShape c = new CircleShape();
			c.m_radius = size;
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = c;
			fixtureDef.density = 1f;
			fixtureDef.friction = 0.5f;
			
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.DYNAMIC;
			bodyDef.position.set(player.getBody().getPosition().x, player.getBody().getPosition().y - size*2);
			for(int i =0; i< 4; ++i){
				GameObject crate = new GameObject(bodyDef, fixtureDef, world, "images/player.png");
				crates.add(crate);
				world.createBody(bodyDef);
			}
		}
		

		if (input.isKeyDown(Input.KEY_V)) {
			if( !crates.isEmpty() ){
				GameObject o = crates.get(10);
				o.getBody().setTransform(new Vec2(
						 player.getBody().getPosition().x + 2,
						 player.getBody().getPosition().y + 1
						) // vec2 end
						, 0
				); // transform end
				// gravity f�r objekt deaktivieren?

			}
		}
		if (input.isKeyDown(Input.KEY_J)) {
			deleteMeRotation -= 10;
		}

		if (input.isKeyDown(Input.KEY_K)) {
			deleteMeRotation+=10;
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
		world.step(delta / 1000f, 18, 6);
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {

		g.pushTransform();
		drawBackground(g);
		
				
		g.translate(cam.getX() * zoom + screenWidth / 2f, cam.getY() * zoom + screenHeight * 2f / 3f);
		g.scale(zoom, zoom);
		
		for (GameObject staticObj : staticObjects) {
			staticObj.draw();
		}
		
		for (Body b : jsonObjects) {
			Fixture f = b.getFixtureList();
//			System.out.println(f.m_shape);
			while (f != null) {
				Polygon p = new Polygon();
				PolygonShape ps = (PolygonShape) f.getShape();
				
				Vec2[] verts = ps.getVertices();
				for (int i = 0; i < ps.getVertexCount(); ++i){
					Vec2 worldPoint = b.getWorldPoint(verts[i]);
					p.addPoint(worldPoint.x, -worldPoint.y);
					//p.addPoint(b.getPosition().x+ verts[i].x, -(b.getPosition().y +verts[i].y));
				}
				g.pushTransform();
//				g.rotate(b.getPosition().x, b.getPosition().y, (float)Math.toDegrees(b.getAngle() ));
				// FIXME uber crap
				g.rotate(b.getPosition().x, b.getPosition().y, deleteMeRotation );
					g.draw(p);

				g.popTransform();
				f = f.getNext();
			}
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
		
		
		// GUI
		g.popTransform();
		g.drawString("10px", 0, 50);
		g.drawRect(50, 50, 10, 1);
		g.drawRect(50, 55, 10*zoom, 1);
		g.setColor(Color.white);
		g.drawString("Count: " + world.getBodyCount(), 0, 0);
		
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
	public void drawBackground(Graphics g){
		// FIXME clean this crap up. 
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
	}
}
