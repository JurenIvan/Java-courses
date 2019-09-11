package hr.fer.zemris.java.hw17.jvdraw.objects.visitors;

import hr.fer.zemris.java.hw17.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw17.jvdraw.objects.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.objects.Line;
import hr.fer.zemris.java.hw17.jvdraw.objects.Triangle;

/**
 * Visitor in visitor design pattern that knows how to visit {@link Line},
 * {@link Circle} and {@link FilledCircle} objects.
 * 
 * @author Marko
 *
 */
public interface GeometricalObjectVisitor {

	/**
	 * Method that visits the {@link Line} object.
	 * 
	 * @param line line that is visited
	 */
	public abstract void visit(Line line);

	/**
	 * Method that visits the {@link Circle} object.
	 * 
	 * @param circle circle that is visited
	 */
	public abstract void visit(Circle circle);

	/**
	 * Method that visits the {@link FilledCircle} object.
	 * 
	 * @param filledCircle filled circle that is visited.
	 */
	public abstract void visit(FilledCircle filledCircle);
	
	public abstract void visit(Triangle triangle);
}