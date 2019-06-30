package hr.fer.zemris.java.helper;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet used for displaying requested picture or thumbnail. If specific
 * thumbnail is requested and no such exists, it is automatically created.
 * 
 * @author juren
 *
 */
@WebServlet("/servleti/picture")
public class PictureLoaderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Constant storing relative path inside project to the thumbnails folder.
	 */
	private static final String thumbnailPath = "/WEB-INF/thumbnails";
	/**
	 * Constant storing relative path inside project to the fullResolution pictures
	 * folder.
	 */
	private static final String fullImagePath = "/WEB-INF/slike";

	/**
	 * Constant storing size of thumbnails.
	 */
	private static final int thumbnailSize = 150;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/jpg");
		Path path = null;

		String filename = req.getParameter("filename");
		String thumbnail = req.getParameter("thumbnail");

		if ("true".equals(thumbnail)) {
			checkAndCreateThumbnail(req, filename);
			path = Paths.get(req.getServletContext().getRealPath(thumbnailPath) + "/" + filename);
		} else {
			path = Paths.get(req.getServletContext().getRealPath(fullImagePath) + "/" + filename);
		}

		BufferedImage renderedImage = ImageIO.read(path.toFile());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(renderedImage, "jpg", baos);
		resp.getOutputStream().write(baos.toByteArray());
	}

	/**
	 * Method that checks whether in thumbnail photos folder exists requested
	 * thumbnail. If no, creates it from full res version.
	 * 
	 * @param req      used to determine path to folders with pictures
	 * @param filename used for getting specific image
	 * @throws IOException if no requested image exists
	 */
	private void checkAndCreateThumbnail(HttpServletRequest req, String filename) throws IOException {
		if (!Files.isDirectory(Paths.get(req.getServletContext().getRealPath(thumbnailPath)))) {
			Files.createDirectory(Paths.get(req.getServletContext().getRealPath(thumbnailPath)));
		}

		if (!Files.isRegularFile(Paths.get(req.getServletContext().getRealPath(thumbnailPath), filename))) {
			Path pathForReading = Paths.get(req.getServletContext().getRealPath(fullImagePath), filename);
			Path pathForWritting = Paths.get(req.getServletContext().getRealPath(thumbnailPath), filename);

			BufferedImage img = ImageIO.read(pathForReading.toFile());

			Image image = img.getScaledInstance(thumbnailSize, thumbnailSize, Image.SCALE_SMOOTH);
			BufferedImage buffered = new BufferedImage(thumbnailSize, thumbnailSize, BufferedImage.TYPE_INT_RGB);
			buffered.getGraphics().drawImage(image, 0, 0, null);

			ImageIO.write((BufferedImage) buffered, "png", pathForWritting.toFile());
		}
	}

}
