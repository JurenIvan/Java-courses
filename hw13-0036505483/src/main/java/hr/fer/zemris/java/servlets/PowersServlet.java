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
			errorOccured("number a", req, resp);
			return;
		}

		try {
			b = Integer.parseInt(bString);
			if (Math.abs(b) > 100)
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			errorOccured("number b", req, resp);
			return;
		}

		try {
			n = Integer.parseInt(nString);
			if (n > 5 || n < 1)
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			errorOccured("number", req, resp);
			return;
		}

		resp.setHeader("Content-Disposition", "attachment; filename=\"tablica.xls\"");
		table(a, b, n).write(resp.getOutputStream());
	}

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

	private void errorOccured(String string, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("errorReasone", string);
		req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
	}

}
