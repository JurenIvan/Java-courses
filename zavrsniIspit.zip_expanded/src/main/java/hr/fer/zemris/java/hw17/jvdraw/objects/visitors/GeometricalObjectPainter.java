package hr.fer.zemris.java.hw17.jvdraw.objects.visitors;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

import hr.fer.zemris.java.hw17.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw17.jvdraw.objects.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.objects.Line;
import hr.fer.zemris.java.hw17.jvdraw.objects.Triangle;

/**
 * Implementation of {@link GeometricalObjectVisitor} that takes graphics object
 * through the constructor which is used to draw visited objects upon visiting.
 * 
 * @author Marko
 *
 */
public class GeometricalObjectPainter implements GeometricalObjectVisitor {

	/**
	 * Graphics used for object drawing
	 */
	private Graphics2D graphics;

	/**
	 * Constructor
	 * 
	 * @param graphics graphics used for object drawing
	 */
	public GeometricalObjectPainter(Graphics2D graphics) {
		this.graphics = graphics;
	}

	@Override
	public void visit(Line line) {
		Point x = line.getX();
		Point y = line.getY();
		graphics.setColor(line.getFgColor());
		graphics.drawLine(x.x, x.y, y.x, y.y);
	}

	@Override
	public void visit(Circle circle) {
		graphics.setColor(circle.getFgColor());
		Point center = circle.getCenter();
		int radius = circle.getRadius();
		graphics.drawOval(center.x - radius, center.y - radius, radius * 2, radius * 2);

	}

	@Override
	public void visit(FilledCircle filledCircle) {
		Point center = filledCircle.getCenter();
		int radius = filledCircle.getRadius();

		graphics.setColor(filledCircle.getBgColor());
		graphics.fillOval(center.x - radius, center.y - radius, radius * 2, radius * 2);
		graphics.setColor(filledCircle.getFgColor());
		graphics.drawOval(center.x - radius, center.y - radius, radius * 2, radius * 2);

	}

	@Override
	public void visit(Triangle triangle) {
		List<Integer> x = triangle.x;
		List<Integer> y = triangle.y;

		int[] xevi = x.stream().mapToInt(i -> i).toArray();
		int[] yevi = y.stream().mapToInt(i -> i).toArray();

		graphics.setColor(triangle.ispuna);
		graphics.fillPolygon(xevi, yevi, 3);
		graphics.setColor(triangle.rub);
		graphics.drawPolygon(xevi, yevi, 3);

	}

}
