import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;

public class Main extends JFrame {
	private static final long serialVersionUID = 1L;

	Panel PanelC; 
	Buttons ButPanC;
	
	private final int WINDOW_WIDTH = 1200; //680 = 17 * 40
	private final int WINDOW_HEIGHT = 680; //680
	
	
	Main() {
		PanelC = new Panel();
		ButPanC = new Buttons(PanelC);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		this.setLayout(new FlowLayout());
		this.add(PanelC);
		this.add(ButPanC);
		this.pack();
		this.setLocationRelativeTo(this);
		this.setResizable(false);
		this.setVisible(true);
		

		//this.addKeyListener(this);
	}
	
	public static void main(String[] args) {
		new Main();
	}

}
