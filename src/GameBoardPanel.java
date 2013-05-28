import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.Image;

public class GameBoardPanel extends JPanel{
	private String mapImgDir;
	private Image mapImg;
	
	// The size of the map
	private final Dimension BGSIZE = new Dimension(1179, 700); 
	
	public GameBoardPanel() {
		setPreferredSize(BGSIZE);
		setMaximumSize(BGSIZE);
		setMinimumSize(BGSIZE);
		setSize(BGSIZE);
		setLayout(null);
		
		mapImgDir = "resources/map.png";
		mapImg = (new ImageIcon(mapImgDir)).getImage();
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(mapImg, 5, 0, null);
	}
}
