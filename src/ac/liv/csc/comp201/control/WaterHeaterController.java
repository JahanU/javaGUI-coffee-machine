package ac.liv.csc.comp201.control;

import ac.liv.csc.comp201.model.IMachine;
import ac.liv.csc.comp201.simulate.WaterHeater;
import java.util.concurrent.TimeUnit;

public class WaterHeaterController {
	
	private IMachine machine;
	private float oldTemp;
	private final int EFFICIENT_TEMP = 76;

	Drinks drink;
		
	public WaterHeaterController(IMachine machine) {
		drink = new Drinks(machine);
		this.machine = machine;
		oldTemp = 0;
	
	}
	
/*
 * The machine should not leave the hot water at boiling point 100 C for any length of time,
 *  as this is not safe.
 *	If the water reaches boiling point the machine should be shutdown. 
 */
	public void boilingPoint() {
		if (machine.getWaterHeater().getTemperatureDegreesC() >= 100) {
			System.out.println("Shutting machine down! boilling point reached");
			machine.shutMachineDown();
		}
	}	
	
//	If you cannot control the water heater 
//	(for example if it increases in heat when you have switched it off),
//	you need to shutdown the machine and display an error message.
	public void machineError() {
		
		float newTemp = machine.getWaterHeater().getTemperatureDegreesC();
		//float diff = newTemp - oldTemp;
		// If heater is off and the temp is going up
		if (machine.getWaterHeater().getHeaterOnStatus() == false && newTemp > oldTemp) {
			System.out.println("prev: " + oldTemp + " new: " + newTemp);
			System.out.println("Shutting machine down! Temp is going up but machine heater is off!");
			machine.shutMachineDown();
		}
		oldTemp = newTemp;
	}
	

	public boolean saveElectricity() { // maintain heat around 75 C
		
		if (machine.getWaterHeater().getTemperatureDegreesC() >= EFFICIENT_TEMP && drink.getIdle())  // if above 76, turn heater off
			machine.getWaterHeater().setHeaterOff();
		 if (machine.getWaterHeater().getTemperatureDegreesC() < EFFICIENT_TEMP && drink.getIdle()) // if below 76, turn heater on
			machine.getWaterHeater().setHeaterOn(); // set the heater on, until it reaches 75 C
		
	return false;
	}


}
