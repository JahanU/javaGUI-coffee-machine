package ac.liv.csc.comp201.control;
import java.util.Arrays;
import ac.liv.csc.comp201.model.IMachine;
import ac.liv.csc.comp201.simulate.Cup;
import ac.liv.csc.comp201.simulate.Hoppers;

public class Drinks extends Drink {
		
	private IMachine machine;
	
	private static boolean IDLE = true; // If temp has reached correct idle temp (75 - 76
	private static boolean maxTempReached, waterLimitReached = false;

	public static String orderCodeString = ""; // Stores complete order code 
	private static int orderCode, keyCode;
	private static int match = -1;
	// Order code stores the orderCode in Int format
	// KeyCode stores the selected keyCode value
	// Match stores the array pos of the drink they selected

	public static int cupSizeInt = 1; // Stores the cupSize they have selected
	
	// Element position:  0 = coffee, 1 = sugar, 2 = milk, 3 = chocolate 
	private static double [] amountLimit = {2, 5, 3, 28}; // Pre set ingredients limit. Can be changed based on cup size
	private static boolean [] dispense = {false, false, false, false}; // Used to set the machine to dispense ingredients or not
	private static String [] captions = {"Coffee","Milk","Sugar","Chocolate"}; // Captions for ingredients

	private final int EFFICIENT_TEMP = 76; // All the set required temperature
	
	private final int MAX_CODE_LEN = 4; // Used to set a length for the orderCode depending if they selected a medium/large cup
	private final int MIN_CODE_LEN = 3;
	
	private final String MEDIUM_CUP_PREFIX = "5"; // Used to check if the order code has a 5/6
	private final String LARGE_CUP_PREFIX = "6";
	private final int RETURN_CHANGE_BUTTON = 9; // Used to check if the user selected return change

	public Drinks(IMachine machine) {
		this.machine = machine;
	}

	private double cupSizeWaterLevel() { // gets the size of cup
		// this returns the volume of the cup, so if its a large cup the water volume is 0.56		
		switch (cupSizeInt) {
			case Cup.MEDIUM_CUP:
				return Cup.MEDIUM;
			case Cup.LARGE_CUP:
				return Cup.LARGE;
			default:
				return Cup.SMALL;
		}
	}
	
	private String cupSizeName() { // gets the name of cup
		
		switch (cupSizeInt) {
			case Cup.MEDIUM_CUP:
				return "MEDIUM CUP";
			case Cup.LARGE_CUP:
				return "LARGE CUP";
			default:
				return "SMALL CUP";
		}
	}
	
	private void calcIngredients(double ingredientsPos) { // scales up ingredients based on cup size
//		divide the size of the cup your are making by the size of the small cup, then multiply by the amount of ingredient...
		double Scaler = cupSizeWaterLevel()/machine.getCup().SMALL;
		int i  = (int) ingredientsPos;
		// Array position is passed in, so we use this to set the amount of coffee to dispense and the limit it goes up to
		amountLimit[i] = amountLimit[i] * Scaler;
		System.out.println("Amount of ingredients: " + Arrays.toString(amountLimit));
		dispense[i] = true;
	}
		
	private boolean checkIngredients(int ingredientsPos) {  // Checks if ingredients for drink is available
				
		if (amountLimit[ingredientsPos] > machine.getHoppers().getHopperLevelsGrams(ingredientsPos)) { // Checks if there is enough ingredients left
			System.out.println("Not enough ingredients at all to make drink!");
			machine.getDisplay().setTextString("Not enough ingredients at all to make drink!");
			reset(); // If not available reset everything and start again
			return false;
		}
		return true; // We do have enough ingredients, return true
	}
	
