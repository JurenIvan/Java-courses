package hr.fer.zemris.java.hw17.jvdraw.shapes;

import java.awt.Color;
import java.awt.Point;

import hr.fer.zemris.java.hw17.jvdraw.editor.CircleEditor;
import hr.fer.zemris.java.hw17.jvdraw.editor.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectVisitor;

/**
 * Class whose function is basically a data structure. Holds basic data about
 * circle that is represented.
 * 
 * @author juren
 *
 */
public class Circle extends GeometricalObject {

	/**
	 * Variable that stores reference to Point that is the center of {@link Circle}
	 */
	protected Point center;
	/**
	 * Variable that stores reference to Point that alongside center defines radius
	 */
	private Point other;
	/**
	 * variable that stores color of outline
	 */
	private Color outlineColor;

	/**
	 * Standard constructor
	 * 
	 * @param center       Variable that stores reference to Point that is the
	 *                     center of {@link Circle}
	 * @param other        Variable that stores reference to Point that alongside
	 *                     center defines radius
	 * @param outlineColor variable that stores color of outline
	 */
	public Circle(Point center, Point other, Color outlineColor) {
		super();
		this.center = center;
		this.other = other;
		this.outlineColor = outlineColor;
	}

	/**
	 * Getter for radius. (calculates it from the distance of two points)
	 * 
	 * @return
	 */
	public double getRadius() {
		return center.distance(other);
	}

	/**
	 * Getter for center.
	 * 
	 * @return the center
	 */
	public Point getCenter() {
		return center;
	}

	/**
	 * Setter for Center. Notifies Listeners that something changed.
	 * 
	 * @param center the center to set
	 */
	public void setCenter(Point center) {
		this.center = center;
		notifyListeners();
	}

	/**
	 * Standard getter
	 * 
	 * @return the other point
	 */
	public Point getOther() {
		return other;
	}

	/**
	 * Setter for other point. Notifies Listeners that something changed.
	 * 
	 * @param other the other to set
	 */
	public void setOther(Point other) {
		this.other = other;
		notifyListeners();
	}

	/**
	 * Getter for outLineColor
	 * 
	 * @return the outlineColor
	 */
	public Color getOutlineColor() {
		return outlineColor;
	}

	/**
	 * Setter for outline color. Notifies Listeners that something changed.
	 * 
	 * @param outlineColor the outlineColor to set
	 */
	public void setOutlineColor(Color outlineColor) {
		this.outlineColor = outlineColor;
		notifyListeners();
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new CircleEditor(this);
	}

	@Override
	public String toString() {
		return String.format("Circle (%d,%d), %d", center.x, center.y, (int) getRadius());
	}

}
