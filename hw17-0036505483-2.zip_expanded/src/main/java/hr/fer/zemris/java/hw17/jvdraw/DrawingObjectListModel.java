package hr.fer.zemris.java.hw17.jvdraw;

import javax.swing.AbstractListModel;

import hr.fer.zemris.java.hw17.jvdraw.drawingModel.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.drawingModel.DrawingModelListener;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObject;

/**
 * Custom implementation of {@link AbstractListModel} that implements
 * {@link DrawingModelListener}. Result is Adapter that connects
 * {@link DrawingModel} and {@link AbstractListModel}. Used to list all
 * {@link GeometricalObject} that are shown in {@link JDrawingCanvas}.
 * 
 * @author juren
 *
 */
public class DrawingObjectListModel extends AbstractListModel<GeometricalObject> implements DrawingModelListener {
	private static final long serialVersionUID = 1L;

	/**
	 * Reference to {@link DrawingModel} used to hold data
	 */
	private DrawingModel dm;

	/**
	 * Standard Constructor that registers instance to {@link DrawingModelListener}
	 * 
	 * @param dm Reference to {@link DrawingModel} used to hold data
	 */
	public DrawingObjectListModel(DrawingModel dm) {
		this.dm = dm;
		dm.addDrawingModelListener(this);
	}

	@Override
	public int getSize() {
		return dm.getSize();
	}

	@Override
	public GeometricalObject getElementAt(int index) {
		return dm.getObject(index);
	}

	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		fireIntervalAdded(source, index0, index1);
	}

	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		fireIntervalRemoved(source, index0, index1);
	}

	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
		fireContentsChanged(source, index0, index1);
	}

}
