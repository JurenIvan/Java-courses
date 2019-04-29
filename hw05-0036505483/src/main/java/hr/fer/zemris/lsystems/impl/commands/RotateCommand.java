package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * Class used as a implementation of {@link Command} interface.
 * 
 * Has constructor that takes one double as an argument and remembers it because
 * it is later used in {@link #execute(Context, Painter)} method to rotate
 * current angle
 * 
 * @author juren
 *
 */
public class RotateCommand implements Command {

	/**
	 * variable that saves double representing angle that is later used in
	 * {@link #execute(Context, Painter)}
	 */
	private double angle;

	/**
	 * Constructor that gets {@link #factor} and saves it.
	 * 
	 * @param angle used later in {@link #execute(Context, Painter)} method
	 */
	public RotateCommand(double angle) {
		this.angle = angle;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState curstate = ctx.getCurrentState();
		curstate.getDirection().rotate(Math.toRadians(this.angle));
	}

}
