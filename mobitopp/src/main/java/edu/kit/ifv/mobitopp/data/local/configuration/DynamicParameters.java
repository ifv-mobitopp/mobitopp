package edu.kit.ifv.mobitopp.data.local.configuration;

import java.io.File;
import java.util.Collections;
import java.util.Map;

import edu.kit.ifv.mobitopp.data.local.Convert;

public class DynamicParameters {

	private final Map<String, String> parameters;

	public DynamicParameters(Map<String, String> parameters) {
		super();
		this.parameters = parameters;
	}
	
	public int valueAsInteger(String parameter) {
		return Integer.parseInt(value(parameter));
	}

	public String value(String parameter) {
		verifyExisting(parameter);
		return parameters.get(parameter).toString();
	}

	private void verifyExisting(String parameter) {
		if (!hasValue(parameter)) {
			throw new IllegalArgumentException("Dynamic parameter missing: " + parameter + " containing: " + parameters.toString());
		}
	}

  public boolean hasValue(String parameter) {
    return parameters.containsKey(parameter);
  }

	public double valueAsDouble(String parameter) {
		return Double.parseDouble(value(parameter));
	}

	public boolean valueAsBoolean(String parameter) {
		return Boolean.parseBoolean(value(parameter));
	}
	
	public File valueAsFile(String parameter) {
		return Convert.asFile(value(parameter));
	}
	
	public Map<String, String> toMap() {
	  return Collections.unmodifiableMap(parameters);
	}

	@Override
	public String toString() {
		return getClass().getName() + " [parameters=" + parameters + "]";
	}

}
