package hr.fer.zemris.java.JIR.gui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Class containing main method used to demonstrate functionalities of class. It
 * is gui of 2 lists that can show primes.
 * 
 * @author juren
 *
 */
public class Drugi extends JFrame {

	private static final long serialVersionUID = 1L;

	private JList list;
	private JLabel label;
	private DefaultListModel<String> listModel = new DefaultListModel<String>();

	/**
	 * Constructor for PrimDemo class
	 */
	public Drugi() {
		setLocation(20, 50);
		
		setTitle("Primes lists");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		initGUI();
		addMenu();
		setListeners();
		setSize(300, 200);
	}

	private void setListeners() {
		list.addListSelectionListener(evt ->
		label.setText(getTextForDate(list.getSelectedValue()==null?null:list.getSelectedValue().toString()))
		);
	}

	private String getTextForDate(String string) {
		if(string==null) return "Pick a date!";
		StringBuilder sb = new StringBuilder();
		String text = "";
		LocalDate ld = LocalDate.parse(string);

		sb.append("Na dan ");
		sb.append(string);

		var dow = ld.getDayOfWeek();
		if (dow == DayOfWeek.MONDAY)
			text = " bio je ponedjeljak.";
		if (dow == DayOfWeek.TUESDAY)
			text = " bio je utorak.";
		if (dow == DayOfWeek.WEDNESDAY)
			text = " bila je srijeda.";
		if (dow == DayOfWeek.THURSDAY)
			text = " bio je četvrtak.";
		if (dow == DayOfWeek.FRIDAY)
			text = " bio je petak.";
		if (dow == DayOfWeek.SATURDAY)
			text = " bila je subota.";
		if (dow == DayOfWeek.SUNDAY)
			text = " bila je nedjelja.";
		sb.append(text);
		return sb.toString();
	}

	/**
	 * 
	 * /** Method used to clear up the constructor.Contains commands to configure
	 * GUI.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new GridLayout(1, 2));
		
		list = new JList<>(listModel);
		cp.add(new JScrollPane(list));
	//	cp.add(list);
		label = new JLabel("Pick a input files");
		cp.add(new JScrollPane(label));

	}

	private final AbstractAction openNewDocument = new AbstractAction("open") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc = new JFileChooser();
			jfc.setDialogTitle("Open file");
			if (jfc.showOpenDialog(Drugi.this) != JFileChooser.APPROVE_OPTION)
				return;

			Path openedFilePath = jfc.getSelectedFile().toPath();
			if (!Files.isReadable(openedFilePath)) {
				JOptionPane.showMessageDialog(Drugi.this, "noReadPersmision", "error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			List<String> dates;
			try {
				dates = Files.readAllLines(openedFilePath);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(Drugi.this, "noReadPersmision", "error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			dates = dates.stream().filter(e2 -> !e2.startsWith("#")).filter(e2 -> !e2.isBlank())
					.collect(Collectors.toList());
			
			list.setSelectedValue("Pick a Date", false);
			listModel.clear();
			
			for (int i = 0; i < dates.size(); i++) {
				listModel.insertElementAt(dates.get(i), i);
			}
		}
	};

	private final AbstractAction exit = new AbstractAction("exit") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	};

	private void addMenu() {
		Container cp = getContentPane();

		JMenuBar mb = new JMenuBar();
		setJMenuBar(mb);

		JMenu file = new JMenu("file");
		mb.add(file);
		file.add(new JMenuItem(openNewDocument));
		file.addSeparator();
		file.add(new JMenuItem(exit));

	}

	/**
	 * Main method used to start program and demonstrate it's functionalities
	 * 
	 * @param args not used.
	 */
	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {
			JFrame frame = new Drugi();
			frame.pack();
			frame.setVisible(true);
		});
	}

}
