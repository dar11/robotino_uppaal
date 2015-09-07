import java.util.List;

import org.ros.concurrent.CancellableLoop;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;

import geometry_msgs.Point32;
import geometry_msgs.Twist;
import sensor_msgs.PointCloud;

public class DummyRobots implements NodeMain {

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
	public void onStart(final ConnectedNode connectedNode) {
		final Publisher<geometry_msgs.Twist> publisher_robot_2 = connectedNode.newPublisher("cmd_vel_2", geometry_msgs.Twist._TYPE);
		final Publisher<geometry_msgs.Twist> publisher_robot_3 = connectedNode.newPublisher("cmd_vel_3", geometry_msgs.Twist._TYPE);
		final Publisher<geometry_msgs.Twist> publisher_robot_4 = connectedNode.newPublisher("cmd_vel_4", geometry_msgs.Twist._TYPE);
		final Publisher<geometry_msgs.Twist> publisher_robot_5 = connectedNode.newPublisher("cmd_vel_5", geometry_msgs.Twist._TYPE);
		final Subscriber<sensor_msgs.PointCloud> sensor_2 = connectedNode.newSubscriber("/distance_sensors_2", sensor_msgs.PointCloud._TYPE);
		final Subscriber<sensor_msgs.PointCloud> sensor_3 = connectedNode.newSubscriber("/distance_sensors_3", sensor_msgs.PointCloud._TYPE);
		final Subscriber<sensor_msgs.PointCloud> sensor_4 = connectedNode.newSubscriber("/distance_sensors_4", sensor_msgs.PointCloud._TYPE);
		final Subscriber<sensor_msgs.PointCloud> sensor_5 = connectedNode.newSubscriber("/distance_sensors_5", sensor_msgs.PointCloud._TYPE);
	
		MessageListener<sensor_msgs.PointCloud> listener = new MessageListener<PointCloud>() {

			@Override
			public void onNewMessage(PointCloud message) {
				List<Point32> points = message.getPoints();
				Point32 point = points.get(1);
				double distance = point.getX()/Math.cos(0.698 * 1) - 0.2;
				if (distance < 0.4) {
					connectedNode.shutdown();
				}
				
			}
		};
		sensor_2.addMessageListener(listener);
		sensor_3.addMessageListener(listener);
		sensor_4.addMessageListener(listener);
		sensor_5.addMessageListener(listener);
		connectedNode.executeCancellableLoop(new CancellableLoop() {
			
			@Override
			protected void loop() throws InterruptedException {
				Twist cmd = publisher_robot_2.newMessage();
				cmd.getLinear().setX(1);
				cmd.getLinear().setY(0);
				cmd.getAngular().setZ(0);
				publisher_robot_2.publish(cmd);
				publisher_robot_3.publish(cmd);
				publisher_robot_4.publish(cmd);
				publisher_robot_5.publish(cmd);
			}
		});
	}


	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("dummyRobots");
	}

}
