package hr.fer.zemris.java.hw17.jvdraw.editing;

import javax.swing.JPanel;

import hr.fer.zemris.java.hw17.jvdraw.objects.GeometricalObject;

/**
 * {@link JPanel} that allows user to input some data and check if the data for
 * that implementation of {@link GeometricalObject} is valid.
 * 
 * @author Marko
 *
 */
public abstract class GeometricalObjectEditor extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Method that checks if every field is filled correctly. If something is not
	 * filled properly, this method will throw an exception.
	 */
	public abstract void checkEditing();

	/**
	 * Method that accepts the user input and fills the objects with the new values.
	 * This method should be called after the {@link #checkEditing()} method.
	 */
	public abstract void acceptEditing();
}
