package hr.fer.zemris.java.hw17.jvdraw.visitors;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw17.jvdraw.drawingModel.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Circle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FilledTriangle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Line;

/**
 * Class that is implementation of {@link GeometricalObjectVisitor} used to get
 * .jvd description of {@link DrawingModel}
 * 
 * @author juren
 *
 */
public class GeometricalObjectOutputter implements GeometricalObjectVisitor {

	/**
	 * List that stores lines of .jvd file
	 */
	private List<String> outputList;

	/**
	 * Default constructor
	 */
	public GeometricalObjectOutputter() {
		this.outputList = new ArrayList<>();
	}

	/**
	 * Method that is used to get output text
	 * 
	 * @return list of lines
	 */
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
				(int) circle.getRadius(), circle.getOutlineColor().getRed(), circle.getOutlineColor().getGreen(),
				circle.getOutlineColor().getBlue()));
	}

	@Override
	public void visit(FilledCircle filledCircle) {
		outputList.add(String.format("FCIRCLE %d %d %d %d %d %d %d %d %d", filledCircle.getCenter().x,
				filledCircle.getCenter().y, (int) filledCircle.getRadius(), filledCircle.getOutlineColor().getRed(),
				filledCircle.getOutlineColor().getGreen(), filledCircle.getOutlineColor().getBlue(),
				filledCircle.getFillColor().getRed(), filledCircle.getFillColor().getGreen(),
				filledCircle.getFillColor().getBlue()));
	}

	@Override
	public void visit(FilledTriangle filledTriangle) {
		outputList.add(String.format("FTRIANGLE %d %d %d %d %d %d %d %d %d %d %d %d", 
				filledTriangle.getX()[0],
				filledTriangle.getY()[0],
				filledTriangle.getX()[1],
				filledTriangle.getY()[1],
				filledTriangle.getX()[2],
				filledTriangle.getY()[2], 
				
				filledTriangle.getFillColor().getRed(),
				filledTriangle.getFillColor().getGreen(),
				filledTriangle.getFillColor().getBlue(),
				filledTriangle.getOutlineColor().getRed(),
				filledTriangle.getOutlineColor().getGreen(),
				filledTriangle.getOutlineColor().getBlue()
				
				));
	}
}
