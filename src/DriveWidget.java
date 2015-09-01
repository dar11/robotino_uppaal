
import org.apache.commons.logging.Log;
import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;

import geometry_msgs.Twist;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class DriveWidget extends JComponent implements NodeMain {
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



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
		final Publisher<geometry_msgs.Twist> publisher_robot_1 = connectedNode.newPublisher("cmd_vel_1", geometry_msgs.Twist._TYPE);
		final Publisher<geometry_msgs.Twist> publisher_robot_2 = connectedNode.newPublisher("cmd_vel_2", geometry_msgs.Twist._TYPE);
		final Publisher<geometry_msgs.Twist> publisher_robot_3 = connectedNode.newPublisher("cmd_vel_3", geometry_msgs.Twist._TYPE);
		final Publisher<geometry_msgs.Twist> publisher_robot_4 = connectedNode.newPublisher("cmd_vel_4", geometry_msgs.Twist._TYPE);
		final Publisher<geometry_msgs.Twist> publisher_robot_5 = connectedNode.newPublisher("cmd_vel_5", geometry_msgs.Twist._TYPE);
		final Log log = connectedNode.getLog();
		log.info("Hallo!");
		createGUI(publisher_robot_1);
		
		connectedNode.executeCancellableLoop(new CancellableLoop() {
			
			@Override
			protected void loop() throws InterruptedException {
				Twist cmd = publisher_robot_2.newMessage();
				cmd.getLinear().setX(1);
				cmd.getLinear().setY(0);
				cmd.getAngular().setZ(0);
				publisher_robot_2.publish(cmd);
			}
		});
	}
	

	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("driveWidget");
	}
	
	
	public void createGUI(final Publisher<geometry_msgs.Twist> publisher) {
		JButton buttonUp = new JButton(getIcon("n"));
		JButton buttonDown = new JButton(getIcon("s"));
		JButton buttonLeft = new JButton(getIcon("w"));
		JButton buttonRight = new JButton(getIcon("o"));
		JButton buttinStop = new JButton(getIcon("stop"));
		JButton buttonTurnRight = new JButton(getIcon("cl"));
		JButton buttonTurnLeft = new JButton(getIcon("ccl"));
		
		buttonRight.addActionListener(new ButtonListener(publisher,1,0 ,0));
		buttonLeft.addActionListener(new ButtonListener(publisher, -1, 0, 0));
		buttonUp.addActionListener(new ButtonListener(publisher, 0, 1, 0));
		buttonDown.addActionListener(new ButtonListener(publisher, 0, -1, 0));
		buttonTurnLeft.addActionListener(new ButtonListener(publisher, 0, 0, 1));
		buttonTurnRight.addActionListener(new ButtonListener(publisher, 0, 0, -1));
		
		JFrame frame = new JFrame("Robotino Steuerung");
		Container container = frame.getContentPane();
		container.setLayout(new GridLayout(3, 3));
		
		container.add(buttonTurnLeft);
		container.add(buttonUp);
		container.add(buttonTurnRight);
		container.add(buttonLeft);
		container.add(buttinStop);
		container.add(buttonRight);
		container.add(new JLabel());
		container.add(buttonDown);
		
		frame.setSize(600, 400);
		frame.setVisible(true);
	}
	
	private Icon getIcon(String name) {
        return new ImageIcon(getClass().getResource("icons/" + name + ".png"));
}

	

	
	private class ButtonListener implements ActionListener {
		private final Publisher<geometry_msgs.Twist> publisher;

		private final float x_vel;
		private final float y_vel;
		private final float omega;
		
		public ButtonListener(Publisher<geometry_msgs.Twist> publisher, float x, float y, float omega) {
			this.publisher = publisher;
			this.x_vel = x;
			this.y_vel = y;
			this.omega = omega;
		}
		

		@Override
		// Publish the velocity message.
		public void actionPerformed(ActionEvent e) {
			Twist cmd = publisher.newMessage();
			cmd.getLinear().setX(x_vel);
			cmd.getLinear().setY(y_vel);
			cmd.getAngular().setZ(omega);
			publisher.publish(cmd);
			
		}
		
	}
}
