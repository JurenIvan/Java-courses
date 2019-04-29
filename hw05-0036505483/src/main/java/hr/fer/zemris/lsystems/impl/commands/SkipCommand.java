package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;
import hr.fer.zemris.math.Vector2D;

/**
 * Class representing a implementation of {@link Command} interface.
 * 
 * Has constructor that takes double as an argument and remembers it because it
 * is later used in {@link #execute(Context, Painter)} method. It's
 * implementation of execute moves turtle but doesn't leave trail
 * 
 * @author juren
 *
 */
public class SkipCommand implements Command {

	/**
	 * Variable that says how big is move without leaving trail (no lines are drawn)
	 */
	private double skip;

	/**
	 * Constructor for {@link SkipCommand} that gets one double representing
	 * {@link #skip}
	 * 
	 * @param skip lenght of trail with no trail.
	 */
	public SkipCommand(double skip) {
		this.skip = skip;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState curr = ctx.getCurrentState();
		Vector2D howMuchToMove = curr.getDirection().scaled(this.skip);

		curr.getCurrentPosition().translate(howMuchToMove);
	}

}
