package hr.fer.zemris.java.gui.charts;

import java.util.List;

public class BarChart {

	private List<XYValue> list;
	private String xDescription;
	private String yDescription;
	private double minY;
	private double maxY;
	private double stepY;

	/**
	 * @param list
	 * @param xDescription
	 * @param yDescription
	 * @param minY
	 * @param maxY
	 * @param stepY
	 */
	public BarChart(List<XYValue> list, String xDescription, String yDescription, double minY, double maxY,
			double stepY) {

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
	 * @return the list
	 */
	public List<XYValue> getList() {
		return list;
	}

	/**
	 * @return the xDescription
	 */
	public String getxDescription() {
		return xDescription;
	}

	/**
	 * @return the yDescription
	 */
	public String getyDescription() {
		return yDescription;
	}

	/**
	 * @return the minY
	 */
	public double getMinY() {
		return minY;
	}

	/**
	 * @return the maxY
	 */
	public double getMaxY() {
		return maxY;
	}

	/**
	 * @return the stepY
	 */
	public double getStepY() {
		return stepY;
	}

}
