package zi.base;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * Class containing main method used to demonstrate functionalities of class. It
 * is gui of 2 lists that can show primes.
 * 
 * @author juren
 *
 */
public class PrimDemo extends JFrame {

	private JTextArea jtextArea;
	private JTable table;
	private JScrollPane jsp1;
	private JScrollPane jsp2;

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for PrimDemo class
	 */
	public PrimDemo() {
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
			jfc.setDialogTitle("Open file");
			if (jfc.showOpenDialog(PrimDemo.this) != JFileChooser.APPROVE_OPTION)
				return;

			Path openedFilePath = jfc.getSelectedFile().toPath();
			if (!Files.isReadable(openedFilePath)) {
				JOptionPane.showMessageDialog(PrimDemo.this, "noReadPersmision", "error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String out;
			Calc calc=new Calc();
			try {
				List<String> ls=Files.readAllLines(openedFilePath).stream()
						.filter(e2 -> !e2.isBlank()).collect(Collectors.toList());
				calc.calcOut(ls);
				jtextArea.setText(String.join("\n",ls ));
				
			try {
			
			out=calc.calcOut(ls);
			}catch(Exception e2){
				JOptionPane.showMessageDialog(PrimDemo.this, "Error occured!");
				return;
			}
			
			String[] lines=out.split("\n");
			
			String[][] data=new String[lines.length][lines[0].split(";").length];
			
			for(int i=0;i<lines.length;i++) {
				data[i]=lines[i].split(";");
			}
			String[] coldata=new String[calc.labels.size()+calc.expand.size()];
			for(int i=0;i<calc.labels.size();i++) {
				coldata[i]=calc.labels.get(i);
			}
			for(int i=calc.labels.size();i<calc.labels.size()+calc.expand.size();i++) {
				coldata[i]=calc.expand.get(i-calc.labels.size());
			}
			
			table.setModel(new AbstractTableModel() {
				@Override
				public String getColumnName(int column) {
					return coldata[column]; 
				};
				
				@Override
				public Object getValueAt(int rowIndex, int columnIndex) {
				return data[rowIndex][columnIndex];
				}
				
				@Override
				public int getRowCount() {
					return data.length;
				}
				
				@Override
				public int getColumnCount() {
					
					return data[1].length;
				}
			});
		
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
	};

	private final AbstractAction exit=new AbstractAction("exit"){private static final long serialVersionUID=1L;

	@Override public void actionPerformed(ActionEvent e){dispose();}};

	/**
	 * Method used to clear up the constructor.Contains commands to configure GUI.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		jtextArea = new JTextArea(10, 50);
		table = new JTable(5, 5);

		jsp1 = new JScrollPane(jtextArea);
		jsp2 = new JScrollPane(table);

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jsp1, jsp2);

		cp.add(splitPane);
	}

	/**
	 * Main method used to start program and demonstrate it's functionalities
	 * 
	 * @param args not used.
	 */
	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {
			JFrame frame = new PrimDemo();
			frame.pack();
			frame.setVisible(true);
		});
	}

}
