package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;
import hr.fer.zemris.math.Vector2D;

/**
 * Class used as a implementation of {@link Command} interface.
 * 
 * Has constructor that takes one double as an argument and remembers it because
 * it is later used in {@link #execute(Context, Painter)} method to draw a line
 * of that length
 * 
 * @author juren
 *
 */
public class DrawCommand implements Command {
	/**
	 * variable that saves double representing length that is later used in
	 * {@link #execute(Context, Painter)}
	 */
	private double step;

	/**
	 * Constructor that gets {@link #factor} and saves it.
	 * 
	 * @param step used later in {@link #execute(Context, Painter)} method as described in {@link DrawCommand}
	 */
	public DrawCommand(double step) {
		this.step = step;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState curr = ctx.getCurrentState();
		Vector2D whereBefore = curr.getCurrentPosition().copy();

		curr.getCurrentPosition().translate(curr.getDirection().scaled(step * curr.getEfectiveLenght()));

		painter.drawLine(whereBefore.getX(), whereBefore.getY(), curr.getCurrentPosition().getX(),
				curr.getCurrentPosition().getY(), curr.getColor(), (float) curr.getEfectiveLenght());
	}

}
