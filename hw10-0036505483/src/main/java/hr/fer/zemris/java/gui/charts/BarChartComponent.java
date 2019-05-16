package hr.fer.zemris.java.gui.charts;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.Objects;

import javax.swing.JComponent;

/**
 * Class representing implementation of {@link JComponent} that is able to draw
 * presented data.
 * 
 * @author juren
 *
 */
public class BarChartComponent extends JComponent {

	private static final long serialVersionUID = 1L;
	/**
	 * Variable/constant that stores color for background
	 */
	private static final Color BACKGROUND_COLOR = Color.pink;
	/**
	 * Variable/constant that stores color for y axis
	 */
	private static final Color Y_AXIS_COLOR = Color.blue;
	/**
	 * Variable/constant that stores color for numbers on y axis
	 */
	private static final Color Y_AXIS_COLOR_NUMBERS = Color.DARK_GRAY;
	/**
	 * Variable/constant that stores color for x axis
	 */
	private static final Color X_AXIS_COLOR = Color.magenta;
	/**
	 * Variable/constant that stores color for numbers on x axis
	 */
	private static final Color X_AXIS_COLOR_NUMBERS = Color.white;
	
	/**
	 * Variable/constant that stores color for lines of x axis
	 */
	private static final Color X_AXIS_COLOR_LINES = Color.yellow;
	/**
	 * Variable/constant that stores color for lines of y axis
	 */
	private static final Color Y_AXIS_COLOR_LINES = Color.yellow;
	/**
	 * Variable/constant that stores color for filament of rectangle
	 */
	private static final Color RECTANGLE_COLOR = Color.green;

	//don't judge me cause of my color palate choice :) 
	//just kidding, just wanted to demonstrate the possibilities
	
	/**
	 * variable/constant that stores the size of triangles
	 */
	private final int TRIANGLE_SIZE = 10;
	/**
	 * variable that stores reference to barChart whose data is represented
	 */
	private BarChart barChart;
	/**
	 * variable that stores width of column of graph
	 */
	private int diffW;
	/**
	 * variable that stores height of column of graph
	 */
	private int diffH;

	/**
	 * Standard constructor
	 * 
	 * @param barChart
	 */
	public BarChartComponent(BarChart barChart) {
		Objects.requireNonNull(barChart, "Cannot plot null graph");
		this.barChart = barChart;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Insets borders = getInsets();
		colorBackground(g, borders, BACKGROUND_COLOR);
		borders.right = borders.right + TRIANGLE_SIZE;
		borders.top = borders.top + TRIANGLE_SIZE;

		borders.left = borders.left + drawYAxisText(g, borders, Y_AXIS_COLOR);
		borders.bottom = borders.bottom + drawXAxisText(g, borders, X_AXIS_COLOR);
		borders.bottom = borders.bottom + drawXAxisSigns(g, borders, X_AXIS_COLOR_NUMBERS, X_AXIS_COLOR_LINES);
		borders.left = borders.left + drawYAxisSigns(g, borders, Y_AXIS_COLOR_NUMBERS, Y_AXIS_COLOR_LINES);

		putTriangles(g, borders);
		drawCollumns(g, borders, RECTANGLE_COLOR);
	}

	/**
	 * Method used to draw rectangles on graph
	 * 
	 * @param g       {@link Graphics} upon it is drawn
	 * @param borders margins of remaining space
	 * @param color   color of columns
	 */
	private void drawCollumns(Graphics g, Insets borders, Color color) {
		List<XYValue> list = barChart.getList();
		g.setColor(color);
		for (int i = 0; i < list.size(); i++) {
			g.fill3DRect(borders.left + diffW * i,
					getHeight() - borders.bottom
							- ((list.get(i).getY() - barChart.getMinY()) / barChart.getStepY()) * diffH,
					diffW, ((list.get(i).getY() - barChart.getMinY()) / barChart.getStepY()) * diffH, true);
		}

	}

	/**
	 * Method used to draw triangles on appropriate place
	 * 
	 * @param g       {@link Graphics} upon it is drawn
	 * @param borders margins of remaining space
	 */
	private void putTriangles(Graphics g, Insets borders) {
		int x[] = new int[3];
		int y[] = new int[3];
		borders.bottom = borders.bottom + g.getFontMetrics().getAscent() / 2;

		x[1] = x[0] = getWidth() - borders.right;
		x[2] = x[0] + TRIANGLE_SIZE;

		y[0] = getHeight() - borders.bottom - TRIANGLE_SIZE / 2;
		y[1] = getHeight() - borders.bottom + TRIANGLE_SIZE / 2;
		y[2] = getHeight() - borders.bottom;
		g.fillPolygon(x, y, 3);

		x[0] = borders.left - TRIANGLE_SIZE / 2;
		x[1] = borders.left + TRIANGLE_SIZE / 2;
		x[2] = borders.left;

		y[0] = y[1] = borders.top;
		y[2] = y[0] - TRIANGLE_SIZE;
		g.fillPolygon(x, y, 3);
	}

