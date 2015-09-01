

import java.awt.*;
import java.awt.Toolkit;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;



public class MainFrame extends JFrame {
	
	protected final DriveWidget driveComponent;
	protected final JPanel centerPanel;
	
	public MainFrame() {
		driveComponent = new DriveWidget();
		
		centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
		centerPanel.add(driveComponent);
		
		Container content = getContentPane();
		content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));
		content.add(centerPanel);
		
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension guiSize = new Dimension(640, 480);
		setSize(guiSize);
		setLocation((d.width - guiSize.width) / 2, (d.height - guiSize.height) / 2);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setVisible(true);
	}

}
