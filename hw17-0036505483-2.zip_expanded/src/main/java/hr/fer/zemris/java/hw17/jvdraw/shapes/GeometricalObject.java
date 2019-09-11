package hr.fer.zemris.java.hw17.jvdraw.shapes;

import java.util.HashSet;
import java.util.Set;

import hr.fer.zemris.java.hw17.jvdraw.editor.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectVisitor;

/**
 * Class that is superclass of all {@link GeometricalObject} and implements all
 * methods that are common for all {@link GeometricalObject}s.
 * 
 * @author juren
 *
 */
public abstract class GeometricalObject {

	/**
	 * Collection of listeners
	 */
	private Set<GeometricalObjectListener> listeners;

	/**
	 * Default Constructor
	 */
	public GeometricalObject() {
		listeners = new HashSet<>();
	}

	/**
	 * Method that accepts {@link GeometricalObjectVisitor}. Enables us to use
	 * Visitor design pattern.
	 * 
	 * @param v visitor used
	 */
	public abstract void accept(GeometricalObjectVisitor v);

	/**
	 * Method that creates {@link GeometricalObjectEditor} of specific kind for
	 * every {@link GeometricalObject} kind
	 * 
	 * @return {@link GeometricalObjectEditor} of specific kind
	 */
	public abstract GeometricalObjectEditor createGeometricalObjectEditor();

	/**
	 * Method that registers provided Listener
	 * 
	 * @param l listener that is saved
	 */
	public void addGeometricalObjectListener(GeometricalObjectListener l) {
		Set<GeometricalObjectListener> newListeners = new HashSet<>(listeners);
		newListeners.add(l);
		listeners = newListeners;

	}

	/**
	 * Method that de-registers provided Listener
	 * 
	 * @param l listener that is removed from set
	 */
	public void removeGeometricalObjectListener(GeometricalObjectListener l) {
		Set<GeometricalObjectListener> newListeners = new HashSet<>(listeners);
		newListeners.remove(l);
		listeners = newListeners;
	}

	/**
	 * Method that notifies allListeners that {@link GeometricalObject} changed
	 */
	protected void notifyListeners() {
		for (var listener : listeners) {
			listener.geometricalObjectChanged(this);
		}
	}
}
