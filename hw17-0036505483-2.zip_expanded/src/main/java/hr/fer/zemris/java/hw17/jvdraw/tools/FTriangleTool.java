package hr.fer.zemris.java.hw17.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.jvdraw.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.colorArea.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.drawingModel.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FilledTriangle;

public class FTriangleTool implements Tool {

	private IColorProvider fillColorProvider;
	private IColorProvider outColorProvider;
	private DrawingModel drawingModel;
	private boolean startNew = true;
	private Point first;
	private Point second;
	private Point third;
	private JDrawingCanvas canvas;

	private FilledTriangle filledTriangle;

	@Override
	public void mouseClicked(MouseEvent e) {
		if (startNew) {
			first = e.getPoint();
			startNew = false;
			second = null;
			third = null;
		} else if (second == null) {
			second = e.getPoint();
		} else if (third == null) {
			third = e.getPoint();
			int[] x = new int[3];
			int[] y = new int[3];

			x[0] = first.x;
			x[1] = second.x;
			x[2] = third.x;

			y[0] = first.y;
			y[1] = second.y;
			y[2] = third.y;

			filledTriangle = new FilledTriangle(fillColorProvider.getCurrentColor(), outColorProvider.getCurrentColor(),x, y);
			drawingModel.add(filledTriangle);
			startNew = true;
		}

	}

	public FTriangleTool(IColorProvider fillColorProvider, IColorProvider outColorProvider, DrawingModel drawingModel,
			JDrawingCanvas canvas) {
		this.fillColorProvider = fillColorProvider;
		this.drawingModel = drawingModel;
		this.canvas = canvas;
		this.outColorProvider = outColorProvider;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
//		if (!beginCircle) {
//			circle = new FilledCircle(start, e.getPoint(), fgColorProvider.getCurrentColor(),
//					bgColorProvider.getCurrentColor());
			canvas.repaint();
//		}
	}

	@Override
	public void paint(Graphics2D g2d) {
		if (!startNew) {
			g2d.setColor(filledTriangle.getFillColor());
			g2d.fillPolygon(filledTriangle.getX(), filledTriangle.getY(), 3);
			g2d.setColor(filledTriangle.getOutlineColor());
			g2d.drawPolygon(filledTriangle.getX(), filledTriangle.getY(), 3);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

}
