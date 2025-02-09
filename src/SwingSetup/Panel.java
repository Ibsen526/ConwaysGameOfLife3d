package SwingSetup;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;

import Util.Vec3;
import Main3D.Mesh;
import Main3D.Triangle;
import Main3D.Camera;
import Main3D.Light;
import Field.Field;

public class Panel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private int P_W, P_H;
	
	private long elapsedTime;
	private float rotX, rotY, rotZ;
	
	private Timer gameLoop;
	
	private Camera cam;
	private Mesh cube;
	
	private Field field;
	
	private Light light;
	
	Panel (int PW, int PH) {
		this.setPreferredSize(new Dimension(PW, PH));
		P_W = PW;
		P_H = PH;
		this.setDoubleBuffered(true);

		cam = new Camera(3.0f, 3.0f, 3.0f, this, P_W, P_H);
		cube = new Mesh(".\\cube3.obj");
		light = new Light(new Vec3(-1.0f, 1.0f, 1.0f), new Vec3(0.0f, 0.0f, -1.0f), 1.0f);

		
		gameLoop = new Timer();
		gameLoop.schedule(new TimerTask() {
			@Override
            public void run() {
                elapsedTime++;
                rotX = (float)Math.cos(elapsedTime / 10f) * 2.0f;
                rotY = (float)Math.sin(elapsedTime / 10f) * 2.0f;
                rotZ = (float)Math.cos(elapsedTime / 10f) * 2.0f;
                repaint();
            }
		}, 1, 10);	
		
		field = new Field(this);
		
	}
	
	public void paintComponent(Graphics graphics) {
		graphics.setColor(new Color(0, 0, 0));
		graphics.fillRect(0, 0, P_W, P_H);
				
		cam.ViewMatrix();		
				
		ArrayList<Triangle> trianglesToRaster = new ArrayList<Triangle>();
		
		trianglesToRaster = field.CalcFieldMeshes(graphics, cube, cam, light, P_W, P_H, new Vec3(rotX, rotY, rotZ));

		//Now sort the triangles (only a hack, because depth buffer would be better)
		ArrayList<Triangle> sortedTriangles = SortTriangles(trianglesToRaster);
		
		//Draw them to the screen 
		DrawTriangle(graphics, sortedTriangles);
	}	

	public static void DrawTriangle(Graphics graphics, ArrayList<Triangle> triangles) {
		for (Triangle triangle : triangles) {
			Polygon p = new Polygon();
			p.addPoint((int) triangle.p1.x, (int) triangle.p1.y);
			p.addPoint((int) triangle.p2.x, (int) triangle.p2.y);
			p.addPoint((int) triangle.p3.x, (int) triangle.p3.y);
			
			graphics.setColor(triangle.col);
			graphics.fillPolygon(p);
		}
	}
	
	private ArrayList<Triangle> SortTriangles(ArrayList<Triangle> t) {
		
		for (int i = 0; i < t.size(); i++) { 		
			float triAvg = (t.get(i).p1.z + t.get(i).p2.z + t.get(i).p3.z) / 3.0f;
			
			for (int j = 0; j < t.size(); j++) {
				float triSwapAvg = (t.get(j).p1.z + t.get(j).p2.z + t.get(j).p3.z) / 3.0f;
				
				if(triAvg > triSwapAvg) {
					
					//Swap the triangles
					Triangle temp = t.get(i);
					t.set(i, t.get(j));
					t.set(j, temp);
					triAvg = triSwapAvg;
				}
			}
		}
		
		return t;		
	}
	
	public void ResetField(int presetIndex) {
		field.InitFieldValues(presetIndex);
	}
}
