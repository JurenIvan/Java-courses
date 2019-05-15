package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class BarChartDemo extends JFrame {

	private static final long serialVersionUID = 1L;
	private static BarChart barChart;
	private static Path pathToFile;

	public BarChartDemo() {

		setLocation(20, 50);
		setTitle("Primes lists");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setVisible(true);
		initGUI();
		pack();

	}

	private void initGUI() {
		JComponent jcomponent = new BarChartComponent(barChart);
		setLayout(new BorderLayout());
		add(new JLabel(pathToFile.toAbsolutePath().toString(),SwingConstants.CENTER),BorderLayout.NORTH);
		add(jcomponent,BorderLayout.CENTER);
	}

	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("There should be exactly one argument which is path to input file!");
			return;
		}
		pathToFile = Path.of(args[0]);
		try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
			String lines[] = new String[6];
			lines[0] = br.readLine();
			lines[1] = br.readLine();
			lines[2] = br.readLine();
			lines[3] = br.readLine();
			lines[4] = br.readLine();
			lines[5] = br.readLine();
			List<XYValue> list = new ArrayList<>();
			String splitted[] = lines[2].split(" ");

			for (String s : splitted) {
				String splitted2[] = s.split(",");
				list.add(new XYValue(Integer.parseInt(splitted2[0]), Integer.parseInt(splitted2[1])));
			}
			barChart = new BarChart(list, lines[0], lines[1], Double.parseDouble(lines[3]),
					Double.parseDouble(lines[4]), Double.parseDouble(lines[5]));
		} catch (IOException e) {
			System.out.println("Invalid input path");
		} catch (NumberFormatException e) {
			System.out.println("Illegal number format");
		}

		SwingUtilities.invokeLater(() -> {
			JFrame frame = new BarChartDemo();
		});

	}

}
