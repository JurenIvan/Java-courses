package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

@WebServlet(urlPatterns = { "/glasanje-xls" })
public class GlasanjeXLSServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setHeader("Content-Disposition", "attachment; filename=\"tablica.xls\"");
		table(req).write(resp.getOutputStream());
	}

	private Workbook table(HttpServletRequest req) throws IOException {
		Workbook hwb = new HSSFWorkbook();
		Sheet sheet = hwb.createSheet("Dataset");

		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");

		List<Record> records = Record.readDefinition(req);
		Record.updateRecords(records, fileName);
		Row firstRow = sheet.createRow(0);
		firstRow.createCell(0).setCellValue("ID");
		firstRow.createCell(1).setCellValue("Band Name");
		firstRow.createCell(2).setCellValue("Sample song url");
		firstRow.createCell(3).setCellValue("Number of votes");

		for (int i = 1; i <= records.size(); i++) {
			Row rowhead = sheet.createRow(i);
			Record r = records.get(i-1);
			rowhead.createCell(0).setCellValue(r.getId());
			rowhead.createCell(1).setCellValue(r.getBandName());
			rowhead.createCell(2).setCellValue(r.getSampleUrl());
			rowhead.createCell(3).setCellValue(r.getVotes());
		}
		return hwb;
	}
}
