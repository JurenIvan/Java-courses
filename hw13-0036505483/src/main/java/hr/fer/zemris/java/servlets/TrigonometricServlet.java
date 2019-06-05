package hr.fer.zemris.java.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class that models a servlet used for preparing data for jsp that is meant for
 * displaying data of trigonometric functions (sin and cos).
 * 
 * @author juren
 *
 */
@WebServlet(urlPatterns = { "/trigonometric" })
public class TrigonometricServlet extends HttpServlet {
	/**
	 * String defining format of output
	 */
	private static final String FUNCTION_OUTPUT_FORMAT = "%.5f";
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String a = req.getParameter("a");
		String b = req.getParameter("b");

		int aValue = parseOrDefault(0, a);
		int bValue = parseOrDefault(360, b);

		if (aValue > bValue) {
			int temp = aValue;
			aValue = bValue;
			bValue = temp;
		}
		if (bValue > aValue + 720) {
			b = a + 720;
		}

		String sin[] = new String[bValue - aValue + 1];
		String cos[] = new String[bValue - aValue + 1];
		String numbers[] = new String[bValue - aValue + 1];

		for (int counter = 0, i = aValue; i <= bValue; i++, counter++) {
			numbers[counter] = String.valueOf(i);
			sin[counter] = String.format(FUNCTION_OUTPUT_FORMAT, Math.sin(Math.toRadians(i)));
			cos[counter] = String.format(FUNCTION_OUTPUT_FORMAT, Math.cos(Math.toRadians(i)));
		}

		req.setAttribute("numData", numbers);
		req.setAttribute("sinData", sin);
		req.setAttribute("cosData", cos);

		req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(req, resp);
	}

	/**
	 * method that tries to parse provided string and returns its value, if string
	 * can not be parsed, default value is returned
	 * 
	 * @param defaultValue in case string can not be parsed
	 * @param number            string that should be parsed
	 * @return integer described above
	 */
	private int parseOrDefault(int defaultValue, String number) {
		try {
			return Integer.parseInt(number);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

}
