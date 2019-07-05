package hr.fer.zemris.java.hw17.jvdraw.visitors;

import java.awt.Graphics;

import hr.fer.zemris.java.hw17.jvdraw.shapes.Circle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Line;

public class GeometricalObjectPainter implements GeometricalObjectVisitor {

	private Graphics g2d;

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

}
