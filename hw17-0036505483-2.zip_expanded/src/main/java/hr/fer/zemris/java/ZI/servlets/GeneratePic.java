package hr.fer.zemris.java.ZI.servlets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw17.jvdraw.drawingModel.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.drawingModel.DrawingModelImpl;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Circle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FilledTriangle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Line;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectBBCalculator;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectPainter;

@WebServlet("/getImage")
public class GeneratePic extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setContentType("image/png");
		DrawingModel drawingModel=new DrawingModelImpl();
		String path = req.getServletContext().getRealPath("/WEB-INF/images");
		Path p=Paths.get(path,req.getParameterValues("imageName"));
		
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
		
		GeometricalObjectBBCalculator bbcalc = new GeometricalObjectBBCalculator();
		for (int i = 0; i < drawingModel.getSize(); i++) {
			drawingModel.getObject(i).accept(bbcalc);
		}
		Rectangle box = bbcalc.getBoundingBox();
		BufferedImage image = new BufferedImage(box.width, box.height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = image.createGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, box.width, box.height);
		g.translate(-box.x, -box.y);

		GeometricalObjectPainter gop = new GeometricalObjectPainter(g);

		for (int i = 0; i < drawingModel.getSize(); i++) {
			drawingModel.getObject(i).accept(gop);
		}
		
		ImageIO.write(image, "png", resp.getOutputStream());
		
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
