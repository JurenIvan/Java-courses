package hr.fer.zemris.java.hw08.shell.util;

import hr.fer.zemris.java.hw06.shell.FilterResult;

/**
 * Interface containing list of methods that every NameBuilder has to have. This
 * is functional interface so it has only one method. Thats execute.
 * 
 * @author juren
 *
 */
@FunctionalInterface
public interface NameBuilder {

	/**
	 * Method that modifies provided StringBuilder and adds new part of name to it.
	 * 
	 * @param result Filter Result containing data needed to build new name
	 * @param sb     String builder that is used as a data container where we can
	 *               add new data
	 */
	void execute(FilterResult result, StringBuilder sb);

}
