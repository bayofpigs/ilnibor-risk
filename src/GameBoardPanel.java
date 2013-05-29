import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Color;

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
		this.setForeground(Color.gray);
		this.setOpaque(true);
		
		mapImgDir = "resources/map.jpg";
		mapImg = (new ImageIcon(mapImgDir)).getImage();
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, BGSIZE.width, BGSIZE.height);
		g.drawImage(mapImg, 15, 0, null);
	}
}