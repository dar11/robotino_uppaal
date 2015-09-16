

/**
 * A sample Java code demonstrating the use of lib/model.jar.
 * The source code assumes that ModelDemo.java is inside Uppaal distribution.
 * Use the following commands to compile and run:
 *
 *     javac -cp lib/model.jar ModelDemo.java
 *     java -cp uppaal.jar:lib/model.jar:. ModelDemo hardcoded
 *
 * ModelDemo will produce result.xml and save a random trace into result.xtr.
 * ModelDemo can also read an external model file (use Control+C to stop):
 *
 *     java -cp uppaal.jar:lib/model.jar:. ModelDemo demo/train-gate.xml
 *
 * @author Marius Mikucionis marius@cs.aau.dk
 */

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import com.uppaal.engine.CannotEvaluateException;
import com.uppaal.engine.Engine;
import com.uppaal.engine.EngineException;
import com.uppaal.engine.Problem;
import com.uppaal.engine.QueryVerificationResult;
import com.uppaal.model.core2.Document;
import com.uppaal.model.core2.Edge;
import com.uppaal.model.core2.Location;
import com.uppaal.model.core2.Property;
import com.uppaal.model.core2.PrototypeDocument;
import com.uppaal.model.core2.Template;
import com.uppaal.model.system.SystemEdge;
import com.uppaal.model.system.SystemLocation;
import com.uppaal.model.system.SystemState;
import com.uppaal.model.system.UppaalSystem;
import com.uppaal.model.system.symbolic.SymbolicState;
import com.uppaal.model.system.symbolic.SymbolicTransition;

public class ModelDemo implements Runnable {

	private Engine engine;
	private int distance;
	public Document doc;
	public UppaalSystem sys;
	private Thread t;

	public ModelDemo(Engine engine, int distance) {
		this.distance = distance;
		this.engine = engine;
	}

	/**
	 * Sets a label on a location.
	 * 
	 * @param l
	 *            the location on which the label is going to be attached
	 * @param kind
	 *            a kind of the label
	 * @param value
	 *            the label value (either boolean or String)
	 * @param x
	 *            the x coordinate of the label
	 * @param y
	 *            the y coordinate of the label
	 */
	public void setLabel(Location l, LKind kind, Object value, int x, int y) {
		l.setProperty(kind.name(), value);
		Property p = l.getProperty(kind.name());
		p.setProperty("x", x);
		p.setProperty("y", y);
	}

	/**
	 * Adds a location to a template.
	 * 
	 * @param t
	 *            the template
	 * @param name
	 *            a name for the new location
	 * @param x
	 *            the x coordinate of the location
	 * @param y
	 *            the y coordinate of the location
	 * @return the new location instance
	 */
	public Location addLocation(Template t, String name, String exprate, int x,
			int y) {
		Location l = t.createLocation();
		t.insert(l, null);
		l.setProperty("x", x);
		l.setProperty("y", y);
		if (name != null)
			setLabel(l, LKind.name, name, x, y - 28);
		if (exprate != null)
			setLabel(l, LKind.exponentialrate, exprate, x, y - 28 - 12);
		return l;
	}

	/**
	 * Sets a label on an edge.
	 * 
	 * @param e
	 *            the edge
	 * @param kind
	 *            the kind of the label
	 * @param value
	 *            the content of the label
	 * @param x
	 *            the x coordinate of the label
	 * @param y
	 *            the y coordinate of the label
	 */
	public void setLabel(Edge e, EKind kind, String value, int x, int y) {
		e.setProperty(kind.name(), value);
		Property p = e.getProperty(kind.name());
		p.setProperty("x", x);
		p.setProperty("y", y);
	}

