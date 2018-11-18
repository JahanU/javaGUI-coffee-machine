package ac.liv.csc.comp201.model;

import ac.liv.csc.comp201.simulate.Cup;

public interface IHoppers {

	void setHopperOn(int hopperIndex);

	void setHopperOff(int hopperIndex);
	
	void updateHoppers(Cup cup);
	
	double  getHopperLevelsGrams(int hopperIndex);

}