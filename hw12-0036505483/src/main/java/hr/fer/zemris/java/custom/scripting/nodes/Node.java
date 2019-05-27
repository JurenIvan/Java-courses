package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Node is a base class for all graph nodes. Nodes are used to represent parsed
 * structure of text. Node class has few methods that are used by all other.
 * 
 * Has only a default constructor.
 * 
 * @author juren
 *
 */
public abstract class Node {

	/**
	 * ArrayIndexedCollection used to store children nodes of this node. Actual
	 * instance of collection is created on demand.
	 */
	private List<Node> childrenCollection;

	/**
	 * Method that adds child node to this node. If it's a first child, collection
	 * is created. (Creation on Demand)
	 * 
	 * @param child Node that's added into childrenCollection
	 * 
	 * @throws NullPointerException if null is referenced
	 */
	public void addChildNode(Node child) {
		Objects.requireNonNull(child);
		if (childrenCollection == null) {
			childrenCollection = new ArrayList<Node>();
		}
		childrenCollection.add(child);
		
	}

	/**
	 * Method that returns current number of children.
	 * 
	 * @return integer number of children in childrenCollection
	 */
	public int numberOfChildren() {
		if(childrenCollection==null)
			return 0;
		return childrenCollection.size();
	}

	/**
	 * Method that returns node stored at index index in collection
	 * 
	 * @param index of node stored in collection (starts at 0)
	 * @return Node stored at index index
	 * 
	 * @throws IndexOutOfBoundsException if index is not legal. Legal indexes are 0
	 *                                   to (number of children -1)
	 */
	public Node getChild(int index) {
		Objects.checkIndex(index, childrenCollection.size());
		return (Node) childrenCollection.get(index);
	}
	
	/**
	 * Used to display content stored in Node and all of it's children (if it has any)
	 * 
	 */
	@Override
	public String toString() {
		return "";
	}
	
	public abstract void accept(INodeVisitor visitor);
	

}
