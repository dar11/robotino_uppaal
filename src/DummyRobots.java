import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;

import geometry_msgs.Twist;

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
	public void onStart(ConnectedNode connectedNode) {
		final Publisher<geometry_msgs.Twist> publisher_robot_2 = connectedNode.newPublisher("cmd_vel_2", geometry_msgs.Twist._TYPE);
		final Publisher<geometry_msgs.Twist> publisher_robot_3 = connectedNode.newPublisher("cmd_vel_3", geometry_msgs.Twist._TYPE);
		final Publisher<geometry_msgs.Twist> publisher_robot_4 = connectedNode.newPublisher("cmd_vel_4", geometry_msgs.Twist._TYPE);
		final Publisher<geometry_msgs.Twist> publisher_robot_5 = connectedNode.newPublisher("cmd_vel_5", geometry_msgs.Twist._TYPE);
		
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
