import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.Color;
import java.util.ArrayList;

/**
 * 
 * The "board" of the risk game. Is called from the mainmenu
 * upon user click of the StartButton
 * @author Michael Zhang
 * @author Akhil Velagapudi
 *
 * @see GuiFrame
 * @see MainMenu
 * @see Engine
 * @see Country
 * 
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel{

	private final Dimension BGSIZE; // The size of the map
	private String mapImgDir; // String directory of where the map image is located
	private Image mapImg; // Stores the map image
							   
	
	public GameBoard(ArrayList<Country> cArray) {
		
		// Set the size of the gameboard
		BGSIZE = new Dimension(1179, 700);
				
		// set the size of the gameboard
		setPreferredSize(BGSIZE);
		setMaximumSize(BGSIZE);
		setMinimumSize(BGSIZE);
		setSize(BGSIZE);
		
		// set the layout of the gameboard to null layout
		setLayout(null);
		
		// Insert the map image into the mapImg variable
		// The map is drawn onto the gameboard in the paintComponent() method
		mapImgDir = "resources/map.jpg";
		mapImg = new ImageIcon(mapImgDir).getImage();
		
		// Creates the map object that contains the corresponding countries associated
		// with x, y coordinates on map.jpg
		
		for (Country a: cArray)
			add(a);
	}
	
	/**
	 * Override of the default JPanel paintComponent method to 
	 * paint the map onto the screen
	 */
	public void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, BGSIZE.width, BGSIZE.height);
		g.drawImage(mapImg, 15, 0, null);
	}
}
