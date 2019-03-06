package edu.kit.ifv.mobitopp.simulation.person;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import edu.kit.ifv.mobitopp.simulation.Mode;
import edu.kit.ifv.mobitopp.simulation.TripData;
import edu.kit.ifv.mobitopp.simulation.Trip;

public class ModeToTrip {

  private final Map<Mode, BiFunction<TripData, SimulationPerson, Trip>> modeToTrip;
  private final BiFunction<TripData, SimulationPerson, Trip> defaultTrip;

  public ModeToTrip(BiFunction<TripData, SimulationPerson, Trip> defaultTrip) {
    super();
    this.defaultTrip = defaultTrip;
    modeToTrip = new HashMap<>();
  }
  
  public static ModeToTrip createDefault() {
    BiFunction<TripData, SimulationPerson, Trip> defaultTrip = NoActionTrip::new;
    ModeToTrip modeToTrip = new ModeToTrip(defaultTrip);
    modeToTrip.add(Mode.CAR, PrivateCarTrip::new);
    modeToTrip.add(Mode.CARSHARING_STATION, CarSharingStationTrip::new);
    modeToTrip.add(Mode.CARSHARING_FREE, CarSharingFreeFloatingTrip::new);
    modeToTrip.add(Mode.PASSENGER, PassengerTrip::new);
    return modeToTrip;
  }
  
  public void add(Mode passenger, BiFunction<TripData, SimulationPerson, Trip> function) {
    modeToTrip.put(passenger, function);
  }

  private BiFunction<TripData, SimulationPerson, Trip> get(Mode mode) {
    return modeToTrip.getOrDefault(mode, defaultTrip);
  }

  public Trip create(Mode mode, TripData tripData, SimulationPerson person) {
    return get(mode).apply(tripData, person);
  }
}