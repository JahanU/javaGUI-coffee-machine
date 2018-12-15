package ac.liv.csc.comp201.control;

import java.util.Arrays;

import ac.liv.csc.comp201.MachineController;
import ac.liv.csc.comp201.model.IMachine;
import ac.liv.csc.comp201.simulate.CoinHandler;

public class CoinValidation  {
	
	private IMachine machine;
	
	private int coinChange[] = new int[6]; // Stores value of change
	private int amount[] = new int[6]; // Stores the amount of change 
	private int coins[] = {100, 50, 20, 10, 5, 1}; // Used to loop through coins (Stored in reverse order)
	private String coinCodes[] = {"ef", "bd", "bc", "ba", "ac", "ab"}; // Stores order code (in reverse order)
	private String oldBalance = ""; // Used to store the current balance before resetting the current balance
	
	public CoinValidation(IMachine machine) {
		this.machine = machine;
	}
	
	
	public void coin(String coinCode) { // returns value based on coinCode
		
		machine.getCoinHandler().clearCoinTry();
		
		String coinCodes[]={"ab","ac","ba","bc","bd","ef"};
		int coinValue[]={1, 5, 10, 20, 50, 100};
		
		for (int i = 0; i < coinValue.length; i++ ) 
			if (coinCode == coinCodes[i]) 
				machine.setBalance(machine.getBalance() + coinValue[i]); // Updates balance based on coinCode based
	}
	
	
	public String convertToMoneyDisplay() { 
		
	    String returnString = "" + machine.getBalance()/100 + "."; // first compute the pounds (whole units) 
	     int penceAmount = machine.getBalance() % 100; // get the pence amount 

	     if (penceAmount < 10) 
	    	 returnString = returnString + "0"; // leading zero to allow 2dp display 
	     
	    return (returnString + penceAmount); 
	} 

	
	private void calcChange() { // returns highest amount of coins that is available within the machine
		
		machine.getCoinHandler().clearCoinTry(); // Empty previous coin tray out
		
		oldBalance = convertToMoneyDisplay(); // Stores balance before resetting the balance / used to display to the user
		
		for (int i = 0; i < coins.length; i++) {
			if (coins[i] <= machine.getBalance() && machine.getCoinHandler().coinAvailable(coinCodes[i])) { // Check If coins is less than that total and if coin is available in machine
				
				int num = machine.getBalance()/coins[i]; // Store amount of times the coin can be divided into the balance
				amount[i] = num; // Stores amount of coins that can be returned 
				coinChange[i] = coins[i]; // Stores the value of the change
				machine.setBalance(machine.getBalance() - (num * coins[i])); // Updates balance 
				
				for (int x = 0; x < num; x++) 
					machine.getCoinHandler().dispenseCoin(coinCodes[i]); // dispense the coincode from machine (based on the amount of coins that can be returned)
			}
			machine.getDisplay().setTextString("Balance: " + convertToMoneyDisplay()); // Shows updated balance
		}
	}
	
	
	protected void getChange() {
		
		//machine.setBalance(210);
		System.out.println(machine.getBalance());
		
		if (machine.getBalance() == 0) { 
			System.out.println("No money entered!");
			machine.getDisplay().setTextString("No money entered");
		}
		else {
			calcChange(); // Calculate change, amount of coins to return etc.
	
			for (int i = 0; i < coinChange.length; i++) {
				if (amount[i] > 0) { // 
					System.out.println("Highest amount of change: " + amount[i]  + " " + coinChange[i] + "p");
					machine.getDisplay().setTextString("Balance: " + convertToMoneyDisplay() + " returned: " + oldBalance);
				}
			}
			for (int i = 0; i < coinChange.length; i++) {
				 if (!machine.getCoinHandler().coinAvailable(coinCodes[i]) && coinChange[i] == 0) 
						System.out.println("No: " + coins[i] + "p coins available to return! " + convertToMoneyDisplay());
				
			}
		}
		
		System.out.println(machine.getBalance());
	}



}