	/**
	 * Method used to draw vertical lines and appropriate numbers. Numbers are
	 * placed in center between 2 lines
	 * 
	 * @param g            {@link Graphics} upon it is drawn
	 * @param borders      margins of remaining space
	 * @param colorNumbers color of numbers
	 * @param colorLines   color of lines
	 * @return space consumed by numbers at the bottom
	 */
	private int drawYAxisSigns(Graphics g, Insets borders, Color colorNumbers, Color colorLines) {
		FontMetrics fm = g.getFontMetrics();

		int rowCount = (barChart.getMaxY() - barChart.getMinY()) / barChart.getStepY() + 1;
		int yOffset = calculateYOffset(g, barChart.getMaxY());
		diffH = (getHeight() - borders.bottom - borders.top) / rowCount;
		int x = borders.left + yOffset;
		int h = getHeight() - borders.bottom - fm.getAscent() / 2;
		g.setColor(colorLines);

		for (int i = 0; i < rowCount; i++) {
			g.drawLine(x, h, getWidth() - borders.right, h);
			h = h - diffH;
		}

		g.setColor(colorNumbers);
		h = getHeight() - borders.bottom - fm.getAscent() / 2;
		for (int i = 0; i < rowCount; i++) {
			int broj = barChart.getMinY() + barChart.getStepY() * i;
			g.drawString(broj + "", x - calculateYOffset(g, broj), h + fm.getAscent() / 2);
			h = h - diffH;
		}
		return fm.getAscent() + 2;
	}

	/**
	 * Method used to draw horizontal lines and appropriate numbers. Numbers are
	 * aligned to the right.
	 * 
	 * @param g            {@link Graphics} upon it is drawn
	 * @param borders      margins of remaining space
	 * @param colorNumbers color of numbers
	 * @param colorLines   color of lines
	 * @return place consumed by numbers on the left side
	 */
	private int drawXAxisSigns(Graphics g, Insets borders, Color colorNumbers, Color colorLines) {

		FontMetrics fm = g.getFontMetrics();
		int collumnCount = barChart.getList().size();
		int yOffset = calculateYOffset(g, barChart.getMaxY());

		diffW = (getWidth() - borders.left - borders.right - yOffset) / collumnCount;
		int x = borders.left + yOffset;
		int h = getHeight() - borders.bottom - fm.getAscent();

		g.setColor(colorLines);
		for (int i = 0; i <= collumnCount; i++) {
			g.drawLine(x, h, x, borders.top);
			x = x + diffW;
		}

		x = borders.left + yOffset + diffW / 2;
		g.setColor(colorNumbers);
		for (int i = 0; i < collumnCount; i++) {
			String toPrint = barChart.getList().get(i).getX() + "";
			g.drawString(toPrint, x - fm.stringWidth(toPrint) / 2, h + fm.getAscent());
			x = x + diffW;
		}
		return yOffset;
	}

	/**
	 * Method that calculates offset caused by number that is passed as an argument
	 * 
	 * @param g       {@link Graphics} that has data about size of strings
	 * @param biggest argument that is measured
	 * @return size of string representation of argument
	 */
	private int calculateYOffset(Graphics g, int biggest) {
		return g.getFontMetrics().stringWidth(biggest + "");
	}

	/**
	 * Method used to color the background
	 * 
	 * @param g       {@link Graphics} upon it is drawn
	 * @param borders margins of remaining space
	 * @param color   color of numbers
	 */
	private void colorBackground(Graphics g, Insets borders, Color color) {
		g.setColor(color);
		g.fillRect(borders.left, borders.right, getSize().width - borders.left - borders.right,
				getSize().height - borders.bottom - borders.top);
	}

	/**
	 * Method used to draw vertical description of text alongside y axis
	 * 
	 * @param g       {@link Graphics} upon it is drawn
	 * @param borders margins of remaining space
	 * @param color   color of text
	 * @return place consumed by numbers alongside left margin
	 */
	private int drawYAxisText(Graphics g, Insets borders, Color color) {
		g.setColor(color);

		FontMetrics fm = g.getFontMetrics();
		int width = fm.stringWidth(barChart.getyDescription());
		int height = fm.getAscent();

		Graphics2D g2d = (Graphics2D) g;
		AffineTransform defaultAt = g2d.getTransform();
		AffineTransform at = new AffineTransform();
		at.rotate(-Math.PI / 2);
		g2d.setTransform(at);

		g2d.drawString(barChart.getyDescription(),
				-borders.top - (getSize().height - borders.top - borders.bottom + width) / 2, height + borders.left);
		g2d.setTransform(defaultAt);

		return height;
	}

	/**
	 * Method used to draw horizontal description of text alongside x axis
	 * 
	 * @param g       {@link Graphics} upon it is drawn
	 * @param borders margins of remaining space
	 * @param color   color of text
	 * @return place consumed by numbers alongside south margin
	 */
	private int drawXAxisText(Graphics g, Insets ins, Color color) {
		g.setColor(color);

		FontMetrics fm = g.getFontMetrics();
		int w = fm.stringWidth(barChart.getxDescription());
		int h = fm.getAscent();

		g.drawString(barChart.getxDescription(), (ins.left + (getSize().width - w - ins.right - ins.left) / 2),
				getSize().height - ins.bottom - h / 2);

		return 3 * h / 2;
	}

}
