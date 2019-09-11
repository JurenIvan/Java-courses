package hr.fer.zemris.java.hw17.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.jvdraw.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.models.DrawingModel;

/**
 * Class that implements the {@link Tool} with all of its methods being empty.
 * Classes that implements this interface can override only the methods that
 * they want to use.
 * 
 * @author Marko
 *
 */
public abstract class AbstractTool implements Tool {

	/**
	 * {@link DrawingModel} that the tool is using to add the objects to.
	 */
	protected DrawingModel model;
	/**
	 * Canvas that is used for drawing the objects to.
	 */
	protected JDrawingCanvas canvas;
	/**
	 * Provider for foreground color
	 */
	protected IColorProvider fgColorProvider;

	/**
	 * Constructor
	 * 
	 * @param model           model that the tool is using to add the objects to
	 * @param canvas          canvas that is being drawn on
	 * @param fgColorProvider foreground color provider
	 */
	public AbstractTool(DrawingModel model, JDrawingCanvas canvas, IColorProvider fgColorProvider) {
		this.model = model;
		this.canvas = canvas;
		this.fgColorProvider = fgColorProvider;
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void paint(Graphics2D g2d) {
	}

}
