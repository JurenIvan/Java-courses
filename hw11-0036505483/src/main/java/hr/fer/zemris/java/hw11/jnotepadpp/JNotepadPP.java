package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;

public class JNotepadPP extends JFrame {
	private static final long serialVersionUID = 1L;

	private static final String FIXED_PART_OF_PATH = ".\\icons\\";
	private static final int DEFAULT_ICON_SIZE = 50;

	private MultipleDocumentModel model;
	private JPanel infoLeft;
	private JPanel infoRight;
	private JLabel clock;
	private SimpleDateFormat formatter;

	
	
	public JNotepadPP() {
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(500, 500);
		initGUI();

		setMinimumSize(new Dimension(500, 500));
		pack();
		setLocationRelativeTo(null);

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

	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		DefaultMultipleDocumentModel dmdm = new DefaultMultipleDocumentModel();
		model = dmdm;
		cp.add(dmdm, BorderLayout.CENTER);
		
		createActions();
		createMenus();
		createToolbar();
		createListeners();
		createStatusbar();
			
		model.createNewDocument();
		updateClockAction.actionPerformed(null);

	}

	// ------------------------------------------------------
	
	private void createStatusbar() {
		JPanel statusBar = new JPanel();
		statusBar.setLayout(new GridLayout(1, 3));
		statusBar.setBorder(new TitledBorder(new LineBorder(Color.gray, 2),
		        "Carret info"));
		getContentPane().add(statusBar, BorderLayout.SOUTH);

		infoLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
		infoRight = new JPanel(new FlowLayout(FlowLayout.LEFT));
		clock = new JLabel();
		
		statusBar.add(infoLeft);
		statusBar.add(infoRight);
		statusBar.add(clock);
		
		infoLeft.setBorder(BorderFactory.createLineBorder(Color.gray,2));
		infoRight.setBorder(BorderFactory.createLineBorder(Color.gray,2));
		clock.setBorder(BorderFactory.createLineBorder(Color.gray,2));

	

		JLabel lenghtLable = new JLabel();
		JLabel lnLable = new JLabel();
		JLabel colLable = new JLabel(); 
		JLabel selLable = new JLabel();
	
		clock.setHorizontalAlignment(SwingConstants.RIGHT);

		infoLeft.add(lenghtLable);
		infoRight.add(lnLable);
		infoRight.add(colLable);
		infoRight.add(selLable);
	}

	private Insets calcThings() {
		JTextArea jta = model.getCurrentDocument().getTextComponent();
		int totalLen = jta.getText().length();
		Caret caret = model.getCurrentDocument().getTextComponent().getCaret();

		int len = Math.abs(caret.getDot() - caret.getMark());
		int lineOfcaret = -1;
		int col = -1;
		try {
			lineOfcaret = jta.getLineOfOffset(jta.getCaretPosition());
			col = jta.getCaretPosition() - jta.getLineStartOffset(lineOfcaret);
		} catch (BadLocationException ignorable) {
		}

		return new Insets(totalLen, lineOfcaret + 1, col, len);
	}

	private void refreshToolbar() { // obavezno fix
		Insets data = calcThings();

		((JLabel) infoLeft.getComponent(0)).setText("Len:" + data.top);

		((JLabel) infoRight.getComponent(0)).setText("Ln:" + data.left);
		((JLabel) infoRight.getComponent(1)).setText("Col:" + data.bottom);
		((JLabel) infoRight.getComponent(2)).setText("Sel:" + data.right);
	}

	// ------------------------------------------------------

