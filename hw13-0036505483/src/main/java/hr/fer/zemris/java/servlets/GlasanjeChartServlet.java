package hr.fer.zemris.java.servlets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.IOException;
import java.util.List;

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
 * Class that models a servlet used for generating chart based on pool results
 * stored in files. It's chart has legend.
 * 
 * @author juren
 *
 */
@WebServlet(urlPatterns = { "/glasanje-grafika" })
public class GlasanjeChartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default width of chart
	 */
	private static final int DEFAULT_WIDTH = 600;
	/**
	 * Default height of chart
	 */
	private static final int DEFAULT_HEIGHT = 400;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/png");
		resp.getOutputStream()
				.write(ChartUtils.encodeAsPNG(getChart(req).createBufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT)));
		resp.getOutputStream().flush();
	}

	/**
	 * Method that creates chart with data originating from poll results.
	 * 
	 * @param req used to get access to text files containing results of poll
	 * @return JFreeChart created
	 * @throws IOException if data can not be read
	 */
	public JFreeChart getChart(HttpServletRequest req) throws IOException {
		DefaultPieDataset dataset = getDataset(req);
		boolean legend = true;
		boolean tooltips = false;
		boolean urls = false;

		JFreeChart chart = ChartFactory.createPieChart("Glasovi", dataset, legend, tooltips, urls);

		chart.setBorderPaint(Color.gray);
		chart.setBorderStroke(new BasicStroke(3.0f));
		chart.setBorderVisible(true);

		return chart;
	}

	/**
	 * Method that creates data set from data found on txt files
	 * 
	 * @param req used to reach the text files with poll results
	 * @return data set appropriate for chart
	 * @throws IOException if files can not be found
	 */
	private DefaultPieDataset getDataset(HttpServletRequest req) throws IOException {
		DefaultPieDataset dataset = new DefaultPieDataset();
		List<Record> records = Record.loader(req);
		for (var e : records) {
			dataset.setValue(e.getBandName(), e.getVotes());
		}
		return dataset;
	}

}
