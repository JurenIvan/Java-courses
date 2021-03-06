package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

/**
 * Class that models a JTabbedPane and implements {@link MultipleDocumentModel}.
 * It is used as a bridge between UI and logic(storing structures) in background
 * 
 * @author juren
 *
 */
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {
	private static final long serialVersionUID = 1L;

	/**
	 * Collection of Document stored inside this {@link MultipleDocumentModel}
	 */
	private List<SingleDocumentModel> listOfDocuments;
	/**
	 * List of Listeners
	 */
	private List<MultipleDocumentListener> listeners;
	/**
	 * private variable representing {@link SingleDocumentModel} that holds the
	 * "focus" or current Document
	 */
	private SingleDocumentModel focused;
	/**
	 * private variable representing {@link HashMap} of icons used because we dont
	 * want to he excessive on IO operations. So its better to cache images into
	 * ram.
	 */
	private HashMap<String, ImageIcon> loadedImages;

	/**
	 * Constant that holds string representation of path to icons needed
	 */
	private static final String FIXED_PART_OF_PATH = ".\\icons\\";
	/**
	 * Constant for displaying size of Icon in tabs.
	 */
	private static final int DEFAULT_ICON_SIZE = 20;

	/**
	 * Standard constructor where initialization of variables happens. Also adds
	 * listener to model.
	 */
	public DefaultMultipleDocumentModel() {
		listOfDocuments = new ArrayList<>();
		listeners = new ArrayList<>();
		loadedImages = new HashMap<>();

		addChangeListener(e -> {
			notifyDocumentChangedListeners(focused, focused = listOfDocuments.get(getSelectedIndex()));
		});

	}

	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return listOfDocuments.iterator();
	}

	@Override
	public SingleDocumentModel createNewDocument() {
		return createNewDocument(null);
	}

	@Override
	public SingleDocumentModel getCurrentDocument() {
		return focused;
	}

	@Override
	public SingleDocumentModel loadDocument(Path path) {
		Objects.requireNonNull(path, "path must not be null");
		for (var doc : listOfDocuments) {
			if (path.equals(doc.getFilePath())) {
				SingleDocumentModel prevFocused = focused;
				focused = doc;
				setSelectedIndex(listOfDocuments.indexOf(doc));
				notifyDocumentChangedListeners(prevFocused, focused);
				return doc;
			}
		}
		SingleDocumentModel newOne = createNewDocument(path);

		String text = null;
		try {
			text = Files.readString(path, Charset.forName("UTF-8"));
		} catch (IOException e1) {
		}
		newOne.getTextComponent().setText(text);
		return newOne;
	}

	/**
	 * Method that creates new document from provided path. If path is null then new
	 * clean document is loaded. Listener is added to {@link SingleDocumentModel}
	 * when it is created.
	 * 
	 * @param path of file that we want to load or null if new document is wanted
	 * @return {@link SingleDocumentModel} that is either loaded from provided path
	 *         or clear one.
	 */
	private SingleDocumentModel createNewDocument(Path path) {
		SingleDocumentModel prevFocused = focused;
		focused = new DefaultSingleDocumentModel(path, new JTextArea());
		listOfDocuments.add(focused);

		addTab(path == null ? "(unnamed)" : path.getFileName().toString(), new JScrollPane(focused.getTextComponent()));
		setIconAt(listOfDocuments.indexOf(focused), getIconNamed("saveGray.png"));

		setSelectedIndex(listOfDocuments.size() - 1);
		notifyDocumentChangedListeners(prevFocused, focused);
		notifyDocumentAddedListeners(focused);

		focused.addSingleDocumentListener(new SingleDocumentListener() {
			@Override
			public void documentModifyStatusUpdated(SingleDocumentModel model) {
				setIconAt(listOfDocuments.indexOf(model), setAppropriateIcon());
			}

			@Override
			public void documentFilePathUpdated(SingleDocumentModel model) {
				setTitleAt(listOfDocuments.indexOf(model), model.getFilePath().getFileName().toString());
				setIconAt(listOfDocuments.indexOf(model), setAppropriateIcon());

			}

			private Icon setAppropriateIcon() {
				return focused.isModified() ? getIconNamed("saveGreen.png") : getIconNamed("saveGray.png");
			}
		});

		return focused;
	}

	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		for (var doc : listOfDocuments) {
			if (newPath.equals(doc.getFilePath()) && !doc.equals(focused)) {
				throw new IllegalStateException();
			}
		}

		model.setFilePath(newPath);
		try {
			Files.write(newPath, model.getTextComponent().getText().getBytes());
		} catch (IOException e) {
			throw new IllegalStateException();
		}
		model.setModified(false);
		notifyDocumentAddedListeners(model);
		notifyDocumentChangedListeners(null, model);
	}

	@Override
	public void closeDocument(SingleDocumentModel model) {
		Objects.requireNonNull(model, "Can not remove null model");
		int index = listOfDocuments.indexOf(model);

		if (getNumberOfDocuments() == 1) {
			createNewDocument(null);
		}
		removeTabAt(index);
		listOfDocuments.remove(index);
		focused = iterator().next();
		notifyDocumentRemovedListeners(model);
		notifyDocumentChangedListeners(model, focused);
	}

	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		Objects.requireNonNull(l, "Can not register a null listener");
		List<MultipleDocumentListener> newListeners = new ArrayList<>(listeners);
		newListeners.add(l);
		listeners = newListeners;
	}

	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		List<MultipleDocumentListener> newListeners = new ArrayList<>(listeners);
		newListeners.remove(l);
		listeners = newListeners;
	}

	@Override
	public int getNumberOfDocuments() {
		return listOfDocuments.size();
	}

	@Override
	public SingleDocumentModel getDocument(int index) {
		return listOfDocuments.get(index);
	}

	/**
	 * Method that is responsible for hashing and memoisation icons that are needed
	 * 
	 * @param name of icon (nameOfFile.PNG)
	 * @return {@link ImageIcon} saved like that
	 * 
	 * @throws IllegalArgumentException when no such icon is found
	 */
	private ImageIcon getIconNamed(String name) {
		Objects.requireNonNull(name, "icon must have a name");

		ImageIcon ic = loadedImages.get(name);
		if (ic != null)
			return ic;

		try (InputStream is = this.getClass().getResourceAsStream(FIXED_PART_OF_PATH + name)) {
			byte[] bytes = is.readAllBytes();
			ImageIcon icon = new ImageIcon((new ImageIcon(bytes)).getImage().getScaledInstance(DEFAULT_ICON_SIZE,
					DEFAULT_ICON_SIZE, Image.SCALE_DEFAULT));
			loadedImages.put(name, icon);
			return icon;
		} catch (IOException e) {
			throw new IllegalArgumentException("No image on provided path");
		}
	}

	// ------------NOTIFY METHODS--------------

	/**
	 * Method that notifies all registered listeners when focused document changes
	 * into another one.
	 * 
	 * @param prevFocused document that was previously focused
	 * @param currFocused newly focused document
	 */
	private void notifyDocumentChangedListeners(SingleDocumentModel prevFocused, SingleDocumentModel currFocused) {
		for (var listener : listeners) {
			listener.currentDocumentChanged(prevFocused, currFocused);
		}
	}

	/**
	 * Method that notifies all registered listeners when document is added
	 * 
	 * @param model {@link SingleDocumentModel} that is added
	 */
	private void notifyDocumentAddedListeners(SingleDocumentModel model) {
		for (var listener : listeners) {
			listener.documentAdded(model);
		}
	}

	/**
	 * Method that notifies all registered listeners when document is removed
	 * 
	 * @param model {@link SingleDocumentModel} that is removed
	 */
	private void notifyDocumentRemovedListeners(SingleDocumentModel model) {
		for (var listener : listeners) {
			listener.documentRemoved(model);
		}
	}

	@Override
	public void setCurrentDocument(SingleDocumentModel model) {
		setSelectedIndex(listOfDocuments.indexOf(model));
	}

}
