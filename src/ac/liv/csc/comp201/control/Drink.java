package ac.liv.csc.comp201.control;

import java.util.ArrayList;
import java.util.*;

import ac.liv.csc.comp201.model.IMachine;

/***
 * @author Jahan
 */

/***
 * The machine will only make a drink if the correct credit is available 
 * 
 * and the levels for the relevant drink ingredients are available.  
 * The details of the drinks are shown in table 1. 
 * 
 * If there is not enough credit or there is not enough ingredients
 * the machine should show an appropriate message.
 * 
 * When the drink is being made the credit should be reduced by an appropriate amount. 
 * If it is impossible to make any of the drinks on the list the machine 
 * should not allow coins to be entered and display an error message. 
 
 *
 */

public class Drink {
	
	protected static ArrayList<Drink> allDrinks = new ArrayList<Drink>();

	protected String name;
	protected int code;
	protected double price;
	protected double temp;
	
	public Drink(String name, int code, double price, double temp) {
		this.name = name;
		this.code = code;
		this.price = price;
		this.temp = temp;

	}
	
	public Drink() {
		// TODO Auto-generated constructor stub
	}

	protected static void init() {
		// SMALL CUP SIZE
		// Name / Code / Price / Temp
		allDrinks.add(new Drink("Black coffee", 101, 120, 95.90));
		allDrinks.add(new Drink("Black coffee With sugar", 102, 130, 95.90));
		allDrinks.add(new Drink("White coffee", 201, 120, 95.90));
		allDrinks.add(new Drink("White coffee With sugar", 202, 130, 95.90));
		allDrinks.add(new Drink("Hot chocolate", 300, 110, 95.90));
		
		// MEDIUM CUP SIZE
		allDrinks.add(new Drink("Medium - Black coffee", 5101, 140, 95.90));
		allDrinks.add(new Drink("Medium - Black coffee With sugar", 5102, 150, 95.90));
		allDrinks.add(new Drink("Medium - White coffee", 5201, 140, 95.90));
		allDrinks.add(new Drink("Medium - White coffee With sugar", 5202, 150, 95.90));
		allDrinks.add(new Drink("Medium - Hot chocolate", 5300, 130, 95.90));
		
		// LARGE CUP SIZE
		allDrinks.add(new Drink("Large - Black coffee", 6101, 145, 95.90));
		allDrinks.add(new Drink("Large - Black coffee With sugar", 6102, 155, 95.90));
		allDrinks.add(new Drink("Large - White coffee", 6201, 145, 95.90));
		allDrinks.add(new Drink("Large - White coffee With sugar", 6202, 155, 95.90));
		allDrinks.add(new Drink("Large - Hot chocolate", 6300, 135, 95.90));
	}
}
