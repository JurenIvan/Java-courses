package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * DocumentNode is class representing an entire document. It inherits from Node class
 * 
 * @author juren
 *
 */
public class DocumentNode extends Node {
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<numberOfChildren();i++) {
			
			Node node=getChild(i);
			sb.append(node.toString());
			
		}
		return sb.toString();
	}
}
