package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Implementation of {@link IWebWorker} that is responsible for page that will
 * add 2 numbers provided as parameters though url
 * 
 * @author juren
 *
 */
public class SumWorker implements IWebWorker {
	/**
	 * Constant that holds relative path to image shown when result is even
	 */
	private final String NAME_OF_FIRST_IMAGE = "\\images\\winBackground.jpg";
	/**
	 * Constant that holds relative path to image shown when result is odd
	 */
	private final String NAME_OF_SECOND_IMAGE = "\\images\\gr8.gif";

	@Override
	public void processRequest(RequestContext context) throws Exception {
		int a = 1;
		int b = 2;
		try {
			a = Integer.parseInt(context.getParameter("a"));
		} catch (NumberFormatException e) {
		}
		try {
			b = Integer.parseInt(context.getParameter("b"));
		} catch (NumberFormatException e) {
		}

		context.setTemporaryParameter("varA", String.valueOf(a));
		context.setTemporaryParameter("nameA", "a");
		context.setTemporaryParameter("varB", String.valueOf(b));
		context.setTemporaryParameter("nameB", "b");
		context.setTemporaryParameter("zbroj", String.valueOf(a + b));
		context.setTemporaryParameter("nameZbroj", "a+b");
		context.setTemporaryParameter("imgName", (a + b) % 2 == 0 ? NAME_OF_FIRST_IMAGE : NAME_OF_SECOND_IMAGE);

		context.getDispatcher().dispatchRequest("\\private\\pages\\calc.smscr");
	}

}
