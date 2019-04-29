package searching.slagalica;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import searching.algorithms.Transition;

/**
 * Class that models puzzle and provides methods able to do basic operations
 * with puzzle config(getting initial setup, getting list of possible steps,
 * testing whether current config is final one.
 * 
 * @author juren
 *
 * @param <S>
 */
public class Slagalica<S> implements Supplier<KonfiguracijaSlagalice>,
		Function<KonfiguracijaSlagalice, List<Transition<KonfiguracijaSlagalice>>>, Predicate<KonfiguracijaSlagalice> {

	/**
	 * Variable that stores reference to current {@link KonfiguracijaSlagalice}
	 */
	private KonfiguracijaSlagalice reference;

	/**
	 * Constructor for {@link Slagalica}
	 * 
	 * @param reference Variable that stores reference to current
	 *                  {@link KonfiguracijaSlagalice}
	 */
	public Slagalica(KonfiguracijaSlagalice reference) {
		Objects.requireNonNull(reference, "Konfiguracija Slagalice must not be null");
		this.reference = reference;
	}

	@Override
	public List<Transition<KonfiguracijaSlagalice>> apply(KonfiguracijaSlagalice t) {

		List<Transition<KonfiguracijaSlagalice>> listToBeReturned = new LinkedList<>();

		int arrayOfPuzzle[] = t.getPolje();
		int zeroPos = t.indexOfSpace();

		KonfiguracijaSlagalice ksLeft = swap(arrayOfPuzzle, zeroPos, -1);
		KonfiguracijaSlagalice ksRight = swap(arrayOfPuzzle, zeroPos, +1);
		KonfiguracijaSlagalice ksUp = swap(arrayOfPuzzle, zeroPos, -3);
		KonfiguracijaSlagalice ksDown = swap(arrayOfPuzzle, zeroPos, +3);

		if (ksLeft != null) {
			listToBeReturned.add(new Transition<KonfiguracijaSlagalice>(ksLeft, 1));
		}
		if (ksRight != null) {
			listToBeReturned.add(new Transition<KonfiguracijaSlagalice>(ksRight, 1));
		}
		if (ksUp != null) {
			listToBeReturned.add(new Transition<KonfiguracijaSlagalice>(ksUp, 1));
		}
		if (ksDown != null) {
			listToBeReturned.add(new Transition<KonfiguracijaSlagalice>(ksDown, 1));
		}

		return listToBeReturned;
	}

	/**
	 * Method that returns {@link KonfiguracijaSlagalice} or null if such can not be
	 * made based on provided array, index of position that will be swapped and int
	 * representing for how much(and where) it should be swapped
	 * 
	 * @param polje   array containing configuration of puzzle
	 * @param zeroPos position that is swapped with position that is for i away from
	 *                it
	 * @param i       distance of digit that will get swapped from zeroPos
	 * @return {@link KonfiguracijaSlagalice} based on new state, or null if such
	 *         swap is illegal
	 */
	private KonfiguracijaSlagalice swap(int[] arrayOfPuzzle, int zeroPos, int i) {

		int rootOfArrayLenght = (int) Math.sqrt(arrayOfPuzzle.length);

		if (zeroPos % rootOfArrayLenght == 0 && i == -1)
			return null;
		if (zeroPos % rootOfArrayLenght == rootOfArrayLenght - 1 && i == 1)
			return null;
		if (zeroPos < rootOfArrayLenght && i == -rootOfArrayLenght)
			return null;
		if (zeroPos > arrayOfPuzzle.length - rootOfArrayLenght - 1 && i == rootOfArrayLenght)
			return null;

		int thisArray[] = Arrays.copyOf(arrayOfPuzzle, arrayOfPuzzle.length);
		if (zeroPos + i >= 0 && zeroPos + i < 9) {

			int temp = thisArray[zeroPos];
			thisArray[zeroPos] = thisArray[zeroPos + i];
			thisArray[zeroPos + i] = temp;
			return new KonfiguracijaSlagalice(thisArray);
		}
		return null;
	}

	@Override
	public KonfiguracijaSlagalice get() {
		return reference;
	}

	@Override
	public boolean test(KonfiguracijaSlagalice t) {
		int[] array = t.getPolje();
		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] != i + 1) {
				return false;
			}
		}
		return array[array.length - 1] == 0;
	}

}
