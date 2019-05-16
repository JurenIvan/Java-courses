package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that holds implementation of {@link LayoutManager2} which models layout
 * of calculator
 * 
 * @author juren
 *
 */
public class CalcLayout implements LayoutManager2 {

	/**
	 * Map that stores components of layout
	 */
	private Map<Component, RCPosition> componentConstraints = new HashMap<>();

	/**
	 * constant for number of rows
	 */
	private final int ROW_COUNT = 5;
	/**
	 * constant for number of columns
	 */
	private final int COLLUMN_COUNT = 7;
	/**
	 * default spacer,given by constructor
	 */
	private final int spacer;

	/**
	 * Standard constructor, defines spacer
	 * 
	 * @param spacer default space in pixels between components
	 * @throws CalcLayoutException if spacer is negative
	 */
	public CalcLayout(int spacer) {
		if (spacer < 0)
			throw new CalcLayoutException("Can not have negative width.");
		this.spacer = spacer;
		componentConstraints = new HashMap<>();
	}

	/**
	 * Default constructor, sets spacer to zero.
	 */
	public CalcLayout() {
		this(0);
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		throw new UnsupportedOperationException("This method should not be called.");
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		componentConstraints.remove(comp);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return getStandardDimension(parent, "pref");
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return getStandardDimension(parent, "min");
	}

	@Override
	public Dimension maximumLayoutSize(Container parent) {
		return getStandardDimension(parent, "max");
	}

	/**
	 * Method that returns layout size for given key. if key is "min" then
	 * minimumLayoutSize is returned if key is "max" then maximumLayoutSize is
	 * returned. created to reduce code redundancy.
	 * 
	 * @param parent container
	 * @param key    that determines which layout is wanted
	 * @return dimensions of wanted layout
	 */
	private Dimension getStandardDimension(Container parent, String key) {
		Dimension dim = getCertainDimension(parent, key);
		if (dim == null)
			return null;

		int newWidth = ((int) dim.getWidth()) * COLLUMN_COUNT + (COLLUMN_COUNT - 1) * spacer;
		int newHeight = ((int) dim.getHeight()) * ROW_COUNT + (ROW_COUNT - 1) * spacer;
		return new Dimension(newWidth, newHeight);
	}

	/**
	 * Method that returns layout size for given key. if key is "min" then
	 * minimumLayoutSize is returned if key is "max" then maximumLayoutSize is
	 * returned. created to reduce code redundancy.
	 * 
	 * @param parent parent container
	 * @param key    that determines which layout will be used
	 * @return dimensions of layout
	 */
	private Dimension getCertainDimension(Container parent, String key) {
		Point pair = null;
		if (key.equals("min") || key.equals("pref")) {
			pair = getMinOrPrefDimension(parent, key);
		} else if (key.equals("max")) {
			getMaxDimension(parent);
		} else
			throw new IllegalArgumentException();

		if (pair == null || pair.x == Integer.MAX_VALUE || pair.x == Integer.MIN_VALUE || pair.y == Integer.MIN_VALUE
				|| pair.y == Integer.MAX_VALUE)
			return null;
		return new Dimension(pair.x, pair.y);
	}

	/**
	 * Method used to get maximum layout dimensions of container upon the method is
	 * called
	 * 
	 * @param parent container upon a method does it's job
	 * @return 2 variables packed into Point structure
	 */
	private Point getMaxDimension(Container parent) {
		int w, h;
		w = h = Integer.MAX_VALUE;
		for (var c : parent.getComponents()) {
			if (c.getMaximumSize() != null) {
				Dimension dimOfComp = c.getMaximumSize();
				if (dimOfComp.getHeight() < h) {
					h = (int) dimOfComp.getHeight();
				}
				if (componentConstraints.get(c).equals(new RCPosition(1, 1))) {
					if (dimOfComp.getWidth() < w * 5 + 3 * spacer) {
						w = ((int) dimOfComp.getWidth() - 4 * spacer) / 5;
					}
				} else {
					if (dimOfComp.getWidth() < w) {
						w = (int) dimOfComp.getWidth();
					}
				}
			}
		}
		return new Point(w, h);
	}

