package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {
	private static final long serialVersionUID = 1L;

	private List<SingleDocumentModel> listOfDocuments;
	private List<MultipleDocumentListener> listeners;
	private SingleDocumentModel focused;

	private static final String FIXED_PART_OF_PATH = ".\\icons\\";
	private static final int DEFAULT_ICON_SIZE = 20;

	public DefaultMultipleDocumentModel() {
		listOfDocuments = new ArrayList<>();
		listeners = new ArrayList<>();

		addChangeListener(e -> { 
			notifyDocumentChangedListeners(focused, focused=listOfDocuments.get(getSelectedIndex())); 
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

		return newOne;
	}

	private SingleDocumentModel createNewDocument(Path path) {
		SingleDocumentModel prevFocused = focused;
		focused = new DefaultSingleDocumentModel(path, new JTextArea());
		listOfDocuments.add(focused);

		addTab(path == null ? "(unnamed)" : path.getFileName().toString(), new JScrollPane(focused.getTextComponent()));
		setIconAt(listOfDocuments.indexOf(focused), getIconNamed("saveGreen.png"));
		
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
				return focused.isModified()?getIconNamed("saveGreen.png"): getIconNamed("saveGray.png");
			}
		});
		

		return focused;
	}

	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		for (var doc : listOfDocuments) {
			if (newPath.equals(doc.getFilePath()) && !doc.equals(focused)) {
				JOptionPane.showMessageDialog(this, "Nothing saved because specified file is already opened.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		
		model.setFilePath(newPath);
		try {
			Files.write(newPath, model.getTextComponent().getText().getBytes());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error while saving", "Error", JOptionPane.ERROR_MESSAGE);
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

	private ImageIcon getIconNamed(String name) {
		Objects.requireNonNull(name, "icon must have a name");

		try (InputStream is = this.getClass().getResourceAsStream(FIXED_PART_OF_PATH + name)) {
			byte[] bytes = is.readAllBytes();
			return new ImageIcon((new ImageIcon(bytes)).getImage().getScaledInstance(DEFAULT_ICON_SIZE,
					DEFAULT_ICON_SIZE, Image.SCALE_DEFAULT));
		} catch (IOException e) {
			throw new IllegalArgumentException("No image on provided path");
		}
	}

	// ------------NOTIFY METHODS--------------

	private void notifyDocumentChangedListeners(SingleDocumentModel prevFocused, SingleDocumentModel currFocused) {
		for (var listener : listeners) {
			listener.currentDocumentChanged(prevFocused, currFocused);
		}
	}

	private void notifyDocumentAddedListeners(SingleDocumentModel model) {
		for (var listener : listeners) {
			listener.documentAdded(model);
		}
	}

	private void notifyDocumentRemovedListeners(SingleDocumentModel model) {
		for (var listener : listeners) {
			listener.documentRemoved(model);
		}
	}

}
