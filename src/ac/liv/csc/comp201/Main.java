package ac.liv.csc.comp201;

import ac.liv.csc.comp201.model.IMachine;
import ac.liv.csc.comp201.simulate.MachineSimulator;

public class Main {
	
	public static void main(String[] args) {
		IMachine machine=new MachineSimulator();
		MachineController controller=new MachineController();
		machine.start(controller); // start machine and pass control to machine controller
	}
}