	/**
	 * Method used to get minimum or prefered layout dimensions of container upon
	 * the method is called
	 * 
	 * @param parent container upon a method does it's job
	 * @return 2 variables packed into Point structure
	 */
	private Point getMinOrPrefDimension(Container parent, String key) {
		int w, h;
		w = h = Integer.MIN_VALUE;
		for (var c : parent.getComponents()) {
			if (c.getMinimumSize() != null && key.equals("min") || c.getPreferredSize() != null && key.equals("pref")) {
				Dimension dimOfComp = key.equals("min") ? c.getMinimumSize() : c.getPreferredSize();
				if (dimOfComp.getHeight() > h) {
					h = (int) dimOfComp.getHeight();
				}
				if (componentConstraints.get(c).equals(new RCPosition(1, 1))) {
					if (dimOfComp.getWidth() > w * 5 + 4 * spacer) {
						w = ((int) dimOfComp.getWidth() - 4 * spacer) / 5;
					}
				} else {
					if (dimOfComp.getWidth() > w) {
						w = (int) dimOfComp.getWidth();
					}
				}
			}
		}
		return new Point(w, h);
	}

	@Override
	public void layoutContainer(Container parent) {

		Insets parentInsets = parent.getInsets();
		int totalW = parent.getWidth() - parentInsets.left - parentInsets.right;
		int totalH = parent.getHeight() - parentInsets.top - parentInsets.bottom;

		double widthOfComponent = (totalW - (COLLUMN_COUNT - 1.0) * spacer) / COLLUMN_COUNT;
		double heightOfComponent = (totalH - (ROW_COUNT - 1.0) * spacer) / ROW_COUNT;

		for (var c : componentConstraints.keySet()) {
			RCPosition rcPos = componentConstraints.get(c);

			int x = (int) Math.round(widthOfComponent * (rcPos.getColumn() - 1.0) + spacer * (rcPos.getColumn() - 1.0));
			int y = (int) Math.round(heightOfComponent * (rcPos.getRow() - 1.0) + ((rcPos.getRow() - 1.0) * spacer));

			if (rcPos.equals(new RCPosition(1, 1))) {
				c.setBounds(parentInsets.left, parentInsets.top, (int) Math.round(widthOfComponent * 5 + 4 * spacer),
						(int) Math.round(heightOfComponent));
			} else {
				c.setBounds(parentInsets.left + x, parentInsets.top + y, (int) Math.round(widthOfComponent),
						(int) Math.round(heightOfComponent));
			}
		}
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		if (constraints instanceof String) {
			String[] splited = ((String) constraints).split(",");
			constraints = new RCPosition(Integer.parseInt(splited[0]), Integer.parseInt(splited[1]));
		}

		if (!(constraints instanceof RCPosition))
			throw new CalcLayoutException("Only RCPosition type allowed as border");

		if (!checkBorders((RCPosition) constraints))
			throw new CalcLayoutException("Invalid position");

		componentConstraints.put(comp, (RCPosition) constraints);
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0;
	}

	@Override
	public void invalidateLayout(Container target) {
	}

	/**
	 * Method used to check validity of position of component.
	 * 
	 * @param pos Structure holding position of component
	 * @return boolean whose state is determined by validity of position
	 */
	private boolean checkBorders(RCPosition pos) {
		if (pos.getRow() < 1 || pos.getRow() > ROW_COUNT || pos.getColumn() < 1 || pos.getColumn() > COLLUMN_COUNT) {
			return false;
		}
		for (var e : componentConstraints.values()) {
			if (pos.equals(e))
				return false;
		}
		if (pos.getRow() == 1 && pos.getColumn() > 1 && pos.getColumn() < 6)
			return false;
		return true;
	}

}
