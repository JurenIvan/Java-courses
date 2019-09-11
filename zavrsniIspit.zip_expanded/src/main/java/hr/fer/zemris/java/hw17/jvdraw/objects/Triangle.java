package hr.fer.zemris.java.hw17.jvdraw.objects;

import java.awt.Color;
import java.util.List;

import hr.fer.zemris.java.hw17.jvdraw.editing.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.editing.TriangleEditor;
import hr.fer.zemris.java.hw17.jvdraw.objects.visitors.GeometricalObjectVisitor;

public class Triangle extends GeometricalObject {

	public List<Integer> x;
	public List<Integer> y;
	public Color rub;
	public Color ispuna;

	public Triangle(List<Integer> x, List<Integer> y, Color rub, Color ispuna) {
		super();
		this.x = x;
		this.y = y;
		this.rub = rub;
		this.ispuna = ispuna;
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new TriangleEditor(this);
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}
	
	@Override
	public String toString() {
		return String.format("Triangle (%d,%d,%d) - (%d,%d,%d)", x.get(0), x.get(1), x.get(2), y.get(0), y.get(1), y.get(2));
	}

	public void update(Color bgcolor, Color fgcolor) {
		this.rub = fgcolor;
		this.ispuna = bgcolor;
		
		notifyObjectChanged();
	}

}
