package hr.fer.zemris.java.hw06.shell;

import java.util.List;
import java.util.Objects;

/**
 * Class that models result from filter. Has Constructor and basic retriving
 * methods.
 * 
 * @author juren
 *
 */
public class FilterResult {
	/**
	 * Variable that stores name of result
	 */
	private String name;
	/**
	 * List that stores groups that filter found
	 */
	private List<String> groups;

	/**
	 * Classic constructor that sets provided variables
	 * 
	 * @param fileName name of result
	 * @param groups   list of groups
	 * 
	 * @throws NullPointerException if any argument is null
	 */
	public FilterResult(String fileName, List<String> groups) {
		this.name = Objects.requireNonNull(fileName, "Can not make FilterResult with null as a name.");
		this.groups = Objects.requireNonNull(groups, "Can not make FilterResult with null groups.");
	}

	/**
	 * Standard getter for name
	 * 
	 * @return name of result
	 */
	public String toString() { // vraća ime datoteke (bez staze)
		return name;
	}

	/**
	 * Getter for number of groups filter found.
	 * 
	 * @return number of groups filter found
	 */
	public int numberOfGroups() { // koliko je grupa pronađeno
		return groups.size();
	}

	/**
	 * Getter for certain group (by index)
	 * 
	 * @param index of group that will be returned
	 * @return string representing group
	 * @throws IllegalArgumentException if index is out of range for provided number
	 */
	public String group(int index) { // dohvat tražene grupe: vrijedi 0 <= index <= numberOfGroups()
		Objects.checkIndex(index, groups.size());
		return groups.get(index);
	}

}
