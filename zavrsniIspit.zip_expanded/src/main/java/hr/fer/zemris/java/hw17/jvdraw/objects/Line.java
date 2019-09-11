package hr.fer.zemris.java.hw17.jvdraw.objects;

import java.awt.Color;
import java.awt.Point;

import hr.fer.zemris.java.hw17.jvdraw.editing.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.editing.LineEditor;
import hr.fer.zemris.java.hw17.jvdraw.objects.visitors.GeometricalObjectVisitor;

/**
 * Class modeling a line that contains two {@link Point}, starting point and
 * ending point. Also contains the color of the line.
 * 
 * @author Marko
 *
 */
public class Line extends GeometricalObject {

	/**
	 * Starting point of the line
	 */
	private Point x;
	/**
	 * Ending point of the line
	 */
	private Point y;
	/**
	 * Color of the line
	 */
	private Color fgColor;

	/**
	 * Constructor
	 * 
	 * @param x       starting point of the line
	 * @param y       ending point of the line
	 * @param fgColor color of the line
	 */
	public Line(Point x, Point y, Color fgColor) {
		super();
		this.x = x;
		this.y = y;
		this.fgColor = fgColor;
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new LineEditor(this);
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	/**
	 * @return the x
	 */
	public Point getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public Point getY() {
		return y;
	}

	/**
	 * @return the fgColor
	 */
	public Color getFgColor() {
		return fgColor;
	}

	@Override
	public String toString() {
		return String.format("Line (%d,%d) - (%d,%d)", x.x, x.y, y.x, y.y);
	}

	/**
	 * Method that updates the line with the given values, and notifies the
	 * listeners that the object has changed.
	 * 
	 * @param x1    x coordinate of the start
	 * @param y1    y coordinate of the start
	 * @param x2    x coordinate of the end
	 * @param y2    y coordinate of the end
	 * @param color color of the line
	 */
	public void updateLine(int x1, int y1, int x2, int y2, Color color) {
		this.x = new Point(x1, y1);
		this.y = new Point(x2, y2);
		this.fgColor = color;
		notifyObjectChanged();
	}

}
