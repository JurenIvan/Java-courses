package hr.fer.zemris.java.hw17.jvdraw.visitors;

import hr.fer.zemris.java.hw17.jvdraw.shapes.Circle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FilledTriangle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Line;

/**
 * Interface that models a visitor that is able to go through {@link Line},
 * {@link Circle}s and {@link FilledCircle}s
 * 
 * @author juren
 *
 */
public interface GeometricalObjectVisitor {
	/**
	 * Defines what happens when {@link GeometricalObjectVisitor} is called upon
	 * {@link Line}
	 * 
	 * @param line upon visitor is called
	 */
	public abstract void visit(Line line);

	/**
	 * Defines what happens when {@link GeometricalObjectVisitor} is called upon
	 * {@link Circle}
	 * 
	 * @param circle upon visitor is called
	 */
	public abstract void visit(Circle circle);

	/**
	 * Defines what happens when {@link GeometricalObjectVisitor} is called upon
	 * {@link FilledCircle}
	 * 
	 * @param fillecCircle upon visitor is called
	 */
	public abstract void visit(FilledCircle filledCircle);

	public abstract void visit(FilledTriangle filledTriangle);
}
