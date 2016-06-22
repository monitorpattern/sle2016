package examples.lambda.concrete;

import lambda.ast.Abstraction;
import lambda.ast.Application;
import lambda.ast.Conditional;
import lambda.ast.Constant;
import lambda.ast.Expression;
import lambda.ast.Letrec;
import lambda.ast.Primitive;
import lambda.ast.Variable;
import lambda.domains.DenotableValue;
import monitoring.combine.DecoratedElement;
import monitoring.combine.MonitoringLambdaValuation;
import monitoring.composite.CompositeTracerCollector;
import monitoring.concrete.collector.CollectorAnnotation;
import monitoring.concrete.collector.CollectorState;
import monitoring.concrete.collector.LeafCollector;
import monitoring.concrete.debugger.DebugAnnotator;
import monitoring.concrete.debugger.DebugState;
import monitoring.concrete.debugger.LeafDebugger;
import monitoring.concrete.debugger.ProgramPointElement;
import monitoring.concrete.profiler.LeafProfiler;
import monitoring.concrete.profiler.ProfilerAnnotation;
import monitoring.concrete.profiler.ProfilerState;
import monitoring.concrete.tracer.LeafTracer;
import monitoring.concrete.tracer.TracerAnnotation;
import monitoring.concrete.tracer.TracerState;
import monitoring.framework.CompositeState;
import monitoring.framework.Link;

public class MainTester {
	
	/*
	 * PROFILER
	 * */
	
	private static Letrec profilerEx1(LeafProfiler profiler) {
		Letrec ast = new Letrec();
		ast.setIdentifier(new Variable("fac"));
		
		Abstraction facdef = new Abstraction();
		

		Primitive exp1 = new Primitive("x", "=", 0);
		Constant exp2 = new Constant(1);
		Application app = new Application(
				new Variable("fac"), new Primitive("x", "+", -1)
				);
		
		
		Primitive exp3 = new Primitive("x", "*", app);
		
		// Quick-and-dirty
		Link link = new Link(new ProfilerAnnotation("#fac"), profiler);
		DecoratedElement decoratedNode = new DecoratedElement();
		decoratedNode.setLink(link);
		decoratedNode.setOriginalElement(app);
		//exp3.setRhs(decoratedNode);
		exp3 = new Primitive("x", "*", decoratedNode);
		// End of quick-and-dirty
		
		Conditional cond = new Conditional(exp1, exp2, exp3);
		
		facdef.setArg("x"); // CAREFUL: cannot do facdef.setArg(new MonDecoratedElement(new MonVariable("x")));
		facdef.setExp(cond);

		ast.setAbstraction(facdef);
		ast.setExp(new Application(
				new Variable("fac"), new Constant(5)));
		
		return ast;
	}
	
	
	 
	private static ProfilerState evalProfilerEx1(LeafProfiler profiler) {
		Letrec ast = profilerEx1(profiler); 
		MonitoringLambdaValuation eval = new MonitoringLambdaValuation();
		
		DecoratedElement decoratedNode = new DecoratedElement();
		
		Link link = new Link(new ProfilerAnnotation("#test"), profiler);
		decoratedNode.setLink(link);
		Conditional parentNode = ((Conditional)(ast.getAbstraction().getExp()));
		Expression node  = parentNode.getExp1();
		decoratedNode.setOriginalElement(node);
		// ast binding abstraction exp exp1: decoratedNode.
		parentNode.setExp1((Expression)decoratedNode);
		DenotableValue result = ast.accept(eval);
		System.out.println("RESULT = " + result);
		return (ProfilerState) profiler.getState();
	}
	
	/*
	 * Collector
	 * */
	
