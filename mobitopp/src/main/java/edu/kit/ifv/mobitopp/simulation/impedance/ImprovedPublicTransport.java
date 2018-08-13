package edu.kit.ifv.mobitopp.simulation.impedance;

import edu.kit.ifv.mobitopp.simulation.ImpedanceIfc;
import edu.kit.ifv.mobitopp.simulation.Mode;
import edu.kit.ifv.mobitopp.time.Time;

public class ImprovedPublicTransport extends ImpedanceDecorator {
	
	private final float travelTimeFactor;

	public ImprovedPublicTransport(ImpedanceIfc impedance, float travelTimeFactor) {
		super(impedance);
		this.travelTimeFactor = travelTimeFactor;
	}
	
	
	public float getTravelTime(int source, int target, Mode mode, Time date) {
		
		if(mode == Mode.PUBLICTRANSPORT) {
			return travelTimeFactor * super.getTravelTime(source, target, mode, date);
		}
		
		return super.getTravelTime(source, target, mode, date);
	}
	

}
