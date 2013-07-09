package Game;

import java.util.ArrayList;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

public class House {
	
	private static final float	FACTOR		= 15f;
	
	private World				world;
	private Body				body;
	
	private float				x;
	private float				y;
	
	private float				waterAngle	= 0f;
	
	private Image				housebasic;
	private Image				housefront;
	private Image				washer;
	private Image				washy;
	private Image				bloody;
	
	private float				washerX;
	
	private float				washerY;

	private float	bloodyAlpha = -1f;
	
	public House(World world, float x, float y) throws SlickException {
		
		this.world = world;
		this.x = x;
		this.y = y;
		
		this.housebasic = Images.getInstance().getImage("images/housebasic.png");
		this.housefront = Images.getInstance().getImage("images/housefront.png");
		this.washer = Images.getInstance().getImage("images/washer.png");
		this.washy = Images.getInstance().getImage("images/washy.png");
		this.bloody = Images.getInstance().getImage("images/deathwashy.png");
		washy.setCenterOfRotation(washy.getWidth() / 2, washy.getHeight() / 2);
		bloody.setCenterOfRotation(bloody.getWidth() / 2, bloody.getHeight() / 2);
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.STATIC;
		bodyDef.position.set(x, y);
		
		ArrayList<Vec2[]> arrayList = new ArrayList<Vec2[]>();
		arrayList = createHousePolygons();
		
		this.body = this.world.createBody(bodyDef);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.restitution = 0f;
		fixtureDef.filter.categoryBits = 33;
		
		for (Vec2[] verts : arrayList) {
			PolygonShape polygonShape = new PolygonShape();
			polygonShape.set(verts, verts.length);
			fixtureDef.shape = polygonShape;
			
			this.body.createFixture(fixtureDef);
		}
	}
	
	private ArrayList<Vec2[]> createHousePolygons() {
		ArrayList<Vec2[]> fixtures = new ArrayList<Vec2[]>();
		Vec2[] verts;
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.0005291f, FACTOR * -0.0015873313f);
		verts[2] = new Vec2(FACTOR * 0.046560846f, FACTOR * -0.02169311f);
		verts[1] = new Vec2(FACTOR * 0.047089946f, FACTOR * -0.23862433f);
		verts[0] = new Vec2(FACTOR * 0.0f, FACTOR * -0.2814815f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.047089946f, FACTOR * -0.23862433f);
		verts[2] = new Vec2(FACTOR * 0.6259259f, FACTOR * -0.23915344f);
		verts[1] = new Vec2(FACTOR * 0.62539685f, FACTOR * -0.26666665f);
		verts[0] = new Vec2(FACTOR * 0.6195767f, FACTOR * -0.26719576f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.047089946f, FACTOR * -0.23862433f);
		verts[2] = new Vec2(FACTOR * 0.6195767f, FACTOR * -0.26719576f);
		verts[1] = new Vec2(FACTOR * 0.2015873f, FACTOR * -0.2809524f);
		verts[0] = new Vec2(FACTOR * 0.0f, FACTOR * -0.2814815f);
		fixtures.add(verts);
		
