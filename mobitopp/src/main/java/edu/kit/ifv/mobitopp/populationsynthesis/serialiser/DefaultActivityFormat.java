package edu.kit.ifv.mobitopp.populationsynthesis.serialiser;

import static java.lang.String.valueOf;
import static java.util.Arrays.asList;

import java.util.List;

import edu.kit.ifv.mobitopp.data.PatternActivity;
import edu.kit.ifv.mobitopp.simulation.ActivityType;
import edu.kit.ifv.mobitopp.time.DayOfWeek;

public class DefaultActivityFormat implements SerialiserFormat<PersonPatternActivity> {

	private static final int personIdIndex = 0;
	private static final int activityTypeIndex = 1;
	private static final int dayOfWeekIndex = 2;
	private static final int tripDurationIndex = 3;
	private static final int startTimeIndex = 4;
	private static final int durationIndex = 5;

	@Override
	public List<String> header() {
		return asList("personId", "activityType", "weekDayType", "observedTripDuration", "starttime",
				"duration");
	}
	
	@Override
	public List<String> prepare(PersonPatternActivity activity) {
		int personOid = activity.personOid();
		int activityType = activity.activityTypeAsInt();
		int duration = activity.duration();
		int observedTripDuration = activity.observedTripDuration();
		int starttime = activity.starttime();
		DayOfWeek weekDayType = activity.weekDayType();
		return asList( 
				valueOf(personOid), 
				valueOf(activityType),
				valueOf(weekDayType), 
				valueOf(observedTripDuration), 
				valueOf(starttime),
				valueOf(duration) 
			);
	}

	@Override
	public PersonPatternActivity parse(List<String> data) {
		int personOid = personOidOf(data);
		int activityType = activityTypeOf(data);
		DayOfWeek weekDayType = dayOfWeekOf(data);
		int observedTripDuration = tripDurationOf(data);
		int startTime = startTimeOf(data);
		int duration = durationOf(data);
		
		PatternActivity patternActivity = new PatternActivity(ActivityType.getTypeFromInt(activityType),
				weekDayType, observedTripDuration, startTime, duration);
		return new PersonPatternActivity(personOid, patternActivity);
	}
	
	private int personOidOf(List<String> data) {
		return Integer.parseInt(data.get(personIdIndex));
	}

	private int activityTypeOf(List<String> data) {
		return Integer.parseInt(data.get(activityTypeIndex));
	}

	private DayOfWeek dayOfWeekOf(List<String> data) {
		return DayOfWeek.valueOf(data.get(dayOfWeekIndex));
	}

	private int tripDurationOf(List<String> data) {
		return Integer.parseInt(data.get(tripDurationIndex));
	}

	private int startTimeOf(List<String> data) {
		return Integer.parseInt(data.get(startTimeIndex));
	}

	private int durationOf(List<String> data) {
		return Integer.parseInt(data.get(durationIndex));
	}

}