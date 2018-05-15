package edu.kit.ifv.mobitopp.simulation.opportunities;

import edu.kit.ifv.mobitopp.data.Zone;
import edu.kit.ifv.mobitopp.simulation.ActivityType;
import edu.kit.ifv.mobitopp.simulation.Location;

public class Opportunity {

	private final Zone zone;
	private final ActivityType activityType;
	private final Location location;
	private final Integer attractivity;

	public Opportunity(
			Zone zone, ActivityType activityType, Location location, Integer attractivity) {
		super();
		this.zone = zone;
		this.activityType = activityType;
		this.location = location;
		this.attractivity = attractivity;
	}

	public Zone zone() {
		return zone;
	}

	public ActivityType activityType() {
		return activityType;
	}

	public Location location() {
		return location;
	}

	public Integer attractivity() {
		return attractivity;
	}

	@Override
	public String toString() {
		return "Opportunity [zone=" + zone + ", activityType=" + activityType + ", location=" + location
				+ ", attractivity=" + attractivity + "]";
	}

}