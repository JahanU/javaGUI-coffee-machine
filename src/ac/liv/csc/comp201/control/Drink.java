package ac.liv.csc.comp201.control;

import java.util.ArrayList;
import java.util.*;

import ac.liv.csc.comp201.model.IMachine;

/***
 * @author Jahan
 * ID: 201272455
 */

public class Drink {
	
	//Stores all drinks
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
		allDrinks.add(new Drink("Small - Black coffee", 101, 120, 95.90));
		allDrinks.add(new Drink("Small - Black coffee With sugar", 102, 130, 95.90));
		allDrinks.add(new Drink("Small - White coffee", 201, 120, 95.90));
		allDrinks.add(new Drink("Small - White coffee With sugar", 202, 130, 95.90));
		allDrinks.add(new Drink("Small - Hot chocolate", 300, 110, 90));
		
		// MEDIUM CUP SIZE
		allDrinks.add(new Drink("Medium - Black coffee", 5101, 140, 95.90));
		allDrinks.add(new Drink("Medium - Black coffee With sugar", 5102, 150, 95.90));
		allDrinks.add(new Drink("Medium - White coffee", 5201, 140, 95.90));
		allDrinks.add(new Drink("Medium - White coffee With sugar", 5202, 150, 95.90));
		allDrinks.add(new Drink("Medium - Hot chocolate", 5300, 130, 90));
		
		// LARGE CUP SIZE
		allDrinks.add(new Drink("Large - Black coffee", 6101, 145, 95.90));
		allDrinks.add(new Drink("Large - Black coffee With sugar", 6102, 155, 95.90));
		allDrinks.add(new Drink("Large - White coffee", 6201, 145, 95.90));
		allDrinks.add(new Drink("Large - White coffee With sugar", 6202, 155, 95.90));
		allDrinks.add(new Drink("Large - Hot chocolate", 6300, 135, 90));
	}
}
