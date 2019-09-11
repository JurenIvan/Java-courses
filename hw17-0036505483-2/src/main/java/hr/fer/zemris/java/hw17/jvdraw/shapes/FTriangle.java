package hr.fer.zemris.java.hw17.jvdraw.shapes;

import java.awt.Color;
import java.awt.Point;

import hr.fer.zemris.java.hw17.jvdraw.editor.FTriangleEditor;
import hr.fer.zemris.java.hw17.jvdraw.editor.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectVisitor;

public class FTriangle extends GeometricalObject {

	private Color outlineColor;
	private Color fillColor;
	
	private Point pFirst;
	private Point pSecond;
	private Point pThird;
	
	public FTriangle(Color outlineColor, Color fillColor, Point pFirst, Point pSecond, Point pThird) {
		super();
		this.outlineColor = outlineColor;
		this.fillColor = fillColor;
		this.pFirst = pFirst;
		this.pSecond = pSecond;
		this.pThird = pThird;
	}
	
	
	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}




	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new FTriangleEditor(this);
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
	 * @return the pFirst
	 */
	public Point getpFirst() {
		return pFirst;
	}

	/**
	 * @param pFirst the pFirst to set
	 */
	public void setpFirst(Point pFirst) {
		this.pFirst = pFirst;
	}

	/**
	 * @return the pSecond
	 */
	public Point getpSecond() {
		return pSecond;
	}

	/**
	 * @param pSecond the pSecond to set
	 */
	public void setpSecond(Point pSecond) {
		this.pSecond = pSecond;
	}

	/**
	 * @return the pThird
	 */
	public Point getpThird() {
		return pThird;
	}

	/**
	 * @param pThird the pThird to set
	 */
	public void setpThird(Point pThird) {
		this.pThird = pThird;
	}




	//TODO 
	@Override
	public String toString() {
		String hexColor = String.format("#%06X", (0xFFFFFF & getFillColor().getRGB()));
		
		return String.format("FTRIANGLE  (%d,%d),(%d,%d),(%d,%d), %s", 
				pFirst.x,
				pFirst.y,
				pSecond.x,
				pSecond.y,
				pThird.x,
				pThird.y,
				hexColor
				);
	}
	
	
	
	
}
