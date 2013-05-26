import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.CardLayout;

/**
 * TODO:
 * Implement GameBoardPanel
 * Implement buttons on the mainmenu and have the buttons
 * 	transition the layout to the actual gameboard
 * 
 * @see documentation on cardLayout 
 * 	(here: http://docs.oracle.com/javase/tutorial/uiswing/layout/card.html)
 * @author Michael Zhang
 *
 */
public class GuiFrame extends JFrame {
	private JPanel cards;
	private MainMenuPanel mainMenu;
	//private GameBoardPanel gameBoard;
	private String MENUPANEL = "Menu";
	private String GAMEPANEL = "Game";
	
	public GuiFrame() {
		
		
		setTitle("iLNibor's RISK");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(900, 700);
		setLocationRelativeTo(null);
		setResizable(false);
		cards = new JPanel(new CardLayout());
		
		mainMenu = new MainMenuPanel();
		cards.add(mainMenu, MENUPANEL);
		
		add(cards);
	}
	
	public static void main(String[] args) {
		GuiFrame g = new GuiFrame();
		g.setVisible(true);
	}
}
