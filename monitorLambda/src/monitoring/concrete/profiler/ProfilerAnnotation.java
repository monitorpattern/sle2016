package monitoring.concrete.profiler;

import monitoring.framework.AbstractAnnotation;

public class ProfilerAnnotation extends AbstractAnnotation {
	
	public String symbol;

	public ProfilerAnnotation(String s) {
		symbol = s;
	}
	
	public String getSymbol() {
		return symbol;
	}
}