	private static Letrec collectorEx1(LeafCollector collector) {
		Letrec ast = new Letrec();
		ast.setIdentifier(new Variable("fac"));
		
		Abstraction facdef = new Abstraction();
		
		Primitive exp1 = new Primitive("x", "=", 0);
		Constant exp2 = new Constant(1);
		Application app = new Application(
				new Variable("fac"), new Primitive("x", "+", -1)
				);
		
		
		Primitive exp3 = new Primitive("x", "*", app);
		
		// Quick-and-dirty
		Link link = new Link(new CollectorAnnotation("#fac"), collector);
		DecoratedElement decoratedNode = new DecoratedElement();
		decoratedNode.setLink(link);
		decoratedNode.setOriginalElement(app);
		//exp3.setRhs(decoratedNode);
		exp3 = new Primitive("x", "*", decoratedNode);
		// End of quick-and-dirty
		
		Conditional cond = new Conditional(exp1, exp2, exp3);
		
		facdef.setArg("x");
		facdef.setExp(cond);

		ast.setAbstraction(facdef);
		ast.setExp(new Application(
				new Variable("fac"), new Constant(5)));
		
		return ast;
	}
	
	
	/*
	 * TRACER
	 * */
	
	private static Letrec tracerEx1() {
		Letrec ast = new Letrec();
		ast.setIdentifier(new Variable("fac"));
		
		Abstraction facdef = new Abstraction();
		
		Primitive exp1 = new Primitive("x", "=", 0);
		Constant exp2 = new Constant(1);
		Application app = new Application(
				new Variable("fac"), 
				new Primitive("x", "+", -1)
				);
		
		
		Primitive exp3 = new Primitive("x", "*", app);
		
		Conditional cond = new Conditional(exp1, exp2, exp3);
		
		facdef.setArg("x");
		facdef.setExp(cond);

		ast.setAbstraction(facdef);
		ast.setExp(new Application(
				new Variable("fac"), new Constant(5)));
		
		return ast;
	}
	
	
	public static Letrec buildNotTailRecursiveFactorial0(int n) {

		Letrec ast = new Letrec(
				new Variable("fact"),
				new Abstraction(
						new Variable("x"),
						new Conditional(
								new Primitive(
										new Variable("="),
										new Variable("x"),
										new Constant(1)),
								new Constant(1),
								new Primitive("x", "*",
										new Application(
												new Variable("fact"),
												new Primitive(
														new Variable("+"),
														new Variable("x"),
														new Constant(-1)))

										))),

				new Application(
						new Variable("fact"),
						new Constant(n))
				);

		Letrec mult_ast = new Letrec(
				new Variable("mult"),
				new Abstraction(
						new Variable("x"),
						new Abstraction(
								new Variable("y"),
								new Primitive(
										new Variable("*"),
										new Variable("x"),
										new Variable("y")))
						),
				ast
				);

		return mult_ast;
	}
	
	public static Letrec buildNotTailRecursiveFactorial(int n) {

		Letrec ast = new Letrec(
				new Variable("fact"),
				new Abstraction(
						new Variable("x"),
						new Conditional(
								new Primitive(
										new Variable("="),
										new Variable("x"),
										new Constant(1)),
								new Constant(1),
								new Application(
										new Application(
												new Variable("mult"),
												new Variable("x")),
										new Application(
												new Variable("fact"),
												new Primitive(
														new Variable("+"),
														new Variable("x"),
														new Constant(-1)))

										))),

				new Application(
						new Variable("fact"),
						new Constant(n))
				);

		Letrec mult_ast = new Letrec(
				new Variable("mult"),
				new Abstraction(
						new Variable("x"),
						new Abstraction(
								new Variable("y"),
								new Primitive(
										new Variable("*"),
										new Variable("x"),
										new Variable("y")))
						),
				ast
				);

		return mult_ast;
	}
	
/*	letrec fac n =
		      if n = 0 then 1 else let r = fac (n - 1) in n * r in
		      fac 3 */
	public static Letrec buildFactorialWithLocalVar(int n) {

		Letrec ast = new Letrec(
				new Variable("fact"),
				new Abstraction(
						new Variable("x"),
						new Conditional(
								new Primitive(
										new Variable("="),
										new Variable("x"),
										new Constant(1)),
								new Constant(1),
								new Letrec(
										new Variable("id"),
										new Abstraction(
												new Variable("ph"),
												new Application(
														new Variable("fact"),
														new Primitive("x", "+", new Constant(-1)))),
										new Primitive("x", "*",
												new Application(
														new Variable("id"),
														new Constant(0))
												)))),

				new Application(
						new Variable("fact"),
						new Constant(n))
				);

			return ast;
	}
	

	
	public static Letrec buildTailRecursiveFactorial(int n) {

		Letrec ast = new Letrec(
				new Variable("fact"),
				new Abstraction(
						new Variable("x"),
						new Abstraction(
								new Variable("acc"),
								new Conditional(
										new Primitive(
												new Variable("="),
												new Variable("x"),
												new Constant(1)),
										new Variable("acc"),
										new Application(
												new Application(
														new Variable("fact"),
														new Primitive(
																new Variable("+"),
																new Variable("x"),
																new Constant(-1))),
												new Application(
														new Application(
																new Variable("mult"),
																new Variable("x")),
														new Variable("acc")))))),
				new Application(
						new Application(
								new Variable("fact"),
								new Constant(n)),
						new Constant(1)));

		Letrec mult_ast = new Letrec(
				new Variable("mult"),
				new Abstraction(
						new Variable("x"),
						new Abstraction(
								new Variable("y"),
								new Primitive(
										new Variable("*"),
										new Variable("x"),
										new Variable("y")))
						),
				ast
				);

		return mult_ast;
	}
	 
