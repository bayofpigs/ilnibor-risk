import javax.swing.JPanel;
import javax.swing.ImageIcon;

import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Dimension;


public class MainMenuPanel extends JPanel {
	private Image bg;
	private String filedir;
	
	public MainMenuPanel() {
		Dimension bgsize = new Dimension(900, 700);
		setPreferredSize(bgsize);
		setMinimumSize(bgsize);
		setMaximumSize(bgsize);
		filedir = "resources/bgimage.png";
		bg = new ImageIcon(filedir).getImage();
		setLayout(new GridLayout());
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(bg, 0, 0, null);
	}
}
