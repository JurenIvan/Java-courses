package hr.fer.zemris.java.hw11.jnotepadpp;

/**
 * Simple interface that models listener for {@link SingleDocumentModel}
 * 
 * @author juren
 *
 */
public interface SingleDocumentListener {
	/**
	 * Method called when {@link SingleDocumentModel} modify flag is changed
	 * 
	 * @param model that will be notified
	 */
	void documentModifyStatusUpdated(SingleDocumentModel model);

	/**
	 * Method called when {@link SingleDocumentModel} path is modified
	 * 
	 * @param model that will be notified
	 */
	void documentFilePathUpdated(SingleDocumentModel model);
}