package coloring.algorithms;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Class containing methods that are able to paint area of drawing.
 * 
 * @author juren
 *
 */
public class SubspaceExploreUtil {

	/**
	 * Implementation of dfs algorithm used for filling areas of drawing
	 * 
	 * @param s0         Supplier whose get method is used to provide starting
	 *                   pixels
	 * @param process    Consumer whose accept method is used to color a pixel with
	 *                   a reference color
	 * @param succ       Function whose apply method returns List of neighboring
	 *                   pixels
	 * @param acceptable Predicate whose test function is used to test whether pixel
	 *                   needs coloring
	 */
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

	/**
	 * Implementation of bfs algorithm used for filling areas of drawing
	 *
	 * @param s0         Supplier whose get method is used to provide starting
	 *                   pixels
	 * @param process    Consumer whose accept method is used to color a pixel with
	 *                   a reference color
	 * @param succ       Function whose apply method returns List of neighboring
	 *                   pixels
	 * @param acceptable Predicate whose test function is used to test whether pixel
	 *                   needs coloring
	 */
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

	/**
	 * Smart implementation of bfs algorithm used for filling areas of drawing. Uses
	 * HashSet to determine whether it has already been at certain pixel
	 * 
	 * @param s0         Supplier whose get method is used to provide starting
	 *                   pixels
	 * @param process    Consumer whose accept method is used to color a pixel with
	 *                   a reference color
	 * @param succ       Function whose apply method returns List of neighboring
	 *                   pixels
	 * @param acceptable Predicate whose test function is used to test whether pixel
	 *                   needs coloring
	 */
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
