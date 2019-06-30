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

import hr.fer.zemris.java.dao.DAOProvider;
import hr.fer.zemris.java.model.PollOptionModel;

/**
 * Class that models a servlet used for creating excell file that has 1 page,
 * and on there are results of poll. first row has description for data of
 * appropriate collumn. Records are sorted using id.
 * 
 * @author juren
 *
 */
@WebServlet(urlPatterns = { "/servleti/glasanje-xls" })
public class GlasanjeXLSServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setHeader("Content-Disposition", "attachment; filename=\"tablica.xls\"");
		table(Long.parseLong(req.getParameter("pollID"))).write(resp.getOutputStream());
	}

	/**
	 * Method used to create {@link Workbook} described in
	 * {@link GlasanjeXLSServlet} class javadoc.
	 * 
	 * @param pollID used to filter usefull data from database
	 * @return {@link Workbook} descibed
	 * @throws IOException if files can not be found
	 */
	private Workbook table(long pollID) throws IOException {
		Workbook hwb = new HSSFWorkbook();
		Sheet sheet = hwb.createSheet("Dataset");
		
		List<PollOptionModel> results=DAOProvider.getDao().getResultsData(pollID);

		Row firstRow = sheet.createRow(0);
		firstRow.createCell(0).setCellValue("ID");
		firstRow.createCell(1).setCellValue("Band Name");
		firstRow.createCell(2).setCellValue("Sample song url");
		firstRow.createCell(3).setCellValue("Number of votes");

		for (int i = 1; i <= results.size(); i++) {
			Row rowhead = sheet.createRow(i);
			PollOptionModel r = results.get(i - 1);
			rowhead.createCell(0).setCellValue(r.getId());
			rowhead.createCell(1).setCellValue(r.getOptionTitle());
			rowhead.createCell(2).setCellValue(r.getOptionLink());
			rowhead.createCell(3).setCellValue(r.getVoteCount());
		}
		return hwb;
	}
}
