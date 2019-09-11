package hr.fer.zemris.java.ZI.servlets;

import java.awt.Color;
import java.awt.Point;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw17.jvdraw.shapes.Circle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FilledTriangle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Line;

@WebServlet("/upload")
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getServletContext().getRealPath("/WEB-INF/images");
		String fileName = (String) req.getParameter("title");
		String fileContent = (String) req.getParameter("content");

		String a = checkInput(fileName, fileContent);
		if (a != null) {
			req.setAttribute("message", a);
			req.getRequestDispatcher("/WEB-INF/pages/wrongParameters.jsp").forward(req, resp);
			return;
		}

		try {
			if (!Files.exists(Paths.get(path))) {
				Files.createDirectories(Paths.get(path));
			}

			FileWriter fw = new FileWriter(Paths.get(path, fileName).toFile());
			fw.write(fileContent);
			fw.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("Success...");
		resp.sendRedirect(req.getServletContext().getContextPath() + "/main");

	}

	private String checkInput(String fileName, String fileContent) {
		if (fileName.contains("\\d+") || fileName.contains("\\s+") || fileName.contains("\\r+")
				|| fileName.contains("\n")) {
			return "Invalid name";
		}
		try {

			for (var line : fileContent.split("[\\r\\n]+")) {
				String splitted[] = line.split(" ");
				if (splitted[0].equals("LINE")) {
					createLineFromStringArray(splitted);
				} else if (splitted[0].equals("CIRCLE")) {
					createCircleFromStringArray(splitted);
				} else if (splitted[0].equals("FCIRCLE")) {
					createFilledCircleFromStringArray(splitted);
				} else if (splitted[0].equals("FTRIANGLE")) {
					createFilledTriangleFromStringArray(splitted);
				} else {
					return "Invalid content";
				}
			}
		} catch (Exception e) {
			return "Invalid content";
		}
		return null;

	}

	private GeometricalObject createFilledTriangleFromStringArray(String[] splitted) {
		int x[] = new int[3];
		int y[] = new int[3];

		for (int i = 0; i < 3; i++) {
			x[i] = Integer.parseInt(splitted[1 + 2 * i]);
		}
		for (int i = 0; i < 3; i++) {
			y[i] = Integer.parseInt(splitted[2 + 2 * i]);
		}

		return new FilledTriangle(
				new Color(Integer.parseInt(splitted[7]), Integer.parseInt(splitted[8]), Integer.parseInt(splitted[9])),
				new Color(Integer.parseInt(splitted[10]), Integer.parseInt(splitted[11]),
						Integer.parseInt(splitted[12])),
				x, y);
	}

	/**
	 * Method that is used to get circle out of parsed string.
	 * 
	 * @param splitted array of tokens
	 * @return Circle
	 */
	private FilledCircle createFilledCircleFromStringArray(String[] splitted) {
		return new FilledCircle(new Point(Integer.parseInt(splitted[1]), (Integer.parseInt(splitted[2]))),
				new Point(Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2]) + Integer.parseInt(splitted[3])),
				new Color(Integer.parseInt(splitted[4]), Integer.parseInt(splitted[5]), Integer.parseInt(splitted[6])),
				new Color(Integer.parseInt(splitted[7]), Integer.parseInt(splitted[8]), Integer.parseInt(splitted[9])));
	}

	/**
	 * Method that is used to get line out of parsed string
	 * 
	 * @param splitted array of tokens
	 * @return Circle
	 */
	private Line createLineFromStringArray(String[] splitted) {
		return new Line(new Point(Integer.parseInt(splitted[1]), (Integer.parseInt(splitted[2]))),
				new Point(Integer.parseInt(splitted[3]), (Integer.parseInt(splitted[4]))),
				new Color(Integer.parseInt(splitted[5]), Integer.parseInt(splitted[6]), Integer.parseInt(splitted[7])));
	}

	/**
	 * Method that is used to get circle out of parsed string
	 * 
	 * @param splitted array of tokens
	 * @return Circle
	 */
	private Circle createCircleFromStringArray(String[] splitted) {
		return new Circle(new Point(Integer.parseInt(splitted[1]), (Integer.parseInt(splitted[2]))),
				new Point(Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2]) + Integer.parseInt(splitted[3])),
				new Color(Integer.parseInt(splitted[4]), Integer.parseInt(splitted[5]), Integer.parseInt(splitted[6])));
	}

}
