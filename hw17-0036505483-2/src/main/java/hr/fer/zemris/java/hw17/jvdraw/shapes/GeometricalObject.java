package hr.fer.zemris.java.hw17.jvdraw.shapes;

import java.util.HashSet;
import java.util.Set;

import hr.fer.zemris.java.hw17.jvdraw.editor.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectVisitor;

public abstract class GeometricalObject {

	private Set<GeometricalObjectListener> listeners;

	public GeometricalObject() {
		listeners = new HashSet<>();
	}

	public abstract void accept(GeometricalObjectVisitor v);

	public abstract GeometricalObjectEditor createGeometricalObjectEditor();

	public void addGeometricalObjectListener(GeometricalObjectListener l) {
		Set<GeometricalObjectListener> newListeners = new HashSet<>(listeners);
		newListeners.add(l);
		listeners = newListeners;

	}

	public void removeGeometricalObjectListener(GeometricalObjectListener l) {
		Set<GeometricalObjectListener> newListeners = new HashSet<>(listeners);
		newListeners.remove(l);
		listeners = newListeners;
	}

	protected void notifyListeners() {
		for (var listener : listeners) {
			listener.geometricalObjectChanged(this);
		}
	}
}
