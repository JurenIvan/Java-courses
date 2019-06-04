package hr.fer.zemris.java.webserver;

/**
 * Interface that defines {@link #dispatchRequest(String)} method used by
 * {@link SmartHttpServer} to indicate that it can do something with url
 * provided
 * 
 * @author juren
 *
 */
@FunctionalInterface
public interface IDispatcher {
	/**
	 * Method called when {@link SmartHttpServer} has to process provided string containing path
	 * @param urlPath string that holds requests for server 
	 * @throws Exception if any error occurs
	 */
	void dispatchRequest(String urlPath) throws Exception;
}