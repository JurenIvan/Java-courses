package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.lsystems.Painter;

/**
 * Public interface with {@link #execute(Context, Painter)} method that takes
 * context, and painter and what happens with them depends on implementation of
 * execute method
 * 
 * @author juren
 *
 */
@FunctionalInterface
public interface Command {
	/**
	 * Method that performs some action (depends on implementation) upon current
	 * context and can reflect that to painter (draws it)
	 * 
	 * @param ctx     current {@link Context}
	 * @param painter Painter used graphically represent result of command if needed
	 */
	void execute(Context ctx, Painter painter);
}
