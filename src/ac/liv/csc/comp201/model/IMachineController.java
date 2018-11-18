package ac.liv.csc.comp201.model;

public interface IMachineController {
	public void startController(IMachine machine);
	
	// This method allows the hardware to push
	// events to the controller
	public void updateController();

	public void stopController();
	

	
	
}
