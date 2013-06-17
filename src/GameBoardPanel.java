import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Font;
import java.util.ArrayList;

/**
 * 
 * The "board" of the risk game. Is called from the mainmenu
 * upon user click of the StartButton
 * @author Michael Zhang
 * @author Akhil Velagapudi
 *
 * @see GuiFrame
 * @see MainMenuPanel
 * @see Engine
 * @see Country
 * 
 */
@SuppressWarnings("serial")
public class GameBoardPanel extends JPanel{
	private String mapImgDir; // String directory of where the map image is located
	private Image mapImg; // Stores the map image
	
	private final Dimension BGSIZE; // The size of the map
	protected JLabel instructionLabel;
	public static final String sPREGAME = "Click the country you would like to take!.",
							   sREINFORCE = "Click the country you would like to reinforce.",
							   sRECRUIT = "Click the countries to place your reinforcements!",
							   sATTACK_A = "<html>Click the country you would like to attack from.<br>Click \"Turn Done\" if finished.</html>",
							   sATTACK_B = "Click the country you would like to Conquer!",
							   sOCCUPY = "<html>Continue clicking the conquered territory to send troops.<br>Click \"Turn Done\" if finished</html>",
							   sFORTIFY = "Click the country you would like to send troops from.",
							   sFORTIFY_B = "Click the country you would like to send troops to (must be a neighbor)",
							   sFORTIFY_C = "<html>Continue clicking the receiving country to send more troops.<br>Click \"Turn Done\" when finished</html>";
							   
	
	public GameBoardPanel(ArrayList<Country> cArray) {
		Insets insets = this.getInsets();
		instructionLabel = new JLabel();
		instructionLabel.setBounds(insets.left + 375, insets.top - 10, 
									500, 100);
		instructionLabel.setFont(new Font("Serif", Font.BOLD, 14));
		instructionLabel.setForeground(Color.white);
		instructionLabel.setText(sPREGAME);
		
		// Set the size of the gameboard
		BGSIZE = new Dimension(1179, 700);
		
		// initialize the engine
		
		// set the size of the gameboard
		setPreferredSize(BGSIZE);
		setMaximumSize(BGSIZE);
		setMinimumSize(BGSIZE);
		setSize(BGSIZE);
		
		// set the layout of the gameboard to null layout
		setLayout(null);
		
		// Insert the map image into the mapImg variable
		// The map is drawn onto the gameboard in the paintcomponent() method
		mapImgDir = "resources/map.jpg";
		mapImg = new ImageIcon(mapImgDir).getImage();
		
		// Creates the map object that contains the corresponding countries associated
		// with x, y coordinates on map.jpg
		
		for (Country a: cArray)
			add(a);
		
		this.add(instructionLabel);
		//  Click Coordinate finder, for usage with country buttons
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
