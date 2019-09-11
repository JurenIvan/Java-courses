package hr.fer.zemris.java.hw17.jvdraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
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
import javax.swing.WindowConstants;

import hr.fer.zemris.java.hw17.jvdraw.editing.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.models.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.models.DrawingModelImpl;
import hr.fer.zemris.java.hw17.jvdraw.models.DrawingObjectListModel;
import hr.fer.zemris.java.hw17.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw17.jvdraw.objects.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.objects.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.objects.Line;
import hr.fer.zemris.java.hw17.jvdraw.objects.Triangle;
import hr.fer.zemris.java.hw17.jvdraw.objects.visitors.GeometricalObjectBBCalculator;
import hr.fer.zemris.java.hw17.jvdraw.objects.visitors.GeometricalObjectPainter;
import hr.fer.zemris.java.hw17.jvdraw.objects.visitors.GeometricalObjectSaver;
import hr.fer.zemris.java.hw17.jvdraw.tools.CircleTool;
import hr.fer.zemris.java.hw17.jvdraw.tools.FilledCircleTool;
import hr.fer.zemris.java.hw17.jvdraw.tools.LineTool;
import hr.fer.zemris.java.hw17.jvdraw.tools.Tool;
import hr.fer.zemris.java.hw17.jvdraw.tools.TriangleTool;

/**
 * Class that creates all swing components that are contained in swing
 * application. Allows users to draw three different type of geometric objects:
 * lines, circles, and filled circles. To draw an object user should select one
 * of those three tools in upper left corner, click on the canvas once to start
 * drawing, and click another time to finish. User can change background and
 * foreground colors by clicking on squares in top left corner and choose the
 * color. This application allows saving of the pictures in .jvd format as well
 * as reading .jvd files and presenting them to user. Pictures can be exported
 * in .jpg , .png or .gif format. On the right side of application is the list
 * of objects that are displayed on the screen. User can double click on any
 * object to edit it, press +/- to change the order of the objects, or press the
 * delete key to remove the object from the list.
 * 
 * @author Marko
 *
 */
