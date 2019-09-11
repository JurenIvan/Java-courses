package hr.fer.zemris.java.hw17.jvdraw;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.jvdraw.colorArea.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.drawingModel.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FTriangle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.tools.Tool;

public class FTriangleTool implements Tool {
	
	
	/**
	 * {@link IColorProvider} that is used to get background color
	 */
	private IColorProvider bgColorProvider;
	/**
	 * {@link IColorProvider} that is used to get foreground color
	 */
	private IColorProvider fgColorProvider;
	/**
	 * reference to {@link DrawingModel} where new objects get stored
	 */
	private DrawingModel drawingModel;
	/**
	 * boolean flag that determines whether this is first or second click
	 */
	private boolean beginTriangle = true;
	/**
	 * reference to {@link JDrawingCanvas} upon {@link GeometricalObject} are drawn
	 */
	private JDrawingCanvas canvas;

		
	
	private Point pFirst;
	private Point pSecond;
	private Point pThird;

	
	private FTriangle fTriangle;

	public FTriangleTool(IColorProvider fgColorProvider, IColorProvider bgColorProvider, DrawingModel drawingModel,
			JDrawingCanvas canvas) {
		
		this.fgColorProvider = fgColorProvider;
		this.drawingModel = drawingModel;
		this.canvas = canvas;
		this.bgColorProvider = bgColorProvider;
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		if(pFirst==null) {
			pFirst=e.getPoint();
		}else if(pSecond==null) {
			pSecond=e.getPoint();
		}else if(pThird==null) {
			
			fTriangle=new FTriangle(fgColorProvider.getCurrentColor(), bgColorProvider.getCurrentColor(), new Point(pFirst), new Point(pSecond),e.getPoint());
			
			pFirst=null;
			pSecond=null;
			pThird=null;
			
			drawingModel.add(fTriangle);
			
		}
		
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	

	@Override
	public void paint(Graphics2D g2d) {
		int x[]=new int[3];
		int y[]=new int[3];
	//	if(fTriangle.getpFirst()!=null && fTriangle.getpSecond()!=null && fTriangle.getpThird()!=null) {
		x[0]=fTriangle.getpFirst().x;
		x[1]=fTriangle.getpSecond().x;
		x[2]=fTriangle.getpThird().x;
		
		y[0]=fTriangle.getpFirst().y;
		y[1]=fTriangle.getpSecond().y;
		y[2]=fTriangle.getpThird().y;
		
		g2d.setColor(fTriangle.getFillColor());
		g2d.fillPolygon(x, y, 3);
		g2d.setColor(fTriangle.getOutlineColor());
		g2d.drawPolygon(x,y,3);
	//	}
		
		
	}
	

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
	}

}
