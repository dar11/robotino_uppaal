

import geometry_msgs.Point32;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Subscriber;

import sensor_msgs.PointCloud;

import com.uppaal.engine.Engine;
import com.uppaal.engine.EngineException;
import com.uppaal.engine.EngineStub;


public class Checker implements NodeMain {
	
	public static int actualDistance = 0;

	@Override
	public void onError(Node arg0, Throwable arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onShutdown(Node arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onShutdownComplete(Node arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart(ConnectedNode connectedNode) {
		Subscriber<sensor_msgs.PointCloud> subscriber = connectedNode.newSubscriber("/distance_sensors", sensor_msgs.PointCloud._TYPE);
		final Engine engine = connectToEngine();
		final ModelDemo demo = new ModelDemo(engine, 100 );
		demo.start();
	    subscriber.addMessageListener(new MessageListener<sensor_msgs.PointCloud>() {
			@Override
			public void onNewMessage(PointCloud message) {
				List<Point32> points = message.getPoints();
				Point32 point = points.get(1);
				double distance = point.getX()/Math.cos(0.698 * 1) - 0.2;
				int dist = (int) (distance * 100);
				//log.info("Distance: " + distance + "\"");
				if (dist != actualDistance) {
					System.out.println("Distanz ist " + dist);
					demo.check(dist);
				}
				actualDistance = dist;
			}
	    });
	  }
		
	
	public void modelCheck(Engine engine, int distance, ExecutorService executor) {
//		try {
//			Thread.sleep(700);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		System.out.println("Distanz ist " + distance);
		Runnable worker = new Thread(new ModelDemo(engine, distance));
		executor.execute(worker);
		
	}
	
	public Engine connectToEngine() {
		String path = null;
		path = "/home/daniel/uppaal64-4.1.19/bin-Linux/server";
		System.out.println(path);
		Engine engine = new Engine();
		engine.setServerPath(path);
		engine.setServerHost("localhost");
		engine.setConnectionMode(EngineStub.BOTH);
		try {
			engine.connect();
		} catch (EngineException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return engine;
	}

	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("checker");
	}

}
