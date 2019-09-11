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

/**
 * Class that holds custom implementation of {@link JComponent} that is used as
 * canvas for drawing. Has appropriate mouse listeners.
 * 
 * @author juren
 *
 */
public class JDrawingCanvas extends JComponent implements DrawingModelListener {
	private static final long serialVersionUID = 1L;

	/**
	 * Reference to {@link DrawingModel} used to hold data
	 */
	private DrawingModel drawingModel;
	/**
	 * Supplier of a current tool used for drawing on canvas
	 */
	private Supplier<Tool> toolSupplier;

	/**
	 * Constructor that registers itself to list of {@link DrawingModelListener} and
	 * registers mouse listeners.
	 * 
	 * @param drawingModel Reference to {@link DrawingModel} used to hold data
	 * @param toolSupplier Supplier of a current tool used for drawing on canvas
	 */
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
