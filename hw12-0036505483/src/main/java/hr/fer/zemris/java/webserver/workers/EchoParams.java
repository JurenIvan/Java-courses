package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.RequestContext;

public class EchoParams implements IWebWorker {

	private final String DEFAULT_STYLE = "<style>\r\n" + "table {\r\n" + "  width:100%;\r\n" + "}\r\n"
			+ "table, th, td {\r\n" + "  border: 1px solid black;\r\n" + "  border-collapse: collapse;\r\n" + "}\r\n"
			+ "th, td {\r\n" + "  padding: 15px;\r\n" + "  text-align: left;\r\n" + "}\r\n"
			+ "table#t01 tr:nth-child(even) {\r\n" + "  background-color: #eee;\r\n" + "}\r\n"
			+ "table#t01 tr:nth-child(odd) {\r\n" + " background-color: #fff;\r\n" + "}\r\n" + "</style>";

	@Override
	public void processRequest(RequestContext context) throws Exception {
		StringBuilder tableHTML = new StringBuilder();
		tableHTML.append(DEFAULT_STYLE);
		tableHTML.append("<table id=\"t01\">");
		for (String entry : context.getParameterNames()) {
			tableHTML.append("<tr>");

			tableHTML.append("<td>");
			tableHTML.append(entry);
			tableHTML.append("</td>");
			tableHTML.append("<td>");
			tableHTML.append(context.getParameter(entry));
			tableHTML.append("</td>");
			tableHTML.append("</tr>");
		}
		tableHTML.append("</table>");
		context.write(tableHTML.toString());
	}

}
