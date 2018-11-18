package ac.liv.csc.comp201.model;

public interface IKeyPad {
	
	/**
	 * Returns the next key code
	 * @return  0-9 if key code available -1 if buffer empty
	 */
	public int getNextKeyCode();
	
	public void setCaption(int buttonIndex,String caption);

}
