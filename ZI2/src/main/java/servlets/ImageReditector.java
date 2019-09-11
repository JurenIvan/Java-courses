package servlets;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/imageRedirect")
public class ImageReditector extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String itemSelected = request.getParameter("itemSelected");

		Map<String, Map<Integer, Integer>> var = (Map<String, Map<Integer, Integer>>) getServletContext()
				.getAttribute("values");

		Map<Integer, Integer> a = var.get(itemSelected);

		TreeMap<Integer, Integer> sortedMap = new TreeMap<Integer, Integer>();

		a.forEach((month, quan) -> sortedMap.put(month, quan));

		var output = sortedMap.values().stream().map(e->e.toString()).collect(Collectors.joining(","));
		
		

		request.setAttribute("name", itemSelected);
		request.setAttribute("col", output);

		request.getRequestDispatcher("/WEB-INF/pages/details.jsp").forward(request, response);

	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
}