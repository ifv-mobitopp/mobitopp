package edu.kit.ifv.mobitopp.simulation.destinationChoice;

import java.util.Map;
import java.io.File;
import java.io.BufferedReader;
import java.util.StringTokenizer;

import edu.kit.ifv.mobitopp.simulation.ActivityType;

import java.util.HashMap;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

class TargetChoiceRepetitionParameter2 {


	private Map<ActivityType, Map<Integer, Map<Integer, Map<Integer,Float>>>> parameter
                  = new HashMap<ActivityType, Map<Integer, Map<Integer, Map<Integer,Float>>>>();



	public TargetChoiceRepetitionParameter2(String filename) {
		parseConfigFile(filename);
	}



	private void parseConfigFile(String fileName)
	{

		File f = new File(fileName);
		if (!f.exists()) {
			System.out.println("FEHLER: Datei nicht da!  Aktuelles Verzeichnis " +
					f.getAbsolutePath());
		}

		try {
			String in = null;
			try (BufferedReader inp = new BufferedReader(new FileReader(f))) {
				while ((in = inp.readLine()) != null) {
					parseParameterData(in);
				}
			} catch (FileNotFoundException e) {
				System.out.print(" Datei wurde nicht gefunden : " + fileName);
			} catch (IOException e) {
				System.out.print(" Datei konnte nicht geöffnet werden :" + e);
			}

			printParameter(this.parameter);

		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(2);
		}

	}

	private void parseParameterData(String line_) {

		if (line_.indexOf("#") >= 0) {
			return;
		}

		StringTokenizer tokens = new StringTokenizer(line_, ";");

		if (tokens.countTokens() < 3) {
			return;
		}

		try {
			int activity = Integer.parseInt(tokens.nextToken().trim());
			int previous = Integer.parseInt(tokens.nextToken().trim());
			int community_type = Integer.parseInt(tokens.nextToken().trim());

			ActivityType activityKey = ActivityType.getTypeFromInt(activity);

			Map<Integer,Float> map = new HashMap<Integer,Float>(5,1.0f);
			int nr = tokens.countTokens();

			for (int i = 0; i < nr; i++) {
				float val = Float.parseFloat(tokens.nextToken().trim());
				map.put(new Integer(i+2),val);
			}

			if (!this.parameter.containsKey(activityKey)) {

				this.parameter.put(activityKey, new HashMap<Integer, Map<Integer, Map<Integer,Float>>>(2,1.0f) );
			}
			if (!this.parameter.get(activityKey).containsKey(previous)) {

				this.parameter.get(activityKey).put(previous, new HashMap<Integer, Map<Integer,Float>>(3,1.0f) );
			}
			this.parameter.get(activityKey).get(previous).put(community_type,map);

		} catch (NumberFormatException nex) {
			System.out.println("Probleme mit der Zeile " + line_ + "  " + nex);
		}
	}

	private void printParameter(
		Map<ActivityType, Map<Integer, Map<Integer, Map<Integer,Float>>>> parameter
	) {

		System.out.println("Targetparameter");

		for (ActivityType activity : parameter.keySet()) {

			System.out.println("Type: " + activity.getTypeAsInt() + " " + activity.getTypeAsString());

			for (Integer before : parameter.get(activity).keySet()) {

				System.out.println("    before: " + before + " " + (before==0 ? "FIXED" : "FLEXIBLE") );

				for (Integer community_type : parameter.get(activity).get(before).keySet() ) {

					System.out.print("Community: " + community_type + "	");

					for (Integer wnr : parameter.get(activity).get(before).get(community_type).keySet() ) {
	
						float val = parameter.get(activity).get(before).get(community_type).get(wnr);

						System.out.print("  " + wnr + ": " + val);
					}
					System.out.println();
				}
			}
		}
	}

	public float getParameter(
		ActivityType attType, 
		Integer previousActivity, 
		Integer wnr,
		Integer community_type
	) {
		// Q&D Hack to make mobitopp work for MOP Data
		if (!this.parameter.containsKey(attType)) {
		
			if (attType == ActivityType.SHOPPING) {
				attType = ActivityType.SHOPPING_DAILY;
			}

			if (attType == ActivityType.LEISURE) {
				attType = ActivityType.LEISURE_INDOOR;
			}
		}

		assert this.parameter.containsKey(attType) : attType;
		assert this.parameter.get(attType).containsKey(previousActivity) : previousActivity;
		assert this.parameter.get(attType).get(previousActivity).containsKey(community_type);

		return this.parameter.get(attType).get(previousActivity).get(community_type).get(Math.min(wnr,6));
	}

}