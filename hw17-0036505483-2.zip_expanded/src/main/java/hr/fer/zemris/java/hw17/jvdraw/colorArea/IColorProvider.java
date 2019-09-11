package hr.fer.zemris.java.hw17.jvdraw.colorArea;

import java.awt.Color;

/**
 * Interface that is describing current color provider
 * 
 * @author juren
 *
 */
public interface IColorProvider {
	/**
	 * Returns current color
	 * 
	 * @return current color
	 */
	public Color getCurrentColor();

	/**
	 * Method for registration of listeners triggered when color is changed
	 * 
	 * @param l source of order
	 */
	public void addColorChangeListener(ColorChangeListener l);

	/**
	 * Method for removing the listener
	 * 
	 * @param l listener that will be removed
	 */
	public void removeColorChangeListener(ColorChangeListener l);
}
