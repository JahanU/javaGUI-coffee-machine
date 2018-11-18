package ac.liv.csc.comp201.simulate;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import ac.liv.csc.comp201.model.IDisplay;

public class Display extends JLabel implements IDisplay {
	
	
	public void setTextString(String text) {
		setText(text);		
	}

	/**
	 * Create the panel.
	 */
	public Display() {
		setText("Display");
		Border border=BorderFactory.createBevelBorder(BevelBorder.RAISED,Color.LIGHT_GRAY,Color.GRAY);
		
		setBorder(border);
		
		
	       
		
		setHorizontalAlignment(CENTER);
	}
	
	

}
