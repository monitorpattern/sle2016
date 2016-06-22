package monitoring.concrete.debugger;

import lambda.ast.Expression;
import lambda.domains.DenotableValue;
import monitoring.combine.DecoratedElement;
import monitoring.framework.AbstractMonitor;
import monitoring.framework.Link;

/** Decorated element. Solely encapsulates a default link creation */
public class ProgramPointElement extends DecoratedElement {
	
	public ProgramPointElement() {}
	
	public ProgramPointElement(Expression element, AbstractMonitor debugger) {
		this.originalElement = element;
		this.link = new Link(new DebugAnnotation(), debugger);
	}
		
}
