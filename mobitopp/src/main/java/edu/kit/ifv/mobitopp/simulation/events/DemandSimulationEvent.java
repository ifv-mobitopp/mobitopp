package edu.kit.ifv.mobitopp.simulation.events;

import java.util.Objects;

import edu.kit.ifv.mobitopp.simulation.PersonResults;
import edu.kit.ifv.mobitopp.simulation.TripIfc;
import edu.kit.ifv.mobitopp.simulation.activityschedule.OccupationIfc;
import edu.kit.ifv.mobitopp.simulation.person.SimulationPerson;
import edu.kit.ifv.mobitopp.time.DateFormat;
import edu.kit.ifv.mobitopp.time.Time;

public class DemandSimulationEvent
		implements DemandSimulationEventIfc, Comparable<DemandSimulationEvent> {
  
	private static final int greaterThanOther = 1;
	private static final int lowerThanOther = -1;
	private static final int equal = 0;

  private final Time simulationDate;
  private final int priority;
  private final SimulationPerson person;
  private final OccupationIfc occupation;

  public DemandSimulationEvent(
			int priority,
			SimulationPerson person,
      OccupationIfc occupation,
      Time date
	) {
  	verify(date);
  	this.simulationDate = date;
  	this.priority=priority;
    this.person = person;
		this.occupation = occupation;
  }

	private void verify(Time date) {
		if (null == date) {
  		throw new IllegalArgumentException("Simulation date is null");
  	}
	}

  public DemandSimulationEvent(
  		SimulationPerson person,
      OccupationIfc occupation,
      Time date
	) { 
		this(99, person, occupation, date);
	}

	public int getPriority() {
		return this.priority;
	}


	public int compareTo(DemandSimulationEvent other) {
		if (getSimulationDate().isBefore(other.getSimulationDate())) {
			return lowerThanOther;
		}

		if (getSimulationDate().isAfter(other.getSimulationDate())) {
			return greaterThanOther;
		}

		if (getPriority() < other.getPriority()) {
			return lowerThanOther;
		}

		if (getPriority() > other.getPriority()) {
			return greaterThanOther;
		}

		if (getPersonOid() < other.getPersonOid()) {
			return lowerThanOther;
		}

		if (getPersonOid() > other.getPersonOid()) {
			return greaterThanOther;
		}

		if (getOccupationOid() < other.getOccupationOid()) {
			return lowerThanOther;
		}

		if (getOccupationOid() > other.getOccupationOid()) {
			return greaterThanOther;
		}

		return equal;
  }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((occupation == null) ? 0 : occupation.hashCode());
		result = prime * result + ((person == null) ? 0 : person.hashCode());
		result = prime * result + priority;
		result = prime * result + ((simulationDate == null) ? 0 : simulationDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DemandSimulationEvent other = (DemandSimulationEvent) obj;
		return Objects.equals(this.simulationDate, other.simulationDate)
				&& Objects.equals(this.priority, other.priority)
				&& Objects.equals(this.person, other.person)
				&& Objects.equals(this.occupation, other.occupation);
	}

  private int getPersonOid()
  {

    return this.person.getOid();
  }

  public SimulationPerson getPerson()
  {
    return this.person;
  }

  private int getOccupationOid()
  {
    return this.occupation.getOid();
  }

  public OccupationIfc getOccupation()
  {
    return this.occupation;
  }

  public Time getSimulationDate()
  {
    return this.simulationDate; 
  }

  @Override
  public void writeRemaining(PersonResults results) {
  }

	public String toString() {
		String date = new DateFormat().asFullDate(getSimulationDate());
		return "[" + priority + ": " + getPersonOid() + "," + getOccupationOid() + ", "
				+ (getOccupation() instanceof TripIfc ? "T" : "A") + ", " + date;
	}

}