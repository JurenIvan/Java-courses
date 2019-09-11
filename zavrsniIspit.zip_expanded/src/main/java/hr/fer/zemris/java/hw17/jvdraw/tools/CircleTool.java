package hr.fer.zemris.java.hw17.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.jvdraw.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.models.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.objects.Circle;

/**
 * Class that is extending {@link AbstractTool} and overrides the methods
 * {@link #mouseClicked(MouseEvent)} and {@link #mouseMoved(MouseEvent)} so that
 * canvas is updated when the user does one of those two actions. Those events
 * are modeling the {@link Circle} object.
 * 
 * @author Marko
 *
 */
public class CircleTool extends AbstractTool {

	/**
	 * Center of the circle that the user defined
	 */
	private Point center;
	/**
	 * Radius of the circle.
	 */
	private int radius;

	/**
	 * Constructor
	 * 
	 * @param model           model that the tool is using to add the objects to
	 * @param canvas          canvas that is being drawn on
	 * @param fgColorProvider foreground color provider
	 */
	public CircleTool(DrawingModel model, JDrawingCanvas canvas, IColorProvider fgColorProvider) {
		super(model, canvas, fgColorProvider);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (center != null) {
			Circle circle = new Circle(center, radius, fgColorProvider.getCurrentColor());
			model.add(circle);
			center = null;
		} else {
			this.center = e.getPoint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (center != null) {
			Point p = e.getPoint();
			radius = (int) Math.sqrt(Math.pow(p.x - center.x, 2) + Math.pow(p.y - center.y, 2));
			canvas.repaint();
		}
	}

	@Override
	public void paint(Graphics2D g2d) {
		if (center != null) {
			g2d.setColor(fgColorProvider.getCurrentColor());
			g2d.drawOval(center.x - radius, center.y - radius, radius * 2, radius * 2);
		}
	}

}
