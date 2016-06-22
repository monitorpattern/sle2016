package monitoring.concrete.profiler;

import lambda.ast.Expression;
import lambda.domains.DenotableValue;
import monitoring.framework.AbstractAnnotation;
import monitoring.framework.IConfiguration;
import monitoring.framework.IMonitorState;
import monitoring.framework.LeafMonitor;

public class LeafProfiler extends LeafMonitor {

	public LeafProfiler() {
		this.state = new ProfilerState();
	}
	
	@Override
	public void pre(Expression aNode, AbstractAnnotation annotation, IConfiguration context) {
		ProfilerAnnotation ann = (ProfilerAnnotation) annotation;
		ProfilerState state = (ProfilerState) getState();
		state.getMap().get(ann.getSymbol());
		state.increment(ann.getSymbol());
	}

}
