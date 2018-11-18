package ac.liv.csc.comp201.simulate;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.Border;

import ac.liv.csc.comp201.MachineController;
import ac.liv.csc.comp201.model.ICoinHandler;
import ac.liv.csc.comp201.model.IDisplay;
import ac.liv.csc.comp201.model.IHoppers;
import ac.liv.csc.comp201.model.IKeyPad;
import ac.liv.csc.comp201.model.IMachine;
import ac.liv.csc.comp201.model.IMachineController;
import ac.liv.csc.comp201.model.IWaterHeater;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class MachineSimulator extends Thread implements IMachine  {

	private JFrame frame;
	
	private int balance;
	
	private long currentTime=0;
	
	private IMachineController controller;
	
	MachineSimulator window =null;
	
	private Cup currentCup=null;
	
	private ICoinHandler coinHandler=null;
	
	private IDisplay display=null;
	
	JLabel coinTrayDisplay=null;

	private IKeyPad keyPad;
	
	private int cupType=-1;

	private boolean closed=false;
	
	WaterHeater waterHeater;
	
	
	IHoppers hoppers=null;
	
	public Cup getCup() {
		return(currentCup);
	}
	
	public IHoppers getHoppers() {
		return(hoppers);
	}
	
	public void vendCup(int cupType) {
		this.cupType=cupType;
	}
	
	public void doVendCup() {
		if (cupType!=-1) {
			
		if (currentCup!=null) {
			frame.getContentPane().remove(currentCup);
		}
		currentCup=new Cup(cupType);
		frame.getContentPane().add(this.currentCup);
		frame.invalidate();
		frame.validate();
		this.cupType=-1;
		
		}
	}
	
	private void setController(IMachineController controller) {
		this.controller=controller;
	}
	
	

	/**
	 * Launch the application.
	 */
	public void start(IMachineController controller) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Background b=new Background();
					window = new MachineSimulator();
					window.initialize();
					window.frame.setVisible(true);
					window.setController(controller);
					window.hoppers=new Hoppers();
					window.setCurrentTime(System.currentTimeMillis());
					window.start();  // kick of thread
					controller.startController(window);  // kick off the controller for this machine					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	

	/**
	 * Create the application.
	 */
	public MachineSimulator() {
		
		
	}
	
	public ICoinHandler getCoinHandler() {
		return(coinHandler);
	}
	
	public void shutMachineDown() {
		closed=true;
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		
		int width=250;
		int height=600;
		
		double screenWidth=Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double screenHeight=Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		
		frame.setBounds((int)(screenWidth/2),40, width,height);
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		
		coinHandler=new CoinHandler(this);
		
		KeyPad keyPad=new KeyPad(this);
		keyPad.setBounds(0, 0, 600,400);
		keyPad.setVisible(true);
		waterHeater=new WaterHeater();
		
		
		frame.getContentPane().setLayout(new GridLayout(3, 1, 0, 0));
		
		Display display=new Display();
		
		frame.getContentPane().add(display);
		
		coinTrayDisplay=new JLabel();
		Border b=BorderFactory.createLineBorder(Color.BLACK, 2);
		coinTrayDisplay.setBorder(b);
		coinTrayDisplay.setHorizontalAlignment(JLabel.CENTER);
		
		
		frame.getContentPane().add(coinTrayDisplay);
		
		this.display=display;
		this.keyPad=keyPad;
		
		frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
            	controller.stopController();
            	closed=true;
                keyPad.setVisible(false);
                keyPad.dispose();
                coinHandler.close();
                e.getWindow().dispose();
            }
        });
		
		
	}



	@Override
	public IDisplay getDisplay() {
		return(display);
	}



	@Override
	public void run() {
		while (!closed) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			currentTime=currentTime+10;
			getWaterHeater().updateHeater(currentCup);
			hoppers.updateHoppers(currentCup);
			doVendCup();
			coinTrayDisplay.setText(coinHandler.getCoinTray());
			frame.invalidate();
			frame.repaint();
			
		}
		System.out.println("Terminating thread!!!...");
	}



	@Override
	public void keyPadPressed() {
		controller.updateController();		
	}
	



	@Override
	public IKeyPad getKeyPad() {
		return(keyPad);
	}



	@Override
	public IWaterHeater getWaterHeater() {
		return(waterHeater);
	}

	public long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}

	
	/**
	 * @return the balance
	 */
	public final int getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public final void setBalance(int balance) {
		this.balance = balance;
	}
	
}
