package com.github.rosjava.robotino.run_robotino;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

public class LaunchRobotino implements Runnable {
	
	private Thread t;
	private String threadName;
	
	LaunchRobotino(String name) {
		threadName = name;
		System.out.println("Creating " + threadName);
	}

	@Override
	public void run() {
		StringBuffer output = new StringBuffer();
		Process p;
		String command = "";
		Object[] options = {"One robot", "Two robots", "Multiple Robots"};
		int option = JOptionPane.showOptionDialog(null, "How many robots do you want to connect?", "Launch", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]) ;
		switch (option) {
		case 0: command = "roslaunch robotino_node robotino_node.launch";
			break;
		case 1: command = "roslaunch robotino_node robotino_two_nodes.launch";
			break;
		case 2: command = "roslaunch robotino_node robotino_lanes.launch";
			break;
		}
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(output);
	}
	
	public void start() {
		System.out.println("Starting " + threadName);
		if (t == null) {
			t = new Thread(this, threadName);
			t.start();
		}
	}

}
