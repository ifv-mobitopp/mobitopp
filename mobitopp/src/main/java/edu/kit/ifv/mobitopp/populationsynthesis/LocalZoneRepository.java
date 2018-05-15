package edu.kit.ifv.mobitopp.populationsynthesis;

import static java.util.Comparator.comparing;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import edu.kit.ifv.mobitopp.data.Zone;
import edu.kit.ifv.mobitopp.data.ZoneRepository;
import edu.kit.ifv.mobitopp.data.local.ChargingType;
import edu.kit.ifv.mobitopp.dataimport.DefaultPower;
import edu.kit.ifv.mobitopp.dataimport.ZonesReaderCsvBased;
import edu.kit.ifv.mobitopp.network.SimpleRoadNetwork;
import edu.kit.ifv.mobitopp.visum.VisumNetwork;

public class LocalZoneRepository implements ZoneRepository {

	private final Map<Integer, Zone> zones;
	private final List<Zone> zonesAsList;

	LocalZoneRepository(Map<Integer, Zone> zones) {
		super();
		this.zones = zones;
		zonesAsList = asList(zones);
	}

	private static List<Zone> asList(Map<Integer, Zone> zones) {
		List<Zone> sorted = new ArrayList<>(zones.values());
		sorted.sort(comparing(Zone::getOid));
		return Collections.unmodifiableList(sorted);
	}

	@Override
	public Zone getZoneByOid(int oid) throws NoSuchElementException {
		if (zones.containsKey(oid)) {
			return zones.get(oid);
		}
		throw new IllegalArgumentException("No zone available for oid: " + oid);
	}

	@Override
	public List<Zone> getZones() {
		return zonesAsList;
	}

	@Override
	public Map<Integer, Zone> zones() {
		return Collections.unmodifiableMap(zones);
	}

	public static ZoneRepository from(
			VisumNetwork visumNetwork, SimpleRoadNetwork roadNetwork, int numberOfZones,
			ChargingType charging, DefaultPower defaultPower, File attractivityDataFile) {
		ZonesReaderCsvBased zonesReader = ZonesReaderCsvBased.from(visumNetwork, roadNetwork, charging,
				defaultPower, attractivityDataFile);
		Map<Integer, Zone> mapping = new LocalZoneLoader(zonesReader).mapZones(numberOfZones);
		return new LocalZoneRepository(mapping);
	}

}