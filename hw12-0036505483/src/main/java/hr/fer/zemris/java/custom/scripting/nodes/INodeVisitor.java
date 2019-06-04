package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Interface that is base for a Visitor design pattern. Has methods that are
 * used to visit each type of Node that is present in out parsed tree.
 * 
 * @author juren
 *
 */
public interface INodeVisitor {

	/**
	 * Method that is called upon this implementation of visitor design pattern when
	 * {@link DocumentNode} is visited.
	 * 
	 * @param DocumentNode {@link DocumentNode} that is visited.
	 */
	public void visitDocumentNode(DocumentNode documentNode);

	/**
	 * Method that is called upon this implementation of visitor design pattern when
	 * {@link EchoNode} is visited.
	 * 
	 * @param DocumentNode {@link EchoNode} that is visited.
	 */
	public void visitEchoNode(EchoNode echoNode);

	/**
	 * Method that is called upon this implementation of visitor design pattern when
	 * {@link ForLoopNode} is visited.
	 * 
	 * @param DocumentNode {@link ForLoopNode} that is visited.
	 */
	public void visitForLoopNode(ForLoopNode forLoopNode);

	/**
	 * Method that is called upon this implementation of visitor design pattern when
	 * {@link TextNode} is visited.
	 * 
	 * @param DocumentNode {@link TextNode} that is visited.
	 */
	public void visitTextNode(TextNode textNode);

}
