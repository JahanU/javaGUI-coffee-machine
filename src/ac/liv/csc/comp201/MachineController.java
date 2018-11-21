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
	
	private boolean running = true;
	private IMachine machine;
	
	WaterHeaterController WHC;
	Drinks drinks;
	CoinValidation coins;
	
	private static final String version="1.22";
	
	
	public void startController(IMachine machine) {
		this.machine=machine; // Machine that is being controlled
//		machine.getKeyPad().setCaption(0,"Cup");
//		machine.getKeyPad().setCaption(1,"Water heater on");
//		machine.getKeyPad().setCaption(2,"Water heater off");		
//		machine.getKeyPad().setCaption(3,"Hot Water On");
//		machine.getKeyPad().setCaption(4,"Hot Water Off");		
//		machine.getKeyPad().setCaption(5,"Dispense coffee");
//		machine.getKeyPad().setCaption(6,"Dispense milk");
		machine.getKeyPad().setCaption(7,"");
		machine.getKeyPad().setCaption(8,"");
		machine.getKeyPad().setCaption(9,"Reject");	
		
		// Initialising all objects
		WHC = new WaterHeaterController(machine);
		drinks = new Drinks(machine);
		coins = new CoinValidation(machine);
		
		super.start();
	}
	
	
	public MachineController() {
		
	}
	
	
	private synchronized void runStateMachine() {

		WHC.boilingPoint(); // If temp reaches 100C turn machien off
		WHC.machineError(); // Checks if there is an issue with the machine i.e. temp going up but heater is off
		
						
		int keyCode = machine.getKeyPad().getNextKeyCode();
	
		
		if (drinks.getIdle()) 
			WHC.saveElectricity();
		
		
		 if (drinks.keyCodePressed(keyCode)) 
			 if (drinks.orderCode())  // Create order code
				if (drinks.startDrink())  // checks code to get drink
					System.out.println("!! starting drink !!");	
				else
					drinks.reset();
		
		 
		 drinks.checkDrink();
		 
			 

//		Cup cup = machine.getCup();
//		if (cup != null) {
//			System.out.println("Water level is " + cup.getWaterLevelLitres() + " coffee is " + cup.getCoffeeGrams() + "grams");
//			if (cup.getCoffeeGrams() >= 2) {
//				machine.getHoppers().setHopperOff(Hoppers.COFFEE);
//				System.out.println("Coffee is at: " + cup.getCoffeeGrams() + "\nWater level is at: " + +cup.getWaterLevelLitres());
//			}
//		}
		
		
//		int keyCode = machine.getKeyPad().getNextKeyCode();
		String coinCode = machine.getCoinHandler().getCoinKeyCode();
		if (coinCode != null) {
			coins.coin(coinCode); // Converts coinCode to money
			machine.getDisplay().setTextString("Balance: " +  CoinValidation.convertToMoneyDisplay() + " Order code: " + Drinks.orderCodeString);

		}
		
//		switch (keyCode) {
//			case 0 :
//				System.out.println("Vending cup"); // once cup is made, it is not null, then the above statement runs!
//				machine.vendCup(Cup.SMALL_CUP);
//			break;
//			case 1 :
//				machine.getWaterHeater().setHeaterOn(); // quickly increases temp						
//				break;
//			case 2 :
//				machine.getWaterHeater().setHeaterOff(); // slowly decreases temp
//				break;
//			case 3 :
//				machine.getWaterHeater().setHotWaterTap(true);// quickly decreases temp
//				break;
//			case 4 :
//				machine.getWaterHeater().setHotWaterTap(false); // slowly decreases temp
//				break;
//			case 5 :
//				machine.getHoppers().setHopperOn(Hoppers.COFFEE);
//				break;
//			case 6 :
//				machine.getHoppers().setHopperOn(Hoppers.MILK);
//				break;			
//			case 7 :
//				machine.getWaterHeater().setColdWaterTap(true); // puts cold water on
//				break;
//			case 8 :
//				machine.getWaterHeater().setColdWaterTap(false); // can stop pouring cold water
//				break;
//			case 9:
//				// this will be used for rejecting transaction, returning all coins
//				CoinValidation CV = new CoinValidation(coinCode, machine);
//				CV.getChange();
//				machine.getCoinHandler().getCoinTray(); 
//				break;	
//		}
		
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
