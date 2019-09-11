package hr.fer.zemris.java.hw17.jvdraw.visitors;

import hr.fer.zemris.java.hw17.jvdraw.shapes.Circle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FilledTriangle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Line;

public class CalculateShapesVisitor implements GeometricalObjectVisitor {

	int lineN = 0;
	int circleN = 0;
	int fcircleN = 0;
	int ftriangleN = 0;

	@Override
	public void visit(Line line) {
		lineN++;
	}

	@Override
	public void visit(Circle circle) {
		circleN++;
	}

	@Override
	public void visit(FilledCircle filledCircle) {
		fcircleN++;
	}

	@Override
	public void visit(FilledTriangle filledTriangle) {
		ftriangleN++;
	}

	/**
	 * @return the lineN
	 */
	public int getLineN() {
		return lineN;
	}

	/**
	 * @param lineN the lineN to set
	 */
	public void setLineN(int lineN) {
		this.lineN = lineN;
	}

	/**
	 * @return the circleN
	 */
	public int getCircleN() {
		return circleN;
	}

	/**
	 * @param circleN the circleN to set
	 */
	public void setCircleN(int circleN) {
		this.circleN = circleN;
	}

	/**
	 * @return the fcircleN
	 */
	public int getFcircleN() {
		return fcircleN;
	}

	/**
	 * @param fcircleN the fcircleN to set
	 */
	public void setFcircleN(int fcircleN) {
		this.fcircleN = fcircleN;
	}

	/**
	 * @return the ftriangleN
	 */
	public int getFtriangleN() {
		return ftriangleN;
	}

	/**
	 * @param ftriangleN the ftriangleN to set
	 */
	public void setFtriangleN(int ftriangleN) {
		this.ftriangleN = ftriangleN;
	}
	
	
	
}
