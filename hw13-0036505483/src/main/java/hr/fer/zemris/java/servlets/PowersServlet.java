package hr.fer.zemris.java.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Class that models a servlet used for creating excell file that has n pages,
 * and on each page there are n-th powers of numbers between a and b, where n,a
 * and b are integers that are send through parameters. a and b must be between
 * -100 and 100, and n has to be positive integer smaller than 5
 * 
 * @author juren
 *
 */
@WebServlet(urlPatterns = { "/powers" })
public class PowersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String aString = req.getParameter("a");
		String bString = req.getParameter("b");
		String nString = req.getParameter("n");
		int a, b, n;
		try {
			a = Integer.parseInt(aString);
			if (Math.abs(a) > 100)
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			errorOccured("Parameter a is not valid, it should be between -100 and 100", req, resp);
			return;
		}

		try {
			b = Integer.parseInt(bString);
			if (Math.abs(b) > 100 || b < a)
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			errorOccured("Parameter b is not valid, it should be between -100 and 100 and greater than a", req, resp);
			return;
		}

		try {
			n = Integer.parseInt(nString);
			if (n > 5 || n < 1)
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			errorOccured("Parameter n is not valid, it should be between 1 and 5", req, resp);
			return;
		}

		resp.setHeader("Content-Disposition", "attachment; filename=\"tablica.xls\"");
		table(a, b, n).write(resp.getOutputStream());
	}

	/**
	 * Method that creates excell table described in {@link PowersServlet} class
	 * javadoc
	 * 
	 * @param a lower boundary of interval that is calculated
	 * @param b upper boundary of interval that is calculated
	 * @param n maximum power
	 * @return
	 */
	private Workbook table(int a, int b, int n) {
		Workbook hwb = new HSSFWorkbook();
		for (int i = 1; i <= n; i++) {
			Sheet sheet = hwb.createSheet(String.valueOf(i));
			for (int j = a; j <= b; j++) {
				Row rowhead = sheet.createRow((short) j - a);
				rowhead.createCell(0).setCellValue(j);
				rowhead.createCell(1).setCellValue(Math.pow(j, i));
			}
		}
		return hwb;
	}

	/**
	 * Method that redirects to error page displaying provided text
	 * 
	 * @param string message description of error
	 * @param req    used to get path to error page
	 * @param resp   used to send request
	 * @throws ServletException this method is not supposed to handle them
	 * @throws IOException      if error page can not be found
	 */
	private void errorOccured(String string, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("errorReasone", string);
		req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
	}

}
