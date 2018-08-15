package edu.kit.ifv.mobitopp.populationsynthesis;

import static com.github.npathai.hamcrestopt.OptionalMatchers.hasValue;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ifv.mobitopp.data.Zone;
import edu.kit.ifv.mobitopp.data.ZoneRepository;
import edu.kit.ifv.mobitopp.populationsynthesis.serialiser.PersonFixedDestination;
import edu.kit.ifv.mobitopp.populationsynthesis.serialiser.PopulationContext;
import edu.kit.ifv.mobitopp.simulation.ActivityType;
import edu.kit.ifv.mobitopp.simulation.FixedDestination;
import edu.kit.ifv.mobitopp.simulation.Location;
import edu.kit.ifv.mobitopp.simulation.LocationParser;
import edu.kit.ifv.mobitopp.simulation.Person;

public class DefaultFixedDestinationFormatTest {

	private static final Location location = Example.location;
	private static final ActivityType activityType = ActivityType.HOME;
	private static final int zoneOid = 1;
	private static final int personOid = 2;

	private ZoneRepository zoneRepository;
	private PopulationContext context;
	private FixedDestination fixedDestination;
	private Zone zone;
	private PersonFixedDestination personDestination;
	private Person person;
	private DefaultFixedDestinationFormat format;

	@Before
	public void initialise() {
		zoneRepository = mock(ZoneRepository.class);
		context = mock(PopulationContext.class);
		zone = mock(Zone.class);
		person = mock(Person.class);
		when(person.getOid()).thenReturn(personOid);
		when(zone.getOid()).thenReturn(zoneOid);
		fixedDestination = new FixedDestination(activityType, zone, location);
		personDestination = new PersonFixedDestination(person , fixedDestination);
		format = new DefaultFixedDestinationFormat(zoneRepository);
	}

	@Test
	public void prepareFixedDestination() {
		List<String> prepared = format.prepare(personDestination, context);

		assertThat(prepared, is(equalTo(fixedDestination())));
	}
	
	@Test
	public void parse() {
		prepareExistingZone();
		prepareExsistingPerson();
		
		Optional<PersonFixedDestination> parsed = format.parse(fixedDestination(), context);
		
		assertThat(parsed, hasValue(personDestination));
	}
	
	private void prepareExsistingPerson() {
		when(context.getPersonByOid(personOid)).thenReturn(Optional.of(person));
	}

	private void prepareExistingZone() {
		when(zoneRepository.hasZone(zoneOid)).thenReturn(true);
		when(zoneRepository.getZoneByOid(zoneOid)).thenReturn(zone);
	}
	
	@Test
	public void parseMissingZone() {
		prepareMissingZone();
		
		Optional<PersonFixedDestination> parsed = format.parse(fixedDestination(), context);
		
		assertThat(parsed, isEmpty());
	}

	private void prepareMissingZone() {
		when(zoneRepository.hasZone(zoneOid)).thenReturn(false);
	}

	private List<String> fixedDestination() {
		String serialisedLocation = new LocationParser().serialise(location);
		return asList(
				valueOf(personOid),
				valueOf(activityType),
				valueOf(zoneOid),
				valueOf(serialisedLocation)
		);
	}
}
