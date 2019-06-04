package hr.fer.zemris.java.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet (urlPatterns= {"/setcolor"})
public class SetColorServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String color=req.getParameter("color");
		
		if(color==null) {
			color="FFFFFF";
		}
		
		req.getSession().setAttribute("pickedBgColor", color);
		req.getRequestDispatcher("/index.jsp").forward(req, resp);
	}
	
}
