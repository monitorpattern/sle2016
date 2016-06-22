package monitoring.combine;

import lambda.ast.Expression;
import lambda.ast.ILambdaVisitor;
import lambda.domains.DenotableValue;
import monitoring.framework.Link;

/** Corresponds to: MonDecorator
 *  Decorated element */
public class DecoratedElement extends Expression {
	
	protected Expression originalElement;
	protected Link link;

	public DecoratedElement() {}
	public DecoratedElement(Expression element, Link link) {
		this.originalElement = element;
		this.link = link;
	}

	public Expression getOriginalElement() {
		return originalElement;
	}
	
	public Link getLink() {
		return link;
	}

	public void setOriginalElement(Expression originalElement) {
		this.originalElement = originalElement;
	}

	public void setLink(Link link) {
		this.link = link;
	}

	@Override
	public <T> T accept(ILambdaVisitor<T> visitor) {
		if (visitor instanceof IDecoratedElementVisitor) {
			return ((IDecoratedElementVisitor<T>)visitor).visit(this); 
		}
		else {
			return getOriginalElement().accept(visitor);
		}
	}

	public String toString() {
		return "Ann [" + getLink().getAnnotationAST() + "]:" + originalElement.toString();
	}
	
	public String toString_withAnn() {
		return originalElement.toString();
	}
}
