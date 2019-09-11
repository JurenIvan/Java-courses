package hr.fer.zemris.java.hw17.jvdraw.shapes;

import java.awt.Color;

import hr.fer.zemris.java.hw17.jvdraw.editor.FilledTriangleEditor;
import hr.fer.zemris.java.hw17.jvdraw.editor.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectVisitor;

public class FilledTriangle extends GeometricalObject {

	private Color fillColor;
	private Color outlineColor;

	private int[] x;
	private int[] y;

	public FilledTriangle(Color fillColor, Color outlineColor, int[] x, int[] y) {
		super();
		this.fillColor = fillColor;
		this.outlineColor = outlineColor;
		this.x = x;
		this.y = y;
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
	}

	/**
	 * @return the x
	 */
	public int[] getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int[] x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int[] getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int[] y) {
		this.y = y;
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new FilledTriangleEditor(this);
	}
	
	public String toString() {
		return String.format("FTRIANGLE %d %d %d %d %d %d %d %d %d %d %d %d", 
				this.getX()[0],
				this.getY()[0],
				this.getX()[1],
				this.getY()[1],
				this.getX()[2],
				this.getY()[2], 
				this.getOutlineColor().getRed(),
				this.getOutlineColor().getGreen(),
				this.getOutlineColor().getBlue(),
				this.getFillColor().getRed(),
				this.getFillColor().getGreen(),
				this.getFillColor().getBlue());
	}

}
