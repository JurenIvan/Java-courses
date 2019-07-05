package hr.fer.zemris.java.hw17.jvdraw.visitors;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw17.jvdraw.shapes.Circle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Line;

public class GeometricalObjectOutputter implements GeometricalObjectVisitor {

	private List<String> outputList;

	public GeometricalObjectOutputter() {
		this.outputList = new ArrayList<>();
	}

	public List<String> getOutput() {
		return outputList;
	}

	@Override
	public void visit(Line line) {
		outputList.add(String.format("LINE %d %d %d %d %d %d %d", line.getStart().x, line.getStart().y, line.getEnd().x,
				line.getEnd().y, line.getColor().getRed(), line.getColor().getGreen(), line.getColor().getBlue()));
	}

	@Override
	public void visit(Circle circle) {
		outputList.add(String.format("CIRCLE %d %d %d %d %d %d ", circle.getCenter().x, circle.getCenter().y,
				(int)circle.getRadius(), circle.getOutlineColor().getRed(), circle.getOutlineColor().getGreen(),
				circle.getOutlineColor().getBlue()));
	}

	@Override
	public void visit(FilledCircle filledCircle) {
		outputList.add(String.format("FCIRCLE %d %d %d %d %d %d %d %d %d", filledCircle.getCenter().x, filledCircle.getCenter().y,
				(int)filledCircle.getRadius(), filledCircle.getOutlineColor().getRed(),
				filledCircle.getOutlineColor().getGreen(), filledCircle.getOutlineColor().getBlue(),
				filledCircle.getFillColor().getRed(), filledCircle.getFillColor().getGreen(),
				filledCircle.getFillColor().getBlue()));
	}

}
