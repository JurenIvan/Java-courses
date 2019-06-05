package hr.fer.zemris.java.servlets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 * Class that models a servlet used for creating a JFreeChart.
 * 
 * @author juren
 *
 */
@WebServlet(urlPatterns = { "/reportImage" })
public class ReportImageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_WIDTH = 600;
	private static final int DEFAULT_HEIGHT = 400;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/png");
		resp.getOutputStream()
				.write(ChartUtils.encodeAsPNG(getChart().createBufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT)));
		resp.getOutputStream().flush();
	}

	/**
	 * method that returns {@link JFreeChart} with some data.
	 * 
	 * @return {@link JFreeChart} with non-sense data
	 */
	public JFreeChart getChart() {
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("CPU", 11.1);
		dataset.setValue("GPU", 22.2);
		dataset.setValue("RAM", 33.3);

		boolean legend = true;
		boolean tooltips = false;
		boolean urls = false;

		JFreeChart chart = ChartFactory.createPieChart("OS usage", dataset, legend, tooltips, urls);

		chart.setBorderPaint(Color.gray);
		chart.setBorderStroke(new BasicStroke(3.0f));
		chart.setBorderVisible(true);

		return chart;
	}

}
