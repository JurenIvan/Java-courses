package searching.slagalica;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import searching.algorithms.Transition;

public class Slagalica<S> implements Supplier<KonfiguracijaSlagalice>,
		Function<KonfiguracijaSlagalice, List<Transition<KonfiguracijaSlagalice>>>, Predicate<KonfiguracijaSlagalice> {

	private KonfiguracijaSlagalice reference;

	public Slagalica(KonfiguracijaSlagalice reference) {
		this.reference = reference;
	}

	@Override
	public List<Transition<KonfiguracijaSlagalice>> apply(KonfiguracijaSlagalice t) {

		List<Transition<KonfiguracijaSlagalice>> listToBeReturned = new LinkedList<>();

		int polje[] = t.getPolje();
		int zeroPos = t.indexOfSpace();

		KonfiguracijaSlagalice ksLeft = swap(polje, zeroPos, -1);
		KonfiguracijaSlagalice ksRight = swap(polje, zeroPos, +1);
		KonfiguracijaSlagalice ksUp = swap(polje, zeroPos, -3);
		KonfiguracijaSlagalice ksDown = swap(polje, zeroPos, +3);

		if (ksLeft != null) {
			listToBeReturned.add(new Transition(ksLeft, 1));
		}
		if (ksRight != null) {
			listToBeReturned.add(new Transition(ksRight, 1));
		}
		if (ksUp != null) {
			listToBeReturned.add(new Transition(ksUp, 1));
		}
		if (ksDown != null) {
			listToBeReturned.add(new Transition(ksDown, 1));
		}

		return listToBeReturned;
	}

	private KonfiguracijaSlagalice swap(int[] polje, int zeroPos, int i) {

		if (zeroPos % 3 == 0 && i == -1)
			return null;
		if (zeroPos % 3 == 2 && i == 1)
			return null;
		if (zeroPos < 3 && i == -3)
			return null;
		if (zeroPos > 5 && i == 3)
			return null;

		int thisArray[] = Arrays.copyOf(polje, polje.length);
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
