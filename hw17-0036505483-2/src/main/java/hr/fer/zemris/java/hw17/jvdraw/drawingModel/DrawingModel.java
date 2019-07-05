package hr.fer.zemris.java.hw17.jvdraw.drawingModel;

import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObject;

public interface DrawingModel {
	public int getSize();

	public GeometricalObject getObject(int index);

	public void add(GeometricalObject object);

	public void remove(GeometricalObject object);

	public void changeOrder(GeometricalObject object, int offset);

	public int indexOf(GeometricalObject object);

	public void clear();

	public void clearModifiedFlag();

	public boolean isModified();

	public void addDrawingModelListener(DrawingModelListener l);

	public void removeDrawingModelListener(DrawingModelListener l);
}
