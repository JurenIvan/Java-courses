package hr.fer.zemris.java.hw17.jvdraw.objects;

import java.awt.Color;
import java.awt.Point;

import hr.fer.zemris.java.hw17.jvdraw.editing.CircleEditor;
import hr.fer.zemris.java.hw17.jvdraw.editing.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.objects.visitors.GeometricalObjectVisitor;

/**
 * Class modeling a circle that contains one {@link Point}, center of the
 * circle, radius of the circle and color of the circle.
 * 
 * @author Marko
 *
 */
public class Circle extends GeometricalObject {

	/**
	 * Center of the circle
	 */
	Point center;
	/**
	 * Color of the circle
	 */
	Color fgColor;
	/**
	 * Radius of the circle
	 */
	int radius;

	/**
	 * Constructor
	 * 
	 * @param center  center of the circle
	 * @param radius  radius of the circle
	 * @param fgColor color of the circle
	 */
	public Circle(Point center, int radius, Color fgColor) {
		super();
		this.center = center;
		this.radius = radius;
		this.fgColor = fgColor;
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new CircleEditor(this);
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	/**
	 * @return the center
	 */
	public Point getCenter() {
		return center;
	}

	/**
	 * @return the radius
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * @return the fgColor
	 */
	public Color getFgColor() {
		return fgColor;
	}

	@Override
	public String toString() {
		return String.format("Circle (%d,%d), %d", center.x, center.y, radius);
	}

	/**
	 * Method that updates the circle with the given values, and notifies the
	 * listeners that the object has changed.
	 * 
	 * @param x1           x coordinate of the center
	 * @param y1           y coordinate of the center
	 * @param rad          radius of the circle
	 * @param currentColor color of the circle
	 */
	public void updateCircle(int x1, int y1, int rad, Color currentColor) {
		this.center = new Point(x1, y1);
		this.radius = rad;
		this.fgColor = currentColor;
		notifyObjectChanged();

	}
	
	public boolean isFilled() {
		return false;
	}
}
