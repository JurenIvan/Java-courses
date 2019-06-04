package hr.fer.zemris.java.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet (urlPatterns= {"/trigonometric"})
public class TrigonometricServlet extends HttpServlet {
	private static final String FUNCTION_OUTPUT_FORMAT = "%.5f";
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String a=req.getParameter("a");
		String b=req.getParameter("b");
		
		int aValue=0;
		int bValue=360;
		try {
			aValue=Integer.parseInt(a);
		}catch (NumberFormatException e) {
		}
		try {
			bValue=Integer.parseInt(b);
		}catch (NumberFormatException e) {
		}
		
		if(aValue>bValue) {
			int temp=aValue;
			aValue=bValue;
			bValue=temp;
		}
		if(bValue>aValue+720) {
			b=a+720;
		}
		
		String sin[]=new String[bValue-aValue+1];
		String cos[]=new String[bValue-aValue+1];
		String numbers[]=new String[bValue-aValue+1];
		
		for(int counter=0, i=aValue;i<=bValue;i++, counter++) {
			numbers[counter]=String.valueOf(i);
			sin[counter]=String.format(FUNCTION_OUTPUT_FORMAT, Math.sin(Math.toRadians(i)));
			cos[counter]=String.format(FUNCTION_OUTPUT_FORMAT, Math.cos(Math.toRadians(i)));
		}
		
		req.setAttribute("numData", numbers);
		req.setAttribute("sinData", sin);
		req.setAttribute("cosData", cos);
		
		
		
		req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(req, resp);
	}
	
}
