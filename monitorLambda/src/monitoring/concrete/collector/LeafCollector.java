package monitoring.concrete.collector;

import lambda.ast.Expression;
import lambda.domains.DenotableValue;
import monitoring.framework.AbstractAnnotation;
import monitoring.framework.IConfiguration;
import monitoring.framework.LeafMonitor;

public class LeafCollector extends LeafMonitor {

	public LeafCollector() {
		this.state = new CollectorState();
	}

	@Override
	public void post(Expression aNode, AbstractAnnotation a, DenotableValue result, IConfiguration context) {
		CollectorState<DenotableValue> state = (CollectorState<DenotableValue>) getState();
		String symbol = ((CollectorAnnotation)a).getSymbol();
		state.addValue(symbol, result);
	}
	

}
