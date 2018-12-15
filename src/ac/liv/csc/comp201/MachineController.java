package ac.liv.csc.comp201;
import ac.liv.csc.comp201.control.CoinValidation;
import ac.liv.csc.comp201.control.Drink;
import ac.liv.csc.comp201.control.Drinks;
import ac.liv.csc.comp201.control.WaterHeaterController;
import ac.liv.csc.comp201.model.IMachine;
import ac.liv.csc.comp201.model.IMachineController;
import ac.liv.csc.comp201.simulate.Cup;
import ac.liv.csc.comp201.simulate.Hoppers;

public class MachineController  extends Thread implements IMachineController {
	
	/***
	 * @author Jahan
	 * ID: 201272455
	 */

	private boolean running = true;
	
	private IMachine machine;
	
	WaterHeaterController WHC;
	Drinks drinks;
	CoinValidation coins;
	
	private static final String version="1.22";
	
	
	public void startController(IMachine machine) {
		this.machine=machine; // Machine that is being controlled
		machine.getKeyPad().setCaption(7,"");
		machine.getKeyPad().setCaption(8,"");
		machine.getKeyPad().setCaption(9,"Reject");	
		
		// Initialising all objects (once!)
		WHC = new WaterHeaterController(machine);
		drinks = new Drinks(machine);
		coins = new CoinValidation(machine);
		
		super.start();
	}
	
	
	public MachineController() {
	}
	
	private synchronized void runStateMachine() {

		WHC.boilingPoint(); // If temp reaches 100C turn machine off
		WHC.machineError(); // Checks if there is an issue with the machine i.e. temp going up but heater is off
								
		if (drinks.getIdle()) // If IDLE is true, than go to save electricity mode
			WHC.saveElectricity();	
		
		int keyCode = machine.getKeyPad().getNextKeyCode();
		 if (drinks.keyCodePressed(keyCode)) // Pass and validates keyCode when pressed
			 if (drinks.orderCode())  // Create order code
				if (drinks.startDrink())  // checks code to get drink
					System.out.println("Starting drink");	
				else
					drinks.reset(); // Code is false, not enough money, or ingredients
		
		 
		 drinks.checkDrink(); // Checks drink state, such as temp, if ingredients reached etc

		String coinCode = machine.getCoinHandler().getCoinKeyCode();
		if (coinCode != null) {
			coins.coin(coinCode); // Converts coinCode to money
			machine.getDisplay().setTextString("Balance: " +  coins.convertToMoneyDisplay() + " Order code: " + Drinks.orderCodeString);
		}
	}
	
		
	public void run() {
		// Controlling thread for coffee machine
		int counter=1;
		while (running) {
			//machine.getDisplay().setTextString("Running drink machine controller "+counter);
			counter++;
			try {
				Thread.sleep(10);		// Set this delay time to lower rate if you want to increase the rate
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			runStateMachine();
		}		
	}
	
	public void updateController() {
		//runStateMachine();
	}
	
	public void stopController() {
		running=false;
	}
	
	public int getCurrentBalance() {
		return machine.getBalance();
	}


	

}
