package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Class used as a implementation of {@link Command} interface.
 * 
 * Has {@link #execute(Context, Painter)} method which pops the current state
 * out of provided {@link Context}
 * 
 * @author juren
 *
 */
public class PopCommand implements Command {

	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.popState();
	}

}
