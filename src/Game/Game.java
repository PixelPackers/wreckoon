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
import org.newdawn.slick.SlickException;

import com.google.gson.Gson;

public class Game extends BasicGame {

//	/*/
	private static int screenWidth = 1600;
	private static int screenHeight = 900;
	private static boolean fullScreen = false;
	private static boolean DEFAULT_RUNNING = true;
	/*/
	private static int screenWidth = 1920;
	private static int screenHeight = 1080;
	private static boolean fullScreen = true;
	//*/

	private static boolean debugView = false;

	private static boolean useZoomAreas = false;
	private static float tragetZoom = 128f;
	private static float zoom = tragetZoom;
	private static final float MANUAL_ZOOM_STEP = 4f;
	
	private static float laserAngle;
	private static float laserTargetAngle;

	private static World world;

	private static ArrayList<GameObject> 	staticObjects 	= new ArrayList<GameObject>();
	private static ArrayList<GameObject> 	dynamicObjects 	= new ArrayList<GameObject>();
	private static ArrayList<Enemy> 		enemies			= new ArrayList<Enemy>();
	private static ArrayList<Tile>			tiles			= new ArrayList<Tile>();
	private static Player 					player;
	private static ArrayList<Part>			parts			= new ArrayList<Part>();
	private static ArrayList<Girder>		girders			= new ArrayList<Girder>();
	private static Generator 				generator;
	private static ArrayList<Spike>		spikes			= new ArrayList<Spike>();
	private static ArrayList<Conveyor>		conveyor 		= new ArrayList<Conveyor>();
	private static ArrayList<Bolt>			bolts 			= new ArrayList<Bolt>();
	private static ArrayList<Nut>			nuts 			= new ArrayList<Nut>();
	private static ArrayList<Shred>			shreds 			= new ArrayList<Shred>();


	private static ArrayList<GameObject>	objectsToAdd	 = new ArrayList<GameObject>();
	
	private static ArrayList<GameObject>	objectsToRemove = new ArrayList<GameObject>();
	private static ArrayList<Enemy>			enemiesToRemove = new ArrayList<Enemy>();
	private static ArrayList<Shred>			shredsToRemove 	= new ArrayList<Shred>();


	private static House house;
	private static Camera cam = new Camera(0, 0);
	private static Level level;

	private static Xbox360Controller xbox;

	private Color backgroundColor = new Color(112, 149, 163);
	private float BACKGROUND_SCALE = 0.008f;
	private int angleshit;
	private static Image[] trashpile = new Image[5];
	
	private static Image pauseImage;
	
	private static Laser laser;
	
	private static enum Mode {
		PLAY,
		PAUSE
	}
	private Mode curMode = Mode.PLAY;

