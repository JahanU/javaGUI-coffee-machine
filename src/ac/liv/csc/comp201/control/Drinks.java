package ac.liv.csc.comp201.control;

import java.util.ArrayList;
import java.util.Arrays;
import ac.liv.csc.comp201.model.IMachine;
import ac.liv.csc.comp201.simulate.Cup;
import ac.liv.csc.comp201.simulate.Hoppers;

public class Drinks extends Drink {
		
	private IMachine machine;
	
	private static boolean IDLE = true;
	
	private static boolean orderSuccessful, maxTempReached = false;

	private static String orderCodeString = ""; // Stores complete order code 
	private static int orderCode, keyCode, ingredientsIndex = 0; 
	private static int match = -1;
	// Order code stores the orderCode in Int format
	// KeyCode stores the selected keyCode value
	// Match stores the array pos of the drink they selected

	public static int cupSizeInt = 1; // Stores the cupSize they have selected
	
	// Element position:  0 = coffee, 1 = sugar, 2 = milk, 3 = chocolate 
	private static double amountLimit [] = {2, 5, 3, 28}; // Pre set ingredients limit. Can be changed based on cup size
	private static boolean dispense [] = {false, false, false, false}; // Used to set the machine to dispense ingredients or not
	private static String captions [] = {"Coffee","Milk","Sugar","Chocolate"};

	
	private final int EFFICIENT_TEMP = 76; // All the set required temperature
	private final double MAX_COFFEE_TEMP = 95.9;
	private final int MAX_HOT_CHOCOLATE_TEMP = 90;
	
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
		System.out.println(amountLimit[i]);
		System.out.println(Arrays.toString(amountLimit));

