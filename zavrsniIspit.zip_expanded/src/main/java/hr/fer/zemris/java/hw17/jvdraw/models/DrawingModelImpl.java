package hr.fer.zemris.java.hw17.jvdraw.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hr.fer.zemris.java.hw17.jvdraw.objects.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.objects.GeometricalObjectListener;

/**
 * Implementation of the {@link DrawingModel} interface that allows user to add
 * the objects to this object. Allows {@link DrawingModelListener} to register
 * and notifies them every time this model changes. Implements
 * {@link GeometricalObjectListener} so that every object in this model can
 * notify it when it changes, so the model can update.
 * 
 * @author Marko
 *
 */
public class DrawingModelImpl implements DrawingModel, GeometricalObjectListener {

	/**
	 * List of currently stored {@link GeometricalObject}s.
	 */
	private List<GeometricalObject> objects;
	/**
	 * Set of registered listeners.
	 */
	private Set<DrawingModelListener> listeners;
	/**
	 * Flag that is set when anything changes in the model. Can be set to false only
	 * by calling the method {@link #clearModifiedFlag()}.
	 */
	private boolean modifiedFlag;

	/**
	 * Constructor
	 */
	public DrawingModelImpl() {
		this.objects = new ArrayList<>();
		this.listeners = new HashSet<>();
	}

	@Override
	public int getSize() {
		return objects.size();
	}

	@Override
	public GeometricalObject getObject(int index) {
		return objects.get(index);
	}

	@Override
	public void add(GeometricalObject object) {
		objects.add(object);
		object.addGeometricalObjectListener(this);
		notifyObjectAdded(objects.size() - 1, objects.size() - 1);
		modifiedFlag = true;
	}

	@Override
	public void remove(GeometricalObject object) {
		int index = objects.indexOf(object);
		objects.remove(index);
		notifyObjectRemoved(index, index);
		modifiedFlag = true;
	}

	@Override
	public void changeOrder(GeometricalObject object, int offset) {
		int position = objects.indexOf(object);
		try {
			Collections.swap(objects, position, position + offset);
			notifyObjectChanged(position, position + offset);
			modifiedFlag = true;
		} catch (IndexOutOfBoundsException exc) {
		}
	}

	@Override
	public int indexOf(GeometricalObject object) {
		return objects.indexOf(object);
	}

	@Override
	public void clear() {
		objects.clear();
	}

	@Override
	public void clearModifiedFlag() {
		modifiedFlag = false;
	}

	@Override
	public boolean isModified() {
		return modifiedFlag;
	}

	@Override
	public void addDrawingModelListener(DrawingModelListener l) {
		listeners.add(l);
	}

	@Override
	public void removeDrawingModelListener(DrawingModelListener l) {
		listeners.remove(l);
	}

	private void notifyObjectAdded(int start, int end) {
		for (DrawingModelListener l : listeners) {
			l.objectsAdded(this, start, end);
		}
	}

	private void notifyObjectRemoved(int start, int end) {
		for (DrawingModelListener l : listeners) {
			l.objectsRemoved(this, start, end);
		}
	}

	private void notifyObjectChanged(int start, int end) {
		for (DrawingModelListener l : listeners) {
			l.objectsChanged(this, start, end);
		}
	}

	@Override
	public void geometricalObjectChanged(GeometricalObject o) {
		int index = objects.indexOf(o);
		notifyObjectChanged(index, index);
	}

}
