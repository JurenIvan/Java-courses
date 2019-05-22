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
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
import javax.swing.text.Document;

import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableJLable;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableJMenu;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableJOptionPane;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

/**
 * Main Class of application containing main method used for running JNotepadPP.
 * 
 * JNotepadPP is text editor with support for multiple documents and plethora of
 * possible tools and actions.
 * 
 * i18n implemented for Croatian, German and English.
 * 
 * @author juren
 *
 */
public class JNotepadPP extends JFrame {
	private static final long serialVersionUID = 1L;

	/**
	 * Constant that holds string representation of path to icons needed
	 */
	private static final String FIXED_PART_OF_PATH = ".\\icons\\";
	/**
	 * Constant for displaying size of Icons in tools.
	 */
	private static final int DEFAULT_ICON_SIZE = 50;
	/**
	 * Constant for number of displaying chars in dialog box while exiting .
	 */
	private static final int HOW_MUCH_CHARS_TO_SHOW = 15;
	/**
	 * Constant for minimum size of Window .
	 */
	private static final int MINIMUM_WINDOW_SIZE = 500;
	/**
	 * Constant for default language.
	 */
	private static final String DEFAULT_LANGUAGE = "en";

	/**
	 * Variable containing reference to model of {@link MultipleDocumentModel} over
	 * which all of the operation are performed
	 */
	private MultipleDocumentModel model;
	/**
	 * {@link JPanel} that is left part of status bar
	 */
	private JPanel infoLeft;
	/**
	 * {@link JPanel} that is right of status bar
	 */
	private JPanel infoRight;
	/**
	 * {@link JLabel} that is part of status bar and contains current date and time
	 */
	private JLabel clock;
	/**
	 * Formatter used for displaying time
	 */
	private SimpleDateFormat formatter;
	/**
	 * private variable representing {@link HashMap} of icons used because we dont
	 * want to he excessive on IO operations. So its better to cache images into
	 * ram.
	 */
	private HashMap<String, ImageIcon> loadedImages;
	/**
	 * {@link FormLocalizationProvider} used for implementation of i18n
	 */
	private FormLocalizationProvider flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
	/**
	 * variable containing current language of application
	 */
	private String language = DEFAULT_LANGUAGE;

	{
		LocalizationProvider.getInstance().setLanguage(DEFAULT_LANGUAGE);
	}

