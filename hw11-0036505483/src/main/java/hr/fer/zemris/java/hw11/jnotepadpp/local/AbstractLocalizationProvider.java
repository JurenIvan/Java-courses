package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.HashSet;
import java.util.Set;

/**
 * Abstract class implementing {@link ILocalizationProvider}. Has {@link Set} of
 * listeners and methods to add, remove or fire them
 * 
 * @author juren
 *
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {

	/**
	 * Collection of Listeners
	 */
	private Set<ILocalizationListener> listeners;

	/**
	 * Standard constructor
	 */
	public AbstractLocalizationProvider() {
		listeners = new HashSet<>();
	}

	/**
	 * Method for adding a new {@link ILocalizationProvider} into collection
	 */
	public void addLocalizationListener(ILocalizationListener l) {
		HashSet<ILocalizationListener> newListeners = new HashSet<ILocalizationListener>(listeners);
		newListeners.add(l);
		listeners = newListeners;
	}

	/**
	 * Method for removing a {@link ILocalizationProvider} from collection
	 */
	public void removeLocalizationListener(ILocalizationListener l) {
		HashSet<ILocalizationListener> newListeners = new HashSet<ILocalizationListener>(listeners);
		newListeners.remove(l);
		listeners = newListeners;
	}

	/**
	 * Method that notifies all registered listeners
	 */
	public void fire() {
		for (var l : listeners) {
			l.localizationChanged();
		}
	}
}
