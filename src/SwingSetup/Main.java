package SwingSetup;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Main extends JFrame {
	private static final long serialVersionUID = 1L;

	private final int WINDOW_W = 1280;
	private final int WINDOW_H = 720;
	
	Panel pWindow;
	
	Main() {
		pWindow = new Panel(WINDOW_W, WINDOW_H);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.setPreferredSize(new Dimension(WINDOW_W, WINDOW_H));
		this.add(pWindow);
		this.pack();
		this.setLocationRelativeTo(this);
		this.setResizable(false);
		this.setVisible(true);	
	
		//Hides the cursor
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
		    cursorImg, new Point(0, 0), "blank cursor");
		this.getContentPane().setCursor(blankCursor);
	}

	public static void main(String[] args) {
		new Main();
	}
}
