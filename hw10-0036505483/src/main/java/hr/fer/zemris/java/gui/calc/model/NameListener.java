package hr.fer.zemris.java.gui.calc.model;

/**
 * Interface used to create observer/listener
 * 
 * @author juren
 *
 */
@FunctionalInterface
public interface NameListener {

	/**
	 * Method with which object notfies that the value has changed;
	 */
	public void valueChanged();

}
