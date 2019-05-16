package hr.fer.zemris.java.gui.calc.model;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JCheckBox;

/**
 * Implementation of JCheckBox that has listeners.
 * 
 * @author juren
 *
 */
public class JCheckBoxImpl extends JCheckBox {

	private static final long serialVersionUID = 1L;
	/**
	 * Set of listeners
	 */
	private Set<NameListener> listeners;

	/**
	 * Standard constructor for {@link JCheckBoxImpl}
	 */
	public JCheckBoxImpl() {
		listeners = new HashSet<>();
		addActionListener((e) -> notifyObservers());
	}

	/**
	 * Method that notifies registered observers
	 */
	private void notifyObservers() {
		for (var o : listeners) {
			o.valueChanged();
		}
	}

	/**
	 * Method that adds listener to set
	 * 
	 * @param nl new listener
	 */
	public void addListener(NameListener nl) {
		listeners.add(nl);
	}

	/**
	 * Method to remove listener out of set
	 * 
	 * @param nl listener that will be removed
	 */
	public void removeListener(NameListener nl) {
		listeners.remove(nl);
	}

}
