package com.github.rosjava.robotino.run_robotino;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import geometry_msgs.Twist;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher; 


public class Test extends AbstractNodeMain {

	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("rosjava/test");
	}
	
	public void onStart(final ConnectedNode connectedNode) {
		final Publisher<geometry_msgs.Twist> publisher = 
				connectedNode.newPublisher("cmd_vel", geometry_msgs.Twist._TYPE);
		//MainFrame frame = new MainFrame();
		//createGUI(publisher);
		DriveWidget drive = new DriveWidget();
		connectedNode.executeCancellableLoop(new CancellableLoop() {
			
			@Override
			protected void loop() throws InterruptedException {
//				geometry_msgs.Twist vel = publisher.newMessage();
//				Twist cmd = publisher.newMessage();
//				cmd.getLinear().setX(1);
//				publisher.publish(cmd);	
			}
		});
	}
	
	
		
		
		
}
	
	


