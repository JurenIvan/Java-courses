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
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Line;
import hr.fer.zemris.java.hw17.jvdraw.tools.CircleTools;
import hr.fer.zemris.java.hw17.jvdraw.tools.FCircleTools;
import hr.fer.zemris.java.hw17.jvdraw.tools.LineTools;
import hr.fer.zemris.java.hw17.jvdraw.tools.Tool;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectBBCalculator;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectOutputter;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectPainter;

public class JVDraw extends JFrame {
	private static final long serialVersionUID = 1L;

	private BottomStatusLabel bsl;
	private IColorProvider fgColorProvider;
	private IColorProvider bgColorProvider;
	private DrawingModel drawingModel;
	private JDrawingCanvas canvas;
	private Tool currStateTool;
	private JList<GeometricalObject> rightList;
	private Path filePath;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JVDraw().setVisible(true));
	}

	public JVDraw() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		initGUI();
		setMinimumSize(new Dimension(600, 600));
		setLocationRelativeTo(null);

	}

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

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exitAction.actionPerformed(null);
			}
		});

	}

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
		line.setSelected(true);

		group.add(line);
		group.add(circle);
		group.add(fCircle);

		tb.add(line);
		tb.add(circle);
		tb.add(fCircle);

		Tool lineTool = new LineTools(fgColorProvider, drawingModel, canvas);
		Tool circleTool = new CircleTools(fgColorProvider, drawingModel, canvas);
		Tool fCircleTool = new FCircleTools(fgColorProvider, bgColorProvider, drawingModel, canvas);

		line.addActionListener((e) -> currStateTool = lineTool);
		circle.addActionListener((e) -> currStateTool = circleTool);
		fCircle.addActionListener((e) -> currStateTool = fCircleTool);

		line.setSelected(true);
		currStateTool = lineTool;
		cp.add(tb, BorderLayout.PAGE_START);
	}

	private void createJCanvas(Container cp) {
		canvas = new JDrawingCanvas(drawingModel, () -> currStateTool);
		cp.add(canvas);
	}

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

		private FilledCircle createFilledCircleFromStringArray(String[] splitted) {
			return new FilledCircle(new Point(Integer.parseInt(splitted[1]), (Integer.parseInt(splitted[2]))),
					new Point(Integer.parseInt(splitted[1]),
							Integer.parseInt(splitted[2]) + Integer.parseInt(splitted[3])),
					new Color(Integer.parseInt(splitted[4]), Integer.parseInt(splitted[5]),
							Integer.parseInt(splitted[6])),
					new Color(Integer.parseInt(splitted[7]), Integer.parseInt(splitted[8]),
							Integer.parseInt(splitted[9])));
		}

		private Line createLineFromStringArray(String[] splitted) {
			return new Line(new Point(Integer.parseInt(splitted[1]), (Integer.parseInt(splitted[2]))),
					new Point(Integer.parseInt(splitted[3]), (Integer.parseInt(splitted[4]))),
					new Color(Integer.parseInt(splitted[5]), Integer.parseInt(splitted[6]),
							Integer.parseInt(splitted[7])));
		}

		private Circle createCircleFromStringArray(String[] splitted) {
			return new Circle(new Point(Integer.parseInt(splitted[1]), (Integer.parseInt(splitted[2]))),
					new Point(Integer.parseInt(splitted[1]),
							Integer.parseInt(splitted[2]) + Integer.parseInt(splitted[3])),
					new Color(Integer.parseInt(splitted[4]), Integer.parseInt(splitted[5]),
							Integer.parseInt(splitted[6])));
		}

	};

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
