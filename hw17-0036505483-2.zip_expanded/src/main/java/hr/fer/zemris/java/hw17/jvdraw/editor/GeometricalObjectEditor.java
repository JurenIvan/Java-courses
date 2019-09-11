package hr.fer.zemris.java.hw17.jvdraw.editor;

import javax.swing.JPanel;

import hr.fer.zemris.java.hw17.jvdraw.drawingModel.DrawingModel;

/**
 * Class that models custom {@link JPanel} used for editing of already existing
 * Lines.
 * 
 * @author juren
 *
 */
public abstract class GeometricalObjectEditor extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * Checks whether provided parameters are valid throws exception if not
	 */
	public abstract void checkEditing();

	/**
	 * Method that applies changes to {@link DrawingModel} if all are valid.
	 */
	public abstract void acceptEditing();
}
