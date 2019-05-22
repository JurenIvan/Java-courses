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

public class JNotepadPP extends JFrame {
	private static final long serialVersionUID = 1L;

	private static final String FIXED_PART_OF_PATH = ".\\icons\\";
	private static final int DEFAULT_ICON_SIZE = 50;
	private static final int HOW_MUCH_CHARS_TO_SHOW = 15;
	private static final int MINIMUM_WINDOW_SIZE = 500;
	private static final String DEFAULT_LANGUAGE = "en";

	private MultipleDocumentModel model;
	private JPanel infoLeft;
	private JPanel infoRight;
	private JLabel clock;
	private SimpleDateFormat formatter;
	private HashMap<String, ImageIcon> loadedImages;
	private FormLocalizationProvider flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
	private String language=DEFAULT_LANGUAGE;
	
	{
		LocalizationProvider.getInstance().setLanguage(DEFAULT_LANGUAGE);
	}

	public JNotepadPP() {

		loadedImages = new HashMap<>();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		initGUI();
		setMinimumSize(new Dimension(MINIMUM_WINDOW_SIZE, MINIMUM_WINDOW_SIZE));
		pack();
		setLocationRelativeTo(null);
	}

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

	private void refreshStatusbar() {
		Insets data = calcThings();
		((LocalizableJLable) infoLeft.getComponent(0)).setExtraText(String.valueOf(data.top));
		((LocalizableJLable) infoRight.getComponent(0)).setExtraText(String.valueOf(data.left));
		((LocalizableJLable) infoRight.getComponent(1)).setExtraText(String.valueOf(data.bottom));
		((LocalizableJLable) infoRight.getComponent(2)).setExtraText(String.valueOf(data.right));
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

	private void setTitleOfWindow() {
		Path p = model.getCurrentDocument().getFilePath();
		if (p != null) {
			setTitle(p.toAbsolutePath().toString() + " - JNotepad++");
		} else {
			setTitle("(unnamed) - JNotepad++");
		}
	}

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
			language="de";
			LocalizationProvider.getInstance().setLanguage(language);
			this.pack();
		});
		jmiEN.addActionListener((e) -> {
			language="en";
			LocalizationProvider.getInstance().setLanguage(language);
			this.pack();
		});
		jmiHR.addActionListener((e) -> {
			language="hr";
			LocalizationProvider.getInstance().setLanguage(language);
			this.pack();
		});

		lang.add(jmiEN);
		lang.add(jmiHR);
		lang.add(jmiDE);
	}

	private void createToolbar(Container cp) {
		JToolBar tb = new JToolBar();

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

	private final LocalizableAction openNewDocument = new LocalizableAction("open", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			model.createNewDocument();
		}
	};

	// -----------------------------------------------------------------

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

	private final LocalizableAction closeTab = new LocalizableAction("closeTab", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFrame frame = new JFrame();
			String[] options = { "save", "saveAsDocument", "no", "cancel" };
			if(!model.getCurrentDocument().isModified()) {
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

	private final LocalizableAction exitAplication = new LocalizableAction("exitAplication", flp) {
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

		private int getWantedOperation(SingleDocumentModel sdm) {
			return LocalizableJOptionPane.showOptionDialog(null, "docMofifiedInfo", "warning",
					JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
					new String[] { "save", "no", "cancelE" }, null, flp, sdm.getTextComponent().getText().substring(0,
							Math.min(HOW_MUCH_CHARS_TO_SHOW, sdm.getTextComponent().getText().length())));
		}
	};

	// -----------------------------------------------------------------

	private final LocalizableAction copy = new LocalizableAction("copy", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			model.getCurrentDocument().getTextComponent().copy();
		}
	};

	// -----------------------------------------------------------------

	private final LocalizableAction cut = new LocalizableAction("cut", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			model.getCurrentDocument().getTextComponent().cut();
		}
	};
	// -----------------------------------------------------------------

	private final LocalizableAction paste = new LocalizableAction("paste", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			model.getCurrentDocument().getTextComponent().paste();
		}
	};

	// -----------------------------------------------------------------

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

	private final LocalizableAction toUpperCase = new LocalizableAction("toUpperCase", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			applyFunctionOnDoc(Character::toUpperCase);
		}
	};

	private final LocalizableAction toLowerCase = new LocalizableAction("toLowerCase", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			applyFunctionOnDoc(Character::toLowerCase);
		}
	};

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

	private final LocalizableAction ascending = new LocalizableAction("ascending", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Collator collator = Collator.getInstance(new Locale(language)); 
			DoSortingMagic(collator::compare);
		}
	};

	private final LocalizableAction descening = new LocalizableAction("descening", flp) {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Collator collator = Collator.getInstance(new Locale(language));
			DoSortingMagic((a, b) -> collator.compare(b, a));
		}
	};

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

	private static class MyJFileChooser extends JFileChooser {
		private static final long serialVersionUID = 1L;
		private ILocalizationProvider parent;

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