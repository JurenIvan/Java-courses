package hr.fer.zemris.java.hw17.Servleti;

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
import hr.fer.zemris.java.hw17.jvdraw.shapes.Circle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FTriangle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Line;

@WebServlet("/save")
public class SaveIt extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		try {
		String data = req.getParameter("text").toString();
		String[] lines = data.split("\r\n");
		
		System.out.println(data);
		System.out.println(lines);
		
		
		String fileName=req.getParameter("fileName").toString();
		
		if (!fileName.endsWith(".jvd"))
			throw new IllegalArgumentException();
		
		List<GeometricalObject> listOfParsedObjects = new ArrayList<>();
		
		for (var line : lines) {
			String splitted[] = line.split(" ");
			if (splitted[0].equals("LINE")) {
				listOfParsedObjects.add(createLineFromStringArray(splitted));
			} else if (splitted[0].equals("CIRCLE")) {
				listOfParsedObjects.add(createCircleFromStringArray(splitted));
			} else if (splitted[0].equals("FCIRCLE")) {
				listOfParsedObjects.add(createFilledCircleFromStringArray(splitted));
			} else if(splitted[0].equals("FTRIANGLE")) {
				listOfParsedObjects.add(createFTriangleFromStringArray(splitted));
			}
		}
		
		//Nema gresaka do sad
		
		if (!Files.isDirectory(Paths.get(req.getServletContext().getRealPath("/WEB-INF/images")))) {
			Files.createDirectory(Paths.get(req.getServletContext().getRealPath("/WEB-INF/images")));
		}
		
		//napravljen direktorij
		
		Path filePath=Paths.get(req.getServletContext().getRealPath("/WEB-INF/images"),fileName);
		
		StringBuilder output=new StringBuilder();
		for(var line:lines) {
			output.append(line);
			output.append("/n/r");
		}
		
		Files.write(filePath, output.toString().getBytes());
		
		}catch (Exception e) {
			  req.getRequestDispatcher("WEB-INF/ERROR.html").forward(req,resp);
		}
		
		
	}
	
	
	
	private void loadFile(Path path) {

		

	
		
	
	}

	/**
	 * Method that is used to get circle out of parsed string.
	 * 
	 * @param splitted array of tokens
	 * @return Circle
	 */
	private FTriangle createFTriangleFromStringArray(String[] splitted) {
		return new FTriangle(
				new Color(Integer.parseInt(splitted[7]), Integer.parseInt(splitted[8]),Integer.parseInt(splitted[9])),
				new Color(Integer.parseInt(splitted[10]), Integer.parseInt(splitted[11]),Integer.parseInt(splitted[12])),
				
				new Point(Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2])),
				new Point(Integer.parseInt(splitted[3]),Integer.parseInt(splitted[4])),
				new Point(Integer.parseInt(splitted[5]),Integer.parseInt(splitted[6]))
				);
	}
	
	
	private FilledCircle createFilledCircleFromStringArray(String[] splitted) {
		return new FilledCircle
				(new Point(Integer.parseInt(splitted[1]), (Integer.parseInt(splitted[2]))),
				new Point(Integer.parseInt(splitted[1]),
						Integer.parseInt(splitted[2]) + Integer.parseInt(splitted[3])),
				new Color(Integer.parseInt(splitted[4]), Integer.parseInt(splitted[5]),
						Integer.parseInt(splitted[6])),
				new Color(Integer.parseInt(splitted[7]), Integer.parseInt(splitted[8]),
						Integer.parseInt(splitted[9])));
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
				new Color(Integer.parseInt(splitted[5]), Integer.parseInt(splitted[6]),
						Integer.parseInt(splitted[7])));
	}

	/**
	 * Method that is used to get circle out of parsed string
	 * 
	 * @param splitted array of tokens
	 * @return Circle
	 */
	private Circle createCircleFromStringArray(String[] splitted) {
		return new Circle(new Point(Integer.parseInt(splitted[1]), (Integer.parseInt(splitted[2]))),
				new Point(Integer.parseInt(splitted[1]),
						Integer.parseInt(splitted[2]) + Integer.parseInt(splitted[3])),
				new Color(Integer.parseInt(splitted[4]), Integer.parseInt(splitted[5]),
						Integer.parseInt(splitted[6])));
	}
	
	
}
