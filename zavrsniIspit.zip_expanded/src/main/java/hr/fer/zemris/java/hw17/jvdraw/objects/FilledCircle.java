package hr.fer.zemris.java.hw17.jvdraw.objects;

import java.awt.Color;
import java.awt.Point;

import hr.fer.zemris.java.hw17.jvdraw.editing.FilledCircleEditor;
import hr.fer.zemris.java.hw17.jvdraw.editing.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.objects.visitors.GeometricalObjectVisitor;

/**
 * Class modeling a circle that contains one {@link Point}, center of the
 * circle, radius of the circle, color of the circle and color with which the
 * circle is filled.
 * 
 * @author Marko
 *
 */
public class FilledCircle extends Circle {

	/**
	 * Filling color for the circle
	 */
	private Color bgColor;

	/**
	 * Constructor
	 * 
	 * @param center  center of the circle
	 * @param radius  radius of the circle
	 * @param fgColor color of the circle
	 * @param bgColor filling color for the circle
	 */
	public FilledCircle(Point center, int radius, Color fgColor, Color bgColor) {
		super(center, radius, fgColor);
		this.bgColor = bgColor;
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new FilledCircleEditor(this);
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	/**
	 * @return the bgColor
	 */
	public Color getBgColor() {
		return bgColor;
	}

	@Override
	public String toString() {
		String hexColor = String.format("#%02x%02x%02x", bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
		return String.format("Filled circle (%d,%d), %d, %s", center.x, center.y, radius, hexColor);
	}

	/**
	 * 
	 * Method that updates the circle with the given values, and notifies the
	 * listeners that the object has changed.
	 * 
	 * @param x1      x coordinate of the center
	 * @param y1      y coordinate of the center
	 * @param rad     radius of the circle
	 * @param bgColor filling color for the circle
	 * @param fgColor color of the circle
	 */
	public void updateCircle(int x1, int y1, int rad, Color bgColor, Color fgColor) {
		this.center = new Point(x1, y1);
		this.radius = rad;
		this.bgColor = bgColor;
		this.fgColor = fgColor;
		notifyObjectChanged();
	}
	
	@Override
	public boolean isFilled() {
		return true;
	}

}
