package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * Class used as a implementation of {@link Command} interface.
 * 
 * Has constructor that takes one double as an argument and remembers it because
 * it is later used in {@link #execute(Context, Painter)} method to scale the
 * Effective length by that factor.
 * 
 * @author juren
 *
 */
public class ScaleCommand implements Command {
	/**
	 * variable that saves double representing factor that is later used in
	 * {@link #execute(Context, Painter)}
	 */
	private double factor;

	/**
	 * Constructor that gets {@link #factor} and saves it.
	 * 
	 * @param factor used later in {@link #execute(Context, Painter)} method
	 */
	public ScaleCommand(double factor) {
		this.factor = factor;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState curr = ctx.getCurrentState();
		curr.setEfectiveLenght(curr.getEfectiveLenght() * factor);
	}

}
