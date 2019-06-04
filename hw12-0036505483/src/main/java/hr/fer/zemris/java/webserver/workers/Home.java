package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Implementation of {@link IWebWorker} that is responsible for home page of our
 * server
 * 
 * @author juren
 *
 */
public class Home implements IWebWorker {

	/**
	 * constant holding key for color in temporary parameter map
	 */
	private static final String BACKGROUND_TEMP_KEY = "background";
	/**
	 * constant holding key for color in persistent parameter map
	 */
	private static final String BG_COLOR_KEY = "bgcolor";

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String background;
		if ((background = context.getPersistentParameter(BG_COLOR_KEY)) != null) {
			context.setTemporaryParameter(BACKGROUND_TEMP_KEY, background);
		}
		context.getDispatcher().dispatchRequest("\\private\\pages\\home.smscr");
	}

}