	/**
	 * Adds an edge to the template
	 * 
	 * @param t
	 *            the template where the edge belongs
	 * @param source
	 *            the source location
	 * @param target
	 *            the target location
	 * @param guard
	 *            guard expression
	 * @param sync
	 *            synchronization expression
	 * @param update
	 *            update expression
	 * @return
	 */
	public Edge addEdge(Template t, Location source, Location target,
			String guard, String sync, String update) {
		Edge e = t.createEdge();
		t.insert(e, null);
		e.setSource(source);
		e.setTarget(target);
		int x = (source.getX() + target.getX()) / 2;
		int y = (source.getY() + target.getY()) / 2;
		if (guard != null) {
			setLabel(e, EKind.guard, guard, x - 15, y - 28);
		}
		if (sync != null) {
			setLabel(e, EKind.synchronisation, sync, x - 15, y - 14);
		}
		if (update != null) {
			setLabel(e, EKind.assignment, update, x - 15, y);
		}
		return e;
	}

	public void print(UppaalSystem sys, SystemState s) {
		System.out.print("(");
		for (SystemLocation l : s.getLocations()) {
			System.out.print(l.getName() + ", ");
		}
		ArrayList<String> val = sys.getVariables();
		for (int i = 0; i < sys.getNoOfVariables(); i++) {
			System.out.print(sys.getVariableName(i) + "=" + val.get(i) + ", ");
		}
		// s.getPolyhedron().getAllConstraints(constraints);
		// for (String cs : constraints) {
		// System.out.print(cs + ", ");
		// }
		System.out.println(")");
	}

	public Document createSampleModel() {
		// create a new Uppaal model with default properties:
		Document doc = new Document(new PrototypeDocument());
		// add global variables:
		doc.setProperty("declaration", "chan r, o" + ";");
		// add a TA template:
		Template robot = doc.createTemplate();
		doc.insert(robot, null);
		robot.setProperty("name", "Robot");
		robot.setProperty("declaration", "int x = 0, y = 0;\n\n" + "int v = 0;");
		// the template has initial location:
		Location idle = addLocation(robot, "Idle", null, 0, 0);
		idle.setProperty("init", true);
		addEdge(robot, idle, idle, null, "r?", null);
		
		Template coord = doc.createTemplate();
		doc.insert(coord, null);
		coord.setProperty("name", "Coordinator");
		Location moveObject = addLocation(coord, "MoveObject", null, 0, 0);
		moveObject.setProperty("init", true);
		Location moveRobot = addLocation(coord, "MoveRobot", null, 0, 10);
		addEdge(coord, moveObject, moveRobot, null, "r!", null);
		addEdge(coord, moveRobot, moveObject, null, "o!", null);
		
		Template obstacle = doc.createTemplate();
		doc.insert(obstacle, null);
		doc.setProperty("name", "Obstacle");
		doc.setProperty("declaration", "int x = 10, y = 0;\n" + "int v = 5;");
		Location obstacleIdle = addLocation(obstacle, "idle", null, 0, 0);
		obstacleIdle.setProperty("init", true);
		Location moving = addLocation(obstacle, "Moving", null, 0, 10);
		addEdge(obstacle, idle, idle, "x<=0", "o?", null);
		addEdge(obstacle, idle, moving, "x>0", "o?", "x=x-1");
		addEdge(obstacle, moving, moving, "x>0", "o?", "x=x-1");
		addEdge(obstacle, moving, idle, "x<=0", "o?", null);
		
		

		// add system declaration:
		doc.setProperty("system", "R = Robot();\n" + "C = Coordinator();\n" + "O = Obstacle();\n\n" + "system R, C, O;");
		return doc;
	}

	public static Document loadModel(String location) throws IOException {
		try {
			// try URL scheme (useful to fetch from Internet):
			return new PrototypeDocument().load(new URL(location));
		} catch (MalformedURLException ex) {
			// not URL, retry as it were a local filepath:
			return new PrototypeDocument()
					.load(new URL("file", null, location));
		}
	}

	public UppaalSystem compile(Engine engine, Document doc)
			throws EngineException, IOException {
		// compile the model into system:
		ArrayList<Problem> problems = new ArrayList<Problem>();
		UppaalSystem sys = engine.getSystem(doc, problems);
		if (!problems.isEmpty()) {
			boolean fatal = false;
			System.out.println("There are problems with the document:");
			for (Problem p : problems) {
				System.out.println(p.toString());
				System.out.println(p.getLocation());
				if (!"warning".equals(p.getType())) { // ignore warnings
					fatal = true;
				}
			}
			if (fatal) {
				System.exit(1);
			}
		}
		return sys;
	}

