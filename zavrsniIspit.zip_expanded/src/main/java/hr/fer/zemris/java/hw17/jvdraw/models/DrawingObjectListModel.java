package hr.fer.zemris.java.hw17.jvdraw.models;

import javax.swing.AbstractListModel;

import hr.fer.zemris.java.hw17.jvdraw.objects.GeometricalObject;

/**
 * Method that extends {@link AbstractListModel} with {@link GeometricalObject}s
 * and is using the {@link DrawingModel} in the background to delegate all of
 * its work.
 * 
 * @author Marko
 *
 */
public class DrawingObjectListModel extends AbstractListModel<GeometricalObject> implements DrawingModelListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Model that is used to show the data in the list.
	 */
	private DrawingModel model;

	/**
	 * Constructor
	 * 
	 * @param model model that is used for implementation of all the methods in this
	 *              class
	 */
	public DrawingObjectListModel(DrawingModel model) {
		this.model = model;
		model.addDrawingModelListener(this);
	}

	@Override
	public int getSize() {
		return model.getSize();
	}

	@Override
	public GeometricalObject getElementAt(int index) {
		return model.getObject(index);
	}

	

	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		this.fireIntervalAdded(source, index0, index1);
	}

	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		this.fireIntervalRemoved(source, index0, index1);
	}

	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
		this.fireContentsChanged(source, index0, index1);
	}

}
