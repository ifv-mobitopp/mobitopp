package edu.kit.ifv.mobitopp.populationsynthesis;

import java.io.File;
import java.io.PrintStream;
import java.util.Map;

import edu.kit.ifv.mobitopp.data.DataRepositoryForPopulationSynthesis;
import edu.kit.ifv.mobitopp.data.DemandZoneRepository;
import edu.kit.ifv.mobitopp.data.local.configuration.DynamicParameters;
import edu.kit.ifv.mobitopp.network.SimpleRoadNetwork;
import edu.kit.ifv.mobitopp.result.ResultWriter;
import edu.kit.ifv.mobitopp.simulation.ImpedanceIfc;
import edu.kit.ifv.mobitopp.simulation.carsharing.CarSharingCustomerModel;
import edu.kit.ifv.mobitopp.visum.VisumNetwork;

public interface SynthesisContext {

	WrittenConfiguration configuration();

	DynamicParameters experimentalParameters();

	long seed();

	File carEngineFile();

	VisumNetwork network();

	SimpleRoadNetwork roadNetwork();

	DemandZoneRepository zoneRepository();

	DataRepositoryForPopulationSynthesis dataRepository();

	ImpedanceIfc impedance();

	Map<String, CarSharingCustomerModel> carSharing();

	ActivityScheduleCreator activityScheduleCreator();

	ResultWriter resultWriter();

	void printStartupInformationOn(PrintStream out);

}