	private boolean checkAllIngredients() {  // Checks if ingredients avaliable, if none avaliable lock handler
		// All the ingredients in machine is below the needed limit to make a drink, lock coinHandler
		int x = 0;
		for (int i = 0; i < captions.length; i++) {
			if (machine.getHoppers().getHopperLevelsGrams(i) <= amountLimit[i]) { 
				x++;
				if (x == captions.length) {
					System.out.println("Not enough ingredients at all to make drink!");
					machine.getDisplay().setTextString("Not enough ingredients at all to make drink!");
					reset(); // If not available reset everything and start again
					machine.getCoinHandler().lockCoinHandler();
					return false;
				}
			}
		}
		return true;
	}
	
	
	public boolean keyCodePressed(int keyCodePassed) { // Validate keyCode, check if they pressed reject.
		keyCode = keyCodePassed;

		checkAllIngredients(); // Check if enough there is enough ingredients for at least one drink in machine 
		
		if (keyCode != -1 && !dispense[0] && !dispense[1] && !dispense[2] && !dispense[3] && !waterLimitReached) { // If they select reject, then display change
			if (keyCode == RETURN_CHANGE_BUTTON) {// If reject button, return balance
				CoinValidation.getChange();
				reset();// Reset everything
			}
			// If temp less than efficient temp and they entered ordercode we display error message
			else if (checkAllIngredients() && machine.getWaterHeater().getTemperatureDegreesC() < EFFICIENT_TEMP - 1) {
				System.out.println("Balance: " + CoinValidation.convertToMoneyDisplay() + " Temperture is to low to start making drink, please wait");
				machine.getDisplay().setTextString("Balance: " + CoinValidation.convertToMoneyDisplay() + " Temperture is to low to start making drink, please wait");
			}
			else if (keyCode != -1 && !dispense[0] && !dispense[1] && !dispense[2] && !dispense[3] && !waterLimitReached)// Go to orderCode method
				return true;
		}
		return false;
	}
	
	
	public boolean orderCode() { // Returns the correct order code
	
		machine.getCoinHandler().clearCoinTry();
		Drinks.init(); // Initialise all drinks
		machine.getDisplay().setTextString("Balance: " + CoinValidation.convertToMoneyDisplay()  + " Order code: " + orderCodeString);

		if (orderCodeString.length() < MAX_CODE_LEN)  { // Only store up maximum code length (4)
			orderCodeString += Integer.toString(keyCode); // Converts integer code into string
			System.out.println("Order code is: " + orderCodeString);
			machine.getDisplay().setTextString("Balance: " + CoinValidation.convertToMoneyDisplay() + " Order code: " + orderCodeString);
		}
		
		int codeLen = MIN_CODE_LEN; // Code length is set to auto set to min code length 
		String prefix = orderCodeString.substring(0, 1); // Returns a single letter String of the prefix
		 
		if (prefix.equals(MEDIUM_CUP_PREFIX) || prefix.equals(LARGE_CUP_PREFIX)) { // Codes with prefix are a little longer
			codeLen = MAX_CODE_LEN; // sets code length to 4 since they have selected medium/large cup
			cupSizeInt = prefix.equals(MEDIUM_CUP_PREFIX) ? machine.getCup().MEDIUM_CUP : machine.getCup().LARGE_CUP; // if prefix is 5, then cup size is medium, if prefix is 6, cup size is large
		}
		
		if (orderCodeString.length() == codeLen && orderCodeString.length() <= MAX_CODE_LEN) { // We have got a key code of target length
			System.out.println("FINAL Code is: " + orderCodeString + " Length: " + codeLen);
			orderCode = Integer.parseInt(orderCodeString); // String to Integer // Stores final orderCode
				
			for (int i = 0; i < allDrinks.size(); i++) { // Check order code
				if (orderCode == allDrinks.get(i).code) { // If correct orderCode
					match = i;
					return true;		
				}
			}
			for (int i = 0; i < allDrinks.size(); i++) { // Check order code
				if (orderCode != allDrinks.get(i).code) { // If correct orderCode
					System.out.println("Balance: " + CoinValidation.convertToMoneyDisplay() + " Incorrect order code, try again");
					machine.getDisplay().setTextString("Balance: " + CoinValidation.convertToMoneyDisplay() + " Incorrect order code, try again");	
					reset(); // Wrong code, start again
					return false;	
				}
			}	
		}
	return false;
	}
	
	
	public boolean startDrink() { // Checks if we can make drink
		// Checks price 
		if  (machine.getBalance() < allDrinks.get(match).price) {
			System.out.println("Not enough money, Entered: " + CoinValidation.convertToMoneyDisplay() + " Need: " + allDrinks.get(match).price + " for: " + allDrinks.get(match).name);
			machine.getDisplay().setTextString("Not enough money, Entered: " + CoinValidation.convertToMoneyDisplay() + " Need: " + allDrinks.get(match).price + " for: " + allDrinks.get(match).name);
			return false; // Cannot make drink as not enough money, start again
		}
		// All clear, can start making drink!
		// Code must be correct / money must be >= drink price / Must have enough ingredients
		else {
			
			if (machine.getWaterHeater().getTemperatureDegreesC() >= EFFICIENT_TEMP - 1) { // if temp is above 75, then we can start making drink as this can reach 95 within 4 seconds
				System.out.println("going to 90C+");
				IDLE = false; // stop turning the heater on and off, drink can now be made
				machine.getWaterHeater().setHeaterOn();
			}
			
			if (orderCodeString.contains("101") || orderCodeString.contains("102") || orderCodeString.contains("201") || orderCodeString.contains("202")) {
				if (checkIngredients(0)) // Check we have enough ingredients to start making drink
					calcIngredients(0); // Scaling up the ingredients based on cupSizeWaterLevel if we do have enough ingredients
				else 
					return false;
				if (orderCodeString.contains("102") || orderCodeString.contains("202")) { // For all drinks that contain sugar
					if (checkIngredients(1))
						calcIngredients(1); // Check we have enough ingredients to start making drink
					else 
						return false;
				}
				if (orderCodeString.contains("201") || orderCodeString.contains("202")) { // For all drinks that contain milk
					if (checkIngredients(2))
						calcIngredients(2); 
					else 
						return false;
				}
			}
			
			if (orderCodeString.contains("300")) {
				if (checkIngredients(3))
					calcIngredients(3); 
				else 
					return false;
			}
			
			if (machine.getBalance() >= allDrinks.get(match).price) {
				machine.setBalance((int) (machine.getBalance() - allDrinks.get(match).price)); // updates price
				System.out.println("Making drink: " + allDrinks.get(match).name + " Order: " + allDrinks.get(match).code + " Price is: " + allDrinks.get(match).price);
				machine.getDisplay().setTextString("Making: " + allDrinks.get(match).name + " Order: " + allDrinks.get(match).code + " Price is: " + allDrinks.get(match).price);
				
				machine.getCoinHandler().lockCoinHandler();  // Lock coinHandler as orderCode is correct and can start making drink
				machine.vendCup(cupSizeInt); // works out which cup value to use (1 or 2 or 3)
				System.out.println("Vending cup /" + " CupSize: " + cupSizeName());
				return true;
			}	
		}
		return false;
	}
	

