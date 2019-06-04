package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/glasanje" })
public class GlasanjeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getServletContext().getRealPath("\\WEB-INF\\glasanje-definicija.txt");

		if (!Files.exists(Paths.get(fileName))) {
			Files.createFile(Paths.get(fileName));
		}

		List<Record> listOfRecords = Files.readAllLines(Paths.get(fileName)).stream().map((t) -> Record.makeRecord(t))
					.collect(Collectors.toList());
		
	//	for(var e:listOfRecords) {
	//		System.out.println(e.toString());
	//	}
		
		req.setAttribute("records", listOfRecords);
		req.getRequestDispatcher("\\WEB-INF\\pages\\glasanjeIndex.jsp").forward(req, resp);
	}

}
