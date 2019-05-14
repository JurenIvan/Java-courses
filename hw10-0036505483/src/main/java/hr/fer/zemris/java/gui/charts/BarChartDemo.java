package hr.fer.zemris.java.gui.charts;

import java.util.Arrays;

import javax.swing.JFrame;

public class BarChartDemo extends JFrame {

	BarChart model = new BarChart(
			Arrays.asList(new XYValue(1, 8), new XYValue(2, 20), new XYValue(3, 22), new XYValue(4, 10),
					new XYValue(5, 4)),
			"Number of people in the car", "Frequency", 0, // y-os kreće od 0
			22, // y-os ide do 22
			2);

}
