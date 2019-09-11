package hr.fer.zemris.java.hw17.jvdraw;

import java.awt.Color;

/**
 * Interface modeling a listener in observer design pattern. Listener is
 * notified every time {@link IColorProvider} object changes its color.
 * 
 * @author Marko
 *
 */
public interface ColorChangeListener {

	/**
	 * Method that is called when the color of {@link IColorProvider} changes.
	 * 
	 * @param source   object that changed its color
	 * @param oldColor old color
	 * @param newColor new color
	 */
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor);
}