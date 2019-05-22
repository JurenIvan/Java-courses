package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;

/**
 * Class that represents {@link MultipleDocumentModel} which is like a
 * placeholder for one or more {@link SingleDocumentModel}
 * 
 * @author juren
 *
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
	/**
	 * method that creates new {@link SingleDocumentModel}
	 * 
	 * @return new {@link SingleDocumentModel}
	 */
	SingleDocumentModel createNewDocument();

	/**
	 * Method that returns current {@link SingleDocumentModel}
	 * 
	 * @return current {@link SingleDocumentModel}
	 */
	SingleDocumentModel getCurrentDocument();

	/**
	 * Method that loads data from given path into a new
	 * {@link SingleDocumentModel}. Data is read with UTF-8 charset
	 * 
	 * @param path path of file that will be loaded into {@link SingleDocumentModel}
	 * @return new {@link SingleDocumentModel}
	 */
	SingleDocumentModel loadDocument(Path path);

	/**
	 * Method that saves {@link SingleDocumentModel} into provided path. Data is
	 * written with UTF-8 charset.
	 * 
	 * @param model
	 * @param newPath
	 */
	void saveDocument(SingleDocumentModel model, Path newPath);

	/**
	 * Method that closes and clears model form {@link MultipleDocumentModel}
	 * collection
	 * 
	 * @param model that will be removes
	 */
	void closeDocument(SingleDocumentModel model);

	/**
	 * Method that adds listener into collection of listeners for this
	 * {@link MultipleDocumentModel}
	 * 
	 * @param l listener
	 */
	void addMultipleDocumentListener(MultipleDocumentListener l);

	/**
	 * Method that removes provided listener from collection of listeners for this
	 * {@link MultipleDocumentModel}
	 * 
	 * @param l listener
	 */
	void removeMultipleDocumentListener(MultipleDocumentListener l);

	/**
	 * Method that returns current number of stored {@link SingleDocumentModel} that
	 * this instance of {@link MultipleDocumentModel} manages.
	 * 
	 * @return current number of stored {@link SingleDocumentModel}
	 */
	int getNumberOfDocuments();

	/**
	 * Method that gets us {@link SingleDocumentModel} stored at index-th place in
	 * internal collection of {@link MultipleDocumentModel}.
	 * 
	 * @param index position of {@link SingleDocumentModel}
	 * @return {@link SingleDocumentModel} stored at index-th place in internal
	 *         collection
	 */
	SingleDocumentModel getDocument(int index);

	/**
	 * Method that sets provided model as focused one.
	 * 
	 * @param model that will be set as focused one.
	 */
	void setCurrentDocument(SingleDocumentModel model);
}
