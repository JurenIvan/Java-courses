package hr.fer.zemris.java.helper;

import java.nio.file.Paths;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Used to set up persistence used for loading images from "database" during a
 * boot-up.
 * 
 * @author juren
 *
 */
@WebListener
public class MyServletContextListener implements ServletContextListener {

	/**
	 * Constant storing relative path inside project to the descriptor file.
	 */
	private static final String descriptorPath = "/WEB-INF/opisnik.txt";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			TagLoader.getTagLoader(Paths.get(sce.getServletContext().getRealPath(descriptorPath)));
		} catch (Exception ignorable) {
		}
		System.out.println("TEST");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
