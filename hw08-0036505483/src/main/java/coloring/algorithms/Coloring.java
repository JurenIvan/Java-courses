package coloring.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import marcupic.opjj.statespace.coloring.Picture;

public class Coloring implements Consumer<Pixel>, Function<Pixel, List<Pixel>>, Predicate<Pixel>, Supplier<Pixel> {

	private Pixel reference;
	private Picture picture;
	private int fillColor;
	private int refColor;

	/**
	 * @param reference
	 * @param picture
	 * @param fillColor
	 */
	public Coloring(Pixel reference, Picture picture, int fillColor) {
		super();
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

		if (checkCords(t.getX() + 1, t.getY()))
			newList.add(new Pixel(t.getX() + 1, t.getY()));

		if (checkCords(t.getX(), t.getY() + 1))
			newList.add(new Pixel(t.getX(), t.getY() + 1));

		if (checkCords(t.getX() - 1, t.getY()))
			newList.add(new Pixel(t.getX() - 1, t.getY()));

		if (checkCords(t.getX(), t.getY() - 1))
			newList.add(new Pixel(t.getX(), t.getY() - 1));

		return newList;
	}

	private boolean checkCords(int x, int y) {
		return (x >= 0 && y >= 0 && picture.getHeight() > y && picture.getWidth() > x);
	}

	@Override
	public void accept(Pixel t) {
		picture.setPixelColor(t.getX(), t.getY(), fillColor);
	}

}
