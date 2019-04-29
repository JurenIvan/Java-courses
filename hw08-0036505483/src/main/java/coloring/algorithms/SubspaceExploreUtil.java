package coloring.algorithms;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class SubspaceExploreUtil {

	public static <S> void dfs(Supplier<S> s0, Consumer<S> process, Function<S, List<S>> succ,
			Predicate<S> acceptable) {

		List<S> toExplore = new LinkedList<>();
		toExplore.add(s0.get());

		while (!toExplore.isEmpty()) {
			S si = toExplore.remove(0);
			if (!acceptable.test(si))
				continue;
			process.accept(si);
			toExplore.addAll(0, succ.apply(si));
		}

	}

	public static <S> void bfs(Supplier<S> s0, Consumer<S> process, Function<S, List<S>> succ,
			Predicate<S> acceptable) {

		List<S> toExplore = new LinkedList<>();
		toExplore.add(s0.get());

		while (!toExplore.isEmpty()) {
			S si = toExplore.remove(0);
			if (!acceptable.test(si))
				continue;
			process.accept(si);
			toExplore.addAll(toExplore.size(), succ.apply(si));
		}

	}

	public static <S> void bfsv(Supplier<S> s0, Consumer<S> process, Function<S, List<S>> succ,
			Predicate<S> acceptable) {

		List<S> toExplore = new LinkedList<>();
		Set<S> explored = new HashSet<>();
		toExplore.add(s0.get());
		explored.add(s0.get());

		while (!toExplore.isEmpty()) {
			S si = toExplore.remove(0);
			if (!acceptable.test(si))
				continue;
			process.accept(si);
			List<S> children = succ.apply(si);
			children.removeAll(explored);
			toExplore.addAll(toExplore.size(), children);
			explored.addAll(succ.apply(si));
		}

	}

}
