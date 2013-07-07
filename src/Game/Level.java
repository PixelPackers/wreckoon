package Game;

import java.util.ArrayList;
import java.util.Collections;

public class Level {
	
	@SuppressWarnings("unused")
	private float						version	= 1.0f;
	private Block[][]					blocks;
	private ArrayList<BackgroundObject>	backgroundObjects;
	private ArrayList<ZoomArea>			zoomAreas;
	private ArrayList<Checkpoint>		checkpoints;
	private ArrayList<Part>				parts;
	private ArrayList<SpawnPoint>		enemies;
	private ArrayList<Girder>			girders;
	private ArrayList<Generator>		generators;
	private ArrayList<Conveyor>			conveyors;
	
	// private ArrayList<Tire> tires;
	
	public Level(int width, int height) {
		this.blocks = new Block[width][height];
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				this.blocks[x][y] = new Block();
			}
		}
	}
	
	public void addBackgroundObject(BackgroundObject bo) {
		if (backgroundObjects == null) {
			backgroundObjects = new ArrayList<BackgroundObject>();
		}
		backgroundObjects.add(bo);
		Collections.sort(backgroundObjects);
	}
	
	public void addCheckpoint(Checkpoint cp) {
		if (checkpoints == null) {
			checkpoints = new ArrayList<Checkpoint>();
		}
		checkpoints.add(cp);
	}
	
	public void addConveyor(Conveyor c) {
		if (conveyors == null) {
			conveyors = new ArrayList<Conveyor>();
		}
		conveyors.add(c);
	}
	
	public void addEnemy(SpawnPoint e) {
		if (enemies == null) {
			enemies = new ArrayList<SpawnPoint>();
		}
		enemies.add(e);
	}
	
	public void addGenerator(Generator g) {
		if (generators == null) {
			generators = new ArrayList<Generator>();
		}
		generators.add(g);
	}
	
	public void addGirder(Girder girder) {
		if (girders == null) {
			girders = new ArrayList<Girder>();
		}
		girders.add(girder);
	}
	
	public void addPart(Part p) {
		if (parts == null) {
			parts = new ArrayList<Part>();
		}
		parts.add(p);
	}
	
	// public void addTire(Tire t) {
	// if (tires == null) {
	// tires = new ArrayList<Tire>();
	// }
	// tires.add(t);
	// }
	
	public void addZoomArea(ZoomArea za) {
		if (zoomAreas == null) {
			zoomAreas = new ArrayList<ZoomArea>();
		}
		zoomAreas.add(za);
	}
	
	public ArrayList<BackgroundObject> getBackgroundObjects() {
		return backgroundObjects;
	}
	
	public Block getBlock(int x, int y) {
		return this.blocks[x][y];
	}
	
	public ArrayList<Checkpoint> getCheckpoints() {
		return checkpoints;
	}
	
	public ArrayList<Conveyor> getConveyors() {
		return conveyors;
	}
	
	public ArrayList<SpawnPoint> getEnemies() {
		return enemies;
	}
	
	public ArrayList<Generator> getGenerators() {
		return generators;
	}
	
	public ArrayList<Girder> getGirders() {
		return girders;
	}
	
	public int getHeight() {
		return this.blocks[0].length;
	}
	
	public ArrayList<Part> getParts() {
		return parts;
	}
	
	// public ArrayList<Tire> getTires() {
	// return tires;
	// }
	
	public int getWidth() {
		return this.blocks.length;
	}
	
	public ArrayList<ZoomArea> getZoomAreas() {
		return zoomAreas;
	}
	
	public void resize(int dX, int dY) {
		if (this.getWidth() + dX > 0 && this.getHeight() + dY > 0) {
			Block[][] tmp = new Block[this.getWidth() + dX][this.getHeight() + dY];
			for (int x = 0; x < tmp.length; ++x) {
				for (int y = 0; y < tmp[0].length; ++y) {
					if (x < this.getWidth() && y < this.getHeight()) {
						tmp[x][y] = new Block(this.blocks[x][y].getType(), this.blocks[x][y].getAngle(), this.blocks[x][y].isFlipped());
					} else {
						tmp[x][y] = new Block(0, 0, false);
					}
					
				}
			}
			this.blocks = tmp;
		}
	}
	
	public void setBlock(int x, int y, int brush, int angle, boolean flipped) {
		this.blocks[x][y].set(brush, angle, flipped);
	}
	
	public void shift(int dX, int dY) {
		Block[][] tmp = new Block[this.getWidth()][this.getHeight()];
		for (int x = 0; x < this.getWidth(); ++x) {
			for (int y = 0; y < this.getHeight(); ++y) {
				int refX = (this.getWidth() + x - dX) % this.getWidth();
				int refY = (this.getHeight() + y - dY) % this.getHeight();
				tmp[x][y] = new Block(this.blocks[refX][refY].getType(), this.blocks[refX][refY].getAngle(),
						this.blocks[refX][refY].isFlipped());
			}
		}
		this.blocks = tmp;
	}
	
}
