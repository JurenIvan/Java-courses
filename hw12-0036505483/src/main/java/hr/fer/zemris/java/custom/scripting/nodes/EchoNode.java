package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.tokens.Element;

/**
 * EchoNode class – a node representing a command which generates some textual
 * output dynamically. It inherits from Node class. Has an array of Elements and
 * method to get those and default constructor;
 * 
 * @author juren
 *
 */
public class EchoNode extends Node {
	/**
	 * Array that stores {@link Element}-s of Node.
	 */
	private Element[] elements;

	/**
	 * Default constructor that sets elements.
	 * 
	 * @param elements array that is stored in class.
	 */
	public EchoNode(Element... elements) {
		this.elements = elements;
	}

	/**
	 * Getter for elements array.
	 * 
	 * @return array of elements
	 */
	public Element[] getElements() {
		return elements;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{$= ");

		for (var elem : elements) {
			sb.append(elem.toString());
			sb.append(" ");
		}
		sb.append("$}");

		return sb.toString();
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitEchoNode(this);
	}

}
