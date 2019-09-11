package hr.fer.zemris.java.hw17.jvdraw.visitors;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;

import com.mchange.v2.c3p0.impl.NewPooledConnection;

import hr.fer.zemris.java.hw17.jvdraw.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Circle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FilledTriangle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Line;

/**
 * Class that is implementation of {@link GeometricalObjectVisitor} used to
 * calculate bounds of Objects on {@link JDrawingCanvas}.
 * 
 * @author juren
 *
 */
public class GeometricalObjectBBCalculator implements GeometricalObjectVisitor {
	/**
	 * variable that stores the most upper Left point
	 */
	private Point upperLeft;
	/**
	 * variable that stores the most lower right point
	 */
	private Point lowerRight;

	/**
	 * Method that returns {@link Rectangle} that is box around all
	 * {@link GeometricalObject} that visitor visited so far.
	 * 
	 * @return
	 */
	public Rectangle getBoundingBox() {
		return new Rectangle(upperLeft.x, upperLeft.y, lowerRight.x - upperLeft.x, lowerRight.y - upperLeft.y);
	}

	@Override
	public void visit(Line line) {
		if (upperLeft == null) {
			upperLeft = new Point(line.getStart());
			lowerRight = new Point(line.getStart());
		}

		if (upperLeft.x > line.getStart().x) {
			upperLeft.x = line.getStart().x;
		}
		if (upperLeft.x > line.getEnd().x) {
			upperLeft.x = line.getEnd().x;
		}

		if (upperLeft.y > line.getStart().y) {
			upperLeft.y = line.getStart().y;
		}
		if (upperLeft.y > line.getEnd().y) {
			upperLeft.y = line.getEnd().y;
		}

		if (lowerRight.x < line.getStart().x) {
			lowerRight.x = line.getStart().x;
		}
		if (lowerRight.x < line.getEnd().x) {
			lowerRight.x = line.getEnd().x;
		}

		if (lowerRight.y < line.getStart().y) {
			lowerRight.y = line.getStart().y;
		}
		if (lowerRight.y < line.getEnd().y) {
			lowerRight.y = line.getEnd().y;
		}

	}

	@Override
	public void visit(Circle circle) {
		if (upperLeft == null) {
			upperLeft = new Point(circle.getCenter());
			lowerRight = new Point(circle.getCenter());
		}
		if (upperLeft.x > circle.getCenter().x - circle.getRadius()) {
			upperLeft.x = (int) (circle.getCenter().x - circle.getRadius());
		}
		if (upperLeft.y > circle.getCenter().y - circle.getRadius()) {
			upperLeft.y = (int) (circle.getCenter().y - circle.getRadius());
		}
		if (lowerRight.x < circle.getCenter().x + circle.getRadius()) {
			lowerRight.x = (int) (circle.getCenter().x + circle.getRadius());
		}
		if (lowerRight.y < circle.getCenter().y + circle.getRadius()) {
			lowerRight.y = (int) (circle.getCenter().y + circle.getRadius());
		}
	}

	@Override
	public void visit(FilledCircle circle) {
		if (upperLeft == null) {
			upperLeft = new Point(circle.getCenter());
			lowerRight = new Point(circle.getCenter());
		}
		if (upperLeft.x > circle.getCenter().x - circle.getRadius()) {
			upperLeft.x = (int) (circle.getCenter().x - circle.getRadius());
		}
		if (upperLeft.y > circle.getCenter().y - circle.getRadius()) {
			upperLeft.y = (int) (circle.getCenter().y - circle.getRadius());
		}
		if (lowerRight.x < circle.getCenter().x + circle.getRadius()) {
			lowerRight.x = (int) (circle.getCenter().x + circle.getRadius());
		}
		if (lowerRight.y < circle.getCenter().y + circle.getRadius()) {
			lowerRight.y = (int) (circle.getCenter().y + circle.getRadius());
		}
	}

	@Override
	public void visit(FilledTriangle filledTriangle) {
		int xMIN = Arrays.stream(filledTriangle.getX()).min().getAsInt();
		int xMAX = Arrays.stream(filledTriangle.getX()).max().getAsInt();
		int yMIN = Arrays.stream(filledTriangle.getX()).min().getAsInt();
		int yMAX = Arrays.stream(filledTriangle.getX()).max().getAsInt();

		if(upperLeft==null) {
			upperLeft=new Point(xMIN,yMIN);
			lowerRight=new Point(xMAX,yMAX);
		}else {
			if(xMIN<upperLeft.x) upperLeft.x=xMIN;
			if(xMAX>lowerRight.x) lowerRight.x=xMAX;
			if(yMIN<upperLeft.y) upperLeft.y=yMIN;
			if(yMAX>lowerRight.y) lowerRight.y=yMAX;
		}
		
	}
}
