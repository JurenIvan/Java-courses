package searching.slagalica;

import java.util.Arrays;

/**
 * Class that models configuration of puzzle. Has array of ints representing
 * order of numbers in puzzle
 * 
 * @author juren
 *
 */
public class KonfiguracijaSlagalice {

	/**
	 * Array that contains numbers of puzzle
	 */
	private int[] puzzleConfig;

	/**
	 * Constructor for {@link KonfiguracijaSlagalice}
	 * 
	 * @param puzzleConfig array whose reference gets stored
	 */
	public KonfiguracijaSlagalice(int[] puzzleConfig) {
		this.puzzleConfig = puzzleConfig;
	}

	/**
	 * Getter for copy of array containing configuration of numbers in puzzle
	 * 
	 * @return copy of array containing configuration of numbers in puzzle
	 */
	public int[] getPolje() {
		return Arrays.copyOf(puzzleConfig, puzzleConfig.length);
	}

	/**
	 * Method that returns index of zero in configuration
	 * 
	 * @return index of zero in configuration
	 * @throws IllegalStateException if no zero is found
	 */
	public int indexOfSpace() {
		for (int i = 0; i < puzzleConfig.length; i++) {
			if (puzzleConfig[i] == 0)
				return i;
		}
		throw new IllegalStateException("No zero found in array");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				int toPrint = puzzleConfig[i * 3 + j];
				sb.append(toPrint == 0 ? "* " : toPrint + " ");
			}
			sb.append("\n");
		}
		return sb.toString();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(puzzleConfig);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof KonfiguracijaSlagalice))
			return false;
		KonfiguracijaSlagalice other = (KonfiguracijaSlagalice) obj;
		return Arrays.equals(puzzleConfig, other.puzzleConfig);
	}

}
