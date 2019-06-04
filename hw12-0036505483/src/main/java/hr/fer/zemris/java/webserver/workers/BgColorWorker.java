package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Implementation of {@link IWebWorker} that is responsible for page that will
 * inform user about change of color.
 * 
 * @author juren
 *
 */
public class BgColorWorker implements IWebWorker {
	/**
	 * constant that will be shown when color was not received
	 */
	private static final String MESSAGE_FAIL_SHORT = "Color was not set!";
	/**
	 * constant that will be shown when color is changed
	 */
	private static final String MESSAGE_SUC = "Color succesfully set to #";
	/**
	 * constant that will be shown when provided color is not of right form
	 */
	private static final String MESSAGE_FAIL = "Color was not set! Color should be formed as \"AB12CD\" ";
	/**
	 * constant holding string representing key for message
	 */
	private static final String MESSAGE_KEY = "message";
	/**
	 * constant holding string representing key for background color
	 */
	private static final String BG_COLOR_KEY = "bgcolor";

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String color;
		if ((color = context.getParameter(BG_COLOR_KEY)) != null) {
			if (color.trim().length() != 6) {
				context.setTemporaryParameter(MESSAGE_KEY, MESSAGE_FAIL);
			} else {
				context.setPersistentParameter(BG_COLOR_KEY, "#" + color.trim());
				context.setTemporaryParameter(MESSAGE_KEY, MESSAGE_SUC + color.trim());
			}
		} else {
			context.setPersistentParameter(MESSAGE_KEY, MESSAGE_FAIL_SHORT + color);
		}
		context.getDispatcher().dispatchRequest("\\private\\pages\\color.smscr");

	}

}
