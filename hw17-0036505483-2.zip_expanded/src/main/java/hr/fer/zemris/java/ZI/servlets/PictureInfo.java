package hr.fer.zemris.java.ZI.servlets;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import hr.fer.zemris.java.hw17.jvdraw.drawingModel.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.drawingModel.DrawingModelImpl;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Circle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FilledTriangle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Line;
import hr.fer.zemris.java.hw17.jvdraw.visitors.CalculateShapesVisitor;

@WebServlet("/picture")
public class PictureInfo extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		DrawingModel drawingModel=new DrawingModelImpl();
		String path = req.getServletContext().getRealPath("/WEB-INF/images");
		String fileName=req.getParameterValues("name")[0];
		Path p=Paths.get(path,fileName);
		
		List<String> lines;
		try {
			lines = Files.readAllLines(p);
			List<GeometricalObject> listOfParsedObjects = new ArrayList<>();
			for (var line : lines) {
				String splitted[] = line.split(" ");
				if (splitted[0].equals("LINE")) {
					listOfParsedObjects.add(createLineFromStringArray(splitted));
				} else if (splitted[0].equals("CIRCLE")) {
					listOfParsedObjects.add(createCircleFromStringArray(splitted));
				} else if (splitted[0].equals("FCIRCLE")) {
					listOfParsedObjects.add(createFilledCircleFromStringArray(splitted));
				}else if (splitted[0].equals("FTRIANGLE")) {
					listOfParsedObjects.add(createFilledTriangleFromStringArray(splitted));
				}
			}

			drawingModel.clear();
			for (var elem : listOfParsedObjects) {
				drawingModel.add(elem);
			}

		} catch (Exception e) {
		}
		
		
		
		CalculateShapesVisitor csv=new CalculateShapesVisitor();
		for (int i = 0; i < drawingModel.getSize(); i++) {
			drawingModel.getObject(i).accept(csv);
		}
		
		req.setAttribute("imageName",fileName);
		req.setAttribute("lines", csv.getLineN());
		req.setAttribute("circles", csv.getCircleN());
		req.setAttribute("fcircles", csv.getFcircleN());
		req.setAttribute("ftriangles", csv.getFtriangleN());

		req.getRequestDispatcher("/WEB-INF/pages/picture.jsp").forward(req, resp);
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
