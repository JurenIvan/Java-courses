package hr.fer.zemris.java.hw17.jvdraw;

import javax.swing.AbstractListModel;

import hr.fer.zemris.java.hw17.jvdraw.drawingModel.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.drawingModel.DrawingModelListener;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObject;

public class DrawingObjectListModel extends AbstractListModel<GeometricalObject> implements DrawingModelListener {
	private static final long serialVersionUID = 1L;

	private DrawingModel dm;

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
