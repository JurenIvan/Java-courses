package hr.fer.zemris.java.hw17.jvdraw.drawingModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObjectListener;

/**
 * Implementation of {@link DrawingModel} that also is
 * {@link GeometricalObjectListener}
 * 
 * @author juren
 *
 */
public class DrawingModelImpl implements DrawingModel, GeometricalObjectListener {

	/**
	 * List of all {@link GeometricalObject} stored in this model
	 */
	private List<GeometricalObject> listOfGeometricalObjects;
	/**
	 * flag that tells us whether some changes has been made or notF
	 */
	private boolean modificationFlag;
	/**
	 * Set of listeners due to {@link GeometricalObjectListener}
	 */
	private Set<DrawingModelListener> listeners;

	/**
	 * Standard constructorF
	 */
	public DrawingModelImpl() {
		listOfGeometricalObjects = new ArrayList<>();
		modificationFlag = false;
		listeners = new HashSet<>();
	}

	@Override
	public int getSize() {
		return listOfGeometricalObjects.size();
	}

	@Override
	public GeometricalObject getObject(int index) {
		return listOfGeometricalObjects.get(index);
	}

	@Override
	public void add(GeometricalObject object) {
		listOfGeometricalObjects.add(object);
		this.modificationFlag = true;

		object.addGeometricalObjectListener(this);

		for (var listener : listeners) {
			listener.objectsAdded(this, 0, 0);
		}
	}

	@Override
	public void remove(GeometricalObject object) {
		int indexOfRemovedOne = listOfGeometricalObjects.indexOf(object);
		listOfGeometricalObjects.remove(indexOfRemovedOne);
		this.modificationFlag = true;

		for (var listener : listeners) {
			listener.objectsRemoved(this, indexOfRemovedOne, indexOfRemovedOne);
		}
	}

	@Override
	public void changeOrder(GeometricalObject object, int offset) {
		int prevIndex = listOfGeometricalObjects.indexOf(object);

		if (prevIndex + offset < 0 || prevIndex + offset >= listOfGeometricalObjects.size())
			return;
		Collections.swap(listOfGeometricalObjects, prevIndex, prevIndex + offset);

		for (var listener : listeners) {
			listener.objectsChanged(this, prevIndex, prevIndex + 1);
		}

	}

	@Override
	public int indexOf(GeometricalObject object) {
		return listOfGeometricalObjects.indexOf(object);
	}

	@Override
	public void clear() {
		for (DrawingModelListener l : listeners) {
			l.objectsRemoved(this, 0, listOfGeometricalObjects.size());
		}
		listOfGeometricalObjects.clear();
	}

	@Override
	public void clearModifiedFlag() {
		this.modificationFlag = false;
	}

	@Override
	public boolean isModified() {
		return this.modificationFlag;
	}

	@Override
	public void addDrawingModelListener(DrawingModelListener l) {
		Set<DrawingModelListener> newSet = new HashSet<>(listeners);
		newSet.add(l);
		listeners = newSet;
	}

	@Override
	public void removeDrawingModelListener(DrawingModelListener l) {
		Set<DrawingModelListener> newSet = new HashSet<>(listeners);
		newSet.remove(l);
		listeners = newSet;

	}

	@Override
	public void geometricalObjectChanged(GeometricalObject o) {
		int indexOfChangedOne = listOfGeometricalObjects.indexOf(o);
		for (var listener : listeners) {
			listener.objectsChanged(this, indexOfChangedOne, indexOfChangedOne);
		}
	}

}
