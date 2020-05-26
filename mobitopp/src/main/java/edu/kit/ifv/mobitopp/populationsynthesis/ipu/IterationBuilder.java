package edu.kit.ifv.mobitopp.populationsynthesis.ipu;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Stream;

import edu.kit.ifv.mobitopp.data.DemandRegion;
import edu.kit.ifv.mobitopp.data.PanelDataRepository;
import edu.kit.ifv.mobitopp.data.demand.Demography;

public class IterationBuilder {

	private final PanelDataRepository panelDataRepository;
	private final List<AttributeType> types;

	public IterationBuilder(
			final PanelDataRepository panelDataRepository, final List<AttributeType> types) {
		super();
		this.panelDataRepository = panelDataRepository;
		this.types = types;
	}
	
	public Iteration buildFor(final DemandRegion region) {
		return new IpuIteration(constraintsFor(region));
	}

	List<Constraint> constraintsFor(final DemandRegion region) {
		return attributesFor(region)
				.map(attribute -> attribute.createConstraint(region.nominalDemography()))
				.collect(toList());
	}

	private Stream<Attribute> attributesFor(final DemandRegion region) {
		Demography nominalDemography = region.nominalDemography();
		RegionalContext context = region.getRegionalContext();
		return types.stream().flatMap(type -> type.createAttributes(nominalDemography, context));
	}

	public AttributeResolver createAttributeResolverFor(final DemandRegion region) {
		List<Attribute> attributes = attributesFor(region).collect(toList());
		return new DefaultAttributeResolver(attributes, panelDataRepository);
	}

}