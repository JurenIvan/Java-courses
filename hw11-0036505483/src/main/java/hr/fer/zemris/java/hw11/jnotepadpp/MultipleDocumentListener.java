package hr.fer.zemris.java.hw11.jnotepadpp;

/**
 * Simple interface that models listener for {@link MultipleDocumentModel}
 * 
 * @author juren
 *
 */
public interface MultipleDocumentListener {
	/**
	 * Method that is called when we want to notify that currentModel became
	 * previous model
	 * 
	 * @param previousModel previous one
	 * @param currentModel  new one
	 */
	void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel);

	/**
	 * Method that is called when we want to notify that a new document has been
	 * added into collection of documents
	 * 
	 * @param model that is added
	 */
	void documentAdded(SingleDocumentModel model);

	/**
	 * Method that is called when we want to notify that a document has been removed
	 * from collection of documents
	 * 
	 * @param model that is removed
	 */
	void documentRemoved(SingleDocumentModel model);
}