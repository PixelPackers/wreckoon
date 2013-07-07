package Game;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.Fixture;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;

public class MySensor {
	
	private int				collidingCounter	= 0;
	private Fixture			fixture;
	private PolygonShape	polygonShape;
	private Color			color				= Color.green;
	
	public MySensor(Fixture fixture, PolygonShape polygonShape) {
		this.fixture = fixture;
		Filter filter = new Filter();
		filter.categoryBits = 4;
		filter.maskBits = 1 + 2 + 4;
		this.fixture.setFilterData(filter);
		this.polygonShape = polygonShape;
	}
	
	public void decreaseCollidingCounter() {
		--this.collidingCounter;
	}
	
	// TODO da wird player body uebergeben, nur um getWorldPoint() zu
	// verwenden... anders loesbar?
	public void draw(Graphics g, Body body) {
		Polygon polygonToDraw = new Polygon();
		Vec2[] sensorVerts = polygonShape.getVertices();
		
		for (int i = 0; i < polygonShape.m_vertexCount; ++i) {
			
			Vec2 sensorVert = sensorVerts[i];
			Vec2 sensorWorldPoint = body.getWorldPoint(sensorVert);
			polygonToDraw.addPoint(sensorWorldPoint.x, sensorWorldPoint.y);
			
		}
		Color prevColor = g.getColor();
		if (this.isColliding()) {
			g.setColor(Color.red);
		} else {
			g.setColor(Color.green);
		}
		g.draw(polygonToDraw);
		g.setColor(prevColor);
		
	}
	
	public Color getColor() {
		return color;
	}
	
	public Fixture getFixture() {
		return fixture;
	}
	
	public void increaseCollidingCounter() {
		++this.collidingCounter;
	}
	
	public boolean isColliding() {
		return this.collidingCounter > 0;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
}
