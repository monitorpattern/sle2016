package monitoring.combine;

import lambda.ast.ILambdaVisitor;

public interface IDecoratedElementVisitor<T> extends ILambdaVisitor<T> {
	T visit(DecoratedElement aNode);
}
