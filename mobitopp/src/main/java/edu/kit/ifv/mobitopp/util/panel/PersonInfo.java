package edu.kit.ifv.mobitopp.util.panel;

class PersonInfo {

	public int pole;
	public float weight;
	public int person_number;
	public int sex;
	public int birth_year;
	public int employment_type;
	public int pole_distance;
	public int income = -1;
	public boolean commutation_ticket;
	//modetypeweights
	public boolean fahrrad;
	public boolean apkwverf;
	public boolean ppkwverf;
	public boolean relvmselbst;
	public boolean relvmoev;

	public String toString() {

		return getClass().getName()
					+ ": "
					+ pole + ", "
					+ weight + ", "
					+ person_number + ", "
					+ sex + ", "
					+ birth_year + ", "
					+ employment_type + ", "
					+ pole_distance + ", "
					+ commutation_ticket + ", "
					+ fahrrad + ", "
					+ apkwverf + ", "
					+ ppkwverf + ", "
					+ relvmselbst + ", "
					+ relvmoev + ", "
				;
	}
}