package searching.algorithms;

import java.util.Objects;

public class Node<S> {

	private S state;
	private double cost;
	private Node<S> parentState;

	public S getState() {
		return state;
	}

	public double getCost() {
		return cost;
	}

	public Node<S> getParent() {
		return parentState;
	}

	/**
	 * @param state
	 * @param cost
	 * @param parentState
	 */
	public Node(Node<S> parentState,S state, double cost) {
		super();
		this.state = state;
		this.cost = cost;
		this.parentState = parentState;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(parentState, state);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Node))
			return false;
		Node other = (Node) obj;
		return Objects.equals(parentState, other.parentState) && Objects.equals(state, other.state);
	}

	
	
	
	
}
