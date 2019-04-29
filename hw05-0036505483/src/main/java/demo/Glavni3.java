package demo;

import hr.fer.zemris.lsystems.gui.LSystemViewer;
import hr.fer.zemris.lsystems.impl.LSystemBuilderImpl;

/**
 * Class that contains main method so it can be run and demonstrates
 * functionalities of project when commands for L-systems are given directly as
 * method calls.
 * 
 * @author juren
 *
 */
public class Glavni3 {
	/**
	 * Main method used to start program and demonstrate its functionalities. Enables
	 * us to load file with predefined L-system, parse it and graphically represent
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		LSystemViewer.showLSystem(LSystemBuilderImpl::new);
	}
}