	/**
	 * Standard constructor. Used to initialize GUI, actions and Listeners.
	 */
	public JNotepadPP() {

		loadedImages = new HashMap<>();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		initGUI();
		setMinimumSize(new Dimension(MINIMUM_WINDOW_SIZE, MINIMUM_WINDOW_SIZE));
		pack();
		setLocationRelativeTo(null);
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

	/**
	 * Method used to clear up and format code. Derived from constructor. Sets up
	 * main part of gui.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		DefaultMultipleDocumentModel dmdm = new DefaultMultipleDocumentModel();
		model = dmdm;
		JPanel jp = new JPanel();
		jp.setLayout(new BorderLayout());
		jp.add(dmdm, BorderLayout.CENTER);
		cp.add(jp, BorderLayout.CENTER);

		createActions();
		createMenus();
		createToolbar(cp);
		createListeners();
		createStatusbar(jp);

		model.createNewDocument();
		updateClockAction.actionPerformed(null);

	}

	// ------------------------------------------------------

	/**
	 * Method used to create graphical elements of statusBar which is placed at the
	 * bottom of screen. Adds elements to provided {@link Container}
	 * 
	 * @param cp {@link Container} where status bar elements are added
	 */
	private void createStatusbar(Container cp) {
		JPanel statusBar = new JPanel();
		statusBar.setLayout(new GridLayout(1, 3));
		statusBar.setBorder(new TitledBorder(new LineBorder(Color.gray, 2)));
		cp.add(statusBar, BorderLayout.SOUTH);

		infoLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
		infoRight = new JPanel(new FlowLayout(FlowLayout.LEFT));
		clock = new JLabel();

		statusBar.add(infoLeft);
		statusBar.add(infoRight);
		statusBar.add(clock);

		infoLeft.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
		infoRight.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
		clock.setBorder(BorderFactory.createLineBorder(Color.gray, 2));

		LocalizableJLable lenghtLable = new LocalizableJLable("len", flp);
		LocalizableJLable lnLable = new LocalizableJLable("lin", flp);
		LocalizableJLable colLable = new LocalizableJLable("col", flp);
		LocalizableJLable selLable = new LocalizableJLable("sel", flp);

		clock.setHorizontalAlignment(SwingConstants.RIGHT);

		infoLeft.add(lenghtLable);
		infoRight.add(lnLable);
		infoRight.add(colLable);
		infoRight.add(selLable);
	}

	/**
	 * Method used to calculate data related to current {@link Caret} position in
	 * current {@link SingleDocumentModel
	 * 
	 * @return Insets instance where instance.top represents totalLenghtOfDocument,
	 *         instance.left current line of caret, instance.bottom current column
	 *         and instance.right represents cur distance form begining of document
	 */
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

	/**
	 * Method used to refresh data shown in center and left part of Status bar.
	 * Activated br curret change or document change.
	 */
	private void refreshStatusbar() {
		Insets data = calcThings();
		((LocalizableJLable) infoLeft.getComponent(0)).setExtraText(String.valueOf(data.top));
		((LocalizableJLable) infoRight.getComponent(0)).setExtraText(String.valueOf(data.left));
		((LocalizableJLable) infoRight.getComponent(1)).setExtraText(String.valueOf(data.bottom));
		((LocalizableJLable) infoRight.getComponent(2)).setExtraText(String.valueOf(data.right));
	}

	// ------------------------------------------------------

	/**
	 * Method used to clear up and format code. Derived from constructor. Sets up
	 * most of the listeners needed for gui to work. Including Timer used for time.
	 */
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
				model.getTextComponent().addCaretListener(e -> {
					refreshStatusbar();
					saveDocument.setEnabled(model.isModified());
				});

				setSelectionDependant();

				model.getTextComponent().getCaret().addChangeListener((e) -> {
					setSelectionDependant();
					refreshStatusbar();
				});
			}

			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				setTitleOfWindow();
				refreshStatusbar();
				setSelectionDependant();
				currentModel.getTextComponent().addCaretListener(e -> refreshStatusbar());
				saveDocument.setEnabled(model.getCurrentDocument().isModified());
			}
		});

		Timer t = new Timer(1000, updateClockAction);
		formatter = new SimpleDateFormat("  yyyy/MM/dd HH:mm:ss  ");
		t.start();

	}

	/**
	 * Method that sets title of window. title is path to
	 * {@link SingleDocumentModel} or just (unnamed) - JNotepad++
	 */
	private void setTitleOfWindow() {
		Path p = model.getCurrentDocument().getFilePath();
		if (p != null) {
			setTitle(p.toAbsolutePath().toString() + " - JNotepad++");
		} else {
			setTitle("(unnamed) - JNotepad++");
		}
	}

	/**
	 * Method used to clear up and format code. Derived from constructor. Sets up
	 * all actions that are supported. Mnemonics and acceletor keys are hardCoded
	 * and equal for each language. Supported Actions are {@link #openNewDocument},
	 * {@link #openDocument}, {@link #saveAsDocument}, {@link #saveDocument},
	 * {@link #deleteSelectedPart}, {@link #exitAplication}, {@link #closeTab},
	 * {@link #copy}, {@link #cut}, {@link #paste}, {@link #statistics},
	 * {@link #toLowerCase}, {@link #toUpperCase}, {@link #inverseCase},
	 * {@link #ascending}, {@link #descening} and {@link #unique}
	 * 
	 */
	private void createActions() {
		openNewDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
		openNewDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
		openNewDocument.setEnabled(true);

		openDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
		openDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);

		saveAsDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt S"));
		saveAsDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);

		saveDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
		saveDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);

		deleteSelectedPart.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("F2"));
		deleteSelectedPart.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);
		deleteSelectedPart.setEnabled(false);

		exitAplication.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt Q"));
		exitAplication.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);

		closeTab.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Q"));
		closeTab.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Q);

		copy.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
		copy.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
		copy.setEnabled(false);

		cut.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
		cut.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
		cut.setEnabled(false);

		paste.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
		paste.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_P);
		paste.setEnabled(true);

		statistics.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt S"));
		statistics.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);

		toLowerCase.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control L"));
		toLowerCase.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);
		toLowerCase.setEnabled(false);

		toUpperCase.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control U"));
		toUpperCase.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);
		toUpperCase.setEnabled(false);

		inverseCase.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control I"));
		inverseCase.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);
		inverseCase.setEnabled(false);

		ascending.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control 1"));
		ascending.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_1);
		ascending.setEnabled(false);

		descening.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control 2"));
		descening.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_2);
		descening.setEnabled(false);

		unique.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt U"));
		unique.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);
		unique.setEnabled(false);
	}

	/**
	 * Method used to clear up and format code. Derived from
	 * {@link #createActions()}. Used to grop all methods that needs to be called
	 * and whose action should be perfomed when {@link Caret} is updated because if
	 * selection is set, all of actions listed should be enabled
	 * {@link JNotepadPP#deleteSelectedPart} {@link JNotepadPP#copy},
	 * {@link JNotepadPP#cut}, {@link JNotepadPP#inverseCase},
	 * {@link JNotepadPP#toUpperCase}, {@link JNotepadPP#toLowerCase},
	 * {@link JNotepadPP#ascending}, {@link JNotepadPP#descening} and
	 * {@link JNotepadPP#unique}
	 * 
	 */
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
		unique.setEnabled(hasSelection);
	}

	/**
	 * Method used to clear up and format code. Derived from constructor. Used to
	 * create Menus at the top of the GUI. Creates 5 {@link JMenu}s which are used
	 * to access tools implemented. Menus are File, Edit, Change Case, Sort and
	 * language. Language Menu has list of implemented languages and press on any of
	 * them resets gui of application.
	 *
	 */
	private void createMenus() {
		JMenuBar mb = new JMenuBar();
		setJMenuBar(mb);
		JMenu file = new LocalizableJMenu("file", flp);
		mb.add(file);
		JMenu edit = new LocalizableJMenu("edit", flp);
		mb.add(edit);
		JMenu changeCase = new LocalizableJMenu("changecase", flp);
		mb.add(changeCase);
		JMenu sort = new LocalizableJMenu("sort", flp);
		mb.add(sort);
		JMenu lang = new LocalizableJMenu("language", flp);
		mb.add(lang);

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
		sort.add(new JMenuItem(unique));

		JMenuItem jmiEN = new JMenuItem("en");
		JMenuItem jmiHR = new JMenuItem("hr");
		JMenuItem jmiDE = new JMenuItem("de");

		jmiDE.addActionListener((e) -> {
			language = "de";
			LocalizationProvider.getInstance().setLanguage(language);
			this.pack();
		});
		jmiEN.addActionListener((e) -> {
			language = "en";
			LocalizationProvider.getInstance().setLanguage(language);
			this.pack();
		});
		jmiHR.addActionListener((e) -> {
			language = "hr";
			LocalizationProvider.getInstance().setLanguage(language);
			this.pack();
		});

		lang.add(jmiEN);
		lang.add(jmiHR);
		lang.add(jmiDE);
	}

	/**
	 * 
	 * Method used to clear up and format code. Derived from constructor. Used to
	 * create Toolbar and after it is created it is put into provided
	 * {@link Container}
	 * 
	 * @param cp {@link Container} where toolbar is placed
	 */
	private void createToolbar(Container cp) {
		JToolBar tb = new JToolBar();
		// I wasn't sure whether we had to add icons here or not, but i had methods so
		// it wasnt't a hasle.
		tb.add(makeButtonWithActionAndIcons(openNewDocument, getIconNamed("newFile.png"), null));
		tb.add(makeButtonWithActionAndIcons(closeTab, getIconNamed("closeFile.png"), null));
		tb.add(makeButtonWithActionAndIcons(openDocument, getIconNamed("openFile.png"), null));
		tb.add(makeButtonWithActionAndIcons(saveAsDocument, getIconNamed("save.png"), null));

		tb.add(makeButtonWithActionAndIcons(copy, getIconNamed("copy.png"), null));
		tb.add(makeButtonWithActionAndIcons(cut, getIconNamed("cut.png"), null));
		tb.add(makeButtonWithActionAndIcons(paste, getIconNamed("paste.png"), null));
		tb.add(makeButtonWithActionAndIcons(statistics, getIconNamed("statistics.png"), null));

		cp.add(tb, BorderLayout.PAGE_START);
	}

	/**
	 * Method used to create custom JButton with linked action, icon for enabled
	 * state, and icon for disabled state.
	 * 
	 * @param action       performed when button is clicked
	 * @param enabledIcon  icon for enabled state
	 * @param disabledIcon icon for disabled state
	 * @return configured button
	 */
	private JButton makeButtonWithActionAndIcons(Action action, ImageIcon enabledIcon, ImageIcon disabledIcon) {
		JButton button = new JButton(action);
		button.setIcon(enabledIcon);
		button.setDisabledIcon(disabledIcon != null ? disabledIcon : enabledIcon);
		return button;
	}

	/**
	 * Main method used to start the app.
	 * 
	 * @param args not used.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JNotepadPP().setVisible(true));
	}

//	-----------------------------------------------------------------

	/**
	 * private variable holding implementation of {@link LocalizableAction} derived
	 * from {@link Action} able to change text of appropriate
	 * {@link java.awt.Component}s when language of app changes.
	 * 
	 * Used for opening of a Document.
	 */
	private final LocalizableAction openDocument = new LocalizableAction("openDocument", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc = new JFileChooser();
			jfc.setDialogTitle("Open file");
			if (jfc.showOpenDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION)
				return;

			Path openedFilePath = jfc.getSelectedFile().toPath();
			if (!Files.isReadable(openedFilePath)) {
				LocalizableJOptionPane.showMessageDialog(JNotepadPP.this, "noReadPersmision", "error",
						JOptionPane.ERROR_MESSAGE, flp);
				return;
			}

			model.loadDocument(openedFilePath).setModified(false);
		}
	};

