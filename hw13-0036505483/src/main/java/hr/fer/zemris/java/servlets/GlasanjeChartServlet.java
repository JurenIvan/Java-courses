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

@WebServlet(urlPatterns = { "/glasanje-grafika" })
public class GlasanjeChartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_WIDTH = 600;
	private static final int DEFAULT_HEIGHT = 400;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/png");
		resp.setContentType("image/png");
		resp.setContentType("image/png");
		resp.getOutputStream()
				.write(ChartUtils.encodeAsPNG(getChart(req).createBufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT)));
		resp.getOutputStream().flush();
	}

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

	private DefaultPieDataset getDataset(HttpServletRequest req) throws IOException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		DefaultPieDataset dataset = new DefaultPieDataset();

		List<Record> records = Record.readDefinition(req);
		Record.updateRecords(records, fileName);
		
		return dataset;
		
	}

}
