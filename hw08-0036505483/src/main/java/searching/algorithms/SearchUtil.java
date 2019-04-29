package searching.algorithms;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Class containing methods that are able to solve puzzle.
 * 
 * 
 * @author juren
 *
 */
public class SearchUtil {

	/**
	 * Implementation of bfs algorithm used to search for solution of puzzle
	 * 
	 * @param s0   Supplier whose get method is used to provide starting state
	 * @param succ Function whose apply method returns List of possible steps
	 * 
	 * @param goal Predicate whose test function is used to test whether provided
	 *             state represents complete solution
	 * @return Node that hold solution and reference to all other Nodes required to
	 *         get to that point.
	 */
	public static <S> Node<S> bfs(Supplier<S> s0, Function<S, List<Transition<S>>> succ, Predicate<S> goal) {

		List<Node<S>> toExplore = new LinkedList<Node<S>>();
		toExplore.add(new Node<S>(null, s0.get(), 0));

		while (!toExplore.isEmpty()) {
			Node<S> ni = toExplore.remove(0);
			if (goal.test(ni.getState()))
				return ni;
			for (var elem : succ.apply(ni.getState())) {
				toExplore.add(new Node<S>(ni, elem.getState(), ni.getCost() + elem.getCost()));
			}
		}
		return null;
	}

	/**
	 * Smart implementation of bfs algorithm used to search for solution of puzzle.
	 * Uses HashSet to determine whether it has already been at certain pixel
	 * 
	 * 
	 * @param s0   Supplier whose get method is used to provide starting state
	 * @param succ Function whose apply method returns List of possible steps
	 * 
	 * @param goal Predicate whose test function is used to test whether provided
	 *             state represents complete solution
	 * @return Node that hold solution and reference to all other Nodes required to
	 *         get to that point.
	 */
	public static <S> Node<S> bfsv(Supplier<S> s0, Function<S, List<Transition<S>>> succ, Predicate<S> goal) {

		List<Node<S>> toExplore = new LinkedList<Node<S>>();
		Set<S> explored = new HashSet<>();
		toExplore.add(new Node<S>(null, s0.get(), 0));

		while (!toExplore.isEmpty()) {
			Node<S> ni = toExplore.remove(0);
			if (goal.test(ni.getState()))
				return ni;
			for (var elem : succ.apply(ni.getState())) {
				if (explored.add(elem.getState())) {
					toExplore.add(new Node<S>(ni, elem.getState(), ni.getCost() + elem.getCost()));
				}
			}
		}
		return null;
	}
}
