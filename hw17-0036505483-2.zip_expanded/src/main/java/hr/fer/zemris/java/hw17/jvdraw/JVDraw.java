package hr.fer.zemris.java.hw17.jvdraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import hr.fer.zemris.java.hw17.jvdraw.colorArea.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.colorArea.JColorArea;
import hr.fer.zemris.java.hw17.jvdraw.drawingModel.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.drawingModel.DrawingModelImpl;
import hr.fer.zemris.java.hw17.jvdraw.editor.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Circle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FilledTriangle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Line;
import hr.fer.zemris.java.hw17.jvdraw.tools.CircleTools;
import hr.fer.zemris.java.hw17.jvdraw.tools.FCircleTools;
import hr.fer.zemris.java.hw17.jvdraw.tools.FTriangleTool;
import hr.fer.zemris.java.hw17.jvdraw.tools.LineTools;
import hr.fer.zemris.java.hw17.jvdraw.tools.Tool;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectBBCalculator;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectOutputter;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectPainter;

/**
 * Main class that contains main method used for starting the application.
 * Models JVDraw application. JVDraw application is app that enables user to
 * draw, export images and import special kind of .jvd files that represent a
 * drawing on canvas.
 * 
 * @author juren
 *
 */
public class JVDraw extends JFrame {
	private static final long serialVersionUID = 1L;

	/**
	 * Constant for default window height
	 */
	private static final int DEFAULT_HEIGTH = 800;

	/**
	 * Constant for default window width
	 */
	private static final int DEFAULT_WIDTH = 800;

	/**
	 * Reference to {@link BottomStatusLabel} used for representation of current
	 * color
	 */
	private BottomStatusLabel bsl;
	/**
	 * {@link IColorProvider} for foreground color
	 */
	private IColorProvider fgColorProvider;
	/**
	 * {@link IColorProvider} for background color
	 */
	private IColorProvider bgColorProvider;
	/**
	 * Reference to {@link DrawingModel} used to hold data
	 */
	private DrawingModel drawingModel;
	/**
	 * variable that stores reference to {@link JDrawingCanvas} where drawing is
	 * created
	 */
	private JDrawingCanvas canvas;
	/**
	 * State that holds implementation of tool used at the moment
	 */
	private Tool currStateTool;
	/**
	 * {@link JList} of {@link GeometricalObject} that stores data about all
	 * {@link GeometricalObject} on drawing
	 */
	private JList<GeometricalObject> rightList;
	/**
	 * Variable representing path to file where we store data.
	 */
	private Path filePath;

