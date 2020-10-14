package edu.kit.ifv.mobitopp.populationsynthesis.ipu;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import edu.kit.ifv.mobitopp.data.PanelDataRepository;
import edu.kit.ifv.mobitopp.data.areatype.AreaType;
import edu.kit.ifv.mobitopp.util.panel.HouseholdOfPanelData;
import edu.kit.ifv.mobitopp.util.panel.HouseholdOfPanelDataId;

public class TransferHouseholds {

  private static final int maxDepth = 10;
  static final double defaultWeight = 1.0d;
  private final PanelDataRepository panelDataRepository;
  private final List<Attribute> householdAttributes;
	private final RegionalContext context;

  public TransferHouseholds(
      final PanelDataRepository panelDataRepository, final List<Attribute> householdAttributes,
      final RegionalContext context) {
    super();
    this.panelDataRepository = panelDataRepository;
    this.householdAttributes = householdAttributes;
    this.context = context;
  }

  public List<WeightedHousehold> forAreaType(AreaType areaType) {
    List<HouseholdOfPanelData> households = panelDataRepository.getHouseholds();
    List<WeightedHousehold> weightedHouseholds = emptyList();
    for (int level = 0; isIncomplete(weightedHouseholds) && level < maxDepth; level++) {
      Predicate<HouseholdOfPanelData> filter = createFilter(areaType, level);
      weightedHouseholds = createHouseholds(households, filter);
    }
    return weightedHouseholds;
  }

  private boolean isIncomplete(List<WeightedHousehold> weightedHouseholds) {
    boolean householdMatch = householdAttributes
        .stream()
        .allMatch(a -> weightedHouseholds.stream().anyMatch(h -> isAvailable(h, a)));
    return weightedHouseholds.isEmpty() || !householdMatch;
  }

  private boolean isAvailable(WeightedHousehold household, Attribute attribute) {
    return 0 < attribute.valueFor(household.household(), panelDataRepository);
  }

  private List<WeightedHousehold> createHouseholds(
      List<HouseholdOfPanelData> households, Predicate<HouseholdOfPanelData> filter) {
    return households.stream().filter(filter).map(this::toWeightedHousehold).collect(toList());
  }

  private Predicate<HouseholdOfPanelData> createFilter(AreaType areaType, int level) {
    int filterLevel = Math.toIntExact(Math.round(Math.pow(10, level)));
    return household -> areaType.getTypeAsInt() / filterLevel == household.areaTypeAsInt()
        / filterLevel;
  }

  private WeightedHousehold toWeightedHousehold(HouseholdOfPanelData household) {
    HouseholdOfPanelDataId id = household.id();
    float weight = household.getWeight();
    Map<String, Integer> attributes = Map.of();
    return new WeightedHousehold(id, weight, attributes, context, household);
  }

}
