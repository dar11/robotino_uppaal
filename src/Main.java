

import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import com.google.common.base.Preconditions;



public class Main {
	//Main
	private static DriveWidget widget = new DriveWidget();
	private static Checker checker = new Checker();
	private static DummyRobots dummies = new DummyRobots();
	
	public static void main(String[] args) {
//		LaunchRobotino launch = new LaunchRobotino("Launch robot");
//		Thread thread = new Thread(launch);
//		thread.setDaemon(true);
//		thread.start();
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		NodeMainExecutor nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
		
		NodeConfiguration widgetConfig = NodeConfiguration.newPublic("10.0.2.15");
		Preconditions.checkState(widget != null && checker != null && dummies != null);
		nodeMainExecutor.execute(dummies, widgetConfig);
		nodeMainExecutor.execute(widget, widgetConfig);
		nodeMainExecutor.execute(checker, widgetConfig);
	}
}
