package servlets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/picGenerator")
public class PicGenerator extends HttpServlet{

	private static final int width=600;
	private static final int height=500;
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String[] values=req.getParameter("val").split(",");
		List<Integer> in=Arrays.stream(values).map(e->Integer.parseInt(e)).collect(Collectors.toList());
		
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.setColor(Color.white);
	    g2d.fillRect(0, 0, width, height);
		
	    
	    int numOfColls=in.size();
	    int widthOfColl=width/numOfColls;
	    int sumOfValues=in.stream().reduce(0, (a, b) -> a + b);
	    
	    
	    for(int i=0;i<numOfColls;i++) {
	    	g2d.setColor(new Color(1233*i));
	    	g2d.fillRect(i*widthOfColl, height-in.get(i)*height/sumOfValues, widthOfColl, height);
	    }
	    
		resp.setContentType("image/png");
	    ImageIO.write(bufferedImage, "png", resp.getOutputStream());
	}

}
