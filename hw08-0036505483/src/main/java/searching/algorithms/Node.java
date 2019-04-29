package searching.algorithms;

import java.util.Objects;

/**
 * Class that models data Structure that contains State, reference to
 * parentState node, and holds integer representing cost.
 * 
 * @author juren
 *
 * @param <S> S represents Type of state
 */
public class Node<S> {

	/**
	 * Variable that stores current state
	 */
	private S state;
	/**
	 * Variable that stores cost to get to current state
	 */
	private double cost;
	/**
	 * Variable that stores reference to previous state
	 */
	private Node<S> parentState;

	/**
	 * Standard getter for stored state
	 * 
	 * @return current state
	 */
	public S getState() {
		return state;
	}

	/**
	 * Standard getter for cost up until this point
	 * 
	 * @return current cost
	 */
	public double getCost() {
		return cost;
	}

	/**
	 * Standard getter for parent Node reference
	 * 
	 * @return reference to parent
	 */
	public Node<S> getParent() {
		return parentState;
	}

	/**
	 * Constructor for Node
	 * 
	 * @param state       Variable that stores current state
	 * @param cost        Variable that stores cost to get to current state
	 * @param parentState Variable that stores reference to previous state
	 * @throws NullPointerException if current state is null
	 */
	public Node(Node<S> parentState, S state, double cost) {
		Objects.requireNonNull(state, "state referenced can not be null");
		this.state = state;
		this.cost = cost;
		this.parentState = parentState;
	}

}
