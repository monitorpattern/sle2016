package monitoring.combine;

import lambda.ast.Expression;
import lambda.ast.Letrec;
import lambda.domains.DenotableValue;
import lambda.valuation.LambdaValuation;
import monitoring.framework.Link;

public class MonitoringLambdaValuation extends LambdaValuation implements IDecoratedElementVisitor<DenotableValue> {
		
	public DenotableValue visit(DecoratedElement dNode) {
		Link link = dNode.getLink();
		link.pre(dNode.getOriginalElement(), configuration);
		DenotableValue result = dNode.getOriginalElement().accept(this);
		link.post(dNode.getOriginalElement(), result, configuration);
		return result;
	}


}
