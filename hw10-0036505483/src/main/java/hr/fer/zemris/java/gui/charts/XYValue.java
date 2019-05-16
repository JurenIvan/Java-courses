package hr.fer.zemris.java.gui.charts;

import java.util.Objects;

/**
 * Class that model data structure containing only two integers. has approriate
 * getters and constructor.
 * 
 * @author juren
 *
 */
public class XYValue {
	/**
	 * variable used for storing x(first int)
	 */
	private final int x;
	/**
	 * variable used for storing x(second int)
	 */
	private final int y;

	/**
	 * Standard getter for x
	 * 
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Standard getter for y
	 * 
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Standard constructor
	 * 
	 * @param x value
	 * @param y value 
	 */
	public XYValue(int x, int y) {
		super();
		this.x = x;
		this.y = y;
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
		if (!(obj instanceof XYValue))
			return false;
		XYValue other = (XYValue) obj;
		return x == other.x && y == other.y;
	}

}
