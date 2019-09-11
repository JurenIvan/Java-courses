package hr.fer.zemris.java.hw17.jvdraw.shapes;

/**
 * Interface that models {@link GeometricalObjectListener} and has method that
 * gets called when {@link GeometricalObject} gets changed
 * 
 * @author juren
 *
 */
@FunctionalInterface
public interface GeometricalObjectListener {
	/**
	 * Method that is called when {@link GeometricalObject} is changed
	 * 
	 * @param o reference to {@link GeometricalObject} that is changed
	 */
	public void geometricalObjectChanged(GeometricalObject o);
}