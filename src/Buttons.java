import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Buttons extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L;
	
	private JButton[] arrButton = new JButton[8];
	private final int PANEL_WIDTH = 1200; //680 = 17 * 40
	private final int PANEL_HEIGHT = 100;
	
	Panel PanGra;
	Timer timer;
	
	Buttons(Panel panel){
		this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		this.setBounds(0,0,PANEL_WIDTH,PANEL_HEIGHT);
		//this.setLayout(null);
		//this.setLayout(new GridBagLayout());
		this.setLayout(new FlowLayout());
		PanGra = panel;
		timer = new Timer();
		
		String[] butName = { "Selection","Insertion","Quick","Heap","Radix (LSD)","Cocktail","Merge","Generate" };
		for (int i = 0; i < arrButton.length; i++) {
			arrButton[i] = new JButton(butName[i]);
			arrButton[i].setBounds((i+1)*100-50,PANEL_HEIGHT-75,100,50);
			//arrButton[i].setBounds((i+1)*100-50,PANEL_HEIGHT/2,100,50);
			//this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		    arrButton[i].setFont(new Font("Serif",Font.PLAIN,14));
		    arrButton[i].addMouseListener(this);
			//arrButton[i].setVisible(true);
			//arrButton[i].validate();
			this.add(arrButton[i]);
			//System.out.println(arrButton[i].isDisplayable());
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		//PanGra.comp(e, arrButton);

		timer.cancel();
		PanGra.init();
		
		if(e.getComponent() == arrButton[arrButton.length-1]) {
			PanGra.comp(e, arrButton);
		}
		else {
			int timeInterval = 100;

			if(e.getComponent() == arrButton[2])
				timeInterval = 10;
			else if(e.getComponent() == arrButton[4])
				timeInterval = 50;
			else if(e.getComponent() == arrButton[5])
				timeInterval = 1;
			else if(e.getComponent() == arrButton[6])
				timeInterval = 1000;

			timer = new Timer();
			timer.schedule(new ReceiverTask(PanGra, e, arrButton, timer), 0, timeInterval);
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

}
