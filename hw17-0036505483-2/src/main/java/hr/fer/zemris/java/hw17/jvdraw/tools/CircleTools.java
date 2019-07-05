package hr.fer.zemris.java.hw17.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.jvdraw.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.colorArea.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.drawingModel.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Circle;

public class CircleTools implements Tool {

	private IColorProvider colorProvider;
	private DrawingModel drawingModel;
	private boolean beginCircle = true;
	private Point start;
	private JDrawingCanvas canvas;

	private Circle circle;

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