	private void createListeners() {
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exitAplication.actionPerformed(null);
			}
		});

		model.addMultipleDocumentListener(new MultipleDocumentListener() {

			@Override
			public void documentRemoved(SingleDocumentModel model) {
				setSelectionDependant();
			}

			@Override
			public void documentAdded(SingleDocumentModel model) {
				model.getTextComponent().addCaretListener(e -> refreshToolbar()); 
				setSelectionDependant();
				
				model.getTextComponent().getCaret().addChangeListener((e) -> {
					setSelectionDependant();
					refreshToolbar();
				});
			
			}

			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				setTitleOfWindow();
				refreshToolbar();
				setSelectionDependant();
				currentModel.getTextComponent().addCaretListener(e -> refreshToolbar());
			}
		});

		

		Timer t = new Timer(1000, updateClockAction);
		formatter = new SimpleDateFormat("  yyyy/MM/dd HH:mm:ss  ");
		t.start();

	}

	private void setTitleOfWindow() {
		Path p = model.getCurrentDocument().getFilePath();
		if (p != null) {
			setTitle(p.toAbsolutePath().toString() + " - JNotepad++");
		} else {
			setTitle("(unnamed) - JNotepad++");
		}
	}

	private void createActions() {

		openNewDocument.putValue(Action.NAME, "New");
		openNewDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
		openNewDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
		openNewDocument.putValue(Action.SHORT_DESCRIPTION, "Opens empty document.");
		openNewDocument.setEnabled(true);

		openDocument.putValue(Action.NAME, "Open");
		openDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
		openDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
		openDocument.putValue(Action.SHORT_DESCRIPTION, "Open file from disk");

		saveAsDocument.putValue(Action.NAME, "Save as");
		saveAsDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt S"));
		saveAsDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
		saveAsDocument.putValue(Action.SHORT_DESCRIPTION, "Save file to disk as...");

		saveDocument.putValue(Action.NAME, "Save");
		saveDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
		saveDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
		saveDocument.putValue(Action.SHORT_DESCRIPTION, "Save file to disk");

		deleteSelectedPart.putValue(Action.NAME, "Delete selection");
		deleteSelectedPart.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("F2"));
		deleteSelectedPart.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);
		deleteSelectedPart.putValue(Action.SHORT_DESCRIPTION, "Open file from disk");
		deleteSelectedPart.setEnabled(false);

		exitAplication.putValue(Action.NAME, "Exit");
		exitAplication.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt Q"));
		exitAplication.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
		exitAplication.putValue(Action.SHORT_DESCRIPTION, "Terminates Application");

		closeTab.putValue(Action.NAME, "Close");
		closeTab.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Q"));
		closeTab.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Q);
		closeTab.putValue(Action.SHORT_DESCRIPTION, "Closes current document");

		copy.putValue(Action.NAME, "Copy");
		copy.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
		copy.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
		copy.putValue(Action.SHORT_DESCRIPTION, "copies selected part");
		copy.setEnabled(false);

		cut.putValue(Action.NAME, "Cut");
		cut.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
		cut.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
		cut.putValue(Action.SHORT_DESCRIPTION, "Cuts selected part");
		cut.setEnabled(false);

		paste.putValue(Action.NAME, "Paste");
		paste.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
		paste.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_P);
		paste.putValue(Action.SHORT_DESCRIPTION, "Pastes what is in clipboard");
		paste.setEnabled(true);

		statistics.putValue(Action.NAME, "Statistics");
		statistics.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt S"));
		statistics.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
		statistics.putValue(Action.SHORT_DESCRIPTION, "Displays statistics about characters");

		toLowerCase.putValue(Action.NAME, "To lower case");
		toLowerCase.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control L"));
		toLowerCase.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);
		toLowerCase.putValue(Action.SHORT_DESCRIPTION, "Selected text turns into lower case");
		toLowerCase.setEnabled(false);

		toUpperCase.putValue(Action.NAME, "To upper case");
		toUpperCase.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control U"));
		toUpperCase.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);
		toUpperCase.putValue(Action.SHORT_DESCRIPTION, "Selected text turns into upper case");
		toUpperCase.setEnabled(false);

		inverseCase.putValue(Action.NAME, "Inverse case");
		inverseCase.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control I"));
		inverseCase.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);
		inverseCase.putValue(Action.SHORT_DESCRIPTION, "Invertes case of selected text.");
		inverseCase.setEnabled(false);

		ascending.putValue(Action.NAME, "Ascending sort");
		ascending.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control 1"));
		ascending.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_1);
		ascending.putValue(Action.SHORT_DESCRIPTION, "Sorts selected lines in ascending order");
		ascending.setEnabled(false);

		descening.putValue(Action.NAME, "Descending sort");
		descening.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control 2"));
		descening.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_2);
		descening.putValue(Action.SHORT_DESCRIPTION, "Sorts selected lines in descending order");
		descening.setEnabled(false);
		


	}

	private void setSelectionDependant() {
		boolean hasSelection = model.getCurrentDocument().getTextComponent().getCaret().getDot() != model
				.getCurrentDocument().getTextComponent().getCaret().getMark();
		
		deleteSelectedPart.setEnabled(hasSelection);
		copy.setEnabled(hasSelection);
		cut.setEnabled(hasSelection);
		inverseCase.setEnabled(hasSelection);
		toUpperCase.setEnabled(hasSelection);
		toLowerCase.setEnabled(hasSelection);
		ascending.setEnabled(hasSelection);
		descening.setEnabled(hasSelection);
	}

	private void createMenus() {
		JMenuBar mb = new JMenuBar();
		setJMenuBar(mb);
		JMenu file = new JMenu("File");
		mb.add(file);
		JMenu edit = new JMenu("Edit");
		mb.add(edit);
		JMenu changeCase = new JMenu("Change case");
		mb.add(changeCase);
		JMenu sort = new JMenu("Sort");
		mb.add(sort);

		file.add(new JMenuItem(openNewDocument));
		file.addSeparator();
		file.add(new JMenuItem(openDocument));
		file.add(new JMenuItem(saveAsDocument));
		file.add(new JMenuItem(saveDocument));
		file.addSeparator();
		file.add(new JMenuItem(statistics));
		file.addSeparator();
		file.add(new JMenuItem(closeTab));
		file.add(new JMenuItem(exitAplication));

		edit.add(new JMenuItem(deleteSelectedPart));
		edit.addSeparator();
		edit.add(new JMenuItem(copy));
		edit.add(new JMenuItem(cut));
		edit.add(new JMenuItem(paste));

		changeCase.add(new JMenuItem(toLowerCase));
		changeCase.add(new JMenuItem(toUpperCase));
		changeCase.add(new JMenuItem(inverseCase));

		sort.add(new JMenuItem(ascending));
		sort.add(new JMenuItem(descening));

	}

	private void createToolbar() {
		JToolBar tb = new JToolBar();
		
		tb.add(makeButtonWithActionAndIcons(openNewDocument, getIconNamed("newFile.png"), null));
		tb.add(makeButtonWithActionAndIcons(closeTab, getIconNamed("closeFile.png"), null));
		tb.add(makeButtonWithActionAndIcons(openDocument, getIconNamed("openFile.png"), null));
		tb.add(makeButtonWithActionAndIcons(saveAsDocument, getIconNamed("save.png"), null));

		tb.add(makeButtonWithActionAndIcons(copy, getIconNamed("copy.png"), null));
		tb.add(makeButtonWithActionAndIcons(cut, getIconNamed("cut.png"), null));
		tb.add(makeButtonWithActionAndIcons(paste, getIconNamed("paste.png"), null));
		tb.add(makeButtonWithActionAndIcons(statistics, getIconNamed("statistics.png"), null));

		getContentPane().add(tb, BorderLayout.PAGE_START);
	}

	private JButton makeButtonWithActionAndIcons(Action action, ImageIcon enabledIcon, ImageIcon disabledIcon) {
		JButton button = new JButton(action);
		button.setIcon(enabledIcon);
		button.setDisabledIcon(disabledIcon != null ? disabledIcon : enabledIcon);
		return button;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JNotepadPP().setVisible(true));
	}

