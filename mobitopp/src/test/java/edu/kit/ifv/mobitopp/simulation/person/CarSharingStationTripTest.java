package edu.kit.ifv.mobitopp.simulation.person;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.kit.ifv.mobitopp.data.Zone;
import edu.kit.ifv.mobitopp.simulation.ActivityType;
import edu.kit.ifv.mobitopp.simulation.ImpedanceIfc;
import edu.kit.ifv.mobitopp.simulation.Location;
import edu.kit.ifv.mobitopp.simulation.PersonResults;
import edu.kit.ifv.mobitopp.simulation.TripIfc;
import edu.kit.ifv.mobitopp.simulation.activityschedule.ActivityIfc;
import edu.kit.ifv.mobitopp.simulation.carsharing.CarSharingCar;
import edu.kit.ifv.mobitopp.simulation.carsharing.CarSharingDataForZone;
import edu.kit.ifv.mobitopp.time.Time;

public class CarSharingStationTripTest {

  private TripSetup setup;
  private ImpedanceIfc impedance;
  private SimulationPerson person;
  private Time currentTime;
  private TripIfc trip;
  private Zone zone;
  private Location location;
  private PersonResults results;
  private CarSharingCar car;
  private CarSharingDataForZone carSharingData;

  @BeforeEach
  public void initialise() {
    setup = TripSetup.create();
    impedance = setup.impedance;
    person = setup.person;
    trip = setup.trip;
    zone = setup.zone;
    location = setup.location;
    currentTime = setup.currentTime;
    results = setup.results;
    car = mock(CarSharingCar.class);
    carSharingData = mock(CarSharingDataForZone.class);
    zone.setCarSharing(carSharingData);
    when(car.id()).thenReturn(0);
  }

  @Test
  void allocateVehicle() throws Exception {
    CarSharingCar carSharingCar = mock(CarSharingCar.class);
    setup.configureActivity(ActivityType.HOME);
    when(carSharingData.isStationBasedCarSharingCarAvailable(person)).thenReturn(true);
    when(carSharingData.bookStationBasedCar(person)).thenReturn(carSharingCar);
    CarSharingStationTrip carSharingTrip = new CarSharingStationTrip(trip, person);

    carSharingTrip.allocateVehicle(impedance, currentTime);

    verify(person).useCar(carSharingCar, currentTime);
    verify(carSharingData).bookStationBasedCar(person);
  }

  @Test
  void parkCarAtWork() throws Exception {
    setup.configureActivity(ActivityType.HOME);
    ActivityIfc nextActivity = setup.createActivity(ActivityType.WORK);
    when(trip.nextActivity()).thenReturn(nextActivity);
    when(person.parkCar(zone, location, currentTime)).thenReturn(car);

    TripIfc privateCarTrip = new CarSharingStationTrip(trip, person);

    FinishedTrip finishedTrip = privateCarTrip.finish(currentTime, results);

    assertThat(finishedTrip.vehicleId(), isEmpty());
    verify(person).parkCar(zone, location, currentTime);
  }

  @Test
  void returnCarAtHome() throws Exception {
    setup.configureActivity(ActivityType.WORK);
    ActivityIfc nextActivity = setup.createActivity(ActivityType.HOME);
    when(trip.nextActivity()).thenReturn(nextActivity);
    when(person.releaseCar(currentTime)).thenReturn(car);

    TripIfc privateCarTrip = new CarSharingStationTrip(trip, person);

    FinishedTrip finishedTrip = privateCarTrip.finish(currentTime, results);

    assertThat(finishedTrip.vehicleId(), isEmpty());
    verify(person).releaseCar(currentTime);
    verify(car).returnCar(zone);
  }
}
