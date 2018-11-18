package ac.liv.csc.comp201.simulate;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ac.liv.csc.comp201.model.IKeyPad;
import ac.liv.csc.comp201.model.IMachine;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/** This class simulates the key pad for the drinks machine
 * 
 * @author customer
 *
 */
public class KeyPad extends JFrame implements IKeyPad {


	private Vector <Integer> keyCodes=new Vector <Integer> ();
	
	private int BUTTONS_COUNT=10;
	
	
	/**
	 * Returns the next key code
	 * @return  0-9 if key code available -1 if buffer empty
	 */
	public synchronized int getNextKeyCode() {
		if (keyCodes.size()>0) {
			int code=keyCodes.get(0).intValue();
			keyCodes.remove(0);
			return(code);
		}
	
		return(-1);
	}
	
	
	public void setCaption(int buttonIndex,String caption) {
		buttons[buttonIndex].setText(caption);
	}
	
	
	public JButton [] buttons=new JButton[BUTTONS_COUNT];
	
	
	/**
	 * Create the Keypad, a Java JFrame is used with Swing buttons to simulate the actual buttons
	 */
		
	public KeyPad(IMachine machine) {
		this.setTitle("Key Pad");
		setAlwaysOnTop(true);
		setLayout(new GridLayout(4, 3, 0, 0));
		for (int index=0;index<10;index++) {
			JButton button = new JButton(""+index);
			buttons[index]=button;
			button.setActionCommand(""+index);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					int code=Integer.parseInt(actionEvent.getActionCommand());
					keyCodes.add(new Integer(code));
					//machine.keyPadPressed();
				}
				
			});
			this.getContentPane().add(button);
		}

	}

}
