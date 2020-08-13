package edu.kit.ifv.mobitopp.populationsynthesis.ipu;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Stream;

import edu.kit.ifv.mobitopp.data.DemandRegion;
import edu.kit.ifv.mobitopp.data.PanelDataRepository;
import edu.kit.ifv.mobitopp.data.demand.Demography;
import edu.kit.ifv.mobitopp.populationsynthesis.SynthesisContext;

public class SingleLevelIterationFactory extends BaseIterationFactory implements IterationFactory {

	private final PanelDataRepository panelData;

	public SingleLevelIterationFactory(
			final PanelDataRepository panelData, SynthesisContext context) {
		super(context);
		this.panelData = panelData;
	}
	
	@Override
	protected List<Constraint> constraintsFor(final DemandRegion region) {
		return attributesFor(region)
				.map(attribute -> attribute.createConstraint(region.nominalDemography()))
				.collect(toList());
	}

	@Override
	protected Stream<Attribute> attributesFor(final DemandRegion region) {
		List<AttributeType> types = getContext().attributes(region.regionalLevel());
		Demography nominalDemography = region.nominalDemography();
		RegionalContext context = region.getRegionalContext();
		return types.stream().flatMap(type -> type.createAttributes(nominalDemography, context));
	}

	@Override
	public AttributeResolver createAttributeResolverFor(DemandRegion region) {
		List<Attribute> attributes = attributesFor(region).collect(toList());
		return new DefaultAttributeResolver(attributes, panelData);
	}

}