	public Game() {
		super("The Raccooning");
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
//		MusicManager.getInstance().bgMusic();

		xbox = new Xbox360Controller();

		for (int i = 0; i <= 4; ++i) {
			trashpile[i] = new Image("images/background" + (i + 1) + ".png");
		}
		
		pauseImage = Images.getInstance().getImage("images/Pause.png");

		world = new World(new Vec2(0f, 20f), false);

		// load the level
		try {
			String json = readFile("levels/level1.json");
			level = new Gson().fromJson(json, Level.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		house = new House(world, -21.03f, level.getHeight() - 0.88f);

		// create the tiles
		for (int x = 0; x < level.getWidth(); ++x) {
			for (int y = 0; y < level.getHeight(); ++y) {
				Block b = level.getBlock(x, y);
				if (b.getType() > 0) {
					Tile newTile = new Tile(world, x, y, b.getType(), 180 + b.getAngle(), !b.isFlipped());
					tiles.add(newTile);
					
					//SPIKES
					if (isSpike(b.getType())) {
						Spike newSpike = new Spike(world, x, y, b.getType(), 180 + b.getAngle(), !b.isFlipped());
						spikes.add(newSpike);
					}
				}
			}
		}

		// create the parts
		for (Part p : level.getParts()) {
			parts.add(new Part(world, this, p.getX(), p.getY()));
		}
	

		girders.add(new Girder(world, 30f,  -5f, 7.75f));
		girders.add(new Girder(world, 25f,  -6f, 7.75f));
		girders.add(new Girder(world, 35f, -10f, 7.75f));

		generator = new Generator(world, 5f, 15f, 5f*0.25f, 6f*0.25f);

		//conveyor.add(new Conveyor(world, 7f, 4f, 11f, 0.1f, 0.5f, 0.5f, 0.5f));

		player = new Player(world, 5f, 3f);
		laser = new Laser(world, 0f, 0f);
		player.setLaser(laser);
		
//		player = new Player(world, 25f, 0f);
		world.setContactListener(new MyContactListener(this));
		
		// create the enemies
		for (SpawnPoint e : level.getEnemies()) {
			switch (e.getType()) {
				case 0:
					enemies.add(new DumbPig(this, e.getX(), e.getY(), 0.5f, 0.5f, 3.3f, 0.3f, 0.3f, null, BodyType.DYNAMIC));
				break;
				case 1:
					enemies.add(new SmartPig(this, e.getX(), e.getY(), 0.5f, 0.5f, 3.3f, 0.3f, 0.3f, null, BodyType.DYNAMIC));
				break;
			}
		}

//		for (int i = 0; i < 10; ++i) {
//			enemies.add( new DumbPig(this, 1*i +10f, 5f, 0.5f, 0.5f, 3.3f, 0.3f, 0.3f, null, BodyType.DYNAMIC) );
//			enemies.add( new SmartPig(this, 1f*i, 5f, 0.5f, 0.5f, 3.3f, 0.3f, 0.3f, null, BodyType.DYNAMIC));
//		}

	}

	private boolean isSpike(int type) {
		if ((type >= 50 && type <= 55) ||
			(type == 58) ||
			(type == 41)) {
			return true;
		}
		return false;
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
		Input input = gc.getInput();
		if (xbox.isButtonStartDown() || input.isKeyPressed(Input.KEY_ESCAPE)) {
			if (curMode == Mode.PLAY) {
				curMode = Mode.PAUSE;
				pauseAnimations();
			} else {
				curMode = Mode.PLAY;
				restartAnimations();
			}
		}
		
		if (curMode == Mode.PLAY) {
			processInput(input);
		
			house.updateAnimations();
			
			laserTargetAngle = (float) Math.toRadians(xbox.getLeftThumbDirection() - 90d);
			
	//		if (laserAngle < 360f) {
	//			laserAngle -= 360f;
	//		}
			laserAngle = laser.curveAngle(laserTargetAngle, laserAngle, 10);
			laser.position(
					player.getBody().getPosition().x,
					player.getBody().getPosition().y,
					laserAngle);
			
			player.update();
	
			for(Enemy enemy : enemies){
				enemy.update();
			}
			
			cam.follow(	(float) (player.getBody().getPosition().x + xbox.getRightThumbX() * 3),
						(float) (player.getBody().getPosition().y + xbox.getRightThumbY() * 3),
						10);
			
			
			for (GameObject o :  objectsToRemove){
				world.destroyBody(o.getBody());
				
				if(o instanceof Enemy){
					for(int i = 0; i < 7; ++i){
						
						float power = 10f;
						Vec2 direction = new Vec2( 
								(float)(Math.random()*power*2-power),
								-((float)(Math.random()*power)) );
						
						
						Nut nut = new Nut(this, getWorld(), o.getBody().getPosition().add(new Vec2(0,0)), "images/nut"+ ((int) (Math.random()*3)+1)+".png" );
						nut.getBody().setLinearVelocity( direction);
						getNuts().add(nut);
						
						Bolt bolt = new Bolt(this, getWorld(), o.getBody().getPosition().add(new Vec2(0,0)), "images/bolt"+ ((int) (Math.random()*3)+1)+".png" );
						bolt.getBody().setLinearVelocity( direction);
						getBolts().add(bolt);
	
						Shred shred = new Shred(this, getWorld(), o.getBody().getPosition().add(new Vec2(0,0)), "images/shred"+ ((int) (Math.random()*3)+1)+".png", ((Enemy) o).getPigSize());
						shred.getBody().setLinearVelocity(direction);
						getShreds().add(shred);
						
					}
				}
			}		
			objectsToRemove.clear();
			
			for (Enemy e :  enemiesToRemove){
	
				enemies.remove(e);
			}		
			enemiesToRemove.clear();
			
			for (Shred s : shredsToRemove){
				shreds.remove(s);
			}
			
			for (Shred s : shreds) {
				s.increaseCounter();
			}
			
			for (Part p : parts) {
				p.update();
			}
			
	//		for (GameObject o :  objectsToAdd){
	//			world.destroyBody(o.getBody());
	//		}		
	//		objectsToAdd.clear();
			
			
			world.step(delta / 1000f, 18, 6);
		}
	}

	private void restartAnimations() {
		generator.getAnimation().start();
		for (Enemy e : enemies) {
			e.getCurrentAnimation().start();
		}
		player.getCurrentAnimation().start();
	}

	private void pauseAnimations() {
		generator.getAnimation().stop();
		for (Enemy e : enemies) {
			e.getCurrentAnimation().stop();
		}
		player.getCurrentAnimation().stop();
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		
		g.setBackground(backgroundColor);

		drawBackgroundObjects(g);

		g.pushTransform();
		g.translate(-cam.getX() * zoom + screenWidth / 2f, -cam.getY() * zoom + screenHeight * 2f / 3f);
		g.scale(zoom, zoom);

		house.draw(g, debugView);

		generator.draw(g, debugView);

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
		for (Enemy enemy : enemies) {
			enemy.draw(g, debugView);	
		}

		for (Tile tile : tiles) {
			tile.draw(g, debugView);
		}
		
		for (Spike spike : spikes) {
			spike.draw(g, debugView);
		}

		for (Part part : parts){
			part.draw(g, debugView);
		}

		for (Girder sb : girders) {
			sb.draw(g, debugView);
		}

		for (Spike s : spikes){
			s.draw(g, debugView);
		}

		for (Conveyor c : conveyor){
			c.draw(g, debugView);
		}
		
		for(Bolt b : bolts){
			b.draw(g, debugView);
		}
		for(Nut n : nuts){
			n.draw(g, debugView);
		}
		for(Shred s : shreds){
			s.draw(g, debugView);
		}

		house.drawFront(g, debugView);
		
		//laser.draw(g, debugView);
		
		g.popTransform();
		
		if (curMode == Mode.PAUSE) {
			g.setColor(new Color(0f, 0f, 0f, 0.3f));
			g.fillRect(0, 0, screenWidth, screenHeight);
			pauseImage.drawCentered(screenWidth * 0.5f, screenHeight * 0.4f);
		}
		
		//drawZoomAreas(g);

		// GUI
//		g.setColor(Color.white);
//		// scale pixel size : box2d:size
//		g.drawString("10px", 0, 50);
//		g.drawRect(50, 50, 10, 1);
//		g.drawRect(50, 55, 10 * zoom, 1);
//		g.setColor(Color.white);
//		g.drawString("Count: " + world.getBodyCount(), 0, 0);
//
//		g.drawString("ShootingDirection: " + player.getShootingDirection(), 200, 10);
//		g.drawString("ShootingPower: " + player.getShootingPower(), 200, 30);
//		g.drawString("pos: " + player.getBody().getPosition(), 200, 50);
		
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






	private void drawZoomAreas(Graphics g) {
		if (level.getZoomAreas() != null) {
			for (ZoomArea za : level.getZoomAreas()) {
				g.pushTransform();
				g.translate(-cam.getX() * zoom + screenWidth / 2, -cam.getY() * zoom + screenHeight * 2 / 3);
				g.scale(zoom, zoom);
				g.drawRect(za.getX1(), za.getY1(), za.getWidth(), za.getHeight());
				g.popTransform();
			}
		}
	}

	private void drawBackgroundObjects(Graphics g) {
		if (level.getBackgroundObjects() != null) {
			for (BackgroundObject bo : level.getBackgroundObjects()) {
				g.pushTransform();
				g.translate(-cam.getX() * zoom * bo.getZ() + screenWidth / 2, -cam.getY() * zoom * bo.getZ() + screenHeight * 2 / 3);
				g.scale(zoom * bo.getZ(), zoom * bo.getZ());

				Image tmp = trashpile[bo.getType()];
				tmp = tmp.getFlippedCopy(bo.isFlipped(), false);
				tmp.setImageColor(1f - bo.getZ() + backgroundColor.r * bo.getZ(),
						1f - bo.getZ() + backgroundColor.g * bo.getZ(),
						1f - bo.getZ() + backgroundColor.b * bo.getZ());
				tmp.draw(bo.getX() - tmp.getWidth() * BACKGROUND_SCALE / 2,
						 bo.getY() - tmp.getHeight() * BACKGROUND_SCALE / 2,
						 tmp.getWidth() * BACKGROUND_SCALE, tmp.getHeight() * BACKGROUND_SCALE);
				g.popTransform();
			}
		}
	}

	public void processInput(Input input) throws SlickException{

		if (xbox.isButtonADown() || input.isKeyPressed(Input.KEY_SPACE) || input.isKeyPressed(Input.KEY_W) || input.isKeyPressed(Input.KEY_UP)) {

			 if( !player.isCharging() ) {
				player.jump();
			}

		}

		/// XXX MAGIC NUMBERS
		/// max gegenlenken
		float minCounterSteerSpeed = (!player.isJumpingFromWall() ) ? -5 : 5;
		// FIXME set this to 420 to see the bug... only works in one direction
		float slowDownForce = 5f;
		float slowDownThreshold = 0.5f;

		if (xbox.isLeftThumbTiltedLeft() || input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_A)) {
			if( player.isCharging() ){
//				player.getShootingDirection().x -= 1;
				player.increaseShootingDirection(-1, 0);
			} else {
//				if (player.isOnGround()) {
					player.setLeft(true);
//				} 
				if (player.movesLeft()) {
					player.accelerate((float) xbox.getLeftThumbMagnitude());
				} 
//				else if( player.getBody().getLinearVelocity().x > minCounterSteerSpeed) { 
//					player.setLeft(true);
//					player.accelerate();
//					player.setLeft(false);					
//				}
			}
		}
		
		if (input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_A)) {
			if (player.movesLeft()) {
				player.accelerate(1f);
			}
		}

		if (xbox.isLeftThumbTiltedRight() || input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D)) {

			if(player.isCharging()){
//				player.getShootingDirection().x += 1;
				player.increaseShootingDirection( 1, 0);
			} else {
//				if (player.isOnGround()) {
					player.setLeft(false);
//				}
				if (!player.movesLeft()) {
					player.accelerate((float) xbox.getLeftThumbMagnitude());
				}
//				else if (player.getBody().getLinearVelocity().x < -minCounterSteerSpeed) { 
//					player.setLeft(false);
//					player.accelerate();
//					player.setLeft(true);					
//				}
			}
		}
		
		if (input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D)) {
			if (!player.movesLeft()) {
				player.accelerate(1f);
			}
		}

