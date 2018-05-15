package edu.kit.ifv.mobitopp.data.local.configuration;

import java.io.IOException;

import edu.kit.ifv.mobitopp.data.DataRepositoryForPopulationSynthesis;
import edu.kit.ifv.mobitopp.data.DataRepositoryForSimulation;
import edu.kit.ifv.mobitopp.data.DataSource;
import edu.kit.ifv.mobitopp.data.InputSpecification;
import edu.kit.ifv.mobitopp.data.PanelDataRepository;
import edu.kit.ifv.mobitopp.data.StartDateSpecification;
import edu.kit.ifv.mobitopp.dataimport.StructuralData;
import edu.kit.ifv.mobitopp.network.SimpleRoadNetwork;
import edu.kit.ifv.mobitopp.result.ResultWriter;
import edu.kit.ifv.mobitopp.simulation.ElectricChargingWriter;
import edu.kit.ifv.mobitopp.simulation.PublicTransportData;
import edu.kit.ifv.mobitopp.visum.VisumNetwork;

public class TestSource implements DataSource {

	/**
	 * SnakeYaml needs a single argument constructor for classes without attributes.
	 * @param dummy  
	 */
	public TestSource(String dummy) {
	}

	@Override
	public DataRepositoryForPopulationSynthesis forPopulationSynthesis(
			VisumNetwork visumNetwork, SimpleRoadNetwork roadNetwork, StructuralData demographyData,
			PanelDataRepository panelDataRepository, int numberOfZones, StartDateSpecification input,
			ResultWriter results) throws IOException {
		throw new RuntimeException("dummy implementation");
	}

	@Override
	public DataRepositoryForSimulation forSimulation(
			VisumNetwork visumNetwork, SimpleRoadNetwork roadNetwork, int numberOfZones,
			InputSpecification input, PublicTransportData data, ResultWriter results,
			ElectricChargingWriter electricChargingWriter) throws IOException {
		throw new RuntimeException("dummy implementation");
	}

	@Override
	public void validate() {
		throw new RuntimeException("dummy implementation");
	}

}