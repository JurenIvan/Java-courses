package hr.fer.zemris.java.hw17.jvdraw.models;

/**
 * Models a listener to the {@link DrawingModel} that is notified every time
 * that the model changes.
 * 
 * @author Marko
 *
 */
public interface DrawingModelListener {

	/**
	 * Method that is called when the objects are added to the model
	 * 
	 * @param source model in which something was added to
	 * @param index0 index where the object was added
	 * @param index1 index where the object was added
	 */
	public void objectsAdded(DrawingModel source, int index0, int index1);

	/**
	 * Method that is called when the objects are removed from the model
	 * 
	 * @param source model in which something was added to
	 * @param index0 index of the object that was removed
	 * @param index1 index of the object that was removed
	 */
	public void objectsRemoved(DrawingModel source, int index0, int index1);

	/**
	 * Method that is called when the objects change in the model
	 * 
	 * @param source model in which something was added to
	 * @param index0 index of the first object that changed
	 * @param index1 index of the last object that changed
	 */
	public void objectsChanged(DrawingModel source, int index0, int index1);
}
