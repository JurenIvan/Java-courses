package hr.fer.zemris.java.hw08.vjezba;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Prozor extends JFrame {
	private static final long serialVersionUID = 1L;
	private String language;
	private JButton gumb;
	private FormLocalizationProvider flp;

	public Prozor(String language) throws HeadlessException {
		this.language = language;
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocation(0, 0);
		setTitle("Demo");

		flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
		initGUI();
		flp.addLocalizationListener(new ILocalizationListener() {

			@Override
			public void localizationChanged() {
				gumb.setText(LocalizationProvider.getInstance().getString("login"));
			}
		});

		pack();
	}

	private void initGUI() {

		getContentPane().setLayout(new BorderLayout());
		

		gumb = new JButton(new LocalizableAction("login", flp));

		JMenuBar jmb = new JMenuBar();
		JMenu jm = new JMenu("Languages");
		JMenuItem jmiEN = new JMenuItem("en");
		JMenuItem jmiHR = new JMenuItem("hr");
		JMenuItem jmiDE = new JMenuItem("de");

		jmiDE.addActionListener((e) -> LocalizationProvider.getInstance().setLanguage("de"));
		jmiEN.addActionListener((e) -> LocalizationProvider.getInstance().setLanguage("en"));
		jmiHR.addActionListener((e) -> LocalizationProvider.getInstance().setLanguage("hr"));

		jm.add(jmiEN);
		jm.add(jmiHR);
		jm.add(jmiDE);

		getContentPane().add(jm, BorderLayout.NORTH);
		getContentPane().add(gumb, BorderLayout.CENTER);
		jmb.add(jm);
		setJMenuBar(jmb);

		gumb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Napravi prijavu...
			}
		});

	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Očekivao sam oznaku jezika kao argument!");
			System.err.println("Zadajte kao parametar hr ili en.");
			System.exit(-1);
		}
		final String jezik = args[0];
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Prozor(jezik).setVisible(true);
			}
		});
	}

}
