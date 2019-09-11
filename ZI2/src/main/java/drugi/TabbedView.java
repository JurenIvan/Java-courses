package drugi;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import zi2.prvi.Calc;

/**
 * Class containing main method used to demonstrate functionalities of class. It
 * is gui of 2 lists that can show primes.
 * 
 * @author juren
 *
 */
public class TabbedView extends JFrame {

	private JPanel[] tabs;
	private JTable[] tables;
	private JTabbedPane jTabbedPane;

	public TabbedView() {
		setLocation(20, 50);
		setSize(300, 200);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		initGUI();
		addMenu();
	}

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

	private final AbstractAction openNewDocument = new AbstractAction("open") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			
			JFileChooser jfc = new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			jfc.setDialogTitle("Open dir");
			if (jfc.showOpenDialog(TabbedView.this) != JFileChooser.APPROVE_OPTION)
				return;

			Path openedFilePath = jfc.getSelectedFile().toPath();
			if (!Files.isDirectory(openedFilePath)) {
				JOptionPane.showMessageDialog(TabbedView.this, "noReadPersmision", "error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			Calc c=new Calc();
			try {
				c.DoCalculations(openedFilePath);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(TabbedView.this,"ERROR");
			}
			var data=c.values;
			
			//handle data
			
			var thisData=new HashMap<Integer,HashMap<String,Integer>>();
			
			for(int i=1;i<13;i++) {
				thisData.put(i,new HashMap<String,Integer>());
			}
			
			c.values.forEach((name,map)->{
				map.forEach((month,quan)->{
					var a=thisData.get(month);
					if(a.containsKey(name)) {
						thisData.get(month).put(name,a.get(name)+quan);
					}else {
						thisData.get(month).put(name,quan);
					}
				});		
			});
			
			for(int i=0;i<12;i++) {
				String[] v= {"Product","Total"};
				Object[][] data2=new Object[c.values.keySet().size()][2];
				var inner=thisData.get(i+1);
				int count=0;
				for(var e2:inner.entrySet()) {
					data2[count][0]=e2.getKey();
					data2[count][1]=e2.getValue();
					count++;
				}
				
				tables[i]=new JTable(data2,v);
				tables[i].invalidate();
			}
			return;
		}
	};

	private final AbstractAction exit = new AbstractAction("exit") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	};

	private void initGUI() {
		jTabbedPane = new JTabbedPane();
		getContentPane().add(jTabbedPane);
		tables = new JTable[12];
		tabs = new JPanel[12];

		for (int i = 0; i < 12; i++) {
			tables[i] = new JTable();
			tabs[i] = new JPanel();

			tables[i] = new JTable(4, 4);

			tabs[i].add(new JScrollPane(tables[i]));
			jTabbedPane.addTab("" + (i + 1), tabs[i]);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new TabbedView();
			frame.pack();
			frame.setVisible(true);
		});
	}

}
