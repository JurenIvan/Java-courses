package hr.fer.zemris.java.hw17.jvdraw.visitors;

import hr.fer.zemris.java.hw17.jvdraw.shapes.Circle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Line;

public interface GeometricalObjectVisitor {
	public abstract void visit(Line line);

	public abstract void visit(Circle circle);

	public abstract void visit(FilledCircle filledCircle);
}
