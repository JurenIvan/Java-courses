package hr.fer.zemris.java.gui.layouts;

import java.util.Objects;

/**
 * Class that represents structure whose variables stores position of components
 * 
 * @author juren
 *
 */
public class RCPosition {
	/**
	 * variable that stores row
	 */
	private final int row;
	/**
	 * variable that stores column
	 */
	private final int column;

	/**
	 * Standard constructor.
	 * @param row
	 * @param column
	 */
	public RCPosition(int row, int column) {
		super();
		this.row = row;
		this.column = column;
	}

	/**
	 * Standard getter.
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Standard getter.
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(column, row);
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
		if (!(obj instanceof RCPosition))
			return false;
		RCPosition other = (RCPosition) obj;
		return column == other.column && row == other.row;
	}

}
