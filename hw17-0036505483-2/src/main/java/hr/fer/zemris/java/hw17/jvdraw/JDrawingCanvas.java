package hr.fer.zemris.java.hw17.jvdraw;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.function.Supplier;

import javax.swing.JComponent;

import hr.fer.zemris.java.hw17.jvdraw.drawingModel.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.drawingModel.DrawingModelListener;
import hr.fer.zemris.java.hw17.jvdraw.tools.Tool;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectPainter;

public class JDrawingCanvas extends JComponent implements DrawingModelListener {
	private static final long serialVersionUID = 1L;

	private DrawingModel drawingModel;
	private Supplier<Tool> toolSupplier;

	public JDrawingCanvas(DrawingModel drawingModel, Supplier<Tool> toolSupplier) {
		this.drawingModel = drawingModel;
		this.toolSupplier = toolSupplier;

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				toolSupplier.get().mouseClicked(e);
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				toolSupplier.get().mouseMoved(e);
			}
		});

		drawingModel.addDrawingModelListener(this);
	}

	@Override
	protected void paintComponent(Graphics g) {
		GeometricalObjectPainter gop = new GeometricalObjectPainter(g);
		
		for (int i = 0; i < drawingModel.getSize(); i++) {
			drawingModel.getObject(i).accept(gop);
		}
		
		toolSupplier.get().paint((Graphics2D) g);
	}

	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		repaint();
	}

	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		repaint();
	}

	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
		repaint();
	}

}
