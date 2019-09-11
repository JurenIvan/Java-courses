package hr.fer.zemris.java.hw17.jvdraw.objects.visitors;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

import hr.fer.zemris.java.hw17.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw17.jvdraw.objects.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.objects.Line;
import hr.fer.zemris.java.hw17.jvdraw.objects.Triangle;

/**
 * Implementation of {@link GeometricalObjectVisitor} that upon visiting an
 * objects creates a line that represents that objects and stores the line in
 * private text. Calling of the method {@link #getFileText()} returns the
 * representation of all the objects that were visited before the method was
 * called.
 * 
 * @author Marko
 *
 */
public class GeometricalObjectSaver implements GeometricalObjectVisitor {

	/**
	 * Created text
	 */
	private StringBuilder text;

	/**
	 * Constructor
	 */
	public GeometricalObjectSaver() {
		this.text = new StringBuilder();
	}

	@Override
	public void visit(Line line) {
		Point start = line.getX();
		Point end = line.getY();
		Color color = line.getFgColor();
		text.append(String.format("LINE %d %d %d %d %d %d %d", start.x, start.y, end.x, end.y, color.getRed(),
				color.getGreen(), color.getBlue()));
		text.append(System.lineSeparator());
	}

	@Override
	public void visit(Circle circle) {
		Point center = circle.getCenter();
		int radius = circle.getRadius();
		Color color = circle.getFgColor();
		text.append(String.format("CIRCLE %d %d %d %d %d %d", center.x, center.y, radius, color.getRed(),
				color.getGreen(), color.getBlue()));
		text.append(System.lineSeparator());
	}

	@Override
	public void visit(FilledCircle filledCircle) {
		Point center = filledCircle.getCenter();
		int radius = filledCircle.getRadius();
		Color fgColor = filledCircle.getFgColor();
		Color bgColor = filledCircle.getBgColor();
		text.append(String.format("FCIRCLE %d %d %d %d %d %d %d %d %d", center.x, center.y, radius, fgColor.getRed(),
				fgColor.getGreen(), fgColor.getBlue(), bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue()));
		text.append(System.lineSeparator());
	}

	@Override
	public void visit(Triangle triangle) {
		Color rub = triangle.rub;
		Color ispuna = triangle.ispuna;
		List<Integer> xevi = triangle.x;
		List<Integer> yevei = triangle.y;

		text.append(String.format("FTRIANGLE %d %d %d %d %d %d %d %d %d %d %d %d", xevi.get(0), yevei.get(0),
				xevi.get(1), yevei.get(1), xevi.get(2), yevei.get(2), rub.getRed(), rub.getGreen(), rub.getBlue(),
				ispuna.getRed(), ispuna.getGreen(), ispuna.getBlue()));
		text.append(System.lineSeparator());
	}

	/**
	 * Getter for constructed text
	 * 
	 * @return created text from visited objects
	 */
	public String getFileText() {
		return text.toString();
	}
}
