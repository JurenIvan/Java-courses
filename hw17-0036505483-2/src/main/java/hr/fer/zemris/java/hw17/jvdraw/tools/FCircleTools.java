package hr.fer.zemris.java.hw17.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.jvdraw.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.colorArea.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.drawingModel.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FilledCircle;

public class FCircleTools implements Tool {

	private IColorProvider bgColorProvider;
	private IColorProvider fgColorProvider;
	private DrawingModel drawingModel;
	private boolean beginCircle = true;
	private Point start;
	private JDrawingCanvas canvas;

	private FilledCircle circle;

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
