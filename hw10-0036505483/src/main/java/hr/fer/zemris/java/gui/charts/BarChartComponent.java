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

public class BarChartComponent extends JComponent {

	private static final long serialVersionUID = 1L;

	private static final Color BACKGROUND_COLOR = Color.gray;
	private static final Color Y_AXIS_COLOR = Color.magenta;
	private static final Color X_AXIS_COLOR = Color.magenta;
	private static final Color X_AXIS_COLOR_NUMBERS = Color.BLACK;
	private static final Color Y_AXIS_COLOR_NUMBERS = Color.DARK_GRAY;
	private static final Color X_AXIS_COLOR_LINES = Color.yellow;
	private static final Color Y_AXIS_COLOR_LINES = Color.yellow;
	private static final Color RECTANGLE_COLOR = Color.RED;
	private final int TRIANGLE_SIZE = 10;
	private BarChart barChart;
	
	
	private int diffW;
	private int diffH;
	

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
		drawChartColumns(g, borders, RECTANGLE_COLOR);
	}


	private void drawChartColumns(Graphics g, Insets borders, Color color) {
		List<XYValue> list = barChart.getList();	

		for (int i = 0; i < list.size(); i++) {
			g.fill3DRect(borders.left + diffW * i,
					getHeight() - borders.bottom
							- ((list.get(i).getY() - barChart.getMinY()) / barChart.getStepY()) * diffH,
					diffW, ((list.get(i).getY() - barChart.getMinY()) / barChart.getStepY()) * diffH, true);
		}

	}

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

		x[0] = borders.left - TRIANGLE_SIZE / 2 ;
		x[1] = borders.left + TRIANGLE_SIZE / 2 ;
		x[2] = borders.left ;

		y[0] = y[1] = borders.top;
		y[2] = y[0] - TRIANGLE_SIZE;
		g.fillPolygon(x, y, 3);
	}

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

	private int calculateYOffset(Graphics g, int biggest) {
		return g.getFontMetrics().stringWidth(biggest + "");
	}

	private void colorBackground(Graphics g, Insets borders, Color color) {
		g.setColor(color);
		g.fillRect(borders.left, borders.right, getSize().width - borders.left - borders.right,
				getSize().height - borders.bottom - borders.top);
	}

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
