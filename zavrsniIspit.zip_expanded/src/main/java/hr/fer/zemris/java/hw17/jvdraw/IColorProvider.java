package hr.fer.zemris.java.hw17.jvdraw;

import java.awt.Color;

/**
 * Object that is Subject in observer design pattern. It offers methods for
 * registration and deregistration of the objects. Every time color changes in
 * this object, it should notify its observers that change occurred.
 * 
 * @author Marko
 *
 */
public interface IColorProvider {

	/**
	 * Method that returns the currently selected color
	 * 
	 * @return currently selected color
	 */
	public Color getCurrentColor();

	/**
	 * Method that registers the listener to this object
	 * 
	 * @param l listener that should be registered
	 */
	public void addColorChangeListener(ColorChangeListener l);

	/**
	 * Method that removes the listener from the list of listeners
	 * 
	 * @param l listener that should be removed
	 */
	public void removeColorChangeListener(ColorChangeListener l);
}
