package Game;

import java.util.ArrayList;
import java.util.Collections;

public class Level {

	@SuppressWarnings("unused")
	private float version = 1.0f;
	private Block[][] blocks;
	private ArrayList<BackgroundObject> backgroundObjects;
	private ArrayList<ZoomArea> zoomAreas;
	
	public Level(int width, int height) {
		this.blocks = new Block[width][height];
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				this.blocks[x][y] = new Block();
			}
		}
	}
	
	public ArrayList<ZoomArea> getZoomAreas() {
		return zoomAreas;
	}
	
	public void addZoomArea(ZoomArea za) {
		if (zoomAreas == null) {
			zoomAreas = new ArrayList<ZoomArea>();
		}
		zoomAreas.add(za);
	}
	
	public void removeZoomArea(ZoomArea za) {
		zoomAreas.remove(za);
	}
	
	public boolean containsZoomArea(ZoomArea za) {
		return zoomAreas.contains(za);
	}
	
	public ArrayList<BackgroundObject> getBackgroundObjects() {
		return backgroundObjects;
	}
	
	public void addBackgroundObject(BackgroundObject bo) {
		if (backgroundObjects == null) {
			backgroundObjects = new ArrayList<BackgroundObject>();
		}
		backgroundObjects.add(bo);
		Collections.sort(backgroundObjects);
	}
	
	public void removeBackgroundObject(BackgroundObject bo) {
		backgroundObjects.remove(bo);
	}
	
	public boolean containsBackgroundObject(BackgroundObject bo) {
		return backgroundObjects.contains(bo);
	}
	
	public void setBlock(int x, int y, int brush, int angle, boolean flipped) {
		this.blocks[x][y].set(brush, angle, flipped);
	}
	
	public Block getBlock(int x, int y) {
		return this.blocks[x][y];
	}
	
	public int getWidth() {
		return this.blocks.length;
	}
	
	public int getHeight() {
		return this.blocks[0].length;
	}

	public void shift(int dX, int dY) {
		Block[][] tmp = new Block[this.getWidth()][this.getHeight()];
		for (int x = 0; x < this.getWidth(); ++x) {
			for (int y = 0; y < this.getHeight(); ++y) {
				int refX = (this.getWidth() + x - dX) % this.getWidth();
				int refY = (this.getHeight() + y - dY) % this.getHeight();
				tmp[x][y] = new Block(this.blocks[refX][refY].getType(),
						this.blocks[refX][refY].getAngle(),
						this.blocks[refX][refY].isFlipped());
			}
		}
		this.blocks = tmp;
	}
	
	public void resize(int dX, int dY) {
		if (this.getWidth() + dX > 0 && this.getHeight() + dY > 0) {
			Block[][] tmp = new Block[this.getWidth() + dX][this.getHeight() + dY];
			for (int x = 0; x < tmp.length; ++x) {
				for (int y = 0; y < tmp[0].length; ++y) {
					if (x < this.getWidth() && y < this.getHeight()) {
						tmp[x][y] = new Block(this.blocks[x][y].getType(),
								this.blocks[x][y].getAngle(),
								this.blocks[x][y].isFlipped());
					} else {
						tmp[x][y] = new Block(0, 0, false);
					}
					
				}
			}
			this.blocks = tmp;
		}
	}
	
}
