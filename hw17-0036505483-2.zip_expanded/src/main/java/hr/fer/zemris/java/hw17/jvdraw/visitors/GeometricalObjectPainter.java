package hr.fer.zemris.java.hw17.jvdraw.visitors;

import java.awt.Graphics;

import hr.fer.zemris.java.hw17.jvdraw.shapes.Circle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FilledTriangle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Line;

/**
 * Class that is implementation of {@link GeometricalObjectVisitor} used paint
 * all {@link GeometricalObject} to {@link Graphics} provided via constructor
 * 
 * @author juren
 *
 */
public class GeometricalObjectPainter implements GeometricalObjectVisitor {

	/**
	 * Reference to {@link Graphics} upon which is drawn
	 */
	private Graphics g2d;

	/**
	 * Standard constructor
	 * 
	 * @param g2d reference to {@link Graphics} upon which is drawn
	 */
	public GeometricalObjectPainter(Graphics g2d) {
		this.g2d = g2d;
	}

	@Override
	public void visit(Line line) {
		if (line != null) {
			g2d.setColor(line.getColor());
			g2d.drawLine(line.getStart().x, line.getStart().y, line.getEnd().x, line.getEnd().y);
		}
	}

	@Override
	public void visit(Circle circle) {
		g2d.setColor(circle.getOutlineColor());
		double radius = circle.getCenter().distance(circle.getOther());
		g2d.drawOval((int) (circle.getCenter().x - radius), (int) (circle.getCenter().y - radius), (int) radius * 2,
				(int) radius * 2);
	}

	@Override
	public void visit(FilledCircle circle) {
		g2d.setColor(circle.getFillColor());
		double radius = circle.getCenter().distance(circle.getOther());
		g2d.fillOval((int) (circle.getCenter().x - radius), (int) (circle.getCenter().y - radius), (int) radius * 2,
				(int) radius * 2);

		g2d.setColor(circle.getOutlineColor());
		g2d.drawOval((int) (circle.getCenter().x - radius), (int) (circle.getCenter().y - radius), (int) radius * 2,
				(int) radius * 2);
	}

	@Override
	public void visit(FilledTriangle filledTriangle) {

		g2d.setColor(filledTriangle.getFillColor());
		g2d.fillPolygon(filledTriangle.getX(), filledTriangle.getY(), 3);
		g2d.setColor(filledTriangle.getOutlineColor());
		g2d.drawPolygon(filledTriangle.getX(), filledTriangle.getY(), 3);

	}

}
