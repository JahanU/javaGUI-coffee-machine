package ac.liv.csc.comp201.model;

public interface ICoinHandler {

	/**
	 * Returns the next code code from the buffer
	 * Returns empty string if buffer is empty
	 * @return coin code
	 */
	String getCoinKeyCode();

	/*
	 * Returns the coins in the coin tray that can be 
	 * picked by the customer
	 */
	String getCoinTray();
	
	
	/*
	 * Emptry out the coin tray
	 */
	void clearCoinTry();
		
	
	/**
	 * Returns true if the coin is available
	 * @param coinCode   Code for coin
	 * @return  True if can dispense this code
	 */
	boolean coinAvailable(String coinCode);

	/**
	 * Set's hopper level for this coin
	 * @param coinCode   Code for coin
	 * @param level  Level for hopper
	 */
	void setHopperLevel(String coinCode, int level);

	/**
	 * Returns level for this coinf
	 * @param coinCode  String containing coin code
	 * @return  Level as number of coins
	 */
	int getCoinHopperLevel(String coinCode);

	/*
	 * Sends 1 coin of this type into the coin tray
	 */
	boolean dispenseCoin(String coinCode);


	/**
	 * Stop's customers entering coins (locks the machine coin handler)
	 */
	void lockCoinHandler();

	/**
	 * Allow's customers to enter coins
	 */
	void unlockCoinHandler();	
	
	
	/**
	 * Close the simulator down
	 */
	void close();

}