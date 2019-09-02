package edu.kit.ifv.mobitopp.util.panel;

import java.util.Objects;

import edu.kit.ifv.mobitopp.data.person.HouseholdId;
import edu.kit.ifv.mobitopp.data.person.PersonId;

public class PersonOfPanelDataId implements Comparable<PersonOfPanelDataId> {

	private final HouseholdOfPanelDataId householdId;
  private final byte personNumber;

  public PersonOfPanelDataId(
		HouseholdOfPanelDataId householdId,
		int personNumber)
  {
		assert householdId != null;
		assert personNumber >= 0;


    this.householdId = householdId;
    this.personNumber = (byte) personNumber;
  }

  public HouseholdOfPanelDataId getHouseholdId()
  {
    return this.householdId;
  }

  public int getPersonNumber()
  {
    return this.personNumber;
  }

  public boolean equals(Object obj)
  {

    PersonOfPanelDataId otherObject = 
      (PersonOfPanelDataId) obj;

		return householdId.equals(otherObject.householdId)
				&& personNumber == otherObject.personNumber;
  }

	public int compareTo(PersonOfPanelDataId o) {

		int hh_comparison = householdId.compareTo(o.householdId);

		if (hh_comparison != 0) {
			return hh_comparison;
		}

		return personNumber - o.personNumber;
	}

	public int hashCode() {
		return Objects.hash(householdId.hashCode(), personNumber);
	}
	
	public static PersonOfPanelDataId fromPersonId(PersonId id) {
		return new PersonOfPanelDataId(createPanelId(id.getHouseholdId()), id.getPersonNumber());
	}

	private static HouseholdOfPanelDataId createPanelId(HouseholdId id) {
		return new HouseholdOfPanelDataId(id.getYear(), id.getHouseholdNumber());
	}

}