	public void checkDrink() {
		
		final int MIN = 70; // If around 70 - 73, can stop displaying error message and show balance and orderCode
		
		if (machine.getWaterHeater().getTemperatureDegreesC() > MIN && machine.getWaterHeater().getTemperatureDegreesC() < (MIN + 3)) // Used to stop displaying error message when they press the keypad below IDLE temp
			machine.getDisplay().setTextString("Balance: " + CoinValidation.convertToMoneyDisplay() + " Order code: " + orderCodeString);
		
		Cup cup = machine.getCup();
		if (cup != null) {  // If cup has been initialised 
			
			 // If temp has reached correct temp and the 
			 if (!maxTempReached && machine.getWaterHeater().getTemperatureDegreesC() >= allDrinks.get(match).temp) {
				System.out.println("Correct drink temp reached! Cooling down!");
				IDLE = true; // go back to saving electricity mode
				maxTempReached = true; // reached correct temp, can start dispensing ingredients
			 }
			 // If drink water level has reached water level then stop dispensing water, else dispense water
			if (maxTempReached && cup.getWaterLevelLitres() >= cupSizeWaterLevel()) { 
				machine.getWaterHeater().setHotWaterTap(false); 
				waterLimitReached = true;
				System.out.println("Water amount is correct: " + cup.getWaterLevelLitres());
			}
			else if (maxTempReached && cup.getWaterLevelLitres() < cupSizeWaterLevel()) 
				machine.getWaterHeater().setHotWaterTap(true); 
			
			 // If drink is complete, temp has reached correct temp, and all other ingredients have stopped dispensing, then drink is done!
			 if (IDLE && maxTempReached && waterLimitReached && !dispense[0] && !dispense[1] && !dispense[2] && !dispense[3]) {
				 machine.getDisplay().setTextString("Balance: " + CoinValidation.convertToMoneyDisplay() + " " + allDrinks.get(match).name + " ready");
				 reset();	
			 }
			
			if (dispense[0] && maxTempReached && cup.getCoffeeGrams() >= amountLimit[0]) {
				machine.getHoppers().setHopperOff(Hoppers.COFFEE);
				System.out.println("Coffee amount is correct: " + cup.getCoffeeGrams());
				dispense[0] = false;
			}
			else if (maxTempReached && dispense[0]) 
				machine.getHoppers().setHopperOn(Hoppers.COFFEE);
			
			
			if (dispense[1] && maxTempReached && cup.getSugarGrams() >= amountLimit[1]) {
				machine.getHoppers().setHopperOff(Hoppers.SUGAR);
				System.out.println("Sugar amount is correct: " + cup.getSugarGrams());
				dispense[1] = false;
			}
			else if (maxTempReached && dispense[1]) 
				machine.getHoppers().setHopperOn(Hoppers.SUGAR);
			
			
			if (dispense[2] && maxTempReached && cup.getMilkGrams() >=  amountLimit[2]) {
				machine.getHoppers().setHopperOff(Hoppers.MILK);
				System.out.println("Milk Powder amount is correct: " + cup.getMilkGrams());
				dispense[2] = false;
			}
			else if (maxTempReached && dispense[2]) 
				machine.getHoppers().setHopperOn(Hoppers.MILK);  

			
			if (dispense[3] && maxTempReached && cup.getChocolateGrams() >= amountLimit[3]) {
				machine.getHoppers().setHopperOff(Hoppers.CHOCOLATE);
				System.out.println("Chocolate amount is correct: " + cup.getChocolateGrams());
				dispense[3] = false;
			}
			else if (maxTempReached && dispense[3]) 
				machine.getHoppers().setHopperOn(Hoppers.CHOCOLATE);
			
		}
	}
	
	public void reset() { // Resets everything back to normal

		IDLE = true;
		orderCodeString = "";
		orderCode = 0;
		cupSizeInt = 1;
		maxTempReached = false;
		Arrays.fill(dispense, false);
		waterLimitReached = false;
		machine.getCoinHandler().unlockCoinHandler(); // Can allow users to start entering coins again
		amountLimit[0] = 2;
		amountLimit[1] = 5;
		amountLimit[2] = 3;
		amountLimit[3] = 28;
	}

	public boolean getIdle() {
		return IDLE;
	}	
} // Class end

	
	