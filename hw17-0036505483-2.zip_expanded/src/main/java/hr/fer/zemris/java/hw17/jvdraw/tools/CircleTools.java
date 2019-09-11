package hr.fer.zemris.java.hw17.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.jvdraw.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.colorArea.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.drawingModel.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Circle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObject;

public class CircleTools implements Tool {

	/**
	 * {@link IColorProvider} that is used to get line color
	 */
	private IColorProvider colorProvider;
	/**
	 * reference to {@link DrawingModel} where new objects get stored
	 */
	private DrawingModel drawingModel;
	/**
	 * boolean flag that determines whether this is first or second click
	 */
	private boolean beginCircle = true;
	/**
	 * variable that stores reference to Point that was first clicked
	 */
	private Point start;
	/**
	 * reference to {@link JDrawingCanvas} upon {@link GeometricalObject} are drawn
	 */
	private JDrawingCanvas canvas;

	/**
	 * variable that stores object that is being drawn
	 */
	private Circle circle;

	/**
	 * Standard constructor.
	 * 
	 * 
	 * @param colorProvider {@link IColorProvider} that is used to get line color
	 * @param drawingModel  reference to {@link DrawingModel} where new objects get
	 *                      stored
	 * @param canvas        reference to {@link JDrawingCanvas} upon
	 *                      {@link GeometricalObject} are drawn
	 */
	public CircleTools(IColorProvider colorProvider, DrawingModel drawingModel, JDrawingCanvas canvas) {
		this.colorProvider = colorProvider;
		this.drawingModel = drawingModel;
		this.canvas = canvas;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (beginCircle) {
			start = e.getPoint();
			beginCircle = false;
		} else {
			circle = new Circle(start, e.getPoint(), colorProvider.getCurrentColor());
			beginCircle = true;
			drawingModel.add(circle);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (!beginCircle) {
			circle = new Circle(start, e.getPoint(), colorProvider.getCurrentColor());
			canvas.repaint();
		}
	}

	@Override
	public void paint(Graphics2D g2d) {
		if (!beginCircle) {
			g2d.setColor(circle.getOutlineColor());
			double radius = circle.getCenter().distance(circle.getOther());
			g2d.drawOval((int) (circle.getCenter().x - radius), (int) (circle.getCenter().y - radius), (int) radius * 2,
					(int) radius * 2);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
