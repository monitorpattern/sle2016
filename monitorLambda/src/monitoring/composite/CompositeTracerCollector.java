package monitoring.composite;

import monitoring.concrete.collector.LeafCollector;
import monitoring.concrete.tracer.LeafTracer;
import monitoring.framework.CompositeMonitor;

public class CompositeTracerCollector extends CompositeMonitor {
	private LeafTracer tracer;
	private LeafCollector collector;
	
	public CompositeTracerCollector() {
		tracer = new LeafTracer();
		collector = new LeafCollector();
		addMonitor(tracer);
		addMonitor(collector);
	}

}