	private static TracerState evalTracerEx1(LeafTracer tracer) {
		
		Letrec ast = tracerEx1(); 
		MonitoringLambdaValuation eval = new MonitoringLambdaValuation();
		
		DecoratedElement decoratedNode = new DecoratedElement();
		
		/* Annotate application of fac to x */
		String[] facargs = { "x" };
 		Link link = new Link(new TracerAnnotation("#fac", facargs), tracer);
		decoratedNode.setLink(link);
		Abstraction parentNode = ((Abstraction)(ast.getAbstraction()));
		Expression node  = parentNode.getExp();
		decoratedNode.setOriginalElement(node);
		// ast binding abstraction exp exp1: decoratedNode.
		parentNode.setExp((Expression)decoratedNode);
		DenotableValue result = ast.accept(eval);
		System.out.println("RESULT = " + result);
		return (TracerState) tracer.getState();
	}
	
	private static TracerState evalTracerEx2(LeafTracer tracer) {
		tracer.setState(new TracerState());
		Letrec ast = buildTailRecursiveFactorial(5); 
		MonitoringLambdaValuation eval = new MonitoringLambdaValuation();
		
		DecoratedElement multDecoratedNode = new DecoratedElement();
		DecoratedElement facDecoratedNode = new DecoratedElement();
		
		/* Annotate application of mult to x and y*/
		Link link = new Link(new TracerAnnotation("#mult", new String[] { "x", "y" }), tracer);
		multDecoratedNode.setLink(link);
		Abstraction multParentNode = ((Abstraction)((Abstraction)(ast.getAbstraction())).getExp());
		Expression multNode  = multParentNode.getExp();
		// ast binding abstraction exp exp1: decoratedNode.
		multDecoratedNode.setOriginalElement(multNode);
		multParentNode.setExp((Expression)multDecoratedNode);
			
		/* Annotate application of facc to x and y*/		
		Abstraction facParentNode = ((Abstraction)((Letrec)ast.getExp()).getAbstraction().getExp());
		Expression facNode = facParentNode.getExp();
		
		facDecoratedNode.setLink(new Link(new TracerAnnotation("#fac", new String[] { "x","acc" }), tracer));
		facDecoratedNode.setOriginalElement(facNode);
		facParentNode.setExp(facDecoratedNode);		
		DenotableValue result = ast.accept(eval);
		System.out.println("FOR "+ ast + ", RESULT2 = " + result + " Size = " + eval.getConfiguration().getEnvironmentStack().size());
		return (TracerState) tracer.getState();
	}
	
