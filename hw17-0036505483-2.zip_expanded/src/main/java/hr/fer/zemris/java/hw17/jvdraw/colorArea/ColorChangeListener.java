package hr.fer.zemris.java.hw17.jvdraw.colorArea;

import java.awt.Color;

/**
 * Interface that models color change listener
 * 
 * @author juren
 *
 */
@FunctionalInterface
public interface ColorChangeListener {
	/**
	 * Method that is called when notification needs to be delivered to multiple
	 * places.
	 * 
	 * @param source of change
	 * @param oldColor old color
	 * @param newColor new colorF
	 */
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor);
}