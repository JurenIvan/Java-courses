package hr.fer.zemris.java.hw17.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.jvdraw.JDrawingCanvas;

/**
 * Interface representing a state in State design pattern. Each implementation
 * of this interface can be used by the {@link JDrawingCanvas} for drawing of
 * the different shapes, based on the implementation of the interface. Tools can
 * draw the content using the {@link #paint(Graphics2D)} method.
 * 
 * @author Marko
 *
 */
public interface Tool {

	/**
	 * Method that should be called when the mouse is pressed in the object that is
	 * using the tool (context in the state design pattern).
	 * 
	 * @param e mouse event
	 */
	public void mousePressed(MouseEvent e);

	/**
	 * Method that should be called when the mouse is released in the object that is
	 * using the tool (context in the state design pattern).
	 * 
	 * @param e mouse event
	 */
	public void mouseReleased(MouseEvent e);

	/**
	 * Method that should be called when the mouse is clicked in the object that is
	 * using the tool (context in the state design pattern).
	 * 
	 * @param e mouse event
	 */
	public void mouseClicked(MouseEvent e);

	/**
	 * Method that should be called when the mouse is moved in the object that is
	 * using the tool (context in the state design pattern).
	 * 
	 * @param e mouse event
	 */
	public void mouseMoved(MouseEvent e);

	/**
	 * Method that should be called when the mouse is dragged in the object that is
	 * using the tool (context in the state design pattern).
	 * 
	 * @param e mouse event
	 */
	public void mouseDragged(MouseEvent e);

	/**
	 * Method that allows the tool to draw the content the to graphics if it has
	 * something to draw.
	 * 
	 * @param g2d graphics used for drawing
	 */
	public void paint(Graphics2D g2d);
}