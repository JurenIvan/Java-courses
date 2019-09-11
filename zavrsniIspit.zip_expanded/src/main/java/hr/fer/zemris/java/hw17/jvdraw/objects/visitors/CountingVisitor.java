package hr.fer.zemris.java.hw17.jvdraw.objects.visitors;

import hr.fer.zemris.java.hw17.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw17.jvdraw.objects.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.objects.Line;
import hr.fer.zemris.java.hw17.jvdraw.objects.Triangle;

public class CountingVisitor implements GeometricalObjectVisitor {

	public int circles = 0;
	public int lines = 0;
	public int filled = 0;
	public int triangles = 0;

	@Override
	public void visit(Line line) {
		lines++;

	}

	@Override
	public void visit(Circle circle) {
		circles++;
	}

	@Override
	public void visit(FilledCircle filledCircle) {
		filled++;
	}

	@Override
	public void visit(Triangle triangle) {
		triangles++;
	}

	/**
	 * @return the circles
	 */
	public int getCircles() {
		return circles;
	}

	/**
	 * @return the lines
	 */
	public int getLines() {
		return lines;
	}

	/**
	 * @return the filled
	 */
	public int getFilled() {
		return filled;
	}

	/**
	 * @return the triangles
	 */
	public int getTriangles() {
		return triangles;
	}

}
