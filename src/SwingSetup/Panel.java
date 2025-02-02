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
	
	Light light;
	
	Panel (int PW, int PH) {
		//Panel = new JPanel(new GridLayout(1,7));
		this.setPreferredSize(new Dimension(PW, PH));
		P_W = PW; P_H = PH;
		this.setDoubleBuffered(true);
		//this.setBounds(0, 0, PW, PH);

		cam = new Camera(3.0f, 3.0f, 3.0f, this, P_W, P_H);
		cube = new Mesh("C:\\Users\\Marti\\Desktop\\Ranzig\\Programme_nichtSchule\\Java_Projekte\\3D_Graphics_API\\assets\\cube3.obj");
		light = new Light(new Vec3(-1.0f, 1.0f, 1.0f), new Vec3(0.0f, 0.0f, -1.0f), 1.0f);

		
		gameLoop = new Timer();
		gameLoop.schedule(new TimerTask() {
			@Override
            public void run() {
                elapsedTime++;
                rotX = (float) Math.cos(elapsedTime) * 2.0f;
                rotY = (float) Math.sin(elapsedTime) * 2.0f;
                rotZ = (float) Math.cos(elapsedTime) * 2.0f;
                repaint();
            }
		}, 1, 10);	
		
		field = new Field(this);
		
	}
	
	public void paintComponent(Graphics g) {
		//Graphics2D g2D = (Graphics2D) g;
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, P_W, P_H);
				
		cam.ViewMatrix();		
				
		ArrayList<Triangle> trianglesToRaster = new ArrayList<Triangle>();
		
		trianglesToRaster = field.CalcFieldMeshes(g, cube, cam, light, P_W, P_H, new Vec3(rotX, rotY, rotZ));

		//Now sort the triangles (only a hack, because depth buffer would be better)
		ArrayList<Triangle> sortedTriangles = SortTriangles(trianglesToRaster);
		
		//Draw them to the screen 
		DrawTriangle(g, sortedTriangles);
	}	

	public static void DrawTriangle(Graphics g, ArrayList<Triangle> t) {
		for (Triangle tri : t) {
			Polygon p = new Polygon();
			p.addPoint((int) tri.p1.x, (int) tri.p1.y);
			p.addPoint((int) tri.p2.x, (int) tri.p2.y);
			p.addPoint((int) tri.p3.x, (int) tri.p3.y);
			
			g.setColor(tri.col);
			g.fillPolygon(p);
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
	
	public void ResetField() {
		field.InitFieldValues();
	}
}
