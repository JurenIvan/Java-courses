package hr.fer.zemris.java.webserver.workers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import hr.fer.zemris.java.webserver.RequestContext;

public class CircleWorker implements IWebWorker {
	@Override
	public void processRequest(RequestContext context) throws IOException {

		BufferedImage bim = new BufferedImage(200, 200, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = bim.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, bim.getWidth(), bim.getHeight());
		g2d.setColor(Color.RED);
		g2d.fillOval(0, 0, bim.getWidth(), bim.getHeight());
		g2d.dispose();
		context.setMimeType("image/png");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bim, "png", baos);
		byte[] podaci = baos.toByteArray();
		context.write(podaci);
	}
}