	public ArrayList<SymbolicTransition> symbolicSimulation(Engine engine,
			UppaalSystem sys) throws EngineException, IOException {
		ArrayList<SymbolicTransition> trace = new ArrayList<SymbolicTransition>();
		// compute the initial state:
		SymbolicState state = engine.getInitialState(sys);
		// add the initial transition to the trace:
		trace.add(new SymbolicTransition(null, null, state));
		while (state != null) {
			print(sys, state);
			// compute the successors (including "deadlock"):
			ArrayList<SymbolicTransition> trans = null;
			try {
				trans = engine.getTransitions(sys, state);
			} catch (CannotEvaluateException e1) {
				e1.printStackTrace();
			}
			// select a random transition:
			int n = (int) Math.floor(Math.random() * trans.size());
			SymbolicTransition tr = trans.get(n);
			// check the number of edges involved:
			if (tr.getSize() == 0) {
				// no edges, something special (like "deadlock"):
				System.out.println(tr.getEdgeDescription());
			} else {
				// one or more edges involved, print them:
				for (SystemEdge e : tr.getEdges()) {
					System.out.print(e.getProcessName() + ": "
							+ e.getEdge().getSource().getPropertyValue("name")
							+ " \u2192 "
							+ e.getEdge().getTarget().getPropertyValue("name")
							+ ", ");
				}
			}
			// jump to a successor state (null in case of deadlock):
			state = tr.getTarget();
			// if successfull, add the transition to the trace:
			if (state != null)
				trace.add(tr);
		}
		return trace;
	}
	
	public void check(int distance) {
		try {
		doc.setProperty("declaration", "int distance =" + distance + ";");
		doc.save(new File("/home/daniel/rosjava_workspace/src/robotino/run_robotino/result.xml"));
		
		sys = compile(engine, doc);

		// perform a random symbolic simulation and get a trace:
		ArrayList<SymbolicTransition> trace = symbolicSimulation(engine,
				sys);
		System.out.println(trace.toString());
		QueryFeedbackCustomized qf = new QueryFeedbackCustomized();

		String query = "E<> Exp1.L1";
		QueryVerificationResult r;
		
			r = engine.query(sys, options, query, qf);
		
		if (r.result == 'T') {
			System.out.println("True: There is a Path to L1");
		} else {
			System.out.println("False: There is no Path to L1");
		}
		System.out
				.println("===============================================");
		} catch (EngineException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	} 


	@Override
	public void run() {
		

			doc = createSampleModel();
			if (doc == null) {
				System.out.println("Doc ist null");
			} else {
				System.out.println("Doc ist nicht null");
			}
			try {
				doc.save(new File("/home/daniel/rosjava_workspace/src/robotino/run_robotino/dointNothing.xml"));
			
			// create a link to a local Uppaal process:
			sys = compile(engine, doc);

			// perform a random symbolic simulation and get a trace:
//			ArrayList<SymbolicTransition> trace = symbolicSimulation(engine,
//					sys);
//			System.out.println(trace.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (EngineException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// save the trace to an XTR file:
			// saveXTRFile(trace, "result.xtr");

			// simple model-checking:
			
	}
	
	public void start() {
		System.out.println("Starting ");
		if (t == null) {
			t = new Thread(this);
			t.start();
		}
	}

	public static final String options = "order 0\n"
			+ "reduction 1\n"
			+ "representation 0\n"
			+ "trace 0\n"
			+ "extrapolation 0\n"
			+ "hashsize 27\n"
			+ "reuse 1\n"
			+ "smcparametric 1\n"
			+ "modest 0\n"
			+ "statistical 0.01 0.01 0.05 0.05 0.05 0.9 1.1 0.0 0.0 1280.0 0.01";

}
