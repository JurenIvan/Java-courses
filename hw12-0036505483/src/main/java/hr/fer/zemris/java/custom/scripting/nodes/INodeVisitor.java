package hr.fer.zemris.java.custom.scripting.nodes;

public interface INodeVisitor {
	
	public void visitDocumentNode(DocumentNode object);

	public void visitEchoNode(EchoNode object);

	public void visitForLoopNode(ForLoopNode object);

	public void visitTextNode(TextNode textNode);

}
