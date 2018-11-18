package ac.liv.csc.comp201.simulate;

import java.awt.Toolkit;

import javax.swing.JFrame;

public class Background extends JFrame {
	public Background() {
		int screenWidth=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int screenHeight=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		this.setBounds(0, 0,screenWidth,screenHeight);
		this.setUndecorated(true);
		setVisible(true);

	}
}
