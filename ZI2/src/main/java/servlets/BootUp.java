package servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import zi2.prvi.Calc;

@WebListener
public class BootUp implements ServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		try {
			List<String> pathOfData = Files.readAllLines(Paths.get(sce.getServletContext().getRealPath("/WEB-INF/staza")));
			Calc c = new Calc();
			c.DoCalculations(Paths.get(pathOfData.get(0)));
			sce.getServletContext().setAttribute("values", c.values);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
