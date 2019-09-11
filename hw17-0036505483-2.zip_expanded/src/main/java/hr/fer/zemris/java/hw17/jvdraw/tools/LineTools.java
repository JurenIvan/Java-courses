package hr.fer.zemris.java.hw17.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.jvdraw.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.colorArea.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.drawingModel.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Line;

public class LineTools implements Tool {

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
	private boolean beginLine = true;
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
	private Line line;

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
	public LineTools(IColorProvider colorProvider, DrawingModel drawingModel, JDrawingCanvas canvas) {
		this.colorProvider = colorProvider;
		this.drawingModel = drawingModel;
		this.canvas = canvas;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (beginLine) {
			start = e.getPoint();
			beginLine = false;
		} else {
			line = new Line(start, e.getPoint(), colorProvider.getCurrentColor());
			beginLine = true;
			drawingModel.add(line);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (!beginLine) {
			line = new Line(start, e.getPoint(), colorProvider.getCurrentColor());
			canvas.repaint();
		}
	}

	@Override
	public void paint(Graphics2D g2d) {
		if (!beginLine) {
			g2d.setColor(line.getColor());
			g2d.drawLine(line.getStart().x, line.getStart().y, line.getEnd().x, line.getEnd().y);
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