//	-----------------------------------------------------------------

	private final Action openDocument = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc = new JFileChooser();
			jfc.setDialogTitle("Open file");
			if (jfc.showOpenDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION)
				return;

			Path openedFilePath = jfc.getSelectedFile().toPath();
			if (!Files.isReadable(openedFilePath)) {
				JOptionPane.showMessageDialog(JNotepadPP.this, "No read permission to read file", "ERROR",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			String text = null;
			try {
				text = Files.readString(openedFilePath, Charset.forName("UTF-8"));
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(JNotepadPP.this, "Error while reading", "ERROR",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			SingleDocumentModel focused = model.loadDocument(openedFilePath);

			if (focused.isModified()) {
				JOptionPane.showMessageDialog(JNotepadPP.this,
						"File that you tried to open is already opened and has unsaved changes. Please save before rewriting",
						"Warning", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			model.getCurrentDocument().getTextComponent().setText(text);
		}
	};

//	 -----------------------------------------------------------------

	private final Action deleteSelectedPart = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {

			Document doc = model.getCurrentDocument().getTextComponent().getDocument();
			Caret caret = model.getCurrentDocument().getTextComponent().getCaret();

			try {
				doc.remove(Math.min(caret.getDot(), caret.getMark()), Math.abs(caret.getDot() - caret.getMark()));
			} catch (BadLocationException ignorable) {
			}
		}
	};

	// -----------------------------------------------------------------

	private final Action openNewDocument = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			model.createNewDocument();
		}
	};

	// -----------------------------------------------------------------

	private final Action saveDocument = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {

			if (model.getCurrentDocument().getFilePath() == null) {
				saveAsDocument.actionPerformed(e);
			} else {
				model.saveDocument(model.getCurrentDocument(), model.getCurrentDocument().getFilePath());
			}
		}
	};

	// -----------------------------------------------------------------

	private final Action saveAsDocument = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc = new MyJFileChooser();
			jfc.setDialogTitle("Save file");
			if (jfc.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(JNotepadPP.this, "Nothing saved", "Warning",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			model.saveDocument(model.getCurrentDocument(), jfc.getSelectedFile().toPath());

		}
	};

	// -----------------------------------------------------------------

	private final Action closeTab = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFrame frame = new JFrame();
			String[] options = { "Save", "Save as", "No", "Cancel" };
			int result = JOptionPane.showOptionDialog(frame.getContentPane(),
					"Document is modified, save before close?", "Warning", JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
			switch (result) {
			case 0:
				saveDocument.actionPerformed(e);
				model.closeDocument(model.getCurrentDocument());
				break;
			case 1:
				saveAsDocument.actionPerformed(e);
				model.closeDocument(model.getCurrentDocument());
				break;
			case 2:
				model.closeDocument(model.getCurrentDocument());
				break;

			default:
				break;
			}
		}
	};

	// -----------------------------------------------------------------

	private final Action exitAplication = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (isAllSaved(e)) {
				dispose();
			}
		}

		private boolean isAllSaved(ActionEvent e) {
			SingleDocumentModel sdm;
			for (int i = 0; i < model.getNumberOfDocuments(); i++) {
				sdm = model.getDocument(i);
				if (sdm.isModified() == true) {
					int result = getWantedOperation(sdm);
					switch (result) {
					case 0:
						JFileChooser jfc = new MyJFileChooser();
						jfc.setDialogTitle("Save file");
						if (jfc.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
							JOptionPane.showMessageDialog(JNotepadPP.this, "Exit Canceled", "Warning",
									JOptionPane.INFORMATION_MESSAGE);
							return false;
						}
						model.saveDocument(sdm, jfc.getSelectedFile().toPath());
						break;
					case 1:
						break;
					default:
						return false;
					}
				}
			}
			return true;
		}

		private int getWantedOperation(SingleDocumentModel sdm) {
			return JOptionPane.showOptionDialog(null,
					"Document is modified, save before close?\n Documents starts with:" + sdm.getTextComponent()
							.getText().substring(0, Math.min(20, sdm.getTextComponent().getText().length())),
					"Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
					new String[] { "Save", "No", "Cancel exit" }, null);
		}
	};

	// -----------------------------------------------------------------

	private final Action copy = new DefaultEditorKit.CopyAction();
