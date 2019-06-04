package hr.fer.zemris.java.webserver;

/**
 * Interface containing only one method that models objects capable of
 * processing {@link RequestContext} requests.
 * 
 * @author juren
 *
 */
@FunctionalInterface
public interface IWebWorker {
	/**
	 * Method processes {@link RequestContext} requests and creates certain content.
	 * 
	 * @param context needed for data flow
	 * @throws Exception if method is not capable of doing what it is supposed to
	 *                   do.
	 */
	public void processRequest(RequestContext context) throws Exception;
}