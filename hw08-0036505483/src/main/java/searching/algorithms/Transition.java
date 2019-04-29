package searching.algorithms;

public class Transition<S> {

	private S state;
	private int cost;

	/**
	 * @param state
	 * @param cost
	 */
	public Transition(S state, int cost) {
		super();
		this.state = state;
		this.cost = cost;
	}

	/**
	 * @return the state
	 */
	public S getState() {
		return state;
	}

	/**
	 * @return the cost
	 */
	public int getCost() {
		return cost;
	}

	
	

}
