package hr.fer.zemris.java.hw17.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw17.jvdraw.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.models.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.objects.Triangle;

public class TriangleTool extends AbstractTool {

	IColorProvider bgColorProvider;

	public List<Integer> x;
	public List<Integer> y;

	public TriangleTool(DrawingModel model, JDrawingCanvas canvas, IColorProvider fgColorProvider,
			IColorProvider bgColorProvider) {
		super(model, canvas, fgColorProvider);
		this.bgColorProvider = bgColorProvider;
		this.x = new ArrayList<>();
		this.y = new ArrayList<>();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (x.isEmpty()) {
			x.add(e.getPoint().x);
			y.add(e.getPoint().y);
		}else if (x.size() == 2) {
			x.add(e.getPoint().x);
			y.add(e.getPoint().y);
		} else if (x.size() == 3) {
			x.remove(2);
			y.remove(2);
			x.add(e.getPoint().x);
			y.add(e.getPoint().y);

			Triangle t = new Triangle(x, y, fgColorProvider.getCurrentColor(), bgColorProvider.getCurrentColor());
			model.add(t);
			x = new ArrayList<>();
			y = new ArrayList<>();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (x.size() == 1) {
			x.add(e.getPoint().x);
			y.add(e.getPoint().y);
			canvas.repaint();
		} else if (x.size() == 2) {
			x.remove(1);
			y.remove(1);
			x.add(e.getPoint().x);
			y.add(e.getPoint().y);
			canvas.repaint();
		} else if (x.size() == 3) {
			x.remove(2);
			y.remove(2);
			x.add(e.getPoint().x);
			y.add(e.getPoint().y);
			canvas.repaint();
		}
	}

	@Override
	public void paint(Graphics2D g2d) {
		if (x.size() == 2) {
			g2d.setColor(fgColorProvider.getCurrentColor());
			g2d.drawLine(x.get(0), y.get(0), x.get(1), y.get(1));
		} else if (x.size() == 3) {
			int[] xevi = x.stream().mapToInt(i -> i).toArray();
			int[] yevi = y.stream().mapToInt(i -> i).toArray();

			g2d.setColor(bgColorProvider.getCurrentColor());
			g2d.fillPolygon(xevi, yevi, 3);
			g2d.setColor(fgColorProvider.getCurrentColor());
			g2d.drawPolygon(xevi, yevi, 3);
		}
	}

}
