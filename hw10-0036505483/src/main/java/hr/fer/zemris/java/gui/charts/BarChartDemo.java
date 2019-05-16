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

/**
 * Class containing main method used to demonstrate functionalities of class. It
 * is program that read file, parses it and creates graph out of it.
 * 
 * @author juren
 *
 */
public class BarChartDemo extends JFrame {

	private static final long serialVersionUID = 1L;
	/**
	 * reference to barChart
	 */
	private static BarChart barChart;
	/**
	 * Provided path to file
	 */
	private static Path pathToFile;

	/**
	 * Constructor for {@link BarChartDemo}. Sets some gui configurations.
	 */
	public BarChartDemo() {
		setLocation(20, 50);
		setVisible(true);
		initGUI();
		pack();
	}

	/**
	 * Method used to clear up the constructor.Contains commands to configure GUI.
	 */
	private void initGUI() {
		JComponent jcomponent = new BarChartComponent(barChart);
		setLayout(new BorderLayout());
		add(new JLabel(pathToFile.toAbsolutePath().toString(), SwingConstants.CENTER), BorderLayout.NORTH);
		add(jcomponent, BorderLayout.CENTER);
	}

	/**
	 * Main method used to start program and demonstrate it's functionalities of
	 * graphing data
	 * 
	 * @param args expecting one string representing path to file
	 */
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
			barChart = new BarChart(list, lines[0], lines[1], Integer.parseInt(lines[3]), Integer.parseInt(lines[4]),
					Integer.parseInt(lines[5]));
		} catch (IOException e) {
			System.out.println("Invalid input path");
			return;
		} catch (NumberFormatException e) {
			System.out.println("Illegal number format");
			return;
		}

		SwingUtilities.invokeLater(() -> {
			JFrame frame = new BarChartDemo();
			frame.setTitle("Primes lists");
			frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			frame.setSize(640, 400);
		});

	}

}
