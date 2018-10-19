package edu.kit.ifv.mobitopp.util.parameter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.nio.file.Files;

public class ParameterFormularParser {

	private static final String commentPrefix = "//";

	public void parseConfig(File file, Object model) {
		try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
			parseConfig(reader, model);
		} catch (IOException cause) {
			throw new UncheckedIOException(cause);
		}
	}
	
	public void parseConfig(InputStream stream, Object model) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
			parseConfig(reader, model);
		} catch (IOException cause) {
			throw new UncheckedIOException(cause);
		}
	}

	public void parseConfig(BufferedReader reader, Object model) {
		reader.lines().filter(this::lineWithData).map(this::parse).forEach(p -> write(p, model));
	}
	
	private boolean lineWithData(String line) {
		return !(isEmpty(line) || isCommented(line));
	}

	private boolean isEmpty(String line) {
		return line.isEmpty();
	}

	private boolean isCommented(String line) {
		return line.trim().startsWith(commentPrefix);
	}

	private Coefficient parse(String line) {
		String[] fields = line.split("=");
		String name = fields[0].trim();
		String value = fields[1];
		return new Coefficient(name, value);
	}

	private void write(Coefficient coefficient, Object model) {
		try {
			setValue(coefficient, model);
		} catch (NoSuchFieldException | IllegalAccessException cause) {
			throw new IllegalArgumentException(cause);
		}
	}

	private void setValue(Coefficient coefficient, Object model)
			throws NoSuchFieldException, IllegalAccessException {
		Field field = model.getClass().getDeclaredField(coefficient.name);
		field.setAccessible(true);
		if (Integer.class.equals(field.getType())) {
			field.set(model, coefficient.asInt());
		} else if (Double.class.equals(field.getType())){
			field.set(model, coefficient.asDouble());
		} else {
			throw new IllegalArgumentException("Field is not an int neither a double: " + field.getName());
		}
	}

}