		ingredientsIndex = i;
	}
	
	// Want to pass in the returned value of calcIngredients into checkIngredients
	
	private boolean checkIngredients() {  // Checks if ingredients for drink is available
				
		
		if (amountLimit[ingredientsIndex] > machine.getHoppers().getHopperLevelsGrams(ingredientsIndex)) { // Checks if there is enough ingredients left
			System.out.println("Not enough " + captions[ingredientsIndex] + " left to make drink!");
			machine.getDisplay().setTextString("Not enough " + captions[ingredientsIndex] + " left to make drink!");
			reset();
			return false;
		}
		machine.vendCup(cupSizeInt); // works out which cup value to use (1 or 2 or 3)
		System.out.println("Vending cup /" + " CupSize: " + cupSizeName());
		dispense[ingredientsIndex] = true;
		return true;
	}
	
	
	public boolean keyCodePressed(int keyCodePassed) { // Validate keyCode, check if they pressed reject.
		keyCode = keyCodePassed;

		// If they select reject, then display change
		if (keyCode != -1) {
			if (keyCode == RETURN_CHANGE_BUTTON) {// If reject button, return balance
				CoinValidation.getChange();
				reset();
				machine.getDisplay().setTextString("Balance: " + machine.getBalance());
				return false;
			}
			// If temp hasn't reached idle yet then the user cannot start entering ordercode until at IDLE temp
			else if (machine.getWaterHeater().getTemperatureDegreesC() < EFFICIENT_TEMP) {
				System.out.println("Balance: " + machine.getBalance() + " Temperture is to low to start making drink, please wait");
				machine.getDisplay().setTextString("Balance: " + machine.getBalance() + " Temperture is to low to start making drink, please wait");
				return false;
			}
			else
				return true;
		}
		return false;
	}
	
	

	public boolean orderCode() { // Returns the correct order code
		
		Drinks.init(); // Initialise all drinks
		machine.getDisplay().setTextString("Balance: " + CoinValidation.convertToMoneyDisplay() + " Order code: " + orderCodeString);


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
		
			 if (match == -1) {
				System.out.println("Balance: " + CoinValidation.convertToMoneyDisplay() + " Incorrect order code, try again");
				machine.getDisplay().setTextString("Balance: " + CoinValidation.convertToMoneyDisplay() + " Incorrect order code, try again");	
				reset();
				return false;	
			}
		}
		
	return false;
	}
	
	
	public boolean startDrink() { // Validate the code and check if it matches the list of other drinks
				
		// Checks price 
		if  (machine.getBalance() < allDrinks.get(match).price) {
			System.out.println("Not enough money, Entered: " + CoinValidation.convertToMoneyDisplay() + " Need: " + allDrinks.get(match).price);
			machine.getDisplay().setTextString("Not enough money, Entered: " + CoinValidation.convertToMoneyDisplay() + " Need: " + allDrinks.get(match).price);
			return false;
		}
		
		
		// All clear, can start making drink!
		// Code must be correct / money must be >= drink price / Must have enough ingredients
		else {
			
			if (machine.getWaterHeater().getTemperatureDegreesC() >= EFFICIENT_TEMP) { // if temp is above 75, then we can start making drink as this can reach 95 within 4 seconds
				System.out.println("going to 90C+");
				IDLE = false; // stop turning the heater on and off, drink can now be made
				machine.getWaterHeater().setHeaterOn();
			}
			
			if (orderCodeString.contains("101") || orderCodeString.contains("102") || orderCodeString.contains("201") || orderCodeString.contains("202")) {
				calcIngredients(0); // Scaling up the ingredients based on cupSizeWaterLevel
				if (!checkIngredients()) // Check we have enough ingredients to start making drink
					return false;
	
				if (orderCodeString.contains("102") || orderCodeString.contains("202")) { // For all drinks that contain sugar
					calcIngredients(1); 
					if (!checkIngredients()) // Check we have enough ingredients to start making drink
						return false;
				}
				if (orderCodeString.contains("201") || orderCodeString.contains("202")) { // For all drinks that contain milk
					calcIngredients(2); 
					if (!checkIngredients()) // Check we have enough ingredients to start making drink
						return false;
				}
			}
			
			if (orderCodeString.contains("300")) {
				calcIngredients(3); 
				if (!checkIngredients()) // Check we have enough ingredients to start making drink
					return false;
			}
			
			
			if (machine.getBalance() >= allDrinks.get(match).price) {
				machine.setBalance((int) (machine.getBalance() - allDrinks.get(match).price)); // updates price
				System.out.println("Making drink: " + allDrinks.get(match).name + "\nOrder is: " + allDrinks.get(match).code + "\nPrice is: �" + allDrinks.get(match).price);
				machine.getDisplay().setTextString("Change is: " + CoinValidation.convertToMoneyDisplay() + " Order code: " + orderCode + " Drink: " + allDrinks.get(match).name);
				orderSuccessful = true;
				return true;
			}
			
		}
			
		

		return false;
	}
	

	

	
	
	public void checkDrink() {

		if (!maxTempReached && machine.getWaterHeater().getTemperatureDegreesC() > EFFICIENT_TEMP) { // displays at the start of making the drink
			machine.getDisplay().setTextString("total is: " +  CoinValidation.convertToMoneyDisplay() + " Order code: " + orderCodeString);
		}
		

		Cup cup = machine.getCup();
		if (cup != null) {  // Control drink making here and get levels of ingredients
			
			machine.getDisplay().setTextString("Balance: " + machine.getBalance() + " Order code: " + orderCodeString + " Drink: " + allDrinks.get(match).name);
			if (cup.getWaterLevelLitres() >= cupSizeWaterLevel())
				machine.getDisplay().setTextString("Balance: " + machine.getBalance() + " - " +  allDrinks.get(match).name + " ready");

			 // If coffee was selected and temperature has reached correct temp
			 if (!dispense[3] && maxTempReached == false && machine.getWaterHeater().getTemperatureDegreesC() >= MAX_COFFEE_TEMP) {
				System.out.println("Correct coffee temp reached! Cooling down!");
				IDLE = true; // go back to saving electricity mode
				maxTempReached = true; // reached correct temp, can start dispensing ingredients
			 }

			 
			if (maxTempReached && cup.getWaterLevelLitres() >= cupSizeWaterLevel()) { 
				machine.getWaterHeater().setHotWaterTap(false); 
				System.out.println("Water amount is correct: " + cup.getWaterLevelLitres());
				reset();
			}
			
			else if (maxTempReached) 
				machine.getWaterHeater().setHotWaterTap(true); 
			
			
			if (maxTempReached && cup.getCoffeeGrams() >= amountLimit[0]) {
				machine.getHoppers().setHopperOff(Hoppers.COFFEE);
				System.out.println("Coffee amount is correct: " + cup.getCoffeeGrams());
				dispense[0] = false;
			}
			else if (maxTempReached && dispense[0]) 
				machine.getHoppers().setHopperOn(Hoppers.COFFEE);
			
			
			if (maxTempReached && cup.getSugarGrams() >= amountLimit[1]) {
				machine.getHoppers().setHopperOff(Hoppers.SUGAR);
				System.out.println("Sugar amount is correct: " + cup.getSugarGrams());
				dispense[1] = false;
			}
			else if (maxTempReached && dispense[1]) 
				machine.getHoppers().setHopperOn(Hoppers.SUGAR);
			
			
			if (maxTempReached && cup.getMilkGrams() >=  amountLimit[2]) {
				machine.getHoppers().setHopperOff(Hoppers.MILK);
				System.out.println("Milk Powder amount is correct: " + cup.getMilkGrams());
				dispense[2] = false;
			}
			else if (maxTempReached && dispense[2]) 
				machine.getHoppers().setHopperOn(Hoppers.MILK); 
		
		 
			if (dispense[3] && !maxTempReached && machine.getWaterHeater().getTemperatureDegreesC() >= MAX_HOT_CHOCOLATE_TEMP) {
				System.out.println("Correct hot chocolate temp reached! Cooling down!");
				IDLE = true;
				maxTempReached = true;
			}
			else if (maxTempReached)
				machine.getWaterHeater().setHotWaterTap(true); 

			if (maxTempReached && amountLimit[3] != 0 && cup.getChocolateGrams() >= amountLimit[3]) {
				machine.getHoppers().setHopperOff(Hoppers.CHOCOLATE);
				System.out.println("Chocolate amount is correct: " + cup.getChocolateGrams());
				machine.getDisplay().setTextString(allDrinks.get(match).name + " ready, Balance: " + CoinValidation.convertToMoneyDisplay());
				reset();
			}
			else if (maxTempReached && dispense[3]) 
				machine.getHoppers().setHopperOn(Hoppers.CHOCOLATE);
			
		}
		
	}
	
	
	public void reset() {

		IDLE = true;
		orderCodeString = "";
		orderCode = 0;
		maxTempReached = false;
		Arrays.fill(dispense, false);
	}

	
	public boolean getIdle() {
		return IDLE;
	}
	
	
	
	
} // Class end
	
	
	
	


