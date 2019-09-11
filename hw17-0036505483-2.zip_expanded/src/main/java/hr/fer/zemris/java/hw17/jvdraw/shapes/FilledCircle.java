package hr.fer.zemris.java.hw17.jvdraw.shapes;

import java.awt.Color;
import java.awt.Point;

import hr.fer.zemris.java.hw17.jvdraw.editor.FilledCircleEditor;
import hr.fer.zemris.java.hw17.jvdraw.editor.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectVisitor;

/**
 * Class whose function is basically a data structure. Holds basic data about
 * Filled circle that is represented.
 * 
 * @author juren
 *
 */
public class FilledCircle extends Circle {

	/**
	 * variable that stores color of the fill
	 */
	private Color fillColor;

	/**
	 * Standard constructor
	 * 
	 * @param center       Variable that stores reference to Point that is the
	 *                     center of {@link Circle}
	 * @param other        Variable that stores reference to Point that alongside
	 *                     center defines radius
	 * @param outlineColor variable that stores color of outline
	 * @param fillColor    variable that stores color of the fill
	 */
	public FilledCircle(Point center, Point other, Color outlineColor, Color fillColor) {
		super(center, other, outlineColor);
		this.fillColor = fillColor;

	}

	/**
	 * Getter for the fillColor
	 * 
	 * @return the fillColor
	 */
	public Color getFillColor() {
		return fillColor;
	}

	/**
	 * Setter for fill color. Notifies Listeners that something changed.
	 * 
	 * @param fillColor the fillColor to set
	 */
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
		notifyListeners();
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new FilledCircleEditor(this);
	}

	@Override
	public String toString() {
		String hexColor = String.format("#%06X", (0xFFFFFF & getFillColor().getRGB()));
		return String.format("Filled circle (%d,%d), %d %s", center.x, center.y, (int) getRadius(), hexColor);
	}

}
