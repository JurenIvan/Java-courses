package searching.algorithms;

import java.util.Objects;

/**
 * Class that represents data structure containing two private variables; state
 * and cost, constructor for class, and getters for varialbes
 * 
 * @author juren
 *
 * @param <S>
 */
public class Transition<S> {

	/**
	 * Variable that stores current state
	 */
	private S state;
	/**
	 * Variable that stores cost to get to current state
	 */
	private int cost;

	/**
	 * Constructor for Transition.
	 * @param state Variable that stores current state
	 * @param cost Variable that stores cost to get to current state 
	 * @throws NullPointerException is current state is null reference
	 */
	public Transition(S state, int cost) {
		Objects.requireNonNull(state);
		this.state = state;
		this.cost = cost;
	}

	/**
	 * Standard getter for state
	 * @return the state
	 */
	public S getState() {
		return state;
	}

	/**
	 * Standard getter for cost
	 * @return the cost
	 */
	public int getCost() {
		return cost;
	}

}
