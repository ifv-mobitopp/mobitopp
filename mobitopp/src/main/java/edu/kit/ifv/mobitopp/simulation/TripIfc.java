package edu.kit.ifv.mobitopp.simulation;

import edu.kit.ifv.mobitopp.simulation.activityschedule.ActivityIfc;
import edu.kit.ifv.mobitopp.simulation.activityschedule.OccupationIfc;
import edu.kit.ifv.mobitopp.time.Time;
import edu.kit.ifv.mobitopp.simulation.person.FinishedTrip;

public interface TripIfc extends OccupationIfc {

	ZoneAndLocation origin();

	ZoneAndLocation destination();

	Mode mode();

	Time startDate();

	int plannedDuration();

	ActivityIfc previousActivity();

	ActivityIfc nextActivity();

  FinishedTrip finish(Time currentDate);

}