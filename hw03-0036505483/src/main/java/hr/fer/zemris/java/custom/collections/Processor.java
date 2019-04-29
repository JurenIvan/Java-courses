package hr.fer.zemris.java.custom.collections;

/**
 * Processor interface with declared process method. It's a model of an object
 * capable of performing some operation on the passed object.
 * 
 * @author juren
 *
 */
public interface Processor {
	/**
	 * Functional method that takes value and does an action over it.
	 * 
	 * @param value to be taken action upon
	 */
	public void process(Object value);
}
