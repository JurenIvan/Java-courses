package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * TextNode class is a node representing a piece of textual data. It inherits
 * from Node class.
 * 
 * Has a constructor that takes String and stores it
 * 
 * @author juren
 *
 */
public class TimeNode extends Node {
	/**
	 * Read-only property of for loop node.
	 */
	private String text;

	/**
	 * Default constructor of
	 * 
	 * @param text
	 */
	public TimeNode(String text) {
		this.text = text;
	}

	/**
	 * Getter that ensures that user gets text without knowing that escaping ever
	 * occurred.
	 * 
	 * @return String text but with no escapes possible
	 */
	public String getText() {
		StringBuilder sb = new StringBuilder();
		if (text == null)
			return "";

		char[] arrayOfChars = text.toCharArray();
		for (int i = 0; i < text.length(); i++) {
			if (arrayOfChars[i] != '\\')
				sb.append(arrayOfChars[i]);
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return this.text;
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitTimeNode(this);
	}

}