//	 -----------------------------------------------------------------

	/**
	 * private variable holding implementation of {@link LocalizableAction} derived
	 * from {@link Action} able to change text of appropriate
	 * {@link java.awt.Component}s when language of app changes.
	 * 
	 * Used for deleting a selected part of text.
	 */
	private final LocalizableAction deleteSelectedPart = new LocalizableAction("deleteSelectedPart", flp) {
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

	/**
	 * private variable holding implementation of {@link LocalizableAction} derived
	 * from {@link Action} able to change text of appropriate
	 * {@link java.awt.Component}s when language of app changes.
	 * 
	 * Used for opening a brand new document with no changes made.
	 */
	private final LocalizableAction openNewDocument = new LocalizableAction("open", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			model.createNewDocument();
		}
	};

	// -----------------------------------------------------------------

	/**
	 * private variable holding implementation of {@link LocalizableAction} derived
	 * from {@link Action} able to change text of appropriate
	 * {@link java.awt.Component}s when language of app changes.
	 * 
	 * Used for saving current document without asking if path is known.
	 */
	private final LocalizableAction saveDocument = new LocalizableAction("saveDocument", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (model.getCurrentDocument().getFilePath() == null) {
				saveAsDocument.actionPerformed(e);
			} else {
				try {
					model.saveDocument(model.getCurrentDocument(), model.getCurrentDocument().getFilePath());
				} catch (IllegalStateException e2) {
					LocalizableJOptionPane.showMessageDialog(getContentPane(), "nothingSaved", "error",
							JOptionPane.ERROR_MESSAGE, flp);
					return;
				}
			}
		}
	};

	// -----------------------------------------------------------------

	/**
	 * private variable holding implementation of {@link LocalizableAction} derived
	 * from {@link Action} able to change text of appropriate
	 * {@link java.awt.Component}s when language of app changes.
	 * 
	 * Used for saving current document always asking for new path.
	 */
	private final LocalizableAction saveAsDocument = new LocalizableAction("saveAsDocument", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc = new MyJFileChooser(flp);
			jfc.setDialogTitle("Save file");
			if (jfc.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				LocalizableJOptionPane.showMessageDialog(JNotepadPP.this, "nothingsaved", "warning",
						JOptionPane.INFORMATION_MESSAGE, flp);
				return;
			}
			try {
				model.saveDocument(model.getCurrentDocument(), jfc.getSelectedFile().toPath());
			} catch (IllegalStateException e2) {
				LocalizableJOptionPane.showMessageDialog(getContentPane(), "nothingSaved", "error",
						JOptionPane.ERROR_MESSAGE, flp);
				return;
			}
		}
	};

	// -----------------------------------------------------------------

	/**
	 * private variable holding implementation of {@link LocalizableAction} derived
	 * from {@link Action} able to change text of appropriate
	 * {@link java.awt.Component}s when language of app changes.
	 * 
	 * Used for closing a current tab.If {@link SingleDocumentModel} is modified,
	 * user is asked to save.
	 */
	private final LocalizableAction closeTab = new LocalizableAction("closeTab", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFrame frame = new JFrame();
			String[] options = { "save", "saveAsDocument", "no", "cancel" };
			if (!model.getCurrentDocument().isModified()) {
				model.closeDocument(model.getCurrentDocument());
				return;
			}
			int result = LocalizableJOptionPane.showOptionDialog(frame.getContentPane(), "docModified", "warning",
					JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null, flp);
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

	/**
	 * private variable holding implementation of {@link LocalizableAction} derived
	 * from {@link Action} able to change text of appropriate
	 * {@link java.awt.Component}s when language of app changes.
	 * 
	 * Used for exiting out of application. If modified {@link SingleDocumentModel}
	 * exist, user is asked to save them.
	 */
	private final LocalizableAction exitAplication = new LocalizableAction("exitAplication", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (isAllSaved(e)) {
				dispose();
			}
		}

		/**
		 * method that iterates though all {@link SingleDocumentModel} stored in
		 * {@link MultipleDocumentModel} and looks if they are modified or not. if they
		 * aren't, user is asked whether he wants to save or not. If he cancels Exit
		 * nothing happens(except files getting saved)
		 * 
		 * @param e not used
		 * @return boolean dependent of conditions described above
		 */
		private boolean isAllSaved(ActionEvent e) {
			SingleDocumentModel sdm;
			for (int i = 0; i < model.getNumberOfDocuments(); i++) {
				sdm = model.getDocument(i);
				model.setCurrentDocument(sdm);
				if (sdm.isModified() == false)
					continue;
				if (sdm.getFilePath() == null && sdm.getTextComponent().getText().isEmpty())
					continue;
				int result = getWantedOperation(sdm);
				switch (result) {
				case 0:
					JFileChooser jfc = new MyJFileChooser(flp);
					jfc.setDialogTitle("Save file");
					if (jfc.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
						LocalizableJOptionPane.showMessageDialog(JNotepadPP.this, "exitcanceled", "warning",
								JOptionPane.INFORMATION_MESSAGE, flp);
						return false;
					}
					try {
						model.saveDocument(sdm, jfc.getSelectedFile().toPath());
					} catch (IllegalStateException e2) {
						LocalizableJOptionPane.showMessageDialog(getContentPane(), "nothingSaved", "error",
								JOptionPane.ERROR_MESSAGE, flp);
						return false;
					}
					break;
				case 1:
					break;
				default:
					return false;
				}
			}

			return true;
		}

		/**
		 * Method used as a part of saving document.
		 * 
		 * @param sdm model that needs to be saved
		 * @return 0,1,2 representing user response. 0->save document, 1->do not save
		 *         2->cancel exiting
		 */
		private int getWantedOperation(SingleDocumentModel sdm) {
			return LocalizableJOptionPane.showOptionDialog(null, "docMofifiedInfo", "warning",
					JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
					new String[] { "save", "no", "cancelE" }, null, flp, sdm.getTextComponent().getText().substring(0,
							Math.min(HOW_MUCH_CHARS_TO_SHOW, sdm.getTextComponent().getText().length())));
		}
	};

	// -----------------------------------------------------------------

	/**
	 * private variable holding implementation of {@link LocalizableAction} derived
	 * from {@link Action} able to change text of appropriate
	 * {@link java.awt.Component}s when language of app changes.
	 * 
	 * Used for putting selected part of text into clipboard.
	 */
	private final LocalizableAction copy = new LocalizableAction("copy", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			model.getCurrentDocument().getTextComponent().copy();
		}
	};

	// -----------------------------------------------------------------

	/**
	 * private variable holding implementation of {@link LocalizableAction} derived
	 * from {@link Action} able to change text of appropriate
	 * {@link java.awt.Component}s when language of app changes.
	 * 
	 * Used for putting selected part of text into clipboard and deleting it from
	 * {@link JTextArea}
	 */
	private final LocalizableAction cut = new LocalizableAction("cut", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			model.getCurrentDocument().getTextComponent().cut();
		}
	};
	// -----------------------------------------------------------------

	/**
	 * private variable holding implementation of {@link LocalizableAction} derived
	 * from {@link Action} able to change text of appropriate
	 * {@link java.awt.Component}s when language of app changes.
	 * 
	 * Used for putting selected part of text from clipboard to {@link JTextArea}
	 */
	private final LocalizableAction paste = new LocalizableAction("paste", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			model.getCurrentDocument().getTextComponent().paste();
		}
	};

	// -----------------------------------------------------------------

	/**
	 * private variable holding implementation of {@link LocalizableAction} derived
	 * from {@link Action} able to change text of appropriate
	 * {@link java.awt.Component}s when language of app changes.
	 * 
	 * Used for outputting data for current document for number of characters,number
	 * of nonWhitespace characters number of whitespace, and number of lines.
	 */
	private final LocalizableAction statistics = new LocalizableAction("statistics", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			String text = model.getCurrentDocument().getTextComponent().getText();
			int numOfChars = text.length();
			int numOfNonBlancks = numOfChars - count(text, Character::isWhitespace);
			int numOfLines = count(text, (a) -> a == '\n');

			StringBuilder sb = new StringBuilder();
			sb.append(flp.getString("statNOC") + ':');
			sb.append(String.valueOf(numOfChars));
			sb.append('\n' + flp.getString("statNONWS") + ':');
			sb.append(String.valueOf(numOfNonBlancks));
			sb.append('\n' + flp.getString("statNOL") + ':');
			sb.append(String.valueOf(numOfLines + 1));

			JOptionPane.showMessageDialog(null, sb.toString());
		}

		/**
		 * Method that goes through whole text and count how much times was test
		 * positive for that particular character
		 * 
		 * @param text subject of text
		 * @param test {@link Predicate}
		 * @return number of positive results
		 */
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

	/**
	 * private variable holding implementation of {@link LocalizableAction} derived
	 * from {@link Action} able to change text of appropriate
	 * {@link java.awt.Component}s when language of app changes.
	 * 
	 * Used for setting selected part of text to UPPERCASE
	 */
	private final LocalizableAction toUpperCase = new LocalizableAction("toUpperCase", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			applyFunctionOnDoc(Character::toUpperCase);
		}
	};

	/**
	 * private variable holding implementation of {@link LocalizableAction} derived
	 * from {@link Action} able to change text of appropriate
	 * {@link java.awt.Component}s when language of app changes.
	 * 
	 * Used for setting selected part of text to LOWERCASE
	 */
	private final LocalizableAction toLowerCase = new LocalizableAction("toLowerCase", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			applyFunctionOnDoc(Character::toLowerCase);
		}
	};

	/**
	 * private variable holding implementation of {@link LocalizableAction} derived
	 * from {@link Action} able to change text of appropriate
	 * {@link java.awt.Component}s when language of app changes.
	 * 
	 * Used for setting selected part of text to UPPERCASE if chars were lowercase
	 * and vice versa
	 */
	private final LocalizableAction inverseCase = new LocalizableAction("inverseCase", flp) {
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

	/**
	 * Method that applies provided {@link Function} over current document
	 * 
	 * @param func that is applied over current document
	 */
	private void applyFunctionOnDoc(Function<Character, Character> func) {
		Objects.requireNonNull(func, "Cannot apply null to chars");
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

	/**
	 * Method that applies provided function over provided text
	 * 
	 * @param text over which {@link Function} is applied
	 * @param func that is applied
	 * @return new String
	 */
	private String manipulateText(String text, Function<Character, Character> func) {
		Objects.requireNonNull(text, "Cannot apply function over nothing");
		Objects.requireNonNull(func, "Cannot apply null to chars");
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

	/**
	 * private variable holding implementation of {@link LocalizableAction} derived
	 * from {@link Action} able to change text of appropriate
	 * {@link java.awt.Component}s when language of app changes.
	 * 
	 * Used for asending sorting of selected lines.
	 */
	private final LocalizableAction ascending = new LocalizableAction("ascending", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Collator collator = Collator.getInstance(new Locale(language));
			DoSortingMagic(collator::compare);
		}
	};

	/**
	 * private variable holding implementation of {@link LocalizableAction} derived
	 * from {@link Action} able to change text of appropriate
	 * {@link java.awt.Component}s when language of app changes.
	 * 
	 * Used for descending sorting of selected lines.
	 */
	private final LocalizableAction descening = new LocalizableAction("descening", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Collator collator = Collator.getInstance(new Locale(language));
			DoSortingMagic((a, b) -> collator.compare(b, a));
		}
	};

	/**
	 * Method that takes comparator sorts selected lines
	 * 
	 * @param comp {@link Comparator} used for sorting
	 */
	private void DoSortingMagic(Comparator<String> comp) {

		List<String> lines = model.getCurrentDocument().getTextComponent().getText().lines()
				.collect(Collectors.toList());
		int startingLine = getNumberOfLine(lines, (a, b) -> Math.min(a, b));
		int endingLine = getNumberOfLine(lines, (a, b) -> Math.max(a, b));
		if (endingLine == lines.size()) {
			endingLine = lines.size() - 1;
		}
		List<String> onesToSort = new ArrayList<>(lines.subList(startingLine, endingLine + 1));
		List<String> finalOnes = new ArrayList<>(lines.subList(0, startingLine));
		onesToSort.sort(comp);
		finalOnes.addAll(onesToSort);
		finalOnes.addAll(lines.subList(endingLine + 1, lines.size()));
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < finalOnes.size() - 1; i++) {
			sb.append(finalOnes.get(i) + '\n');
		}
		sb.append(finalOnes.get(finalOnes.size() - 1));
		model.getCurrentDocument().getTextComponent().setText(sb.toString());
	}

	/**
	 * Method that returns order number of line where {@link Caret} is situated
	 * 
	 * @param lines  collection of Lines
	 * @param bifunc used to determine whether dot or mark is used.
	 * @return order number of line where {@link Caret} is situated
	 */
	private int getNumberOfLine(List<String> lines, BiFunction<Integer, Integer, Integer> bifunc) {
		Objects.requireNonNull(lines, "Must have something to see shere getDotStarts");
		Objects.requireNonNull(bifunc, "Cannot apply null to chars");

		JTextArea jta = model.getCurrentDocument().getTextComponent();
		Caret c = jta.getCaret();
		int number = bifunc.apply(c.getDot(), c.getMark());
		try {
			return jta.getLineOfOffset(number);
		} catch (BadLocationException ignorable) {
			return -1;
		}
	}

	// -----------------------------------------------------------------

	/**
	 * {@link ActionListener} used for clock updates
	 */
	private ActionListener updateClockAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			clock.setText(formatter.format(new Date(System.currentTimeMillis())));
		}
	};

