package hr.fer.zemris.java.servlets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
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

import hr.fer.zemris.java.hw17.jvdraw.models.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.models.DrawingModelImpl;
import hr.fer.zemris.java.hw17.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw17.jvdraw.objects.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.objects.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.objects.Line;
import hr.fer.zemris.java.hw17.jvdraw.objects.Triangle;
import hr.fer.zemris.java.hw17.jvdraw.objects.visitors.GeometricalObjectBBCalculator;
import hr.fer.zemris.java.hw17.jvdraw.objects.visitors.GeometricalObjectPainter;

@WebServlet("/slika")
public class SlikaServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = req.getParameter("name");

		String path = req.getServletContext().getRealPath("/WEB-INF/images");
		path = path + "/" + name;
		DrawingModel model = new DrawingModelImpl();

		Path p = Paths.get(path);

		if (!Files.exists(p)) {
			req.setAttribute("message", "Invalid link");
			req.getRequestDispatcher("/WEB-INF/pages/wrongParameters.jsp").forward(req, resp);
			return;
		}

		readObjectsFromDocument(p, model);

		GeometricalObjectBBCalculator bbcalc = new GeometricalObjectBBCalculator();
		for (int i = 0; i < model.getSize(); i++) {
			model.getObject(i).accept(bbcalc);
		}

		Rectangle box = bbcalc.getBoundingBox();
		BufferedImage image = new BufferedImage(box.width, box.height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = image.createGraphics();
		g.translate(-box.x, -box.y);
		g.setColor(Color.WHITE);
		g.fillRect(box.x, box.y, box.width, box.height);
		GeometricalObjectPainter painter = new GeometricalObjectPainter(g);
		for (int i = 0; i < model.getSize(); i++) {
			model.getObject(i).accept(painter);
		}

		resp.setContentType("image/png");
		
		ImageIO.write(image, "png", resp.getOutputStream());
	}

	private void readObjectsFromDocument(Path openedFilePath, DrawingModel model) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(openedFilePath.toFile()))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("LINE")) {
					model.add(createLineFromString(line));
				} else if (line.startsWith("CIRCLE")) {
					model.add(createCircleFromString(line));
				} else if (line.startsWith("FCIRCLE")) {
					model.add(createFilledCircleFromString(line));
				}
				if (line.startsWith("FTRIANGLE")) {
					model.add(createTriangleFromString(line));
				}
			}
		}
	}

	private GeometricalObject createTriangleFromString(String line) {
		String[] splitted = line.split("\\s+");
		if (splitted.length != 13) {
			throw new IllegalArgumentException();
		}
		int x1 = Integer.parseInt(splitted[1]);
		int y1 = Integer.parseInt(splitted[2]);
		int x2 = Integer.parseInt(splitted[3]);
		int y2 = Integer.parseInt(splitted[4]);
		int x3 = Integer.parseInt(splitted[5]);
		int y3 = Integer.parseInt(splitted[6]);
		int r1 = Integer.parseInt(splitted[7]);
		int g1 = Integer.parseInt(splitted[8]);
		int b1 = Integer.parseInt(splitted[9]);
		int r2 = Integer.parseInt(splitted[10]);
		int g2 = Integer.parseInt(splitted[11]);
		int b2 = Integer.parseInt(splitted[12]);

		List<Integer> x = new ArrayList<>();
		List<Integer> y = new ArrayList<>();
		x.add(x1);
		x.add(x2);
		x.add(x3);
		y.add(y1);
		y.add(y2);
		y.add(y3);

		return new Triangle(x, y, new Color(r1, g1, b1), new Color(r2, g2, b2));
	}

	/**
	 * Method that creates the {@link FilledCircle} based on the line that
	 * represents it. Line should start with FCIRCLE followed by nine numbers, x
	 * coordinate of the center, y coordinate of the center, radius of the circle ,
	 * r,g,b values of foreground color, r,g,b values of the background color
	 * 
	 * @param line line representing a filled circle object
	 * @return object represented by the given line
	 */
	private GeometricalObject createFilledCircleFromString(String line) {
		String[] splitted = line.split("\\s+");
		if (splitted.length != 10) {
			throw new IllegalArgumentException();
		}
		int x1 = Integer.parseInt(splitted[1]);
		int y1 = Integer.parseInt(splitted[2]);
		int radius = Integer.parseInt(splitted[3]);
		int r1 = Integer.parseInt(splitted[4]);
		int g1 = Integer.parseInt(splitted[5]);
		int b1 = Integer.parseInt(splitted[6]);
		int r2 = Integer.parseInt(splitted[7]);
		int g2 = Integer.parseInt(splitted[8]);
		int b2 = Integer.parseInt(splitted[9]);

		return new FilledCircle(new Point(x1, y1), radius, new Color(r1, g1, b1), new Color(r2, g2, b2));
	}

	/**
	 * Method that creates the {@link Circle} based on the line that represents it.
	 * Line should start with CIRCLE followed by six numbers, x coordinate of the
	 * center, y coordinate of the center, radius of the circle , r,g,b values of
	 * foreground color.
	 * 
	 * @param line line representing a circle object
	 * @return object represented by the given line
	 */
	private GeometricalObject createCircleFromString(String line) {
		String[] splitted = line.split("\\s+");
		if (splitted.length != 7) {
			throw new IllegalArgumentException();
		}
		int x1 = Integer.parseInt(splitted[1]);
		int y1 = Integer.parseInt(splitted[2]);
		int radius = Integer.parseInt(splitted[3]);
		int r = Integer.parseInt(splitted[4]);
		int g = Integer.parseInt(splitted[5]);
		int b = Integer.parseInt(splitted[6]);

		return new Circle(new Point(x1, y1), radius, new Color(r, g, b));
	}

	/**
	 * Method that creates a {@link Line} object based on the line that represents
	 * it. Line should start with LINE followed by seven numbers, x and y
	 * coordinates of the starting point, x and y coordinates of the ending point,
	 * r,g,b values of the line color.
	 * 
	 * @param line line representing a line object
	 * @return object represented by the given line
	 */
	private GeometricalObject createLineFromString(String line) {
		String[] splitted = line.split("\\s+");
		if (splitted.length != 8) {
			throw new IllegalArgumentException();
		}
		int x1 = Integer.parseInt(splitted[1]);
		int y1 = Integer.parseInt(splitted[2]);
		int x2 = Integer.parseInt(splitted[3]);
		int y2 = Integer.parseInt(splitted[4]);
		int r = Integer.parseInt(splitted[5]);
		int g = Integer.parseInt(splitted[6]);
		int b = Integer.parseInt(splitted[7]);

		return new Line(new Point(x1, y1), new Point(x2, y2), new Color(r, g, b));
	}

}
