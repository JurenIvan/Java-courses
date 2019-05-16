package hr.fer.zemris.java.gui.charts;

import java.util.List;

/**
 * Class that models data structure representing bar chart. Has appropriate
 * constructor and getters
 * 
 * @author juren
 *
 */
public class BarChart {
	/**
	 * private variable used for storing List of Pairs of data
	 */
	private final List<XYValue> list;
	/**
	 * private variable used for storing String containing description of xAxis
	 */
	private final String xDescription;
	/**
	 * private variable used for storing String containing description of yAxis
	 */
	private final String yDescription;
	/**
	 * private variable used for storing minimal value on y axis
	 */
	private final int minY;
	/**
	 * private variable used for storing maximal value on y axis
	 */
	private final int maxY;
	/**
	 * private variable used for storing how big step is taken on graph
	 */
	private final int stepY;

	/**
	 * standard constructor.
	 * 
	 * @param list          variable used for storing List of Pairs of data
	 * @param xDescription  variable used for storing String containing description
	 *                      of xAxis
	 * @param yDescription  variable used for storing String containing description
	 *                      of yAxis
	 * @param minY          variable used for storing minimal value on y axis
	 * @param maxY          variable used for storing maximal value on y axis
	 * @param stepYvariable used for storing how big step is taken on graph
	 * @throws IllegalArgumentException if minY is smaller than zero, or any y value
	 *                                  is smaller than minY or maxY is smaller than
	 *                                  minY
	 */
	public BarChart(List<XYValue> list, String xDescription, String yDescription, int minY, int maxY, int stepY) {

		for (var elem : list) {
			if (elem.getY() < minY)
				throw new IllegalArgumentException();
		}
		if (minY < 0 || maxY < minY)
			throw new IllegalArgumentException();

		this.list = list;
		this.xDescription = xDescription;
		this.yDescription = yDescription;
		this.minY = minY;
		this.maxY = maxY;
		this.stepY = stepY;
	}

	/**
	 * Standard getter.
	 * 
	 * @return the list
	 */
	public List<XYValue> getList() {
		return list;
	}

	/**
	 * Standard getter.
	 * 
	 * @return the xDescription
	 */
	public String getxDescription() {
		return xDescription;
	}

	/**
	 * Standard getter.
	 * 
	 * @return the yDescription
	 */
	public String getyDescription() {
		return yDescription;
	}

	/**
	 * Standard getter.
	 * 
	 * @return the minY
	 */
	public int getMinY() {
		return minY;
	}

	/**
	 * Standard getter.
	 * 
	 * @return the maxY
	 */
	public int getMaxY() {
		return maxY;
	}

	/**
	 * Standard getter.
	 * 
	 * @return the stepY
	 */
	public int getStepY() {
		return stepY;
	}

}
