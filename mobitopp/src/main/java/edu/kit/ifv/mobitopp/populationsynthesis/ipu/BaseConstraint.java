package edu.kit.ifv.mobitopp.populationsynthesis.ipu;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public abstract class BaseConstraint implements Constraint {

	public static final double greaterZero = 1e-6;
	private final double requestedWeight;

	public BaseConstraint(double requestedWeight) {
		super();
		this.requestedWeight = ensureGreaterZero(requestedWeight);
	}

	private double ensureGreaterZero(double requestedWeight) {
		if (0.0d >= requestedWeight) {
			System.out.println("RequestedWeight must be greater than zero, but was: " + requestedWeight);
			return greaterZero;
		}
		return requestedWeight;
	}

	@Override
	public List<WeightedHousehold> scaleWeightsOf(List<WeightedHousehold> households) {
		double totalWeight = totalWeight(households);
		double withFactor = requestedWeight / totalWeight;
		return scaleWeightsOf(households, withFactor);
	}

	private double totalWeight(List<WeightedHousehold> households) {
		return households.stream().filter(this::matches).mapToDouble(this::totalWeight).sum();
	}

	private List<WeightedHousehold> scaleWeightsOf(
			List<WeightedHousehold> households, double factor) {
		ArrayList<WeightedHousehold> newHouseholds = new ArrayList<>(notProcessed(households));
		households
				.stream()
				.filter(this::matches)
				.map(h -> h.newWeight(h.weight() * factor))
				.forEach(newHouseholds::add);
		return newHouseholds;
	}

	private List<WeightedHousehold> notProcessed(List<WeightedHousehold> households) {
		Predicate<WeightedHousehold> predicate = this::matches;
		return households.stream().filter(predicate.negate()).collect(toList());
	}

	@Override
	public double calculateGoodnessOfFitFor(List<WeightedHousehold> households) {
		double totalWeight = totalWeight(households);
		return Math.abs(totalWeight - requestedWeight) / requestedWeight;
		}

	protected abstract boolean matches(WeightedHousehold household);

	protected abstract double totalWeight(WeightedHousehold household);

	@Override
	public int hashCode() {
		return Objects.hash(requestedWeight);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseConstraint other = (BaseConstraint) obj;
		return Double.doubleToLongBits(requestedWeight) == Double
				.doubleToLongBits(other.requestedWeight);
	}

	@Override
	public String toString() {
		return "requestedWeight=" + requestedWeight;
	}

}