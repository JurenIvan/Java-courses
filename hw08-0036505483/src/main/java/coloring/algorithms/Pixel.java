package coloring.algorithms;

import java.util.Objects;

/**
 * Class representing model of Pixel. Has toString, Equals,Hash and x and y
 * coordinates
 * 
 * @author juren
 *
 */
public class Pixel {
	/**
	 * Variable that stores value of x coordinate
	 */
	private int x;
	/**
	 * Variable that stores value of x coordinate
	 */
	private int y;

	/**
	 * Getter for coordinate
	 * 
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Getter for coordinate
	 * 
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Standard Constructor.
	 * 
	 * @param x
	 * @param y
	 */
	public Pixel(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + this.x + "," + y + ")";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Pixel))
			return false;
		Pixel other = (Pixel) obj;
		return x == other.x && y == other.y;
	}

}
