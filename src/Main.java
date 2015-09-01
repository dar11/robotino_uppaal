

import javax.swing.UIManager;

import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import com.google.common.base.Preconditions;
import com.uppaal.engine.Engine;



public class Main {
	//Main
	private static DriveWidget widget = new DriveWidget();
	private static Checker checker = new Checker();
	
	public static void main(String[] args) {
		LaunchRobotino launch = new LaunchRobotino("Launch robot");
		launch.start();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		NodeMainExecutor nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
		
		NodeConfiguration widgetConfig = NodeConfiguration.newPublic("10.0.2.15");
		Preconditions.checkState(widget != null && checker != null);
		nodeMainExecutor.execute(widget, widgetConfig);
		nodeMainExecutor.execute(checker, widgetConfig);
	}
}
