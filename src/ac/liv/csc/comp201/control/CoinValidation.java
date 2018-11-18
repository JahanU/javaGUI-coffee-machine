package ac.liv.csc.comp201.control;

import java.util.Arrays;

import ac.liv.csc.comp201.MachineController;
import ac.liv.csc.comp201.model.IMachine;
import ac.liv.csc.comp201.simulate.CoinHandler;

public class CoinValidation extends MachineController {
	
	private static IMachine machine;
	private static int totalCoins = 0;
	private static String stringTotalCoins, coinCode;
	
	static int coinChange[] = new int[6];
	static int amount[] = new int[6];
	
	
	public CoinValidation(IMachine machine) {
		this.machine = machine;
	}
	
	
	public static int coin(String coinCode) { // returns value based on coinCode
		
		String coinCodes[]={"ab","ac","ba","bc","bd","ef"};
		int coinValue[]={1, 5, 10, 20, 50, 100};
		
		for (int i = 0; i < coinValue.length; i++ ) {
			if (coinCode == coinCodes[i]) {
				machine.setBalance(machine.getBalance() + coinValue[i]);
				return coinValue[i];
			}
		}
		return 0;
	}
	
	
	public static String convertToMoneyDisplay() { 
		
	    String returnString = "" + machine.getBalance()/100 + "."; // first compute the pounds (whole units) 

	     int penceAmount = machine.getBalance() % 100; // get the pence amount 

	     if (penceAmount < 10) 
	    	 returnString = returnString + "0"; // leading zero to allow 2dp display 

	    return(returnString + penceAmount); 
	} 




	
	
	private static void calcChange() { // returns highest amount of coins that is available within the machine
		
		int coins[] = {100, 50, 20, 10, 5, 1};
		String coinCodes[] = {"ef", "bd", "bc", "ba", "ac", "ab"};
		
		for (int i = 0; i < coins.length; i++) {
			if (coins[i] <= machine.getBalance() && machine.getCoinHandler().coinAvailable(coinCodes[i])) { // Check If coins is less than that total and if coin is available in machine
				
				machine.getCoinHandler().dispenseCoin(coinCodes[i]); // dispense the coincode from machine
	
				int num = machine.getBalance()/coins[i];
				amount[i] = num; // Stores amount of coins that can be returned 
				coinChange[i] = coins[i]; // Stores the value of the change
				
				machine.setBalance(machine.getBalance() - (num * coins[i])); // Updates balance 
			}
		}
		
		for (int i = 0; i < coins.length; i++) {
			 if (!machine.getCoinHandler().coinAvailable(coinCodes[i])) {
				System.out.println("No: " + coins[i] + " coins available to return!" + CoinValidation.convertToMoneyDisplay());
				machine.getDisplay().setTextString("No: " + coins[i] + " coins available to return!" + CoinValidation.convertToMoneyDisplay());
			}
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
		}
	}



}