//			new AbstractAction() {
//		private static final long serialVersionUID = 1L;
//
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			Document doc = model.getCurrentDocument().getTextComponent().getDocument();
//
//			Caret caret = model.getCurrentDocument().getTextComponent().getCaret();
//			int start = Math.min(caret.getDot(), caret.getMark());
//			int len = Math.abs(caret.getDot() - caret.getMark());
//
//			if (len == 0) {
//				clipBoard = "";
//				return;
//			}
//
//			try {
//				clipBoard = doc.getText(start, len);
//			} catch (BadLocationException ignorable) {
//			}
//		}
//	};

	// -----------------------------------------------------------------

	private final Action cut = new DefaultEditorKit.CutAction();
//			new AbstractAction() {
//		private static final long serialVersionUID = 1L;
//
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			Document doc = model.getCurrentDocument().getTextComponent().getDocument();
//
//			Caret caret = model.getCurrentDocument().getTextComponent().getCaret();
//			int start = Math.min(caret.getDot(), caret.getMark());
//			int len = Math.abs(caret.getDot() - caret.getMark());
//
//			if (len == 0) {
//				clipBoard = "";
//				return;
//			}
//
//			try {
//				clipBoard = doc.getText(start, len);
//				StringBuilder sb = new StringBuilder();
//				sb.append(doc.getText(0, start));
//				sb.append(doc.getText(start + len, doc.getLength() - 1));
//				model.getCurrentDocument().getTextComponent().setText(sb.toString());
//			} catch (BadLocationException ignorable) {
//			}
//			
//		}
//	};

	// -----------------------------------------------------------------

	private final Action paste = new DefaultEditorKit.PasteAction();
