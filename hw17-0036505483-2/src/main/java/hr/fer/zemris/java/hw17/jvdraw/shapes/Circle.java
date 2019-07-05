package hr.fer.zemris.java.hw17.jvdraw.shapes;

import java.awt.Color;
import java.awt.Point;

import hr.fer.zemris.java.hw17.jvdraw.editor.CircleEditor;
import hr.fer.zemris.java.hw17.jvdraw.editor.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectVisitor;

public class Circle extends GeometricalObject {

	protected Point center;
	private Point other;
	private Color outlineColor;

	public Circle(Point center, Point other, Color outlineColor) {
		super();
		this.center = center;
		this.other = other;
		this.outlineColor = outlineColor;
	}

	public double getRadius() {
		return center.distance(other);
	}

	/**
	 * @return the center
	 */
	public Point getCenter() {
		return center;
	}

	/**
	 * @param center the center to set
	 */
	public void setCenter(Point center) {
		this.center = center;
		notifyListeners();
	}

	/**
	 * @return the other
	 */
	public Point getOther() {
		return other;
	}

	/**
	 * @param other the other to set
	 */
	public void setOther(Point other) {
		this.other = other;
		notifyListeners();
	}

	/**
	 * @return the outlineColor
	 */
	public Color getOutlineColor() {
		return outlineColor;
	}

	/**
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

	public String toString() {
		return String.format("Circle (%d,%d), %d", center.x, center.y, (int) getRadius());
	}

}