public class JVDraw extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Object that the is being drawn on
	 */
	private JDrawingCanvas canvas;
	/**
	 * Drawing model containing all the objects that are displayed on the canvas
	 */
	private DrawingModel model;
	/**
	 * Color area that is providing the foreground color
	 */
	private JColorArea fgColorArea;
	/**
	 * Color area that is providing the background color
	 */
	private JColorArea bgColorArea;
	/**
	 * Currently selected tool
	 */
	private Tool currentTool;
	/**
	 * Path to the file that is currently opened
	 */
	private Path currentFile;

	/**
	 * Constructor that initializes the window
	 */
	public JVDraw() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setSize(1000, 800);
		initGUI();
		setLocationRelativeTo(null);
	}

	/**
	 * Method that initializes all the components that are displayed to the user
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		setTitle("JVDraw");

		fgColorArea = new JColorArea(Color.RED);
		bgColorArea = new JColorArea(Color.BLUE);
		model = new DrawingModelImpl();
		canvas = new JDrawingCanvas(model, new Supplier<Tool>() {

			@Override
			public Tool get() {
				return currentTool;
			}
		});
		currentTool = new LineTool(model, canvas, fgColorArea);
		ColorLabel colorLabel = new ColorLabel(fgColorArea, bgColorArea);

		cp.add(colorLabel, BorderLayout.SOUTH);
		createToolbar(fgColorArea, bgColorArea);

		cp.add(canvas, BorderLayout.CENTER);

		addList();

		createMenu();

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				if (exit()) {
					dispose();
				}
			}
		});

	}

	/**
	 * Creates the menus and initializes them with actions.
	 */
	private void createMenu() {
		JMenuBar mb = new JMenuBar();
		JMenu file = new JMenu("File");

		JMenuItem open = new JMenuItem(openAction);
		JMenuItem save = new JMenuItem(saveAction);
		JMenuItem saveAs = new JMenuItem(saveAsAction);
		JMenuItem export = new JMenuItem(exportAction);
		JMenuItem exit = new JMenuItem(exitAction);

		file.add(open);
		file.add(save);
		file.add(saveAs);
		file.add(export);
		file.add(exit);

		mb.add(file);

		setJMenuBar(mb);
	}

	/**
	 * Method that creates the list that is displayed to the user and registers the
	 * needed listeners so that user can change the order of the objects in list and
	 * delete objects from the list.
	 */
	private void addList() {
		DrawingObjectListModel listModel = new DrawingObjectListModel(model);
		JList<GeometricalObject> objectList = new JList<>(listModel);
		JScrollPane listScrollPane = new JScrollPane(objectList);
		getContentPane().add(listScrollPane, BorderLayout.EAST);

		objectList.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (objectList.getSelectedIndex() == -1)
					return;
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					model.remove(objectList.getSelectedValue());
				} else if (e.getKeyCode() == KeyEvent.VK_PLUS || e.getKeyCode() == KeyEvent.VK_ADD) {
					model.changeOrder(objectList.getSelectedValue(), 1);
					objectList.setSelectedIndex(objectList.getSelectedIndex() + 1);
				} else if (e.getKeyCode() == KeyEvent.VK_MINUS || e.getKeyCode() == KeyEvent.VK_SUBTRACT) {
					model.changeOrder(objectList.getSelectedValue(), -1);
					objectList.setSelectedIndex(objectList.getSelectedIndex() - 1);
				}
			}
		});

		objectList.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					GeometricalObject clicked = objectList.getSelectedValue();
					GeometricalObjectEditor editor = clicked.createGeometricalObjectEditor();
					if (JOptionPane.showConfirmDialog(JVDraw.this, editor, "Editor",
							JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
						try {
							editor.checkEditing();
							editor.acceptEditing();
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(JVDraw.this, "Wrong input.", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		});
	}

	/**
	 * Method that creates the top toolbar with all the buttons and color areas
	 * 
	 * @param fgColorArea foreground color area
	 * @param bgColorArea background color area
	 */
	private void createToolbar(JColorArea fgColorArea, JColorArea bgColorArea) {
		JToolBar tb = new JToolBar();
		tb.setFloatable(false);
		tb.add(fgColorArea);
		tb.add(bgColorArea);
		JToggleButton line = new JToggleButton("Line");
		JToggleButton circle = new JToggleButton("Circle");
		JToggleButton filledCircle = new JToggleButton("Filled circle");
		JToggleButton triangle = new JToggleButton("Triangle");

		line.addActionListener(l -> {
			currentTool = new LineTool(model, canvas, fgColorArea);
		});

		circle.addActionListener(l -> {
			currentTool = new CircleTool(model, canvas, fgColorArea);
		});

		filledCircle.addActionListener(l -> {
			currentTool = new FilledCircleTool(model, canvas, fgColorArea, bgColorArea);
		});

		triangle.addActionListener(l -> {
			currentTool = new TriangleTool(model, canvas, fgColorArea, bgColorArea);
		});

		line.setSelected(true);
		ButtonGroup group = new ButtonGroup();
		group.add(line);
		group.add(circle);
		group.add(filledCircle);
		group.add(triangle);
		tb.add(line);
		tb.add(circle);
		tb.add(filledCircle);
		tb.add(triangle);

		getContentPane().add(tb, BorderLayout.PAGE_START);
	}

	/**
	 * {@link AbstractAction} that overrides the method that tries to open the file,
	 * read it and display that file to the user.
	 */
	private final AbstractAction openAction = new AbstractAction("Open") {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			openFile();
		}

	};

	/**
	 * Method that checks if the model is modified and checks with user if he wants
	 * to save before proceeding. Opens the file and displays objects contained in
	 * file to the user.
	 */
	private void openFile() {
		if (model.isModified()) {
			int result = JOptionPane.showConfirmDialog(JVDraw.this,
					"Do you want to save the file before opening new one?", "Alert", JOptionPane.YES_NO_CANCEL_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				if (!save()) {
					return;
				}
			} else if (result == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Open file");
		if (jfc.showOpenDialog(JVDraw.this) != JFileChooser.APPROVE_OPTION) {
			return;
		}

		Path openedFilePath = jfc.getSelectedFile().toPath();
		String extension = getExtension(openedFilePath);

		if (!Files.isReadable(openedFilePath) || !extension.equals("jvd")) {
			JOptionPane.showMessageDialog(JVDraw.this, "Only .jvd files can be opened.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			readObjectsFromDocument(openedFilePath);
			currentFile = openedFilePath;
			model.clearModifiedFlag();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(JVDraw.this, "File is not formatted properly.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Method that gets the extension of the file from file path.
	 * 
	 * @param openedFilePath path whose extension is needed
	 * @return file extension
	 */
	private String getExtension(Path openedFilePath) {
		int i = openedFilePath.toString().lastIndexOf('.');
		String extension = "";
		if (i > 0) {
			extension = openedFilePath.toString().substring(i + 1);
		}
		return extension;
	}

	/**
	 * Method that creates the objects from the document and adds them to the model.
	 * 
	 * @param openedFilePath file that is read
	 * @throws IOException if there is a problem while reading the file
	 */
	private void readObjectsFromDocument(Path openedFilePath) throws IOException {
		model.clear();
		try (BufferedReader br = new BufferedReader(new FileReader(openedFilePath.toFile()))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("LINE")) {
					model.add(createLineFromString(line));
				} else if (line.startsWith("CIRCLE")) {
					model.add(createCircleFromString(line));
				} else if (line.startsWith("FCIRCLE")) {
					model.add(createFilledCircleFromString(line));
				}
				if (line.startsWith("FTRIANGLE")) {
					model.add(createTriangleFromString(line));
				}
			}
		}
	}

	private GeometricalObject createTriangleFromString(String line) {
		String[] splitted = line.split("\\s+");
		if (splitted.length != 13) {
			throw new IllegalArgumentException();
		}
		int x1 = Integer.parseInt(splitted[1]);
		int y1 = Integer.parseInt(splitted[2]);
		int x2 = Integer.parseInt(splitted[3]);
		int y2 = Integer.parseInt(splitted[4]);
		int x3 = Integer.parseInt(splitted[5]);
		int y3 = Integer.parseInt(splitted[6]);
		int r1 = Integer.parseInt(splitted[7]);
		int g1 = Integer.parseInt(splitted[8]);
		int b1 = Integer.parseInt(splitted[9]);
		int r2 = Integer.parseInt(splitted[10]);
		int g2 = Integer.parseInt(splitted[11]);
		int b2 = Integer.parseInt(splitted[12]);

		List<Integer> x = new ArrayList<>();
		List<Integer> y = new ArrayList<>();
		x.add(x1);
		x.add(x2);
		x.add(x3);
		y.add(y1);
		y.add(y2);
		y.add(y3);

		return new Triangle(x, y, new Color(r1, g1, b1), new Color(r2, g2, b2));
	}

	/**
	 * Method that creates the {@link FilledCircle} based on the line that
	 * represents it. Line should start with FCIRCLE followed by nine numbers, x
	 * coordinate of the center, y coordinate of the center, radius of the circle ,
	 * r,g,b values of foreground color, r,g,b values of the background color
	 * 
	 * @param line line representing a filled circle object
	 * @return object represented by the given line
	 */
	private GeometricalObject createFilledCircleFromString(String line) {
		String[] splitted = line.split("\\s+");
		if (splitted.length != 10) {
			throw new IllegalArgumentException();
		}
		int x1 = Integer.parseInt(splitted[1]);
		int y1 = Integer.parseInt(splitted[2]);
		int radius = Integer.parseInt(splitted[3]);
		int r1 = Integer.parseInt(splitted[4]);
		int g1 = Integer.parseInt(splitted[5]);
		int b1 = Integer.parseInt(splitted[6]);
		int r2 = Integer.parseInt(splitted[7]);
		int g2 = Integer.parseInt(splitted[8]);
		int b2 = Integer.parseInt(splitted[9]);

		return new FilledCircle(new Point(x1, y1), radius, new Color(r1, g1, b1), new Color(r2, g2, b2));
	}

	/**
	 * Method that creates the {@link Circle} based on the line that represents it.
	 * Line should start with CIRCLE followed by six numbers, x coordinate of the
	 * center, y coordinate of the center, radius of the circle , r,g,b values of
	 * foreground color.
	 * 
	 * @param line line representing a circle object
	 * @return object represented by the given line
	 */
	private GeometricalObject createCircleFromString(String line) {
		String[] splitted = line.split("\\s+");
		if (splitted.length != 7) {
			throw new IllegalArgumentException();
		}
		int x1 = Integer.parseInt(splitted[1]);
		int y1 = Integer.parseInt(splitted[2]);
		int radius = Integer.parseInt(splitted[3]);
		int r = Integer.parseInt(splitted[4]);
		int g = Integer.parseInt(splitted[5]);
		int b = Integer.parseInt(splitted[6]);

		return new Circle(new Point(x1, y1), radius, new Color(r, g, b));
	}

	/**
	 * Method that creates a {@link Line} object based on the line that represents
	 * it. Line should start with LINE followed by seven numbers, x and y
	 * coordinates of the starting point, x and y coordinates of the ending point,
	 * r,g,b values of the line color.
	 * 
	 * @param line line representing a line object
	 * @return object represented by the given line
	 */
	private GeometricalObject createLineFromString(String line) {
		String[] splitted = line.split("\\s+");
		if (splitted.length != 8) {
			throw new IllegalArgumentException();
		}
		int x1 = Integer.parseInt(splitted[1]);
		int y1 = Integer.parseInt(splitted[2]);
		int x2 = Integer.parseInt(splitted[3]);
		int y2 = Integer.parseInt(splitted[4]);
		int r = Integer.parseInt(splitted[5]);
		int g = Integer.parseInt(splitted[6]);
		int b = Integer.parseInt(splitted[7]);

		return new Line(new Point(x1, y1), new Point(x2, y2), new Color(r, g, b));
	}

	/**
	 * Action that saves the currently displayed picture to the .jvd file.
	 */
	private final AbstractAction saveAction = new AbstractAction("Save") {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			save();
		}
	};

	/**
	 * Action that saves the currently displayed picture to the .jvd file on the
	 * selected location.
	 */
	private final AbstractAction saveAsAction = new AbstractAction("Save as") {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			saveAs();
		}
	};

	/**
	 * Action that exits the application. If the canvas is modified user is asked if
	 * he wants to save before exiting.
	 */
	private final AbstractAction exitAction = new AbstractAction("Exit") {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (exit()) {
				dispose();
			}
		}
	};

	/**
	 * Action that exports the currently displayed picture to .jpg, .png or .gif
	 * file.
	 */
	private final AbstractAction exportAction = new AbstractAction("Export") {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				export();
			} catch (IOException exc) {
				JOptionPane.showMessageDialog(JVDraw.this, "Export failed", "Information",
						JOptionPane.INFORMATION_MESSAGE);
			}

		}
	};

	/**
	 * Method that does the exporting of the currently displayed canvas. User can
	 * choose format of exporting and path where he wants the picture to be
	 * exported.
	 * 
	 * @throws IOException if there is a problem while creating the new file
	 */
	private void export() throws IOException {

		String[] choices = { ".png", ".gif", ".jpg" };
		String result = (String) JOptionPane.showInputDialog(JVDraw.this, "Choose export format:", "Export",
				JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);

		if (result == null) {
			return;
		}

		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Export file");
		if (jfc.showSaveDialog(JVDraw.this) != JFileChooser.APPROVE_OPTION) {
			JOptionPane.showMessageDialog(JVDraw.this, "Export failed", "Information", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		Path exportPath = jfc.getSelectedFile().toPath();
		if (!getExtension(exportPath).equals(result)) {
			exportPath = Paths.get(exportPath.toString() + result);
		}

		GeometricalObjectBBCalculator bbcalc = new GeometricalObjectBBCalculator();
		for (int i = 0; i < model.getSize(); i++) {
			model.getObject(i).accept(bbcalc);
		}

		Rectangle box = bbcalc.getBoundingBox();
		BufferedImage image = new BufferedImage(box.width, box.height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = image.createGraphics();
		g.translate(-box.x, -box.y);
		g.setColor(Color.WHITE);
		g.fillRect(box.x, box.y, box.width, box.height);
		GeometricalObjectPainter painter = new GeometricalObjectPainter(g);
		for (int i = 0; i < model.getSize(); i++) {
			model.getObject(i).accept(painter);
		}
		g.dispose();

		File file = exportPath.toFile();
		ImageIO.write(image, result.substring(1), file);

		JOptionPane.showMessageDialog(JVDraw.this, "Export successful", "Information", JOptionPane.INFORMATION_MESSAGE);

	}

	/**
	 * Method that is called when the application should terminate. It asks user if
	 * he wants to save the file if the canvas is modified.
	 * 
	 * @return true if user approved closing, false otherwise
	 */
	private boolean exit() {
		if (!model.isModified()) {
			return true;
		} else {
			int result = JOptionPane.showConfirmDialog(JVDraw.this, "Do you want to save the file before closing?",
					"Alert", JOptionPane.YES_NO_CANCEL_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				if (!save()) {
					return false;
				}
			} else if (result == JOptionPane.CANCEL_OPTION) {
				return false;
			}

			return true;
		}
	}

	/**
	 * Method that saves the current state of the canvas to .jvd file. If current
	 * file is not selected work is delegated to the saveAs method to determine the
	 * saving path.
	 * 
	 * @return true if saving was successful, false otherwise
	 */
	private boolean save() {
		if (currentFile == null) {
			return saveAs();
		}

		GeometricalObjectSaver visitor = new GeometricalObjectSaver();

		for (int i = 0; i < model.getSize(); i++) {
			model.getObject(i).accept(visitor);
		}

		try {
			Files.write(currentFile, visitor.getFileText().getBytes());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(JVDraw.this, "File saving failed", "Information",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		JOptionPane.showMessageDialog(JVDraw.this, "File saved successfully", "Information",
				JOptionPane.INFORMATION_MESSAGE);
		return true;
	}

	/**
	 * Method that determines where the file should be saved.
	 * 
	 * @return true if file should be saved, false otherwise
	 */
	private boolean saveAs() {
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Save file");
		if (jfc.showSaveDialog(JVDraw.this) != JFileChooser.APPROVE_OPTION) {
			JOptionPane.showMessageDialog(JVDraw.this, "Saving failed", "Information", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		Path savePath = jfc.getSelectedFile().toPath();
		String extension = getExtension(savePath);

		if (!extension.equals("jvd")) {
			JOptionPane.showMessageDialog(JVDraw.this, "Can only save .jvd files", "Information",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		this.currentFile = savePath;
		return save();
	}

	/**
	 * Method that is called when the application starts
	 * 
	 * @param args none are expected
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new JVDraw().setVisible(true);
		});
	}

}
