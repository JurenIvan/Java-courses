package hr.fer.zemris.java.hw17.jvdraw.drawingModel;

/**
 * Interface that holds methods that every {@link DrawingModelListener} should
 * have. Those are :objectsAdded, objectsRemoved, objectsChanged. their names
 * are really related to their purpouse.
 * 
 * @author juren
 *
 */
public interface DrawingModelListener {
	/**
	 * Method called when {@link DrawingModel} adds a new object
	 * 
	 * @param source {@link DrawingModel} that had change
	 * @param index0 not used
	 * @param index1 not used
	 */
	public void objectsAdded(DrawingModel source, int index0, int index1);

	/**
	 * Method called when {@link DrawingModel} removes an object
	 * 
	 * @param source {@link DrawingModel} that had change
	 * @param index0 not used
	 * @param index1 not used
	 */
	public void objectsRemoved(DrawingModel source, int index0, int index1);

	/**
	 * Method called when {@link DrawingModel} changes certain object.
	 * 
	 * @param source {@link DrawingModel} that had change
	 * @param index0 not used
	 * @param index1 not used
	 */
	public void objectsChanged(DrawingModel source, int index0, int index1);
}
