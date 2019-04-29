package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Class used as a implementation of {@link Command} interface.
 * 
 * Has {@link #execute(Context, Painter)} method which pushes the current state
 * out of provided {@link Context} onto it.
 * 
 * @author juren
 *
 */
public class PushCommand implements Command {

	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.pushState(ctx.getCurrentState().copy());
	}

}
