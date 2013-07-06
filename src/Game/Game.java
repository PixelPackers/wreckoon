package Game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

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
	private static boolean DOOMSDAY = false;

	private static boolean useZoomAreas = false;
	private static float tragetZoom = 128f;
	private static float zoom = tragetZoom;
	private static final float MANUAL_ZOOM_STEP = 4f;
	
	private static double laserAngle;
	private static double laserTargetAngle;

	private static World world;

	private static ArrayList<GameObject> 	staticObjects 	= new ArrayList<GameObject>();
	private static ArrayList<GameObject> 	dynamicObjects 	= new ArrayList<GameObject>();
	private static ArrayList<Enemy> 		enemies			= new ArrayList<Enemy>();
	private static ArrayList<Tile>			tiles			= new ArrayList<Tile>();
	private static Player 					player;
	private static ArrayList<Part>			parts			= new ArrayList<Part>();
	private static ArrayList<Girder>		girders			= new ArrayList<Girder>();
	private static Generator 				generator;
	private static ArrayList<Spike>			spikes		= new ArrayList<Spike>();
	private static ArrayList<Conveyor>		conveyor 	= new ArrayList<Conveyor>();
//	private static ArrayList<Bolt>			bolts 		= new ArrayList<Bolt>();
//	private static ArrayList<Nut>			nuts 		= new ArrayList<Nut>();
//	private static ArrayList<Shred>			shreds 		= new ArrayList<Shred>();
	private static ArrayList<DropItem>		dropItems	= new ArrayList<DropItem>();
	private static ArrayList<DropItem>		dropItemsToRemove = new ArrayList<DropItem>();
	


	private static ArrayList<GameObject>	objectsToAdd	 = new ArrayList<GameObject>();
	
	private static ArrayList<GameObject>	objectsToRemove = new ArrayList<GameObject>();
	private static ArrayList<Enemy>			enemiesToRemove = new ArrayList<Enemy>();
//	private static ArrayList<Shred>			shredsToRemove 	= new ArrayList<Shred>();

