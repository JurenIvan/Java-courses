package hr.fer.zemris.java.hw17.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.jvdraw.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.colorArea.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.drawingModel.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Line;

public class LineTools implements Tool {

	private IColorProvider colorProvider;
	private DrawingModel drawingModel;
	private boolean beginLine = true;
	private Point start;
	private JDrawingCanvas canvas;

	private Line line;

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
