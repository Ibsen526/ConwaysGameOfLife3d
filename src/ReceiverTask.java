import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;

public class ReceiverTask extends TimerTask {

	private Panel PanGra;
	private MouseEvent event;
	private JButton[] arrBut;
	private Boolean timerAct = false;
	private Timer timer2;
	
	ReceiverTask(Panel panel, MouseEvent e, JButton[] but, Timer timer) {
		PanGra = panel;
		event = e;
		arrBut = but;
		timer2 = timer;
	}
	
	public void run() {
		if(!timerAct)
			timerAct = PanGra.comp(event, arrBut);
		else {
			timer2.purge();
			timer2.cancel();
			PanGra.init();
			System.out.println("Finish!");
		}
	}

}
