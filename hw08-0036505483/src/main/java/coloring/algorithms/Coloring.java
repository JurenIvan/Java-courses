package coloring.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import marcupic.opjj.statespace.coloring.Picture;


/**
 * Class that models operations made over certain picture.
 * 
 * @author juren
 *
 */
public class Coloring implements Consumer<Pixel>, Function<Pixel, List<Pixel>>, Predicate<Pixel>, Supplier<Pixel> {

	/**
	 * Variable that saves reference to {@link Pixel}.
	 */
	private Pixel reference;
	/**
	 * Variable that saves reference to {@link Picture}.
	 */
	private Picture picture;
	/**
	 * Variable that stores integer representation of color which is used to fill.
	 */
	private int fillColor;
	/**
	 * Variable that stores integer representation of color that will be changed.
	 */
	private int refColor;

	/**
	 * Constructor for class. Takes  variable that saves reference to {@link Pixel}, variable that saves reference to {@link Picture}, variable that stores integer representation of color which is used to fill and figures out variable that stores integer representation of color that will be changed.
	 * 
	 * @param reference Variable that saves reference to {@link Pixel}.
	 * @param picture Variable that saves reference to {@link Picture}.
	 * @param fillColor Variable that stores integer representation of color which is used to fill.
	 */
	public Coloring(Pixel reference, Picture picture, int fillColor) {
		Objects.requireNonNull(picture, "Picture referenced must not be null.");
		Objects.requireNonNull(reference, "Pixel reference referenced must not be null");

		this.reference = reference;
		this.picture = picture;
		this.fillColor = fillColor;
		this.refColor = picture.getPixelColor(reference.getX(), reference.getY());
	}

	@Override
	public Pixel get() {
		return reference;
	}

	@Override
	public boolean test(Pixel t) {
		return picture.getPixelColor(t.getX(), t.getY()) == refColor;
	}

	@Override
	public List<Pixel> apply(Pixel t) {
		List<Pixel> newList = new ArrayList<Pixel>();

		if (t.getX() + 1 < picture.getWidth())
			newList.add(new Pixel(t.getX() + 1, t.getY()));

		if (t.getY() + 1 < picture.getHeight())
			newList.add(new Pixel(t.getX(), t.getY() + 1));

		if (t.getX() - 1 >= 0)
			newList.add(new Pixel(t.getX() - 1, t.getY()));

		if (t.getY() - 1 >= 0)
			newList.add(new Pixel(t.getX(), t.getY() - 1));

		return newList;
	}

	@Override
	public void accept(Pixel t) {
		picture.setPixelColor(t.getX(), t.getY(), fillColor);
	}

}