// -----------------------------------------------------------------

	/**
	 * private variable holding implementation of {@link LocalizableAction} derived
	 * from {@link Action} able to change text of appropriate
	 * {@link java.awt.Component}s when language of app changes.
	 * 
	 * Used for deleting lines that are repeated.
	 */
	private final LocalizableAction unique = new LocalizableAction("unique", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			List<String> lines = model.getCurrentDocument().getTextComponent().getText().lines()
					.collect(Collectors.toList());
			int startingLine = getNumberOfLine(lines, (a, b) -> Math.min(a, b));
			int endingLine = getNumberOfLine(lines, (a, b) -> Math.max(a, b));

			List<String> onesToFilter = new ArrayList<>(lines.subList(startingLine, endingLine + 1));
			Set<String> unique = new HashSet<>();
			List<String> remaining = new ArrayList<String>();
			for (int i = 0; i < onesToFilter.size(); i++) {
				if (unique.add(onesToFilter.get(i))) {
					remaining.add(onesToFilter.get(i));
				}
			}
			List<String> finalOnes = new ArrayList<>(lines.subList(0, startingLine));
			finalOnes.addAll(remaining);
			finalOnes.addAll(lines.subList(endingLine + 1, lines.size()));

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < finalOnes.size(); i++) {
				sb.append(finalOnes.get(i) + '\n');
			}
			model.getCurrentDocument().getTextComponent().setText(sb.toString());
		}
	};

	// -----------------------------------------------------------------

	/**
	 * Class implementing {@link JFileChooser} that has options for localization
	 * 
	 * @author juren
	 *
	 */
	private static class MyJFileChooser extends JFileChooser {
		private static final long serialVersionUID = 1L;
		/**
		 * variable storing {@link ILocalizationProvider} used for i18n
		 */
		private ILocalizationProvider parent;

		/**
		 * Standard constructor for {@link MyJFileChooser}
		 * 
		 * @param parent ILocalizationProvider used for i18n
		 */
		public MyJFileChooser(ILocalizationProvider parent) {
			this.parent = parent;
		}

		@Override
		public void approveSelection() {
			Path path = getSelectedFile().toPath();
			if (Files.exists(path) && getDialogType() == SAVE_DIALOG) {

				String[] options = { "yes", "no" };
				int result = LocalizableJOptionPane.showOptionDialog(this, "overwriteQuestion", "existingFile",
						JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null, parent);

				switch (result) {
				case 0:
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