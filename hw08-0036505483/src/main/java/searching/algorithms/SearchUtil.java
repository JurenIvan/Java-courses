package searching.algorithms;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class SearchUtil {

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

public static <S> Node<S> bfsv(Supplier<S> s0, Function<S, List<Transition<S>>> succ, Predicate<S> goal) {
		
		List<Node<S>> toExplore = new LinkedList<Node<S>>();
		Set<S> explored =new HashSet<>();
		toExplore.add(new Node<S>(null,s0.get() , 0));
		
		while(!toExplore.isEmpty()) {
			Node<S>	ni=toExplore.remove(0);
			if(goal.test(ni.getState())) return ni;
			for(var elem:succ.apply(ni.getState())) {
				if(explored.add(elem.getState())){
				toExplore.add(new Node<S>(ni,elem.getState(),ni.getCost()+elem.getCost()));
				}
			}
		}	
	return null;
	}
}
