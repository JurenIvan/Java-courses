package hr.fer.zemris.java.hw17.jvdraw.objects.visitors;

import java.awt.Point;
import java.awt.Rectangle;

import hr.fer.zemris.java.hw17.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw17.jvdraw.objects.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.objects.Line;
import hr.fer.zemris.java.hw17.jvdraw.objects.Triangle;

/**
 * Implementation of {@link GeometricalObjectVisitor} that stores the bounding
 * box for all the visited objects.
 * 
 * @author Marko
 *
 */
public class GeometricalObjectBBCalculator implements GeometricalObjectVisitor {

	/**
	 * Minimum x found in the visited objects
	 */
	private int minX = Integer.MAX_VALUE;
	/**
	 * Minimum y found in the visited objects
	 */
	private int minY = Integer.MAX_VALUE;
	/**
	 * Maximum x found in the visited objects
	 */
	private int maxX = Integer.MIN_VALUE;
	/**
	 * Maximum y found in the visited objects
	 */
	private int maxY = Integer.MIN_VALUE;

	@Override
	public void visit(Line line) {
		Point p1 = line.getX();
		Point p2 = line.getY();

		int newMinX = p1.x < p2.x ? p1.x : p2.x;
		int newMinY = p1.y < p2.y ? p1.y : p2.y;
		int newMaxX = p1.x > p2.x ? p1.x : p2.x;
		int newMaxY = p1.y > p2.y ? p1.y : p2.y;

		swapValues(newMinX, newMinY, newMaxX, newMaxY);

	}

	@Override
	public void visit(Circle circle) {
		Point center = circle.getCenter();
		int radius = circle.getRadius();

		swapValues(center.x - radius, center.y - radius, center.x + radius, center.y + radius);
	}

	@Override
	public void visit(FilledCircle filledCircle) {
		Point center = filledCircle.getCenter();
		int radius = filledCircle.getRadius();

		swapValues(center.x - radius, center.y - radius, center.x + radius, center.y + radius);
	}

	/**
	 * Method that returns the bounding box for all the objects that were visited.
	 * 
	 * @return bounding box for visited objects.
	 */
	public Rectangle getBoundingBox() {
		return new Rectangle(minX, minY, maxX - minX, maxY - minY);
	}

	/**
	 * Method that checks if any of the new values should replace currently saved
	 * values, and changes them if needed
	 * 
	 * @param newMinX new minimum x candidate
	 * @param newMinY new minimum y candidate
	 * @param newMaxX new maximum x candidate
	 * @param newMaxY new maximum y candidate
	 */
	private void swapValues(int newMinX, int newMinY, int newMaxX, int newMaxY) {
		if (minX > newMinX) {
			minX = newMinX;
		}
		if (minY > newMinY) {
			minY = newMinY;
		}
		if (maxX < newMaxX) {
			maxX = newMaxX;
		}
		if (maxY < newMaxY) {
			maxY = newMaxY;
		}
	}

	@Override
	public void visit(Triangle triangle) {
		int newMinX = Integer.MAX_VALUE;
		int newMaxX = Integer.MIN_VALUE;
		int newMinY = Integer.MAX_VALUE;
		int newMaxY = Integer.MIN_VALUE;

		for (Integer i : triangle.x) {
			if (i < newMinX) {
				newMinX = i;
			}

			if (i > newMaxX) {
				newMaxX = i;
			}
		}

		for (Integer i : triangle.y) {
			if (i < newMinY) {
				newMinY = i;
			}

			if (i > newMaxY) {
				newMaxY = i;
			}
		}

		swapValues(newMinX, newMinY, newMaxX, newMaxY);

	}

}
