package searching.slagalica;

import java.util.Arrays;

public class KonfiguracijaSlagalice {

	private int[] puzzleConfig;

	public KonfiguracijaSlagalice(int[] puzzleConfig) {
		this.puzzleConfig = puzzleConfig;
	}

	public int[] getPolje() {
		return Arrays.copyOf(puzzleConfig, puzzleConfig.length);
	}

	public int indexOfSpace() {
		int i = 0;
		while (puzzleConfig[i++] != 0)
			;
		return --i;
	}
	

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
			int toPrint=puzzleConfig[i*3+j];
				sb.append(toPrint==0?"* ":toPrint + " ");
			}
			sb.append("\n");
		}
		return sb.toString();

	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(puzzleConfig);
		return result;
	}

	/* (non-Javadoc)
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