		if (xbox.getLeftThumbY() > 0.5f || input.isKeyPressed(Input.KEY_DOWN) || input.isKeyPressed(Input.KEY_S)) {
			if( !player.isCharging() && !player.isOnGround()) {
				player.groundpoundInit();
			}
//			} else if (player.isOnWall()){
//				if(player.leftWallColliding()){
//					// XXX magic numbers
//					player.getBody().setLinearVelocity(new Vec2(3,0));
//				} else if(player.rightWallColliding()){
//					player.getBody().setLinearVelocity(new Vec2(-3,0));
//				}
//			}
		}

		if (xbox.isButtonADown() || input.isKeyDown(Input.KEY_SPACE) || input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP)) {
			if( player.isCharging() ){
//				player.getShootingDirection().y += 1;
				player.increaseShootingDirection(0, 1);
			} 
		}
		
		if (xbox.isButtonAUp() && player.getBody().getLinearVelocity().y < 0f && !player.isOnGround()) {
			player.cancelJump();
		}

		if (xbox.isLeftThumbTiltedDown() || input.isKeyDown(Input.KEY_DOWN) || input.isKeyDown(Input.KEY_S)) {
			if( player.isCharging() ){
//				player.getShootingDirection().y -= 1;
				player.increaseShootingDirection(0, -1);
			}
		}
		
		if (input.isKeyPressed(Input.KEY_Z)) {
			useZoomAreas = !useZoomAreas;
		}
		
		
		
		// TODO Kamera Smoothness muss auch angepasst werden, je nach Zoom
		if (useZoomAreas) {
			float biggestZoom = 0f;
			for (ZoomArea za: level.getZoomAreas()) {
				Vec2 pos = player.getBody().getPosition();
				if (za.isInArea(pos.x, pos.y)) {
					if (za.getZoom() > biggestZoom) {
						biggestZoom = za.getZoom();
					}
				}
			}
			if (biggestZoom > 0.001f) {
				tragetZoom = biggestZoom;
			}
			zoom = cam.curveValue(tragetZoom, zoom, 30);
		} else {
			if (input.isKeyDown(Input.KEY_E)) {
				if (zoom < 200) {
					zoom += MANUAL_ZOOM_STEP;
				}
			}
			if (input.isKeyDown(Input.KEY_R)) {
				if (zoom - MANUAL_ZOOM_STEP > MANUAL_ZOOM_STEP) {
					zoom -= MANUAL_ZOOM_STEP;
				}
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
			world.setGravity(new Vec2(0f, 0f));
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

			world.setGravity(new Vec2(0f, 20f));
		}
		if (input.isKeyDown(Input.KEY_C)) {
			float max_size = 0.2f;
			float min_size = 0.05f;
			float size = (float) Math.random() * (max_size - min_size) + min_size;

			for (int i = 0; i < 4; ++i) {

//				dynamicObjects.add( new GameObjectCircle(world, player.getBody().getPosition().x, player.getBody().getPosition().y - size * 2, size, 1f, 0.5f, 0f, "images/player.png", BodyType.DYNAMIC));
				bolts.add(new Bolt(this, world, player.getBody().getPosition().add(new Vec2(0,-1)), "images/bolt"+ ((int) (Math.random()*3)+1)+".png" ));
				nuts.add(new Nut(this, world, player.getBody().getPosition().add(new Vec2(0,-1)), "images/nut"+ ((int) (Math.random()*3)+1)+".png" ));
			}
		}

		if (input.isKeyDown(Input.KEY_V)) {
			if (!dynamicObjects.isEmpty()) {
				GameObject o = dynamicObjects.get(10);
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
		if(!xbox.isLeftTriggerDown() && !input.isKeyDown(input.KEY_LSHIFT) && !input.isKeyDown(input.KEY_RSHIFT)){
//			if (player.isOnGround() ){
				player.setRunning(DEFAULT_RUNNING);
//			}
		}
		if (xbox.isLeftTriggerDown() || input.isKeyDown(input.KEY_LSHIFT) || input.isKeyDown(input.KEY_RSHIFT)){
			if (player.isOnGround() && player.getBody().getLinearVelocity().x != 0){
				player.setRunning(!DEFAULT_RUNNING);
			} else if(player.isOnWall()){
				player.setRunning(!DEFAULT_RUNNING);
//				XXX wenn das einkommentiert is, rutscht er wenn er im sprint modus is
//				player.switchHitboxes();
			}

		}

		if (xbox.isButtonXDown() || input.isKeyDown(input.KEY_H) ){
			player.tailwhipInit();
		}

		if (xbox.isButtonYDown() || input.isKeyPressed(input.KEY_T) ){

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

		if(xbox.isButtonYDown() || input.isKeyPressed(input.KEY_J)){
			player.createLaser();
		}
		if(input.isKeyPressed(input.KEY_K)){
			player.destroyLaser();
		}

		if(input.isKeyPressed(input.KEY_P)){
			player.bite();
		}

		if(input.isKeyPressed(input.KEY_L)){
			player.die();
		}

		xbox.resetStates();
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

	public static Generator getGenerator() {
		return generator;
	}

	public static ArrayList<Spike> getSpikes() {
		return spikes;
	}

	public static ArrayList<Conveyor> getConveyor() {
		return conveyor;
	}
	
	public static ArrayList<Bolt> getBolts() {
		return bolts;
	}
	
	public static ArrayList<Nut> getNuts() {
		return nuts;
	}
	
	public static ArrayList<GameObject> getObjectsToAdd() {
		return objectsToAdd;
	}
	
	public static ArrayList<GameObject> getObjectsToRemove() {
		return objectsToRemove;
	}
	
	public static ArrayList<Enemy> getEnemiesToRemove() {
		return enemiesToRemove;
	}
	
	public static ArrayList<Shred> getShreds() {
		return shreds;
	}
	
	public static ArrayList<Shred> getShredsToRemove() {
		return shredsToRemove;
	}
}