//public void startDrink(int orderCode) { // start making drink!
//	
//	String codeS = Integer.toString(orderCode); // Int to String
//	
////	 else {
////		 machine.getDisplay().setTextString("Change is: " + machine.getBalance() + " Order code: " + orderCode + " Drink: " + allDrinks.get(match).name);
////		 orderSuccessful = false; // orderCode is not complete yet
////	 }
//	
//	if (machine.getWaterHeater().getTemperatureDegreesC() >= EFFICIENT_TEMP - 1) { // if temp is above 75, then we can start making drink as this can reach 95 within 4 seconds
//		System.out.println("going to 90C+");
//		IDLE = false; // stop turning the heater on and off, drink can now be made
//		machine.getWaterHeater().setHeaterOn();
//
//		
//		if (codeS.contains("101") || codeS.contains("102") || codeS.contains("201") || codeS.contains("202")) {
//			calcIngredients(0); // Scaling up the ingredients based on cupSizeWaterLevel
//			dispense[0] = true;
//
//			if (codeS.contains("102") || codeS.contains("202")) { // for all drinks that contain sugar
//				calcIngredients(1); // Scaling up the ingredients based on cupSizeWaterLevel
//				dispense[1] = true;
//			}
//			if (codeS.contains("201") || codeS.contains("202")) { // for all drinks that contain milk
//				calcIngredients(2); // Scaling up the ingredients based on cupSizeWaterLevel
//				dispense[2] = true;
//			}
//		}
//		
//		if (codeS.contains("300")) {
//			calcIngredients(3); // Scaling up the ingredients based on cupSizeWaterLevel
//			dispense[3] = true;
//		}
//	}
//}
	
	
	
	
	
	
	
	
	
	