	private static TracerState evalTracerEx3(LeafTracer tracer) {
		tracer.setState(new TracerState());
		Letrec ast = buildNotTailRecursiveFactorial(4); 
		MonitoringLambdaValuation eval = new MonitoringLambdaValuation();
		
		DecoratedElement multDecoratedNode = new DecoratedElement();
		DecoratedElement facDecoratedNode = new DecoratedElement();
		
		/* Annotate application of mult to x and y*/
		Link link = new Link(new TracerAnnotation("#mult", new String[] { "x", "y" }), tracer);
		multDecoratedNode.setLink(link);
		Abstraction multParentNode = ((Abstraction)((Abstraction)(ast.getAbstraction())).getExp());
		Expression multNode  = multParentNode.getExp();
		// ast binding abstraction exp exp1: decoratedNode.
		multDecoratedNode.setOriginalElement(multNode);
		multParentNode.setExp((Expression)multDecoratedNode);
			
		/* Annotate application of facc to x and y*/	
		Abstraction facParentNode = ((Letrec)ast.getExp()).getAbstraction();
		Expression facNode = facParentNode.getExp();
		
		facDecoratedNode.setLink(new Link(new TracerAnnotation("#fac", new String[] { "x" }), tracer));
		facDecoratedNode.setOriginalElement(facNode);
		facParentNode.setExp(facDecoratedNode);		
		DenotableValue result = ast.accept(eval);
		System.out.println("FOR:\n  "+ ast + "\n, RESULT = " + result + " Size = " + eval.getConfiguration().getEnvironmentStack().size());
		return (TracerState) tracer.getState();
	}
	
	
	private static TracerState evalTracerEx4(LeafTracer tracer) {
		tracer.setState(new TracerState());
		Letrec ast = buildNotTailRecursiveFactorial0(5); 
		MonitoringLambdaValuation eval = new MonitoringLambdaValuation();
//		LambdaValuation eval = new LambdaValuation();
		
		DecoratedElement multDecoratedNode = new DecoratedElement();
		DecoratedElement facDecoratedNode = new DecoratedElement();
		
		/* Annotate application of mult to x and y*/
		Link link = new Link(new TracerAnnotation("#mult", new String[] { "a", "b" }), tracer);
		multDecoratedNode.setLink(link);
		Abstraction multParentNode = ((Abstraction)((Abstraction)(ast.getAbstraction())).getExp());
		Expression multNode  = multParentNode.getExp();
		// ast binding abstraction exp exp1: decoratedNode.
		multDecoratedNode.setOriginalElement(multNode);
		multParentNode.setExp((Expression)multDecoratedNode);
			
		/* Annotate application of facc to x and y*/	
		Abstraction facParentNode = ((Letrec)ast.getExp()).getAbstraction();
		Expression facNode = facParentNode.getExp();
		
		facDecoratedNode.setLink(new Link(new TracerAnnotation("#fac", new String[] { "x" }), tracer));
		facDecoratedNode.setOriginalElement(facNode);
		facParentNode.setExp(facDecoratedNode);		
		DenotableValue result = ast.accept(eval);
		System.out.println("FOR "+ ast + ", RESULT2 = " + result + " Size = " + eval.getConfiguration().getEnvironmentStack().size());
		return (TracerState) tracer.getState();
	}
	
	
	
	private static DebugState evalDebuggerEx1(LeafDebugger debugger) {
		
		Letrec ast = tracerEx1();
		// Annotate all the elements as program point elements
		// This is only for dirty-early-proof-of-concept-prototyping
		DebugAnnotator dAnnotator = new DebugAnnotator(debugger);
		ProgramPointElement dAst = dAnnotator.visit(ast);
		MonitoringLambdaValuation eval = new MonitoringLambdaValuation();
		System.out.println("ANNEx1:" + dAst.toString_withAnn());
		DenotableValue result = dAst.accept(eval);
		System.out.println("RESULT = " + result);
		return (DebugState) debugger.getState();
	}
	
	private static DebugState evalDebuggerEx2(LeafDebugger debugger) {
		
		debugger.setState(new DebugState());
		Letrec ast = buildFactorialWithLocalVar(3);
		DebugAnnotator dAnnotator = new DebugAnnotator(debugger);
		ProgramPointElement dAst = dAnnotator.visit(ast);
		MonitoringLambdaValuation eval = new MonitoringLambdaValuation();
		System.out.println("ANNEx2:" + dAst.toString_withAnn());
		DenotableValue result = dAst.accept(eval);
		System.out.println("RESULT = " + result);
		return (DebugState) debugger.getState();
		
	}
	
	 
	private static CollectorState<DenotableValue> evalCollectorEx1(LeafCollector collector) {
		
		Letrec ast = collectorEx1(collector); 
		MonitoringLambdaValuation eval = new MonitoringLambdaValuation();
		
		DecoratedElement decoratedNode = new DecoratedElement();
		
		Link link = new Link(new CollectorAnnotation("#test"), collector);
		decoratedNode.setLink(link);
		Conditional parentNode = ((Conditional)(ast.getAbstraction().getExp()));
		Expression node  = parentNode.getExp1();
		decoratedNode.setOriginalElement(node);
		// ast binding abstraction exp exp1: decoratedNode.
		parentNode.setExp1((Expression)decoratedNode);
		DenotableValue result = ast.accept(eval);
		System.out.println("RESULT = " + result);
		return (CollectorState<DenotableValue>) collector.getState();
	}
	
