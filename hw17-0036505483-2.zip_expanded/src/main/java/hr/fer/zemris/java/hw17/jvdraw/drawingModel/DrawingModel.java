package hr.fer.zemris.java.hw17.jvdraw.drawingModel;

import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObject;

/**
 * Interface describing drawing model. Has bundle of methods used for data
 * manipulation such as getObject(index), add(GeometricalObject), remove etc.
 * 
 * @author juren
 *
 */
public interface DrawingModel {
	/**
	 * method used to return quantity of Geometrical objects in {@link DrawingModel}
	 * 
	 * @return size
	 */
	public int getSize();

	/**
	 * Method for retrieving specific {@link GeometricalObject} stored in
	 * {@link DrawingModel}
	 * 
	 * @param index of object
	 * @return object at index
	 */
	public GeometricalObject getObject(int index);

	/**
	 * Method that adds {@link GeometricalObject} to the last place of list of
	 * {@link GeometricalObject} from {@link DrawingModel}
	 * 
	 * @param object that is added
	 */
	public void add(GeometricalObject object);

	/**
	 * Method that removes {@link GeometricalObject} from the list of
	 * {@link GeometricalObject} from {@link DrawingModel}
	 * 
	 * @param object
	 */
	public void remove(GeometricalObject object);

	/**
	 * Method that changes the order of 2 {@link GeometricalObject}. Their order is
	 * important for overlapping
	 * 
	 * @param object object that is moved
	 * @param offset +-1 (up or down the list)
	 */
	public void changeOrder(GeometricalObject object, int offset);

	/**
	 * Method that returns index of provided {@link GeometricalObject} if such is
	 * stored in {@link DrawingModel}. if not, returns -1
	 * 
	 * @param object that is searched for
	 * @return index
	 */
	public int indexOf(GeometricalObject object);

	/**
	 * Clears the {@link JDravingCanvas}
	 */
	public void clear();

	/**
	 * Clears the modificationFlag of {@link DrawingModel}. (Used for save/save as
	 * functionalities)
	 */
	public void clearModifiedFlag();

	/**
	 * Returns modification flag state
	 * 
	 * @return modification flag state
	 */
	public boolean isModified();

	/**
	 * Adds a {@link DrawingModelListener} to it's set of listeners
	 * 
	 * @param l listener that is added
	 */
	public void addDrawingModelListener(DrawingModelListener l);

	/**
	 * Removes a {@link DrawingModelListener} from it's set of listeners
	 * 
	 * @param l listener that is added
	 */
	public void removeDrawingModelListener(DrawingModelListener l);
}
