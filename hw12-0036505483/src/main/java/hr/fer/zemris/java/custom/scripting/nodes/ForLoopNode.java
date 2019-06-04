package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.zemris.java.custom.scripting.tokens.Element;
import hr.fer.zemris.java.custom.scripting.tokens.ElementVariable;

/**
 * ForLoopNode class is a node representing a single for-loop construct. It
 * inherits from Node class.
 * 
 * @author juren
 *
 */
public class ForLoopNode extends Node {
	/**
	 * Read-only property of for loop node.
	 */
	private ElementVariable variable;
	/**
	 * Read-only property of for loop node.
	 */
	private Element startExpression;
	/**
	 * Read-only property of for loop node.
	 */
	private Element endExpression;
	/**
	 * Read-only property of for loop node.
	 */
	private Element stepExpression;

	/**
	 * Default constructor of this class. Sets the appropriate variables. Only
	 * stepExpression can be null-reference. If any other argument is null,
	 * exception is thrown.
	 * 
	 * @param variable        ElementVariable read-only property of for loop node.
	 *                        MUST NOT be null.
	 * @param startExpression Element read-only property of for loop node. MUST NOT
	 *                        be null.
	 * @param endExpression   Element read-only property of for loop node. MUST NOT
	 *                        be null.
	 * @param stepExpression  Element read-only property of for loop node. Can be
	 *                        null reference
	 * 
	 * @throws NullPointerException if any of first three arguments are null
	 *                              references
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression,
			Element stepExpression) {
		Objects.requireNonNull(variable);
		Objects.requireNonNull(startExpression);
		Objects.requireNonNull(endExpression);

		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
	}

	/**
	 * Default getter for variable
	 * 
	 * @return variable
	 */
	public ElementVariable getVariable() {
		return variable;
	}
	
	/**
	 * Default getter for startExpression
	 * 
	 * @return startExpression
	 */
	public Element getStartExpression() {
		return startExpression;
	}
	
	/**
	 * Default getter for endExpression
	 * 
	 * @return endExpression
	 */
	public Element getEndExpression() {
		return endExpression;
	}

	/**
	 * Default getter for stepExpression
	 * 
	 * @return stepExpression
	 */
	public Element getStepExpression() {
		return stepExpression;
	}
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("{$ FOR ");
		
		sb.append(variable.toString());
		sb.append(" ");
		
		sb.append(startExpression.toString());
		sb.append(" ");
		
		sb.append(endExpression.toString());
		sb.append(" ");
		
		if(stepExpression!=null) {
			sb.append(stepExpression.toString());
			sb.append("");
		}
		sb.append("$}");
		
		for(int i=0;i<numberOfChildren();i++) {
			sb.append(super.getChild(i).toString());
		}
		
		sb.append("{$END$}");

		return sb.toString();
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitForLoopNode(this);
	}

}
