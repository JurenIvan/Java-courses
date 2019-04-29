package demo;

import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilderProvider;
import hr.fer.zemris.lsystems.gui.LSystemViewer;
import hr.fer.zemris.lsystems.impl.LSystemBuilderImpl;

/**
 * Class that contains main method so it can be run and demonstrates
 * functionalities of project when commands for L-systems are given directy as
 * method calls.
 * 
 * @author juren
 *
 */
public class Glavni1 {

	/**
	 * Main method used to start program and demonstrate its functionalities.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		LSystemViewer.showLSystem(createKochCurve(LSystemBuilderImpl::new));
	}

	/**
	 * Method that define L-system that we want to represent
	 * 
	 * @return LSystem that is drawn
	 */
	private static LSystem createKochCurve(LSystemBuilderProvider provider) {
		return provider.createLSystemBuilder()
				.registerCommand('F', "draw 1")
				.registerCommand('+', "rotate 60")
				.registerCommand('-', "rotate -60")
				.setOrigin(0.05, 0.4)
				.setAngle(720)
				.setUnitLength(0.9)
				.setUnitLengthDegreeScaler(1.0 / 3.0)
				.registerProduction('F', "F+F-4-F+F")
				.setAxiom("F").build();
	}
}
