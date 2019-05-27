package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.RequestContext;

public interface IWebWorker {
	public void processRequest(RequestContext context) throws Exception;
}