	/*
	 * Composite dependency
	 * */
	
	private static Letrec dependencyEx1() {
		Letrec ast = new Letrec();
		ast.setIdentifier(new Variable("fac"));
		
		Abstraction facdef = new Abstraction();
		
		Primitive exp1 = new Primitive("x", "=", 0);
		Constant exp2 = new Constant(1);
		Application app = new Application(
				new Variable("fac"), new Primitive("x", "+", -1)
				);
		
		Primitive exp3 = new Primitive("x", "*", app);
		
		Conditional cond = new Conditional(exp1, exp2, exp3);
		
		facdef.setArg("x");
		facdef.setExp(cond);

		ast.setAbstraction(facdef);
		ast.setExp(new Application(
				new Variable("fac"), new Constant(5)));
		
		return ast;
	}
	
	
	 
	private static CompositeState evalDependencyEx1(CompositeTracerCollector composite) {
		
		Letrec ast = dependencyEx1(); 
		MonitoringLambdaValuation eval = new MonitoringLambdaValuation();
		
		DecoratedElement decoratedNode = new DecoratedElement();
		
		String[] facargs = { "x" };
 		Link link = new Link(new TracerAnnotation("#fac", facargs), composite.getMonitor(0));
		decoratedNode.setLink(link);
		Link linkC = new Link(new CollectorAnnotation("#fac"), composite.getMonitor(1));
		DecoratedElement decoratedNode2 = new DecoratedElement();
		decoratedNode2.setLink(linkC);
		Abstraction parentNode = ((Abstraction)(ast.getAbstraction()));
		Expression node  = parentNode.getExp();
		decoratedNode.setOriginalElement(node);
		// ast binding abstraction exp exp1: decoratedNode.
		parentNode.setExp((Expression)decoratedNode);
		DenotableValue result = ast.accept(eval);
		System.out.println("RESULT = " + result);
		return (CompositeState) composite.getState();
	}
	
	
	public static void main(String[] args) {
/*		LeafProfiler profiler = new LeafProfiler();
		ProfilerState ps = evalProfilerEx1(profiler);
		System.out.println("State from profiling fac 5 = " + ps);
		System.out.println("\n -------------- END PROFILER EX1 --------------");
	
	
		LeafCollector collector = new LeafCollector();
		CollectorState<DenotableValue> cs = evalCollectorEx1(collector);
		System.out.println("State from collecting fac 5 = " + cs);
	
		LeafTracer tracer = new LeafTracer();
		TracerState ts;

		ts = evalTracerEx1(tracer);
		System.out.println("State from tracing fac 5 = \n" + ts);
		
		ts = evalTracerEx2(tracer);
		System.out.println("State from tracing tailfac 5 = \n" + ts);

		ts = evalTracerEx3(tracer);
		System.out.println("State from tracing not tailfac 5 with mult = \n" + ts); 
		System.out.println("\n -------------- END TRACER EX2 -----------------");

		ts = evalTracerEx4(tracer);
		System.out.println("State from tracing not tailfac 5 with * = \n" + ts); 
		System.out.println("\n -------------- END TRACER EX3 -----------------");

			
		CompositeTracerCollector composite = new CompositeTracerCollector();
		CompositeState ds = evalDependencyEx1(composite);
		System.out.println("State from collecting and tracing fac 5 = \n" + ds);	
		System.out.println("\n -------------- END TRACER/COLLECTOR EX4 ---------");

*/		
		LeafDebugger debugger = new LeafDebugger();
		DebugState debugState;
		
/*		debugState = evalDebuggerEx1((LeafDebugger)debugger);
		System.out.println("Final state = " + debugState);
*/		
		debugState = evalDebuggerEx2((LeafDebugger)debugger);
		System.out.println("Final state = " + debugState);
	}

}
