package edu.kit.ifv.mobitopp.populationsynthesis;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.kit.ifv.mobitopp.data.local.Convert;
import edu.kit.ifv.mobitopp.data.local.FileValidator;

public class Validate implements Validation {

	private WrittenConfiguration configuration;
	private final List<File> files;

	public Validate() {
		super();
		files = new ArrayList<>();
	}

	@Override
	public void now(WrittenConfiguration configuration) {
		this.configuration = configuration;
		validateNumberOfZones();
		validateVisumFile();
		validateCarOwnershipFiles();
		validateCarSharingFiles();
		validateCommuterTicketFile();
		validateDataSource();
		validateDemographyDataFile();
		validatePanelDataFile();
		validateFiles();
	}

	private void validateNumberOfZones() {
		if (1 > configuration.getNumberOfZones()) {
			throw new IllegalArgumentException(
					"There must be at least one zone to process. Specified number of zones: "
							+ configuration.getNumberOfZones());
		}
	}

	private void validateVisumFile() {
		add(configuration.getVisumFile());
	}

	private void add(String path) {
		files.add(Convert.asFile(path));
	}

	private void validateCarOwnershipFiles() {
		CarOwnership carOwnership = configuration.getCarOwnership();
		add(carOwnership.getEngine());
		add(carOwnership.getOwnership());
		add(carOwnership.getSegment());
	}

	private void validateCarSharingFiles() {
		for (String path : configuration.getCarSharing().values()) {
			add(path);
		}
	}

	private void validateCommuterTicketFile() {
		add(configuration.getCommuterTicket());
	}

	private void validateDemographyDataFile() {
		add(configuration.getDemographyData());
	}

	private void validatePanelDataFile() {
		add(configuration.getPanelData());
	}

	private void validateFiles() {
		new FileValidator(files).doExist();
	}

	private void validateDataSource() {
		configuration.getDataSource().validate();
	}
}