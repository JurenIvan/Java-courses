package demo;

import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilderProvider;
import hr.fer.zemris.lsystems.gui.LSystemViewer;
import hr.fer.zemris.lsystems.impl.LSystemBuilderImpl;

/**
 * Class that contains main method so it can be run and demonstrates
 * functionalies of project when commands for L-systems are given directy as
 * text so it has to parse it.
 * 
 * @author juren
 *
 */
public class Glavni2 {

	/**
	 * Main method used to start program and demonstrate its functionalities.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		LSystemViewer.showLSystem(createKochCurve2(LSystemBuilderImpl::new));
	}

	/**
	 * Method that define L-system that we want to represent. The thing that
	 * diferenciates this class from Glavni1 class it that this method parses string
	 * 
	 * @return LSystem that is later drawn
	 */
	private static LSystem createKochCurve2(LSystemBuilderProvider provider) {
		String[] data = new String[] { "origin 0.05 0.4", "angle 0", "unitLength 0.9",
				"unitLengthDegreeScaler 1.0 / 3.0", "", "command F draw 1", "command + rotate 60",
				"command - rotate -60", "", "axiom F", "", "production F F+F--F+F" };
		return provider.createLSystemBuilder().configureFromText(data).build();
	}

}
