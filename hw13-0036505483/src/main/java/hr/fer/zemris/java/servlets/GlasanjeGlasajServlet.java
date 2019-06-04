package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/glasanje-glasaj" })
public class GlasanjeGlasajServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		if (!Files.exists(Paths.get(fileName))) {
			Files.createFile(Paths.get(fileName));
		}

		List<Record> records = Record.readDefinition(req);
		Record.updateRecords(records, fileName);
		
		String id = req.getParameter("id");
		addOne(records, id);
		updateFile(fileName, records);
		
		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
	}

	private static void addOne(List<Record> records, String id) {
		Record r = records.get(Record.findIndexOf(id, records));
		r.setVotes(r.getVotes() + 1);
	}

	private static void updateFile(String fileName, List<Record> records) throws IOException {
		StringBuilder sb = new StringBuilder();
		for (var l : records) {
			sb.append(l.getId() + "\t" + l.getVotes() + "\r\n");
		}
		Files.writeString(Paths.get(fileName), sb.toString());
	}

}
