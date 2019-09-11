package hr.fer.zemris.java.hw17.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * Interface that models a tool that can be applied to JCanvas. Basically it
 * gives definition to MouseActions and has paint method that should know how to
 * draw something.
 * 
 * @author juren
 *
 */
public interface Tool {
	/**
	 * Method that describes procedure that happens after mousePreesed event
	 * 
	 * @param e Mouse event that occurred
	 */
	public void mousePressed(MouseEvent e);

	/**
	 * Method that describes procedure that happens after mouseReleased event
	 * 
	 * @param e Mouse event that occurred
	 */
	public void mouseReleased(MouseEvent e);

	/**
	 * Method that describes procedure that happens after mouseClicked event
	 * 
	 * @param e Mouse event that occurred
	 */
	public void mouseClicked(MouseEvent e);

	/**
	 * Method that describes procedure that happens after mouseMoved event
	 * 
	 * @param e Mouse event that occurred
	 */
	public void mouseMoved(MouseEvent e);

	/**
	 * Method that describes procedure that happens after mouseDragged event
	 * 
	 * @param e Mouse event that occurred
	 */
	public void mouseDragged(MouseEvent e);

	/**
	 * Method that paints provided {@link Graphics2D}.
	 * 
	 * @param g2d graphics that is provided
	 */
	public void paint(Graphics2D g2d);
}