		// verts = new Vec2[4];
		// verts[3] = new Vec2(FACTOR * 0.2015873f, FACTOR * -0.2809524f);
		// verts[2] = new Vec2(FACTOR * 0.6195767f, FACTOR * -0.26719576f);
		// verts[1] = new Vec2(FACTOR * 0.6195767f, FACTOR * -0.28042328f);
		// verts[0] = new Vec2(FACTOR * 0.60952383f, FACTOR * -0.28042328f);
		// fixtures.add(verts);
		
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.60952383f, FACTOR * -0.28042328f);
		verts[2] = new Vec2(FACTOR * 0.60952383f, FACTOR * -0.2936508f);
		verts[1] = new Vec2(FACTOR * 0.6010582f, FACTOR * -0.2936508f);
		verts[0] = new Vec2(FACTOR * 0.2015873f, FACTOR * -0.2809524f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.6010582f, FACTOR * -0.2936508f);
		verts[2] = new Vec2(FACTOR * 0.6005291f, FACTOR * -0.305291f);
		verts[1] = new Vec2(FACTOR * 0.5920635f, FACTOR * -0.305291f);
		verts[0] = new Vec2(FACTOR * 0.2015873f, FACTOR * -0.2809524f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.5920635f, FACTOR * -0.305291f);
		verts[2] = new Vec2(FACTOR * 0.5920635f, FACTOR * -0.3174603f);
		verts[1] = new Vec2(FACTOR * 0.584127f, FACTOR * -0.3179894f);
		verts[0] = new Vec2(FACTOR * 0.2015873f, FACTOR * -0.2809524f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.584127f, FACTOR * -0.3179894f);
		verts[2] = new Vec2(FACTOR * 0.5835979f, FACTOR * -0.3301587f);
		verts[1] = new Vec2(FACTOR * 0.57566136f, FACTOR * -0.33068782f);
		verts[0] = new Vec2(FACTOR * 0.2015873f, FACTOR * -0.2809524f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.57566136f, FACTOR * -0.33068782f);
		verts[2] = new Vec2(FACTOR * 0.57566136f, FACTOR * -0.34285712f);
		verts[1] = new Vec2(FACTOR * 0.5677249f, FACTOR * -0.34338623f);
		verts[0] = new Vec2(FACTOR * 0.2015873f, FACTOR * -0.2809524f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.5677249f, FACTOR * -0.34338623f);
		verts[2] = new Vec2(FACTOR * 0.568254f, FACTOR * -0.35555553f);
		verts[1] = new Vec2(FACTOR * 0.5587302f, FACTOR * -0.35608464f);
		verts[0] = new Vec2(FACTOR * 0.2015873f, FACTOR * -0.2809524f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.5587302f, FACTOR * -0.35608464f);
		verts[2] = new Vec2(FACTOR * 0.5582011f, FACTOR * -0.36825395f);
		verts[1] = new Vec2(FACTOR * 0.55026454f, FACTOR * -0.36878306f);
		verts[0] = new Vec2(FACTOR * 0.2015873f, FACTOR * -0.2809524f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.55026454f, FACTOR * -0.36878306f);
		verts[2] = new Vec2(FACTOR * 0.55026454f, FACTOR * -0.38095236f);
		verts[1] = new Vec2(FACTOR * 0.54232806f, FACTOR * -0.38095236f);
		verts[0] = new Vec2(FACTOR * 0.2015873f, FACTOR * -0.2809524f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.54232806f, FACTOR * -0.38095236f);
		verts[2] = new Vec2(FACTOR * 0.54285717f, FACTOR * -0.39365077f);
		verts[1] = new Vec2(FACTOR * 0.53280425f, FACTOR * -0.39365077f);
		verts[0] = new Vec2(FACTOR * 0.2015873f, FACTOR * -0.2809524f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.53280425f, FACTOR * -0.39365077f);
		verts[2] = new Vec2(FACTOR * 0.53280425f, FACTOR * -0.4047619f);
		verts[1] = new Vec2(FACTOR * 0.5243386f, FACTOR * -0.4042328f);
		verts[0] = new Vec2(FACTOR * 0.2015873f, FACTOR * -0.2809524f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.5243386f, FACTOR * -0.4042328f);
		verts[2] = new Vec2(FACTOR * 0.5243386f, FACTOR * -0.41746032f);
		verts[1] = new Vec2(FACTOR * 0.5164021f, FACTOR * -0.41746032f);
		verts[0] = new Vec2(FACTOR * 0.2015873f, FACTOR * -0.2809524f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.5164021f, FACTOR * -0.41746032f);
		verts[2] = new Vec2(FACTOR * 0.51693124f, FACTOR * -0.43015873f);
		verts[1] = new Vec2(FACTOR * 0.50793654f, FACTOR * -0.43015873f);
		verts[0] = new Vec2(FACTOR * 0.2015873f, FACTOR * -0.2809524f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.50793654f, FACTOR * -0.43015873f);
		verts[2] = new Vec2(FACTOR * 0.5084656f, FACTOR * -0.44497353f);
		verts[1] = new Vec2(FACTOR * 0.5f, FACTOR * -0.44497353f);
		verts[0] = new Vec2(FACTOR * 0.2015873f, FACTOR * -0.2809524f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.5f, FACTOR * -0.44497353f);
		verts[2] = new Vec2(FACTOR * 0.5f, FACTOR * -0.45714283f);
		verts[1] = new Vec2(FACTOR * 0.4910053f, FACTOR * -0.45714283f);
		verts[0] = new Vec2(FACTOR * 0.2015873f, FACTOR * -0.2809524f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.4910053f, FACTOR * -0.45714283f);
		verts[2] = new Vec2(FACTOR * 0.4904762f, FACTOR * -0.46878308f);
		verts[1] = new Vec2(FACTOR * 0.48359787f, FACTOR * -0.46825397f);
		verts[0] = new Vec2(FACTOR * 0.2015873f, FACTOR * -0.2809524f);
		fixtures.add(verts);
		verts = new Vec2[3];
		verts[2] = new Vec2(FACTOR * 0.48359787f, FACTOR * -0.46825397f);
		verts[1] = new Vec2(FACTOR * 0.48412699f, FACTOR * -0.47989416f);
		verts[0] = new Vec2(FACTOR * 0.2015873f, FACTOR * -0.2809524f);
		fixtures.add(verts);
		verts = new Vec2[3];
		verts[2] = new Vec2(FACTOR * 0.48412699f, FACTOR * -0.47989416f);
		verts[1] = new Vec2(FACTOR * 0.22698413f, FACTOR * -0.48042327f);
		verts[0] = new Vec2(FACTOR * 0.2015873f, FACTOR * -0.2809524f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.2015873f, FACTOR * -0.2809524f);
		verts[2] = new Vec2(FACTOR * 0.22698413f, FACTOR * -0.48042327f);
		verts[1] = new Vec2(FACTOR * 0.22751322f, FACTOR * -0.6698413f);
		verts[0] = new Vec2(FACTOR * 0.2015873f, FACTOR * -0.6994709f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.22698413f, FACTOR * -0.5793651f);
		verts[2] = new Vec2(FACTOR * 0.36137566f, FACTOR * -0.5798942f);
		verts[1] = new Vec2(FACTOR * 0.36084655f, FACTOR * -0.5925926f);
		verts[0] = new Vec2(FACTOR * 0.22592592f, FACTOR * -0.5925926f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.22751322f, FACTOR * -0.6698413f);
		verts[2] = new Vec2(FACTOR * 0.46719578f, FACTOR * -0.67037034f);
		verts[1] = new Vec2(FACTOR * 0.48412699f, FACTOR * -0.7005291f);
		verts[0] = new Vec2(FACTOR * 0.2015873f, FACTOR * -0.6994709f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.46772486f, FACTOR * -0.55132276f);
		verts[2] = new Vec2(FACTOR * 0.4846561f, FACTOR * -0.55079365f);
		verts[1] = new Vec2(FACTOR * 0.48412699f, FACTOR * -0.7005291f);
		verts[0] = new Vec2(FACTOR * 0.46719578f, FACTOR * -0.67037034f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.19259259f, FACTOR * -0.6994709f);
		verts[2] = new Vec2(FACTOR * 0.21481481f, FACTOR * -0.6989418f);
		verts[1] = new Vec2(FACTOR * 0.47566137f, FACTOR * -0.92116404f);
		verts[0] = new Vec2(FACTOR * 0.46931216f, FACTOR * -0.9417989f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.47566137f, FACTOR * -0.92116404f);
		verts[2] = new Vec2(FACTOR * 1.0978836f, FACTOR * -0.9206349f);
		verts[1] = new Vec2(FACTOR * 1.105291f, FACTOR * -0.9412698f);
		verts[0] = new Vec2(FACTOR * 0.46931216f, FACTOR * -0.9417989f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 1.0978836f, FACTOR * -0.9206349f);
		verts[2] = new Vec2(FACTOR * 1.3592592f, FACTOR * -0.69841266f);
		verts[1] = new Vec2(FACTOR * 1.3825396f, FACTOR * -0.69841266f);
		verts[0] = new Vec2(FACTOR * 1.105291f, FACTOR * -0.9412698f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.0005291f, FACTOR * -0.0015873313f);
		verts[2] = new Vec2(FACTOR * 0.6925926f, FACTOR * -0.0010582209f);
		verts[1] = new Vec2(FACTOR * 0.6920635f, FACTOR * -0.02222222f);
		verts[0] = new Vec2(FACTOR * 0.046560846f, FACTOR * -0.02169311f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.6920635f, FACTOR * -0.02222222f);
		verts[2] = new Vec2(FACTOR * 0.56560844f, FACTOR * -0.037037015f);
		verts[1] = new Vec2(FACTOR * 0.55714285f, FACTOR * -0.037037015f);
		verts[0] = new Vec2(FACTOR * 0.55767196f, FACTOR * -0.02222222f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.6920635f, FACTOR * -0.02222222f);
		verts[2] = new Vec2(FACTOR * 0.5740741f, FACTOR * -0.050264537f);
		verts[1] = new Vec2(FACTOR * 0.56560844f, FACTOR * -0.049735427f);
		verts[0] = new Vec2(FACTOR * 0.56560844f, FACTOR * -0.037037015f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.6920635f, FACTOR * -0.02222222f);
		verts[2] = new Vec2(FACTOR * 0.58201057f, FACTOR * -0.06296295f);
		verts[1] = new Vec2(FACTOR * 0.573545f, FACTOR * -0.06243384f);
		verts[0] = new Vec2(FACTOR * 0.5740741f, FACTOR * -0.050264537f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.6920635f, FACTOR * -0.02222222f);
		verts[2] = new Vec2(FACTOR * 0.5899471f, FACTOR * -0.07513225f);
		verts[1] = new Vec2(FACTOR * 0.58201057f, FACTOR * -0.07513225f);
		verts[0] = new Vec2(FACTOR * 0.58201057f, FACTOR * -0.06296295f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.6920635f, FACTOR * -0.02222222f);
		verts[2] = new Vec2(FACTOR * 0.5984127f, FACTOR * -0.08835977f);
		verts[1] = new Vec2(FACTOR * 0.5899471f, FACTOR * -0.08783066f);
		verts[0] = new Vec2(FACTOR * 0.5899471f, FACTOR * -0.07513225f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.6920635f, FACTOR * -0.02222222f);
		verts[2] = new Vec2(FACTOR * 0.6068783f, FACTOR * -0.101587296f);
		verts[1] = new Vec2(FACTOR * 0.5978836f, FACTOR * -0.101587296f);
		verts[0] = new Vec2(FACTOR * 0.5984127f, FACTOR * -0.08835977f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.6920635f, FACTOR * -0.02222222f);
		verts[2] = new Vec2(FACTOR * 0.6153439f, FACTOR * -0.11534393f);
		verts[1] = new Vec2(FACTOR * 0.6068783f, FACTOR * -0.11481482f);
		verts[0] = new Vec2(FACTOR * 0.6068783f, FACTOR * -0.101587296f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.6920635f, FACTOR * -0.02222222f);
		verts[2] = new Vec2(FACTOR * 0.6238095f, FACTOR * -0.12804234f);
		verts[1] = new Vec2(FACTOR * 0.6153439f, FACTOR * -0.12804234f);
		verts[0] = new Vec2(FACTOR * 0.6153439f, FACTOR * -0.11534393f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.6920635f, FACTOR * -0.02222222f);
		verts[2] = new Vec2(FACTOR * 0.6322751f, FACTOR * -0.14074075f);
		verts[1] = new Vec2(FACTOR * 0.6238095f, FACTOR * -0.14074075f);
		verts[0] = new Vec2(FACTOR * 0.6238095f, FACTOR * -0.12804234f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.6920635f, FACTOR * -0.02222222f);
		verts[2] = new Vec2(FACTOR * 0.64074075f, FACTOR * -0.15449733f);
		verts[1] = new Vec2(FACTOR * 0.6328042f, FACTOR * -0.15449733f);
		verts[0] = new Vec2(FACTOR * 0.6322751f, FACTOR * -0.14074075f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.6920635f, FACTOR * -0.02222222f);
		verts[2] = new Vec2(FACTOR * 0.64867723f, FACTOR * -0.16666669f);
		verts[1] = new Vec2(FACTOR * 0.64126986f, FACTOR * -0.16613758f);
		verts[0] = new Vec2(FACTOR * 0.64074075f, FACTOR * -0.15449733f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.6920635f, FACTOR * -0.02222222f);
		verts[2] = new Vec2(FACTOR * 0.6571429f, FACTOR * -0.17883599f);
		verts[1] = new Vec2(FACTOR * 0.64867723f, FACTOR * -0.17830688f);
		verts[0] = new Vec2(FACTOR * 0.64867723f, FACTOR * -0.16666669f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.6920635f, FACTOR * -0.02222222f);
		verts[2] = new Vec2(FACTOR * 0.66507936f, FACTOR * -0.1915344f);
		verts[1] = new Vec2(FACTOR * 0.6571429f, FACTOR * -0.1915344f);
		verts[0] = new Vec2(FACTOR * 0.6571429f, FACTOR * -0.17883599f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.6920635f, FACTOR * -0.02222222f);
		verts[2] = new Vec2(FACTOR * 0.6730159f, FACTOR * -0.20582008f);
		verts[1] = new Vec2(FACTOR * 0.66507936f, FACTOR * -0.20529103f);
		verts[0] = new Vec2(FACTOR * 0.66507936f, FACTOR * -0.1915344f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.6920635f, FACTOR * -0.02222222f);
		verts[2] = new Vec2(FACTOR * 0.68095237f, FACTOR * -0.2185185f);
		verts[1] = new Vec2(FACTOR * 0.6724868f, FACTOR * -0.2185185f);
		verts[0] = new Vec2(FACTOR * 0.6730159f, FACTOR * -0.20582008f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.6920635f, FACTOR * -0.02222222f);
		verts[2] = new Vec2(FACTOR * 0.6888889f, FACTOR * -0.23121691f);
		verts[1] = new Vec2(FACTOR * 0.68095237f, FACTOR * -0.23068786f);
		verts[0] = new Vec2(FACTOR * 0.68095237f, FACTOR * -0.2185185f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.6920635f, FACTOR * -0.02222222f);
		verts[2] = new Vec2(FACTOR * 0.6925926f, FACTOR * -0.24391532f);
		verts[1] = new Vec2(FACTOR * 0.689418f, FACTOR * -0.24391532f);
		verts[0] = new Vec2(FACTOR * 0.6888889f, FACTOR * -0.23121691f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.6925926f, FACTOR * -0.0010582209f);
		verts[2] = new Vec2(FACTOR * 1.3666667f, FACTOR * -0.0010582209f);
		verts[1] = new Vec2(FACTOR * 1.3666667f, FACTOR * -0.25767195f);
		verts[0] = new Vec2(FACTOR * 0.6920635f, FACTOR * -0.25714284f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.6820106f, FACTOR * -0.33756614f);
		verts[2] = new Vec2(FACTOR * 0.7063492f, FACTOR * -0.33703703f);
		verts[1] = new Vec2(FACTOR * 0.68095237f, FACTOR * -0.47989416f);
		verts[0] = new Vec2(FACTOR * 0.5825397f, FACTOR * -0.47989416f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.7063492f, FACTOR * -0.33703703f);
		verts[2] = new Vec2(FACTOR * 0.7063492f, FACTOR * -0.6719577f);
		verts[1] = new Vec2(FACTOR * 0.6814815f, FACTOR * -0.69841266f);
		verts[0] = new Vec2(FACTOR * 0.68095237f, FACTOR * -0.47989416f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.7063492f, FACTOR * -0.6719577f);
		verts[2] = new Vec2(FACTOR * 1.3391534f, FACTOR * -0.6724868f);
		verts[1] = new Vec2(FACTOR * 1.3693122f, FACTOR * -0.6989418f);
		verts[0] = new Vec2(FACTOR * 0.6814815f, FACTOR * -0.69841266f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.6814815f, FACTOR * -0.69841266f);
		verts[2] = new Vec2(FACTOR * 0.6179894f, FACTOR * -0.6989418f);
		verts[1] = new Vec2(FACTOR * 0.61851853f, FACTOR * -0.69206345f);
		verts[0] = new Vec2(FACTOR * 0.68095237f, FACTOR * -0.69206345f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 1.3402116f, FACTOR * -0.34232807f);
		verts[2] = new Vec2(FACTOR * 1.368254f, FACTOR * -0.34232807f);
		verts[1] = new Vec2(FACTOR * 1.3693122f, FACTOR * -0.6989418f);
		verts[0] = new Vec2(FACTOR * 1.3391534f, FACTOR * -0.6724868f);
		fixtures.add(verts);
		verts = new Vec2[3];
		verts[2] = new Vec2(FACTOR * 1.368254f, FACTOR * -0.36243385f);
		verts[1] = new Vec2(FACTOR * 1.3936508f, FACTOR * -0.38095236f);
		verts[0] = new Vec2(FACTOR * 1.3677249f, FACTOR * -0.4380952f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 1.3936508f, FACTOR * -0.38095236f);
		verts[2] = new Vec2(FACTOR * 1.4148148f, FACTOR * -0.35502648f);
		verts[1] = new Vec2(FACTOR * 1.4386244f, FACTOR * -0.35502648f);
		verts[0] = new Vec2(FACTOR * 1.3677249f, FACTOR * -0.4380952f);
		fixtures.add(verts);
		verts = new Vec2[4];
		verts[3] = new Vec2(FACTOR * 0.47037038f, FACTOR * -0.08518517f);
		verts[2] = new Vec2(FACTOR * 0.4830688f, FACTOR * -0.08465606f);
		verts[1] = new Vec2(FACTOR * 0.4830688f, FACTOR * -0.24021167f);
		verts[0] = new Vec2(FACTOR * 0.46984127f, FACTOR * -0.23968256f);
		fixtures.add(verts);
		
		return fixtures;
	}
	
	public void draw(Graphics g, boolean debugView) {
		if (debugView) {
			this.drawOutline(g);
		} else {
			this.drawImage(g);
		}
	}
	
	public void drawFront(Graphics g, boolean debugView) {
		if (!debugView) {
			g.pushTransform();
			g.translate(this.x, this.y - FACTOR);
			g.scale(FACTOR, FACTOR);
			housefront.draw(0, 0, 1 * 1.5873016f, 1);
			g.popTransform();
		}
	}
	
	public void drawImage(Graphics g) {
		g.pushTransform();
		g.translate(this.x, this.y - FACTOR);
		g.scale(FACTOR, FACTOR);
		housebasic.draw(0, 0, 1 * 1.5873016f, 1);
		g.pushTransform();
		g.translate(0.74867725f + washerX, 0.4099f + washerY);
		washy.setRotation(waterAngle);
		washy.draw(0.15f, 0.12f, 0.11f, 0.11f);
		bloody.setRotation(waterAngle);
		if (bloodyAlpha >= 0f) {
			bloodyAlpha += 0.01f;
			bloody.setAlpha(bloodyAlpha );
			bloody.draw(0.15f, 0.12f, 0.11f, 0.11f);
		}
		washer.draw(0, 0, 0.35f, 0.33346036f);
		g.popTransform();
		g.popTransform();
	}
	
	public void startBloodBath() {
		bloodyAlpha = 0f;
	}
	
	public void drawOutline(Graphics g) {
		Fixture fixtureList = this.body.getFixtureList();
		while (fixtureList != null) {
			Polygon polygon = new Polygon();
			PolygonShape polygonShape = (PolygonShape) fixtureList.getShape();
			
			Vec2[] verts = polygonShape.getVertices();
			for (int i = 0; i < polygonShape.getVertexCount(); ++i) {
				Vec2 worldPoint = body.getWorldPoint(verts[i]);
				polygon.addPoint(worldPoint.x, worldPoint.y);
			}
			
			g.pushTransform();
			g.draw(polygon);
			g.popTransform();
			fixtureList = fixtureList.getNext();
		}
	}
	
	public void updateAnimations() {
		waterAngle += 7.5f;
		washerX = (float) Math.random() * 0.001f - 0.0005f;
		washerY = (float) Math.random() * 0.001f - 0.0005f;
	}
	
}
