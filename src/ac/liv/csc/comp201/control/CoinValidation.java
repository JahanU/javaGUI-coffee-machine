package ac.liv.csc.comp201.control;

import java.util.Arrays;

import ac.liv.csc.comp201.MachineController;
import ac.liv.csc.comp201.model.IMachine;
import ac.liv.csc.comp201.simulate.CoinHandler;

public class CoinValidation extends MachineController {
	
	private static IMachine machine;
<<<<<<< HEAD
=======
	
	static int coinChange[] = new int[6];
	static int amount[] = new int[6];
>>>>>>> fd9df268f9651a98b0373c5d9cf8f541cc5574a4
	
	private static int coinChange[] = new int[6];
	private static int amount[] = new int[6];
	private static int coins[] = {100, 50, 20, 10, 5, 1};
	private static String coinCodes[] = {"ef", "bd", "bc", "ba", "ac", "ab"};
	
	public CoinValidation(IMachine machine) {
		this.machine = machine;
	}
	
	
	public static void coin(String coinCode) { // returns value based on coinCode
		
		String coinCodes[]={"ab","ac","ba","bc","bd","ef"};
		int coinValue[]={1, 5, 10, 20, 50, 100};
		
		for (int i = 0; i < coinValue.length; i++ ) 
			if (coinCode == coinCodes[i]) 
				machine.setBalance(machine.getBalance() + coinValue[i]);
			
		
	}
	
	
	public static String convertToMoneyDisplay() { 
		
	    String returnString = "" + machine.getBalance()/100 + "."; // first compute the pounds (whole units) 

	     int penceAmount = machine.getBalance() % 100; // get the pence amount 

	     if (penceAmount < 10) 
	    	 returnString = returnString + "0"; // leading zero to allow 2dp display 

	    
	    return (returnString + penceAmount); 
	} 




	
	
	private static void calcChange() { // returns highest amount of coins that is available within the machine
		
		machine.getCoinHandler().clearCoinTry(); // Empty previous coin tray out
		
		for (int i = 0; i < coins.length; i++) {
			if (coins[i] <= machine.getBalance() && machine.getCoinHandler().coinAvailable(coinCodes[i])) { // Check If coins is less than that total and if coin is available in machine
				
<<<<<<< HEAD
				int num = machine.getBalance()/coins[i]; // Store amount of times the coin can be divided into the balance
				amount[i] = num; // Stores amount of coins that can be returned 
				coinChange[i] = coins[i]; // Stores the value of the change
=======
	
				int num = machine.getBalance()/coins[i];
				amount[i] = num; // Stores amount of coins that can be returned 
				coinChange[i] = coins[i]; // Stores the value of the change
				
				machine.getCoinHandler().dispenseCoin(coinCodes[i]); // dispense the coincode from machine

>>>>>>> fd9df268f9651a98b0373c5d9cf8f541cc5574a4
				machine.setBalance(machine.getBalance() - (num * coins[i])); // Updates balance 
				
				for (int x = 0; x < num; x++) 
					machine.getCoinHandler().dispenseCoin(coinCodes[i]); // dispense the coincode from machine (based on the amount of coins that can be returned)
			}
			machine.getDisplay().setTextString("Balance: " + CoinValidation.convertToMoneyDisplay());

		}
	}
	
	
	protected static void getChange() {
		
		if (machine.getBalance() == 0) {
			System.out.println("No money entered!");
			machine.getDisplay().setTextString("No money entered");
		}
		else {
			calcChange();
	
			for (int i = 0; i < coinChange.length; i++) {
				if (coinChange[i] > 0) {
					System.out.println("Highest amount of change: " + amount[i]  + " " + coinChange[i] + "p");
					machine.getDisplay().setTextString("returning remaining credit: " + convertToMoneyDisplay());
				}
			}
			for (int i = 0; i < coinChange.length; i++) {
				 if (!machine.getCoinHandler().coinAvailable(coinCodes[i]) && coinChange[i] == 0) 
						System.out.println("No: " + coins[i] + "p coins available to return! " + CoinValidation.convertToMoneyDisplay());
				
			}
		}
	}



}
