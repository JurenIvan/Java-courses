package hr.fer.zemris.java.hw17.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.jvdraw.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.models.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.objects.Line;

/**
 * Class that is extending {@link AbstractTool} and overrides the methods
 * {@link #mouseClicked(MouseEvent)} and {@link #mouseMoved(MouseEvent)} so that
 * canvas is updated when the user does one of those two actions. Those events
 * are modeling the {@link Line} object.
 * 
 * @author Marko
 *
 */
public class LineTool extends AbstractTool {

	/**
	 * Starting point of the line that the user defined
	 */
	private Point firstPoint;
	/**
	 * Second point of the line that the user defined
	 */
	private Point secondPoint;

	/**
	 * Constructor
	 * 
	 * @param model           model that the tool is using to add the objects to
	 * @param canvas          canvas that is being drawn on
	 * @param fgColorProvider foreground color provider
	 */
	public LineTool(DrawingModel model, JDrawingCanvas canvas, IColorProvider fgColorProvider) {
		super(model, canvas, fgColorProvider);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (firstPoint == null) {
			firstPoint = e.getPoint();
		} else if (secondPoint != null) {
			Line line = new Line(firstPoint, secondPoint, fgColorProvider.getCurrentColor());
			model.add(line);
			firstPoint = secondPoint = null;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (firstPoint != null) {
			secondPoint = e.getPoint();
			canvas.repaint();
		}
	}

	@Override
	public void paint(Graphics2D g2d) {
		if (firstPoint != null && secondPoint != null) {
			g2d.setColor(fgColorProvider.getCurrentColor());
			g2d.drawLine(firstPoint.x, firstPoint.y, secondPoint.x, secondPoint.y);
		}
	}

}
