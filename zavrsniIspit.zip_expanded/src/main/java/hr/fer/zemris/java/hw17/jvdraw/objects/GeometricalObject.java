package hr.fer.zemris.java.hw17.jvdraw.objects;

import java.util.HashSet;
import java.util.Set;

import hr.fer.zemris.java.hw17.jvdraw.editing.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.objects.visitors.GeometricalObjectVisitor;

/**
 * Class modeling geometric objects that can create
 * {@link GeometricalObjectEditor} that is used to editing objects. It also
 * contains method {@link #accept(GeometricalObjectVisitor)}, being the Element
 * in the visitor design pattern.
 * 
 * @author Marko
 *
 */
public abstract class GeometricalObject {

	/**
	 * Listeners that are notified when object changes.
	 */
	private Set<GeometricalObjectListener> listeners;

	/**
	 * Constructor for geometric objects.
	 */
	public GeometricalObject() {
		this.listeners = new HashSet<>();
	}

	/**
	 * Method that creates an object that knows how to edit geometric object.
	 * 
	 * @return {@link GeometricalObjectEditor} for this object
	 */
	public abstract GeometricalObjectEditor createGeometricalObjectEditor();

	/**
	 * Method that calls the appropriate method in the
	 * {@link GeometricalObjectVisitor}.
	 * 
	 * @param v visitor
	 */
	public abstract void accept(GeometricalObjectVisitor v);

	/**
	 * Method that registers the listener to this object.
	 * 
	 * @param l listener
	 */
	public void addGeometricalObjectListener(GeometricalObjectListener l) {
		listeners.add(l);
	}

	/**
	 * Method that deregisters the listener from this object
	 * 
	 * @param l listener
	 */
	public void removeGeometricalObjectListener(GeometricalObjectListener l) {
		listeners.remove(l);
	}

	/**
	 * Method that notifies all the listeners that this object has changed.
	 */
	void notifyObjectChanged() {
		for (GeometricalObjectListener listener : listeners) {
			listener.geometricalObjectChanged(this);
		}
	}

}
