package Game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.google.gson.Gson;

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
	
	private float zoom = 30f;
	private final float ZOOM_STEP = 1f;
	
	private World world;

	private ArrayList<GameObject> 	staticObjects 	= new ArrayList<GameObject>();
	private ArrayList<GameObject> 	dynamicObjects 	= new ArrayList<GameObject>();
	private ArrayList<Enemy> 		enemies			= new ArrayList<Enemy>();
	private ArrayList<Tile>			tiles			= new ArrayList<Tile>();
	private Player player;
	private ArrayList<Part>			parts			= new ArrayList<Part>();

	private Image[] trashpile = new Image[5];

	private Camera cam = new Camera(0, screenHeight);
	private Level level;

	public Game() {
		super("The Raccooning");
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		
		for (int i = 0; i <= 4; ++i) {
			trashpile[i] = new Image("images/background" + (i + 1) + ".png");
		}
		
		world = new World(new Vec2(0f, -30f), false);
		
		// load the level
		try {
			String json = readFile("levels/big.json");
			level = new Gson().fromJson(json, Level.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (int x = 0; x < level.getWidth(); ++x) {
			for (int y = 0; y < level.getHeight(); ++y) {
				Block b = level.getBlock(x, y);
				if (b.getType() > 0) {
					Tile newTile = new Tile(world, x * 4, -y * 4, b.getType(), -b.getAngle(), b.isFlipped());
					tiles.add(newTile);
				}
			}
		}
		
		player = new Player(world, 2f, 4f);
		world.setContactListener(new MyContactListener(this));
		for(int i=0; i<10; ++i){

			enemies.add( new EnemyPrimitive(this, 10*i +10f, 5f, 2f, 2f, 3.3f, 0.3f, 0.3f, null, BodyType.DYNAMIC) );
		}
		enemies.add( new EnemyStupidFollower(this, 10f, 5f, 2f, 2f, 3.3f, 0.3f, 0.3f, null, BodyType.DYNAMIC) );
		enemies.add( new EnemyStupidFollower(this, 15f, 5f, 2f, 2f, 3.3f, 0.3f, 0.3f, null, BodyType.DYNAMIC) );
		enemies.add( new EnemyStupidFollower(this, 124f, 5f, 2f, 2f, 3.3f, 0.3f, 0.3f, null, BodyType.DYNAMIC) );
		enemies.add( new EnemyPrimitive		(this, 14f, 5f, 2f, 2f, 3.3f, 0.3f, 0.3f, null, BodyType.DYNAMIC) );
		
//		music();
		
		parts.add( new Part(world, this, 10f, 0f) );
		
	}
	
	private String readFile( String file ) throws IOException {
	    BufferedReader reader = new BufferedReader( new FileReader (file));
	    String line = null;
	    StringBuilder stringBuilder = new StringBuilder();
	    String ls = System.getProperty("line.separator");
	    while((line = reader.readLine()) != null) {
	        stringBuilder.append(line);
	        stringBuilder.append(ls);
	    }
	    return stringBuilder.toString();
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		processInput(gc);
		
		player.update();
		
		for(Enemy enemy : enemies){
			enemy.update();
		}
		
		cam.follow(player.getBody().getPosition().x, player.getBody().getPosition().y, 10);

		world.step(delta / 1000f, 18, 6);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		
		g.setBackground(Color.gray);
		g.pushTransform();
		drawBackground(g);

		g.translate(cam.getX() * zoom + screenWidth / 2f, cam.getY() * zoom + screenHeight * 2f / 3f);
		g.scale(zoom, zoom);

		for (GameObject staticObj : staticObjects) {
			staticObj.draw(g, debugView);
		}

		GameObject glowingObj = chooseTelekinesisTarget();
		for (GameObject ball : dynamicObjects) {
			
			Color tmpColor;
			tmpColor = g.getColor();
			
			if(ball == glowingObj) {
				g.setColor(new Color(155,155,255));
			}
			ball.draw(g, debugView);
			g.setColor(tmpColor);
			
		}

		player.draw(g, debugView);
		for(Enemy enemy : enemies){
			enemy.draw(g, debugView);	
		}
		
		
		for (Tile tile : tiles) {
			tile.draw(g, debugView);
		}

		for (Part part : parts){
			part.draw();
		}
		// GUI
		g.popTransform();

		// scale pixel size : box2d:size
		g.drawString("10px", 0, 50);
		g.drawRect(50, 50, 10, 1);
		g.drawRect(50, 55, 10 * zoom, 1);
		g.setColor(Color.white);
		g.drawString("Count: " + world.getBodyCount(), 0, 0);

		g.drawString("ShootingDirection: " + player.getShootingDirection(), 200, 10);
		g.drawString("ShootingPower: " + player.getShootingPower(), 200, 30);
		

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
		if(!debugView){
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
	}
	
	public void processInput(GameContainer gc) throws SlickException{
		Input input = gc.getInput();

		if (input.isKeyPressed(Input.KEY_SPACE) || input.isKeyPressed(Input.KEY_W) || input.isKeyPressed(Input.KEY_UP)) {
			
			 if( !player.isCharging() ) {
				player.jump();
			}
			 
		}
		
		/// XXX MAGIC NUMBERS
		/// max gegenlenken
		float minCounterSteerSpeed = (!player.isJumpingFromWall() ) ? -5 : 5; 
		float slowDownForce = 20f;
		float slowDownThreshold = 0.5f;
		
		if (input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_A)) {
			if( player.isCharging() ){
//				player.getShootingDirection().x -= 1;
				player.increaseShootingDirection(-1, 0);
			} else {
//				if (player.isOnGround()) {
					player.setLeft(true);
//				} 
				if (player.movesLeft()) {
					player.accelerate();
				} 
//				else if( player.getBody().getLinearVelocity().x > minCounterSteerSpeed) { 
//					player.setLeft(true);
//					player.accelerate();
//					player.setLeft(false);					
//				}
			}
		} else {
			// control slippery
			// TODO evtl herausheben und in der update die bewegungsrichtung überprüfen dh das für links und rechts dasselbe funkt und verwendet wird
			if(player.getBody().getLinearVelocity().x < -slowDownThreshold && player.isOnGround()) {
				player.getBody().applyLinearImpulse(new Vec2(slowDownForce,0), player.getBody().getPosition());
			}
		}

		if (input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D)) {

			if(player.isCharging()){
//				player.getShootingDirection().x += 1;
				player.increaseShootingDirection( 1, 0);
			} else {
//				if (player.isOnGround()) {
					player.setLeft(false);
//				}
				if (!player.movesLeft()) {
					player.accelerate();
				}
//				else if (player.getBody().getLinearVelocity().x < -minCounterSteerSpeed) { 
//					player.setLeft(false);
//					player.accelerate();
//					player.setLeft(true);					
//				}
			}
		}  else {
			if(player.getBody().getLinearVelocity().x > slowDownThreshold && player.isOnGround()) {
				player.getBody().applyLinearImpulse(new Vec2(-slowDownForce,0), player.getBody().getPosition());
			}
		}

		if (input.isKeyPressed(Input.KEY_DOWN) || input.isKeyPressed(Input.KEY_S)) {
			 if( !player.isCharging() && !player.isOnWall() && !player.isOnGround()) {
				player.groundpoundInit();
			} else if (player.isOnWall()){
				if(player.leftWallColliding()){
					// XXX magic numbers
					player.getBody().setLinearVelocity(new Vec2(3,0));
				} else if(player.rightWallColliding()){
					player.getBody().setLinearVelocity(new Vec2(-3,0));
				}
			}
		}
		
		if (input.isKeyDown(Input.KEY_SPACE) || input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP)) {
			if( player.isCharging() ){
//				player.getShootingDirection().y += 1;
				player.increaseShootingDirection(0, 1);
			} 
		}
		
		if (input.isKeyDown(Input.KEY_DOWN) || input.isKeyDown(Input.KEY_S)) {
			if( player.isCharging() ){
//				player.getShootingDirection().y -= 1;
				player.increaseShootingDirection(0, -1);
			}
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
			if (!dynamicObjects.isEmpty()) {
				// GameObject o = crates.get(0);
				// o.getBody().setLinearVelocity( new Vec2(0, 14 ) );
				// world.destroyBody( o.getBody() );
				// crates.remove(o);
			}
			for (GameObject o : dynamicObjects) {

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
				if (!dynamicObjects.isEmpty()) {
					GameObject o = dynamicObjects.get(0);
					world.destroyBody(o.getBody());
					dynamicObjects.remove(o);
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
				dynamicObjects.add(ball);
			}
		}

		if (input.isKeyDown(Input.KEY_V)) {
			if (!dynamicObjects.isEmpty()) {
				GameObject o = dynamicObjects.get(10);
				o.getBody().setTransform(new Vec2(player.getBody().getPosition().x + 2, player.getBody().getPosition().y + 1) // vec2
																																// end
						, 0); // transform end
								// gravity fï¿½r objekt deaktivieren?

			}
		}
		
		if (input.isKeyPressed(Input.KEY_ENTER)) {
			debugView = !debugView;			
		}
		
		
		// TODO crappy, weils keine keyUp() methode gibt. die reihenfolge muss auch so erhalten bleiben, sonsts is immer false
		if(!input.isKeyDown(input.KEY_LSHIFT) && !input.isKeyDown(input.KEY_RSHIFT)){
//			if (player.isOnGround() ){
				player.setRunning(false);
//			}
		}
		if (input.isKeyDown(input.KEY_LSHIFT) || input.isKeyDown(input.KEY_RSHIFT)){
			if (player.isOnGround() && player.getBody().getLinearVelocity().x != 0){
				player.setRunning(true);
			} else if(player.isOnWall()){
				player.setRunning(true);
//				XXX wenn das einkommentiert is, rutscht er wenn er im sprint modus is
//				player.switchHitboxes();
			}
			
		}
		

		if (input.isKeyDown(input.KEY_H) ){
			player.tailwhipInit();
		}
		
		if (input.isKeyPressed(input.KEY_T) ){
			
			if(player.shouldntMove()) {
				return;
			}
			
			if( !player.hasLockedObject() ){
				
				player.lockObject(chooseTelekinesisTarget() );
				
			} else if(player.isCharging()) {
				GameObject locked = player.getLockedObject();
				player.shoot();
				player.lockObject(locked);
			} else {
				player.releaseObject();
			}	
		}


		boolean shootkeyDown = false;
		
		if (input.isKeyDown( input.KEY_N) ) {
			shootkeyDown = true;
		}

		if( player.hasLockedObject() ) {
			if ( shootkeyDown){
				if ( !player.isCharging() ) {
					player.startCharging(); 
				}
			} else if ( player.isCharging() ) {
				player.shoot();
			}
		}		
	}
	public GameObject chooseTelekinesisTarget() {
		for (GameObject object : dynamicObjects) {
			
			float objectX = object.getBody().getPosition().x;
			float playerX = player.getBody().getPosition().x;
			float distance = Math.abs(objectX - playerX);
			float space = 5f;

			if (distance < space) {
				return object;
			}	
		}
		return null;
	}
	
	public World getWorld() {
		return world;
	}
	public Player getPlayer() {
		return player;
	}
	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}
	public ArrayList<GameObject> getDynamicObjects() {
		return dynamicObjects;
	}
	

	public ArrayList<Part> getParts() {
		return parts;
	}

	public void getRidOfPart(Part part) {
//		this.parts.remove(part);		
	}
	
}