//			new AbstractAction() {
//		private static final long serialVersionUID = 1L;
//
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			Document doc = model.getCurrentDocument().getTextComponent().getDocument();
//			int start = model.getCurrentDocument().getTextComponent().getCaret().getMark();
//
//			if (clipBoard == null || clipBoard.length() == 0)
//				return;
//
//			try {
//				doc.insertString(start, clipBoard, null);
//			} catch (BadLocationException ignorable) {
//			}
//
//		}
//	};

	// -----------------------------------------------------------------

	private final Action statistics = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			String text = model.getCurrentDocument().getTextComponent().getText();
			int numOfChars = text.length();
			int numOfNonBlancks = numOfChars - count(text, Character::isWhitespace);
			int numOfLines = count(text, (a) -> a == '\n');

			StringBuilder sb = new StringBuilder();
			sb.append("Number of characters:");
			sb.append(String.valueOf(numOfChars));
			sb.append("\nNumber of non-WhiteSpaces:");
			sb.append(String.valueOf(numOfNonBlancks));
			sb.append("\nNumber of lines:");
			sb.append(String.valueOf(numOfLines + 1));

			JOptionPane.showMessageDialog(null, sb.toString());
		}

		private int count(String text, Predicate<Character> test) {
			char[] chars = text.toCharArray();
			int counter = 0;
			for (int i = 0; i < text.length(); i++) {
				if (test.test(chars[i])) {
					counter++;
				}
			}
			return counter;
		}
	};

	// -----------------------------------------------

	private final Action toUpperCase = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			applyFunctionOnDoc(Character::toUpperCase);
		}
	};

	private final Action toLowerCase = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			applyFunctionOnDoc(Character::toLowerCase);
		}
	};

	private final Action inverseCase = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			applyFunctionOnDoc((a) -> {
				if (Character.isLowerCase(a))
					return Character.toUpperCase(a);
				else if (Character.isUpperCase(a))
					return Character.toLowerCase(a);
				else
					return a;
			});
		}
	};

	private void applyFunctionOnDoc(Function<Character, Character> func) {
		Document doc = model.getCurrentDocument().getTextComponent().getDocument();
		Caret caret = model.getCurrentDocument().getTextComponent().getCaret();
		
		int start = Math.min(caret.getDot(), caret.getMark());
		int len = Math.abs(caret.getDot() - caret.getMark());
		if (len == 0)
			return;
		try {
			String text = manipulateText(doc.getText(start, len), func);
			doc.remove(start, len);
			doc.insertString(start, text, null);
		} catch (BadLocationException ignorable) {
		}

	}

	private String manipulateText(String text, Function<Character, Character> func) {
		StringBuilder sb = new StringBuilder();
		char[] array = text.toCharArray();
		for (int i = 0; i < text.length(); i++) {
			if (Character.isLetter(array[i])) {
				sb.append(func.apply(array[i]));
			} else
				sb.append(array[i]);
		}
		return sb.toString();
	}

	// -----------------------------------------------------------------

	private final Action ascending = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			DoSortingMagic((a, b) -> a.compareTo(b));
		}
	};

	private final Action descening = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			DoSortingMagic((a, b) -> b.compareTo(a));
		}
	};

	private void DoSortingMagic(Comparator<String> comp) {
		// Locale hrLocale = new Locale("hr");
		// Collator hrCollator = Collator.getInstance(hrLocale);
		// int r = hrCollator.compare("Češnjak", "Dinja"); // result is less than zero

		List<String> lines = model.getCurrentDocument().getTextComponent().getText().lines()
				.collect(Collectors.toList());
		int startingLine = getNumberOfLine(lines, (a, b) -> Math.min(a, b));
		int endingLine = getNumberOfLine(lines, (a, b) -> Math.max(a, b));
		List<String> onesToSort = new ArrayList<>(lines.subList(startingLine, endingLine));
		List<String> finalOnes = new ArrayList<>(lines.subList(0, startingLine));
		onesToSort.sort(comp);
		finalOnes.addAll(onesToSort);
		finalOnes.addAll(lines.subList(endingLine, lines.size()));

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < finalOnes.size(); i++) {
			sb.append(finalOnes.get(i) + '\n');
		}
		model.getCurrentDocument().getTextComponent().setText(sb.toString());
	}

	private int getNumberOfLine(List<String> lines, BiFunction<Integer, Integer, Integer> bifunc) {
		Caret caret = model.getCurrentDocument().getTextComponent().getCaret();
		int number = bifunc.apply(caret.getDot(), caret.getMark());
		int lineNumber = 0;
		
		while (number >= 0 && lines.size() > lineNumber) {
			number = number - lines.get(lineNumber++).length();
		}
		return number > 0 ? lineNumber : lineNumber - 1;
	}

	// -----------------------------------------------------------------

	ActionListener updateClockAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			clock.setText(getApropriateTime());
		}

		private String getApropriateTime() {
			Date date = new Date(System.currentTimeMillis());
			return formatter.format(date);
		}
	};

// -----------------------------------------------------------------

	private static class MyJFileChooser extends JFileChooser {
		private static final long serialVersionUID = 1L;

		@Override
		public void approveSelection() {
			Path path = getSelectedFile().toPath();
			if (Files.exists(path) && getDialogType() == SAVE_DIALOG) {
				int result = JOptionPane.showConfirmDialog(this, "The file exists, overwrite?", "Existing file",
						JOptionPane.YES_NO_OPTION);
				switch (result) {
				case JOptionPane.YES_OPTION:
					super.approveSelection();
					return;
				default:
					return;
				}
			}
			super.approveSelection();
		}
	}
}
