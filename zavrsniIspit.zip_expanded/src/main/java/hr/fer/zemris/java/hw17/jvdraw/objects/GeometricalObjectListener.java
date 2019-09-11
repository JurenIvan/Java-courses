package hr.fer.zemris.java.hw17.jvdraw.objects;

/**
 * Interface that should be implemented by the objects that want to know the
 * state of {@link GeometricalObject} and when it changes.
 * 
 * @author Marko
 *
 */
public interface GeometricalObjectListener {

	/**
	 * Method that is called when the geometric object changes
	 * 
	 * @param o object that changed
	 */
	public void geometricalObjectChanged(GeometricalObject o);

}
