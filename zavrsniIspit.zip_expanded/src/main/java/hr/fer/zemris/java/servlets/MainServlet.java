package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/main")
public class MainServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String path = req.getServletContext().getRealPath("/WEB-INF/images");

		Path p = Paths.get(path);
		if (Files.isDirectory(p)) {
			try (Stream<Path> walk = Files.walk(p)) {

				List<String> result = walk.map(x -> x.getFileName().toString()).collect(Collectors.toList());
				result.remove(0);
				Collections.sort(result);

				if (result != null) {
					req.setAttribute("files", result);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req, resp);
	}

	
	
}