//	private Color backgroundColor = new Color(112, 149, 163);
	private static Color skyGradientColor1;
	private static Color skyGradientColor2;
	
	private static Rectangle skyRect;
	private static ShapeFill skyGradient;
	
	private static House house;
	private static Camera cam = new Camera(0, 0);
	private static Level level;

	private static Xbox360Controller xbox;
	
	private float BACKGROUND_SCALE = 0.008f;
	private float smoothBoltGUIAngle;
	private static Image[] trashpile = new Image[5];
	
	private static Image pauseImage;
	private static SpriteSheet digits;
	
	private int lastCheckpoint;
	
	private static Laser laser;
	
	private static enum Mode {
		PLAY,
		PAUSE
	}
	private Mode curMode = Mode.PLAY;
	private Color multiplierColor = new Color(245, 179, 141);
	private Color earthColor = new Color(164, 74, 13);

	
	public Game() {
		super("The Raccooning");
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
//		MusicManager.getInstance().bgMusic();

		xbox = new Xbox360Controller();

		for (int i = 0; i <= 4; ++i) {
			trashpile[i] = Images.getInstance().getImage("images/background" + (i + 1) + ".png");
		}
		
		pauseImage = Images.getInstance().getImage("images/Pause.png");
		digits = Images.getInstance().getSpriteSheet("images/digits.png", 50, 80);

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

		generator = new Generator(world, 5f, 14.6f, 5f*0.25f, 6f*0.25f);

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
		initNormalSky();

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
			
			laserTargetAngle = Math.toRadians(xbox.getLeftThumbDirection() - 90d);
			laserAngle = Laser.curveAngle(laserAngle, laserTargetAngle, 0.1f);
			laser.position(
					player.getBody().getPosition().x,
					player.getBody().getPosition().y - 0.4f,
					(float) laserAngle);
			
			player.update();
	
			for(Enemy enemy : enemies){
				enemy.update();
			}
			
			world.setGravity(new Vec2(0f, (float) ((1d - xbox.getLeftTriggerValue()) * 20f +  ((float) xbox.getRightTriggerValue() * -20f))));
			
			float targetCamX, targetCamY;
			if (player.isLaserActive()) {
				targetCamX = (float) (player.getBody().getPosition().x + xbox.getLeftThumbX() * 4f);
				targetCamY = (float) (player.getBody().getPosition().y + xbox.getLeftThumbY() * 4f);
			} else {
				targetCamX = (float) (player.getBody().getPosition().x + xbox.getRightThumbX() * 3f);
				targetCamY = (float) (player.getBody().getPosition().y + xbox.getRightThumbY() * 3f);
			}
			
			//if (targetCamY > level.getHeight() - screenHeight/2/zoom) targetCamY = level.getHeight() - screenHeight/2/zoom;
			cam.follow(targetCamX, targetCamY, 10);
			if(DOOMSDAY){
				cam.wiggle((player.isLaserActive()) ? 1f : 0.5f);
			} else {
				cam.wiggle((player.isLaserActive()) ? 1f : 0f);	
			}
			//if (cam.getY() > level.getHeight() - screenHeight/2/zoom) cam.setY(level.getHeight() - screenHeight/2/zoom); 
			
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
//						getNuts().add(nut);
						getDropItems().add(nut);
						
						Bolt bolt = new Bolt(this, getWorld(), o.getBody().getPosition().add(new Vec2(0,0)), "images/bolt"+ ((int) (Math.random()*3)+1)+".png" );
						bolt.getBody().setLinearVelocity( direction);
//						getBolts().add(bolt);
						getDropItems().add(bolt);
	
						Shred shred = new Shred(this, getWorld(), o.getBody().getPosition().add(new Vec2(0,0)), "images/shred"+ ((int) (Math.random()*3)+1)+".png", ((Enemy) o).getPigSize());
						shred.getBody().setLinearVelocity(direction);
//						getShreds().add(shred);
						getDropItems().add(shred);
						
					}
				}
			}		
			objectsToRemove.clear();
			
			for (Enemy e :  enemiesToRemove){
	
				enemies.remove(e);
			}		
			enemiesToRemove.clear();
			
			for (DropItem d : dropItemsToRemove){
				dropItems.remove(d);
			}
			dropItemsToRemove.clear();
						
			for (Part p : parts) {
				p.update();
			}
			
			for (DropItem d : dropItems){
				d.update();
			}
			
			Iterator iterator = player.getDropItemsToCollect().iterator();
			while (iterator.hasNext()){
				DropItem dropItem = (DropItem) iterator.next();
				if(dropItem.isCollectable()){
					dropItem.collect();
					iterator.remove();
					getObjectsToRemove().add(dropItem);
				}
			}
			
	//		for (GameObject o :  objectsToAdd){
	//			world.destroyBody(o.getBody());
	//		}		
	//		objectsToAdd.clear();
			
			if(player.isWaitingForLaserToBeKilled()){
				killLaser();
			}
			
			world.step(delta / 1000f, 18, 6);
			
		}
		if(DOOMSDAY){
			doomsdaySky();
		} else {
			initNormalSky();
		}
		
		xbox.resetStates();

	}
	

	private void initNormalSky() {
		skyGradientColor1 = new Color(112, 149, 163);
		skyGradientColor2 = new Color(152, 199, 193);
		
		skyRect = new Rectangle(0, 0, screenWidth, screenHeight);
		skyGradient = new GradientFill(0, 0, skyGradientColor1, 0, screenHeight, skyGradientColor2);
		
	}


	public double skyColorGlow = 0;
	
	private void doomsdaySky() {
		
		int max = 30;
		skyColorGlow += 0.01;
		double x = Math.sin(skyColorGlow) * max;
		
		
		Color skyGradientColor1 = new Color((int)(100+x), 0, 0);
		Color skyGradientColor2 = new Color(0, 0, 0);
		
		skyGradient = new GradientFill(0, 0, skyGradientColor1, 0, screenHeight, skyGradientColor2);
		
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
	
	private void drawRightAlignedDigits(int number, int x, int y) {
		String text = Integer.toString(number);
		while (text.length() < 4) {
			text = "0" + text;
		}
		for (int i = 0; i < text.length(); ++i) {
			int c = text.charAt(i) - 48;
			digits.getSprite(c, 0).draw(x + (i * 50 - text.length() * 50), y);
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		
		g.fill(skyRect, skyGradient);

		drawBackgroundObjects(g);

		g.pushTransform();
		g.translate(-cam.getX() * zoom + screenWidth / 2f, -cam.getY() * zoom + screenHeight * 2f / 3f);
		g.scale(zoom, zoom);
		
		g.setColor(earthColor);
		g.fillRect(-21.5f, level.getHeight() - 1f, level.getWidth() + 21f, 10f);

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
		
//		for(Bolt b : bolts){
//			b.draw(g, debugView);
//		}
//		for(Nut n : nuts){
//			n.draw(g, debugView);
//		}
//		for(Shred s : shreds){
//			s.draw(g, debugView);
//		}
		
		for (DropItem d : dropItems){
			d.draw(g, debugView);
		}

		house.drawFront(g, debugView);
		
		if (player.isLaserActive()) {
			laser.draw(g, debugView);
		}
		
		g.popTransform();
		
		drawBoltCounter();
		
		if (curMode == Mode.PAUSE) {
			g.setColor(new Color(0f, 0f, 0f, 0.3f));
			g.fillRect(0, 0, screenWidth, screenHeight);
			pauseImage.drawCentered(screenWidth * 0.5f, screenHeight * 0.4f);
		} else {
			g.setColor(Color.white);
		}
		
		//drawZoomAreas(g);
		//drawCheckpoints(g);

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
//		g.drawString("pigs: " + player.getPigCounter(), 10, 30);
		g.drawString("laserc: " + player.getLaserTime(), 10, 30);
		
		g.drawString("left thumbstick angle: " +  xbox.getLeftThumbDirection() + "\n" +
						"laser angle: " + laserAngle + "\n" +
						"laser target angle: " + laserTargetAngle, 10, 50);
	}

	private void drawBoltCounter() throws SlickException {
		int boltGUIAngle = player.getBoltCounter();
		Image nut = Images.getInstance().getImage("images/nut2.png");
		smoothBoltGUIAngle = cam.curveValue(boltGUIAngle, smoothBoltGUIAngle, 10);
		nut.setRotation(smoothBoltGUIAngle * 30);
		nut.setAlpha(1f);
		nut.draw(screenWidth - 130, 36);
		drawRightAlignedDigits(boltGUIAngle, screenWidth - 150, 40);
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
	
	private void drawCheckpoints(Graphics g) {
		if (level.getCheckpoints() != null) {
			for (Checkpoint cp : level.getCheckpoints()) {
				g.pushTransform();
				g.translate(-cam.getX() * zoom + screenWidth / 2, -cam.getY() * zoom + screenHeight * 2 / 3);
				g.scale(zoom, zoom);
				g.drawRect(cp.getX1(), cp.getY1(), cp.getWidth(), cp.getHeight());
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
				tmp.setImageColor(1f - bo.getZ() + multiplierColor.r * bo.getZ(),
						1f - bo.getZ() + multiplierColor.g * bo.getZ(),
						1f - bo.getZ() + multiplierColor.b * bo.getZ());
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

		if (xbox.isButtonBDown() || input.isKeyPressed(Input.KEY_DOWN) || input.isKeyPressed(Input.KEY_S)) {
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
		
		for (Checkpoint cp : level.getCheckpoints()) {
			if (cp.isInArea(player.getBody().getPosition().x, player.getBody().getPosition().y)) {
				player.setCheckpoint(cp);
			}
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
				dropItems.add(new Bolt(this, world, player.getBody().getPosition().add(new Vec2(0,-1)), "images/bolt"+ ((int) (Math.random()*3)+1)+".png" ));
				dropItems.add(new Nut(this, world, player.getBody().getPosition().add(new Vec2(0,-1)), "images/nut"+ ((int) (Math.random()*3)+1)+".png" ));
			}
			

			enemies.add(new SmartPig(this, player.getBody().getPosition().x + 5f, player.getBody().getPosition().y - 5f, 0.5f, 0.5f, 3.3f, 0.3f, 0.3f, null, BodyType.DYNAMIC));
		}

		if (xbox.isButtonRightThumbDown()) {
			DOOMSDAY = !DOOMSDAY;
		}
		if(input.isKeyPressed(Input.KEY_ENTER)){
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

			player.setWaitingForLaserToBeKilled(false);
			
			if(!player.isBiting()) {
				player.bite();
				player.initializeLaser();
			}
		}
		
		if(xbox.isButtonYUp()){
			if(player.isBiting()){
				player.stopBiting();
			}
			if(player.isLaserStarted()){ 
				player.setWaitingForLaserToBeKilled(true);
			}
		}

		
	}

	public void killLaser(){
		if(player.isLaserActive()){

			player.destroyLaser();
			player.setWaitingForLaserToBeKilled(false);
			
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

	public static Generator getGenerator() {
		return generator;
	}

	public static ArrayList<Spike> getSpikes() {
		return spikes;
	}

	public static ArrayList<Conveyor> getConveyor() {
		return conveyor;
	}
	
//	public static ArrayList<Bolt> getBolts() {
//		return bolts;
//	}
	
//	public static ArrayList<Nut> getNuts() {
//		return nuts;
//	}
	
	public static ArrayList<GameObject> getObjectsToAdd() {
		return objectsToAdd;
	}
	
	public static ArrayList<GameObject> getObjectsToRemove() {
		return objectsToRemove;
	}
	
	public static ArrayList<Enemy> getEnemiesToRemove() {
		return enemiesToRemove;
	}
	
//	public static ArrayList<Shred> getShreds() {
//		return shreds;
//	}
//	
//	public static ArrayList<Shred> getShredsToRemove() {
//		return shredsToRemove;
//	}
	
	public static ArrayList<DropItem> getDropItems() {
		return dropItems;
	}
	public static ArrayList<DropItem> getDropItemsToRemove() {
		return dropItemsToRemove;
	}
	
	
	
}
