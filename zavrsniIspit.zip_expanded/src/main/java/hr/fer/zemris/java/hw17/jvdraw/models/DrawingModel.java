package hr.fer.zemris.java.hw17.jvdraw.models;

import hr.fer.zemris.java.hw17.jvdraw.objects.GeometricalObject;

/**
 * Interface that models an object that contains the information about all the
 * objects that should be drawn on the canvas, as well as the methods that
 * allows the user to change the list of those objects.
 * 
 * @author Marko
 *
 */
public interface DrawingModel {

	/**
	 * Number of objects currently stored in model
	 * 
	 * @return number of objects
	 */
	public int getSize();

	/**
	 * Method that gets the object with the given index
	 * 
	 * @param index idnex of wanted object
	 * @return object at the given index
	 */
	public GeometricalObject getObject(int index);

	/**
	 * Add the given object to the models list of objects
	 * 
	 * @param object object that should be added
	 */
	public void add(GeometricalObject object);

	/**
	 * Removes the given object from the models list of objects
	 * 
	 * @param object object that should be removed
	 */
	public void remove(GeometricalObject object);

	/**
	 * Moves the object given as the argument of the method for offset (for example
	 * if index of object is 2 and offset is 1, second and third objects are
	 * swapped).
	 * 
	 * @param object object that should be swapped
	 * @param offset offset of the object that should be swapped with object
	 */
	public void changeOrder(GeometricalObject object, int offset);

	/**
	 * Getter for the index of the given object
	 * 
	 * @param object object whose index is wanted
	 * @return index of the given object
	 */
	public int indexOf(GeometricalObject object);

	/**
	 * Removes all the objects from the list of currently stored objects
	 */
	public void clear();

	/**
	 * Clears modification flag of the model
	 */
	public void clearModifiedFlag();

	/**
	 * Getter for modification flag
	 * 
	 * @return modification flag
	 */
	public boolean isModified();

	/**
	 * Method that registers a listener to the model
	 * 
	 * @param l listener that shuld be added
	 */
	public void addDrawingModelListener(DrawingModelListener l);

	/**
	 * Method that deregisters a listener from the model
	 * 
	 * @param l listener that should be removed
	 */
	public void removeDrawingModelListener(DrawingModelListener l);
}
