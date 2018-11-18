package ac.liv.csc.comp201.model;

import ac.liv.csc.comp201.simulate.Cup;

public interface IMachine {

	public IDisplay getDisplay();
	
	public IKeyPad getKeyPad();
	
	public IWaterHeater getWaterHeater();
	
	void start(IMachineController controller);		// Start's machine up
	
	/**
	 * This method is called everytime a key is pressed on the key pad
	 */
	void keyPadPressed();
	
	public Cup getCup();
	
	public void vendCup(int cupType);
	
	public IHoppers getHoppers();
	
	public ICoinHandler getCoinHandler();
	
	public void shutMachineDown();
	
	public int getBalance();
	
	public void setBalance(int balance);
	
	
	
}
