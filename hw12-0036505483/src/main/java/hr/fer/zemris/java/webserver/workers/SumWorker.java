package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.RequestContext;

public class SumWorker implements IWebWorker {

	private final String NAME_OF_FIRST_IMAGE = "\\images\\winBackground.jpg";
	private final String NAME_OF_SECOND_IMAGE = "\\images\\gr8.gif";

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String[] vars = new String[2];
		int[] params = new int[2];
		int count = 0;

		for (String entry : context.getParameterNames()) {
			vars[count] = entry;
			try {
				params[count] = Integer.parseInt(context.getParameter(entry));
			} catch (NumberFormatException e) {
				params[count] = count + 1;
			} finally {
				count++;
			}
		}

		context.setTemporaryParameter("varA", String.valueOf(params[0]));
		context.setTemporaryParameter("nameA", vars[0]);
		context.setTemporaryParameter("varB", String.valueOf(params[1]));
		context.setTemporaryParameter("nameB", vars[1]);
		context.setTemporaryParameter("zbroj", String.valueOf(params[1] + params[0]));
		context.setTemporaryParameter("nameZbroj", vars[0] +"+"+ vars[1]);
		context.setTemporaryParameter("imgName",
				(params[1] + params[0]) % 2 == 0 ? NAME_OF_FIRST_IMAGE : NAME_OF_SECOND_IMAGE);

		context.getDispatcher().dispatchRequest("\\private\\pages\\calc.smscr");
	}

}
