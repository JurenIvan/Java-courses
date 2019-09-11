package hr.fer.zemris.java.hw17.jvdraw;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Supplier;

import javax.swing.JComponent;

import hr.fer.zemris.java.hw17.jvdraw.models.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.models.DrawingModelListener;
import hr.fer.zemris.java.hw17.jvdraw.objects.visitors.GeometricalObjectPainter;
import hr.fer.zemris.java.hw17.jvdraw.tools.Tool;

/**
 * Component that is used for drawing. It implements
 * {@link DrawingModelListener} and every time that something changes in the
 * {@link DrawingModel} it is notified and repaints the picture. It allows user
 * to paint the picture based on the currently selected {@link Tool}. All the
 * work of drawing is redirected to the {@link Tool} itself.
 * 
 * @author Marko
 *
 */
public class JDrawingCanvas extends JComponent implements DrawingModelListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Supplier for the tool (currently selected tool in the {@link JVDraw}).
	 */
	private Supplier<Tool> toolSupplier;
	/**
	 * Model that this component is subscribed to.
	 */
	private DrawingModel model;

	/**
	 * Constructor that creates the mouse listeners that are fired when user wants
	 * to draw something on the canvas.
	 * 
	 * @param model    model that contains the objects that are drawn on the canvas
	 * @param supplier tool supplier
	 */
	public JDrawingCanvas(DrawingModel model, Supplier<Tool> supplier) {
		model.addDrawingModelListener(this);
		this.model = model;
		this.toolSupplier = supplier;
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				toolSupplier.get().mouseClicked(e);
			}
		});

		this.addMouseMotionListener(new MouseAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {
				toolSupplier.get().mouseMoved(e);
			}
		});

	}

	@Override
	protected void paintComponent(Graphics g) {

		GeometricalObjectPainter painter = new GeometricalObjectPainter((Graphics2D) g);
		for (int i = 0; i < model.getSize(); i++) {
			model.getObject(i).accept(painter);
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
