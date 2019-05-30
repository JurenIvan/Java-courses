package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.RequestContext;

public class Home implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String background;
		if((background=context.getPersistentParameter("bgcolor"))!=null) {
			context.setTemporaryParameter("background",background);
		}
		context.getDispatcher().dispatchRequest("\\private\\pages\\home.smscr");
	}

}