	/**
	 * Main method. Used to start the application. For more info check
	 * {@link JVDraw}.
	 * 
	 * @param args not used.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JVDraw().setVisible(true));
	}

	/**
	 * Standard constructor for swing applications. Initializes toolbars, listeners,
	 * menus etc.
	 */
	public JVDraw() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exitAction.actionPerformed(null);
			}
		});
		initGUI();
		setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGTH));
		setLocationRelativeTo(null);

	}

	/**
	 * Method that was created as a product of refactoring code. Has bundle of
	 * bundles of functionalities that are similar to each other.
	 */
	private void initGUI() {
		drawingModel = new DrawingModelImpl();

		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		createJCanvas(cp);
		createMenus(cp);
		createToolbar(cp);

		createGeometricalObjectsList(cp);

		bsl = new BottomStatusLabel(fgColorProvider, bgColorProvider);
		cp.add(bsl, BorderLayout.PAGE_END);
		bsl.newColorSelected(null, null, null);
	}

	/**
	 * Method that creates and initializes {@link DrawingObjectListModel} and tests
	 * it up to the right side of the screen.
	 * 
	 * @param cp
	 */
	private void createGeometricalObjectsList(Container cp) {

		rightList = new JList<>(new DrawingObjectListModel(drawingModel));
		rightList.setVisible(true);
		JScrollPane scrollRightList = new JScrollPane(rightList);
		cp.add(scrollRightList, BorderLayout.EAST);

		MouseListener mouseListener = new MouseAdapter() {
			@SuppressWarnings("unchecked")
			public void mouseClicked(MouseEvent mouseEvent) {
				JList<GeometricalObject> theList = (JList<GeometricalObject>) mouseEvent.getSource();
				if (mouseEvent.getClickCount() == 2) {
					int index = theList.locationToIndex(mouseEvent.getPoint());
					if (index >= 0) {
						GeometricalObject clicked = theList.getModel().getElementAt(index);
						GeometricalObjectEditor editor = clicked.createGeometricalObjectEditor();
						if (JOptionPane.showConfirmDialog(null, editor, "Editor!",
								JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
							try {
								editor.checkEditing();
								editor.acceptEditing();
							} catch (Exception ex) {
								JOptionPane.showMessageDialog(null, ex.getMessage());
								return;
							}
							JOptionPane.showMessageDialog(null, "Succesfull!");
						}
					}
				}
			}
		};

		KeyListener keyListener = new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				switch (e.getKeyChar()) {
				case '+':
					drawingModel.changeOrder(rightList.getSelectedValue(), -1);
					rightList.setSelectedIndex(rightList.getSelectedIndex() - 1);
					break;
				case '-':
					drawingModel.changeOrder(rightList.getSelectedValue(), 1);
					rightList.setSelectedIndex(rightList.getSelectedIndex() + 1);
					break;
				case KeyEvent.VK_DELETE:
					int selectedIndex = rightList.getSelectedIndex();
					drawingModel.remove(rightList.getSelectedValue());
					rightList.setSelectedIndex(selectedIndex - 1 >= 0 ? selectedIndex - 1 : 0);
					break;
				}
			}
		};

		rightList.addMouseListener(mouseListener);
		rightList.addKeyListener(keyListener);
	}

	/**
	 * Method that was created as a product of refactoring code. Has bundle of
	 * bundles of functionalities that are similar to each other. this method
	 * contains initialization of Toolbar section.
	 */
	private void createToolbar(Container cp) {
		JToolBar tb = new JToolBar();

		fgColorProvider = new JColorArea(Color.BLUE);
		bgColorProvider = new JColorArea(Color.RED);

		tb.add((JColorArea) fgColorProvider);
		tb.addSeparator(new Dimension(5, 0));
		tb.add((JColorArea) bgColorProvider);
		tb.addSeparator(new Dimension(20, 0));
		ButtonGroup group = new ButtonGroup();

		JToggleButton line = new JToggleButton("Line");
		JToggleButton circle = new JToggleButton("Circle");
		JToggleButton fCircle = new JToggleButton("Filled Circle");
		JToggleButton fTriangle=new JToggleButton("Filled Triangle");
		line.setSelected(true);

		group.add(line);
		group.add(circle);
		group.add(fCircle);
		group.add(fTriangle);
		
		tb.add(line);
		tb.add(circle);
		tb.add(fCircle);
		tb.add(fTriangle);

		Tool lineTool = new LineTools(fgColorProvider, drawingModel, canvas);
		Tool circleTool = new CircleTools(fgColorProvider, drawingModel, canvas);
		Tool fCircleTool = new FCircleTools(fgColorProvider, bgColorProvider, drawingModel, canvas);
		Tool fTriangleTool= new FTriangleTool(fgColorProvider,bgColorProvider,drawingModel,canvas);

		line.addActionListener((e) -> currStateTool = lineTool);
		circle.addActionListener((e) -> currStateTool = circleTool);
		fCircle.addActionListener((e) -> currStateTool = fCircleTool);
		fTriangle.addActionListener((e)->currStateTool=fTriangleTool);

		line.setSelected(true);
		currStateTool = lineTool;
		cp.add(tb, BorderLayout.PAGE_START);
	}

	/**
	 * Method that was created as a product of refactoring code. Has bundle of
	 * bundles of functionalities that are similar to each other. Makes instance of
	 * {@link JDrawingCanvas} and links it with {@link DrawingModel}.
	 */
	private void createJCanvas(Container cp) {
		canvas = new JDrawingCanvas(drawingModel, () -> currStateTool);
		cp.add(canvas);
	}

	/**
	 * Method that was created as a product of refactoring code. Has bundle of
	 * bundles of functionalities that are similar to each other. Makes menu(s) and
	 * MenuItems
	 */
	private void createMenus(Container cp) {
		JMenuBar mb = new JMenuBar();
		setJMenuBar(mb);
		JMenu file = new JMenu("File");
		mb.add(file);

		JMenuItem open = new JMenuItem(openAction);
		file.add(open);
		JMenuItem save = new JMenuItem(saveAction);
		file.add(save);
		JMenuItem saveAs = new JMenuItem(saveAsAction);
		file.add(saveAs);
		JMenuItem export = new JMenuItem(exportAction);
		file.add(export);
		file.addSeparator();
		JMenuItem exit = new JMenuItem(exitAction);
		file.add(exit);
	}

	/**
	 * Action that is used when user wants to load .jvd file
	 */
	private final Action openAction = new AbstractAction("Open") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc = new JFileChooser();
			jfc.setDialogTitle("Import .jvd file");

			if (jfc.showOpenDialog(JVDraw.this) != JFileChooser.APPROVE_OPTION)
				return;

			loadFile(jfc.getSelectedFile().toPath());
		}

		/**
		 * Method that loads file from given path, reads all lines, parses content and
		 * adds to drawingModel all parsed {@link GeometricalObject}.
		 * 
		 * @param path of inputFile
		 */
		private void loadFile(Path path) {

			if (!path.toString().endsWith(".jvd"))
				return;

			List<String> lines;
			try {
				lines = Files.readAllLines(path);
				List<GeometricalObject> listOfParsedObjects = new ArrayList<>();
				for (var line : lines) {
					String splitted[] = line.split(" ");
					if (splitted[0].equals("LINE")) {
						listOfParsedObjects.add(createLineFromStringArray(splitted));
					} else if (splitted[0].equals("CIRCLE")) {
						listOfParsedObjects.add(createCircleFromStringArray(splitted));
					} else if (splitted[0].equals("FCIRCLE")) {
						listOfParsedObjects.add(createFilledCircleFromStringArray(splitted));
					}else if (splitted[0].equals("FTRIANGLE")) {
						listOfParsedObjects.add(createFilledTriangleFromStringArray(splitted));
					}
				}

				drawingModel.clear();
				for (var elem : listOfParsedObjects) {
					drawingModel.add(elem);
				}

			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Improper structure. Couldn't read file.");
			}
			return;
		}

		private GeometricalObject createFilledTriangleFromStringArray(String[] splitted) {
			int x[]=new int[3];
			int y[]=new int[3];
			
			for(int i=0;i<3;i++) {
				x[i]=Integer.parseInt(splitted[1+2*i]);
			}
			for(int i=0;i<3;i++) {
				y[i]=Integer.parseInt(splitted[2+2*i]);
			}
			
			return new FilledTriangle(
					new Color(Integer.parseInt(splitted[7]), Integer.parseInt(splitted[8]),Integer.parseInt(splitted[9])),
					new Color(Integer.parseInt(splitted[10]), Integer.parseInt(splitted[11]),Integer.parseInt(splitted[12])),
					x,y);
		}

		/**
		 * Method that is used to get circle out of parsed string.
		 * 
		 * @param splitted array of tokens
		 * @return Circle
		 */
		private FilledCircle createFilledCircleFromStringArray(String[] splitted) {
			return new FilledCircle(
					new Point(Integer.parseInt(splitted[1]), (Integer.parseInt(splitted[2]))),
					new Point(Integer.parseInt(splitted[1]),
							Integer.parseInt(splitted[2]) + Integer.parseInt(splitted[3])),
					new Color(Integer.parseInt(splitted[4]), Integer.parseInt(splitted[5]),
							Integer.parseInt(splitted[6])),
					new Color(Integer.parseInt(splitted[7]), Integer.parseInt(splitted[8]),
							Integer.parseInt(splitted[9])));
		}

		/**
		 * Method that is used to get line out of parsed string
		 * 
		 * @param splitted array of tokens
		 * @return Circle
		 */
		private Line createLineFromStringArray(String[] splitted) {
			return new Line(new Point(Integer.parseInt(splitted[1]), (Integer.parseInt(splitted[2]))),
					new Point(Integer.parseInt(splitted[3]), (Integer.parseInt(splitted[4]))),
					new Color(Integer.parseInt(splitted[5]), Integer.parseInt(splitted[6]),
							Integer.parseInt(splitted[7])));
		}

		/**
		 * Method that is used to get circle out of parsed string
		 * 
		 * @param splitted array of tokens
		 * @return Circle
		 */
		private Circle createCircleFromStringArray(String[] splitted) {
			return new Circle(new Point(Integer.parseInt(splitted[1]), (Integer.parseInt(splitted[2]))),
					new Point(Integer.parseInt(splitted[1]),
							Integer.parseInt(splitted[2]) + Integer.parseInt(splitted[3])),
					new Color(Integer.parseInt(splitted[4]), Integer.parseInt(splitted[5]),
							Integer.parseInt(splitted[6])));
		}

	};

	/**
	 * Action that is used when user wants to save .jvd file. Requires no extra
	 * input except for the first time. then it delegates the job to he
	 * {@link SaveAsAction}
	 */
	private final Action saveAction = new AbstractAction("Save") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (filePath == null) {
				saveAsAction.actionPerformed(e);
				return;
			}
			saveAsText(filePath);
		}

	};

	/**
	 * Helper method that writes data to provided path
	 * 
	 * @param filePath
	 */
	private void saveAsText(Path filePath) {
		GeometricalObjectOutputter goop = new GeometricalObjectOutputter();
		for (int i = 0; i < drawingModel.getSize(); i++) {
			drawingModel.getObject(i).accept(goop);
		}
		try {
			Files.write(Paths.get(filePath.toString() + ".jvd"), goop.getOutput(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error occured!");
			return;
		}

		JOptionPane.showMessageDialog(null, "Success!");
		drawingModel.clearModifiedFlag();
	}

	/**
	 * Action that is used when user wants to save .jvd file with option to change
	 * it's nameF
	 */
	private final Action saveAsAction = new AbstractAction("Save as") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {

			JFileChooser jfc = new JFileChooser();
			jfc.setDialogTitle("Export Text file");
			if (jfc.showSaveDialog(JVDraw.this) != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(JVDraw.this, "Exit cancelled", "Warning",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			filePath = jfc.getSelectedFile().toPath();
			saveAsText(filePath);
			return;
		}
	};

	/**
	 * Action that is used when user wants to exit {@link JVDraw}. if unsaved data
	 * is present, user decides whether he wants to save or discard changes
	 */
	private final Action exitAction = new AbstractAction("Exit") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!drawingModel.isModified()) {
				dispose();
				return;
			}
			int result = JOptionPane.showOptionDialog(null, "Document is modified, would you like to save?", "Warning",
					JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
					new String[] { "Save", "Dont save", "Cancel" }, null);

			if (result == 2)
				return;
			if (result == 0) {
				saveAction.actionPerformed(e);
				return;
			}
			dispose();
		}

	};

	/**
	 * Action that is used when user wants to export image to drive . User has 3
	 * options, JPG, PNG i GIF
	 */
	private final Action exportAction = new AbstractAction("Export picture") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			int formatNumber = JOptionPane.showOptionDialog(null, "In which format would you like to Export?", "Export",
					JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
					new String[] { "JPG", "PNG", "GIF" }, null);

			JFileChooser jfc = new JFileChooser();
			jfc.setDialogTitle("Export file");
			if (jfc.showSaveDialog(JVDraw.this) != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(JVDraw.this, "Exit cancelled", "Warning",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			Path exportPath = Paths
					.get(jfc.getSelectedFile().toPath().toString() + "." + determineFormat(formatNumber));

			GeometricalObjectBBCalculator bbcalc = new GeometricalObjectBBCalculator();
			for (int i = 0; i < drawingModel.getSize(); i++) {
				drawingModel.getObject(i).accept(bbcalc);
			}
			Rectangle box = bbcalc.getBoundingBox();
			BufferedImage image = new BufferedImage(box.width, box.height, BufferedImage.TYPE_3BYTE_BGR);
			Graphics2D g = image.createGraphics();
			g.setColor(Color.white);
			g.fillRect(0, 0, box.width, box.height);
			g.translate(-box.x, -box.y);

			GeometricalObjectPainter gop = new GeometricalObjectPainter(g);

			for (int i = 0; i < drawingModel.getSize(); i++) {
				drawingModel.getObject(i).accept(gop);
			}
			g.dispose();

			try {
				ImageIO.write(image, determineFormat(formatNumber), exportPath.toFile());
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "Error occured!");
				return;
			}
			JOptionPane.showMessageDialog(null, "Success!");
		}
	};

	/**
	 * Method that returns String stored under certain number. Used to parse data
	 * from input.
	 * 
	 * @param formatNumber ordinal number describing predefined picture formants
	 * @return
	 */
	private String determineFormat(int formatNumber) {
		if (formatNumber == 0)
			return "JPG";
		if (formatNumber == 1)
			return "PNG";
		if (formatNumber == 2)
			return "GIF";
		return null;
	}
}
