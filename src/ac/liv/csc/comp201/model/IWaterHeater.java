package ac.liv.csc.comp201.model;

import ac.liv.csc.comp201.simulate.Cup;

public interface IWaterHeater {
	
	float getTemperatureDegreesC();
	
	boolean setHeaterOn();
	
	void setHeaterOff();
	
	boolean getHeaterOnStatus();
	
	void setHotWaterTap(boolean open);
	
	void setColdWaterTap(boolean open);
	
	// For simulation this must be called 10 x / second
	void updateHeater(Cup cup);		// read water temperature
	

}
