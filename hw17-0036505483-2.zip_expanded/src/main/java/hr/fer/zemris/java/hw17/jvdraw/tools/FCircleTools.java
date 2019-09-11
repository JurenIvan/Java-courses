package hr.fer.zemris.java.hw17.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.jvdraw.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.colorArea.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.drawingModel.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObject;

/**
 * Class that models a tool used for drawing filled circles. Implements Tool
 * interface
 * 
 * @author juren
 *
 */
public class FCircleTools implements Tool {

	/**
	 * {@link IColorProvider} that is used to get background color
	 */
	private IColorProvider bgColorProvider;
	/**
	 * {@link IColorProvider} that is used to get foreground color
	 */
	private IColorProvider fgColorProvider;
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
	private FilledCircle circle;

	/**
	 * Standard constructor.
	 * 
	 * @param fgColorProvider {@link IColorProvider} that is used to get foreground
	 *                        color
	 * @param bgColorProvider {@link IColorProvider} that is used to get background
	 *                        color
	 * @param drawingModel    reference to {@link DrawingModel} where new objects
	 *                        get stored
	 * @param canvas          reference to {@link JDrawingCanvas} upon
	 *                        {@link GeometricalObject} are drawn
	 */
	public FCircleTools(IColorProvider fgColorProvider, IColorProvider bgColorProvider, DrawingModel drawingModel,
			JDrawingCanvas canvas) {
		this.fgColorProvider = fgColorProvider;
		this.drawingModel = drawingModel;
		this.canvas = canvas;
		this.bgColorProvider = bgColorProvider;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (beginCircle) {
			start = e.getPoint();
			beginCircle = false;
		} else {
			circle = new FilledCircle(start, e.getPoint(), fgColorProvider.getCurrentColor(),
					bgColorProvider.getCurrentColor());
			beginCircle = true;
			drawingModel.add(circle);
		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (!beginCircle) {
			circle = new FilledCircle(start, e.getPoint(), fgColorProvider.getCurrentColor(),
					bgColorProvider.getCurrentColor());
			canvas.repaint();
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

	@Override
	public void paint(Graphics2D g2d) {
		if (!beginCircle) {
			g2d.setColor(circle.getFillColor());
			double radius = circle.getCenter().distance(circle.getOther());
			g2d.fillOval((int) (circle.getCenter().x - radius), (int) (circle.getCenter().y - radius), (int) radius * 2,
					(int) radius * 2);

			g2d.setColor(circle.getOutlineColor());
			g2d.drawOval((int) (circle.getCenter().x - radius), (int) (circle.getCenter().y - radius), (int) radius * 2,
					(int) radius * 2);
		}
	}
}
