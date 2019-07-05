package hr.fer.zemris.java.hw17.jvdraw.shapes;

import java.awt.Color;
import java.awt.Point;

import hr.fer.zemris.java.hw17.jvdraw.editor.FilledCircleEditor;
import hr.fer.zemris.java.hw17.jvdraw.editor.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectVisitor;

public class FilledCircle extends Circle {

	private Color fillColor;

	public FilledCircle(Point center, Point other, Color outlineColor, Color fillColor) {
		super(center, other, outlineColor);
		this.fillColor = fillColor;
		
	}

	/**
	 * @return the fillColor
	 */
	public Color getFillColor() {
		return fillColor;
	}

	/**
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

	public String toString() {

		String hexColor = String.format("#%06X", (0xFFFFFF & getFillColor().getRGB()));

		return String.format("Filled circle (%d,%d), %d %s", center.x, center.y, (int) getRadius(), hexColor);
	}

}
