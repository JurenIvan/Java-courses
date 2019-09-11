package hr.fer.zemris.java.hw17.jvdraw.shapes;

import java.awt.Color;
import java.awt.Point;

import hr.fer.zemris.java.hw17.jvdraw.editor.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.editor.LineEditor;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectVisitor;

/**
 * Class whose function is basically a data structure. Holds basic data about
 * line that is represented.
 * 
 * @author juren
 *
 */
public class Line extends GeometricalObject {

	/**
	 * Variable that stores reference to Point that is the begging of line
	 */
	private Point start;
	/**
	 * Variable that stores reference to Point that is end of line
	 */
	private Point end;
	/**
	 * Variable that stores color of outline
	 */
	private Color color;

	/**
	 * Standard constructor.
	 * 
	 * @param start Variable that stores reference to Point that is the begging of
	 *              line
	 * @param end   Variable that stores reference to Point that is end of line
	 * @param color Variable that stores color of outline
	 */
	public Line(Point start, Point end, Color color) {
		this.start = start;
		this.end = end;
		this.color = color;

	}

	/**
	 * Getter for the start point
	 * 
	 * @return the start
	 */
	public Point getStart() {
		return start;
	}

	/**
	 * Setter for StartPoint. Notifies Listeners that something changed.
	 * 
	 * @param start the start to set
	 */
	public void setStart(Point start) {
		this.start = start;
		notifyListeners();
	}

	/**
	 * Getter for the endPoint
	 * 
	 * @return the end
	 */
	public Point getEnd() {
		return end;
	}

	/**
	 * Setter for endPoint. Notifies Listeners that something changed.
	 * 
	 * @param end the end to set
	 */
	public void setEnd(Point end) {
		this.end = end;
		notifyListeners();

	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	/**
	 * Getter for the color
	 * 
	 * @return color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Setter for color. Notifies Listeners that something changed.
	 * 
	 * @param color
	 */
	public void setColor(Color color) {
		this.color = color;
		notifyListeners();
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new LineEditor(this);
	}

	@Override
	public String toString() {
		return String.format("Line (%d,%d)-(%d,%d)", start.x, start.y, end.x, end.y);
	}

}
