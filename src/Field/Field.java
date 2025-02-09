package Field;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import Main3D.Camera;
import Main3D.Light;
import Main3D.Mesh;
import Main3D.Transformation;
import Main3D.Triangle;
import Util.Mat4;
import Util.Vec3;

public class Field {
	private final int FIELD_WIDTH = 10;
	private final int FIELD_HEIGHT = 10;
	private final int FIELD_LENGTH = 10;
	
	private boolean[][][] arrField1 = new boolean[FIELD_WIDTH][FIELD_HEIGHT][FIELD_LENGTH];
	private boolean[][][] arrField2 = new boolean[FIELD_WIDTH][FIELD_HEIGHT][FIELD_LENGTH];
	private int iNeighbourCount = 0;
	
	private Timer timer;
	
	public Field(SwingSetup.Panel panel) {
		InitFieldValues(0);
		
		timer = new Timer();
		timer.schedule(new TimerTask() { public void run() { CalcFieldPerTick(); }}, 
				2000, 2000);
	}
	
	public void InitFieldValues(int presetIndex) {
		if (presetIndex == 0)
			FigureRandom();
		else if (presetIndex == 1)
			FigureOscillator();
		else if (presetIndex == 2)
			FigureGlider();
		else if (presetIndex == 3)
			FigureStable();
		else if (presetIndex == 4)
			FigureBlinker(); //too close to the edge!!
		else
			FigureRandom();
	}
	
	private Color NormalToRGB(float nr, float ng, float nb) {
		return new Color((int) (nr * 255.0f), (int) (ng * 255.0f), (int) (nb * 255.0f));
		
	}
	
	public ArrayList<Triangle> CalcFieldMeshes(Graphics g, Mesh cube, Camera cam, Light light, int SW, int SH, Vec3 rot) {
		
		ArrayList<Triangle> t = new ArrayList<Triangle>();
		
		for (int i = 1; i < FIELD_WIDTH-1; i++) {
			for (int j = 1; j < FIELD_HEIGHT-1; j++) {
				for (int l = 1; l < FIELD_LENGTH-1; l++) {
					if (arrField1[i][j][l]) {
						Mat4 modelMat = new Mat4();
						modelMat = Transformation.Scale(modelMat, new Vec3(0.1f, 0.1f, 0.1f));
						modelMat = Transformation.RotateX(modelMat, rot.x);
						modelMat = Transformation.RotateY(modelMat, rot.y);
						modelMat = Transformation.RotateZ(modelMat, rot.z);
						float d = 3.5f;
						modelMat = Transformation.Translate(modelMat, new Vec3((float) i / d, (float) j / d, (float) l / d));
						Color c = NormalToRGB((float) i / (float) FIELD_WIDTH, (float) j / (float) FIELD_HEIGHT, (float) l / (float) FIELD_LENGTH);
						t.addAll(cube.DrawMesh(modelMat, SW, SH, cam, true, light, c));
					}
				}
			}
		}
		
		return t;
	}
	
	public void CalcFieldPerTick() {

		for (int i = 1; i < FIELD_WIDTH-1; i++) {
			for (int j = 1; j < FIELD_HEIGHT-1; j++) {
				for (int l = 1; l < FIELD_LENGTH-1; l++) {
	
					iNeighbourCount = 0;

					int x = -1;
					int y = -1;
					int z = -1;

					//Count the adjacent states
					for (int k = 0; k < 27; k++) {

						
						if (x==0 && y==0 && z==0) { }
						else if (arrField1[i+x][j+y][l+z] == true) //sth wrong
							iNeighbourCount++;

						if(x >= 1) {
							if(y < 1) 
								y++;
							else {
								if(z < 1)
									z++;
								else 
									z = -1;
								
								y = -1;
							}
							x = -1;
						}
						else
							x++;
					}
						
					//Change the field values
					if (arrField1[i][j][l] == false) { // First condition
						if (iNeighbourCount == 5) {
							arrField2[i][j][l] = true;
						}
					} 
					else if (arrField1[i][j][l] == true) {
						if (iNeighbourCount < 4 || iNeighbourCount > 5) { // Second condition
							arrField2[i][j][l] = false;
						} 
						else {
							arrField2[i][j][l] = true;
						}
					}
				}
			}
		}

		// Exchange the array values and clear arrField2
		for (int i = 0; i < FIELD_WIDTH; i++) {
			for (int j = 0; j < FIELD_HEIGHT; j++) {
				for (int l = 0; l < FIELD_LENGTH; l++) {
					arrField1[i][j][l] = arrField2[i][j][l];
					arrField2[i][j][l] = false;
				}
			}
		}		
	}

	private void ClearField() {
		for (int i = 0; i < FIELD_WIDTH; i++) {
			for (int j = 0; j < FIELD_HEIGHT; j++) {
				for (int l = 0; l < FIELD_LENGTH; l++) {
					arrField1[i][j][l] = false;
				}
			}
		}
		
	}
	
	private void FigureRandom() {
		Random r = new Random();
		for (int i = 1; i < FIELD_WIDTH-1; i++) {
			for (int j = 1; j < FIELD_HEIGHT-1; j++) {
				for (int l = 1; l < FIELD_LENGTH-1; l++) {
					arrField1[i][j][l] = r.nextBoolean();
				}
			}
		}
	}
	
	private void FigureOscillator() {
		ClearField();
		arrField1[5][5][5] = true;
		arrField1[6][5][6] = true;
		arrField1[7][5][6] = true;
		arrField1[6][5][4] = true;
		arrField1[7][5][4] = true;
		arrField1[8][5][5] = true;
		arrField1[6][6][5] = true;
		arrField1[7][6][5] = true;
	}
	
	private void FigureGlider() {
		ClearField();
		arrField1[4][6][5] = true;
		arrField1[4][6][4] = true;
		arrField1[5][6][6] = true;
		arrField1[5][5][5] = true;
		arrField1[5][5][4] = true;
		arrField1[5][6][3] = true;
		arrField1[6][6][6] = true;
		arrField1[6][5][5] = true;
		arrField1[6][5][4] = true;
		arrField1[6][6][3] = true;
	}

	private void FigureStable() {
		ClearField();
		arrField1[6][3][2] = true;
		arrField1[6][4][2] = true;
		arrField1[6][4][3] = true;
		arrField1[7][4][2] = true;
		arrField1[7][4][3] = true;
		arrField1[7][3][3] = true;
	}
	
	private void FigureBlinker() {
		ClearField();
		arrField1[8][8][8] = true;
		arrField1[7][8][8] = true;
		arrField1[9][8][8] = true;
		arrField1[8][7][8] = true;
		arrField1[8][9][8] = true;
		arrField1[8][8][7] = true;
		arrField1[8][8][9] = true;
	}
}
