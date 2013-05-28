import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JButton;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
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
	private GameBoardPanel gameBoard;
	private String MENUPANEL = "Menu";
	private String GAMEPANEL = "Game";
	
	public GuiFrame() {
		makeMenu();
		this.setBackground(Color.white);
		setTitle("iLNibor's RISK");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1179, 700);
		setLocationRelativeTo(null);
		setResizable(false);
		cards = new JPanel(new CardLayout());
		
		mainMenu = new MainMenuPanel();
		gameBoard = new GameBoardPanel();
		cards.add(mainMenu, MENUPANEL);
		cards.add(gameBoard, GAMEPANEL);
		add(cards);
		
		setupMainMenuListener();
	}
	
	public void setupMainMenuListener() {
		JButton start = mainMenu.getStartButton();
		JButton exit = mainMenu.getExitButton();
		start.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					CardLayout cl = (CardLayout)cards.getLayout();
					cl.show(cards, GAMEPANEL);
				}
			}		
		);
		
		exit.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					exitApp();
				}
			}
		);
				
	}
	
	public void exitApp() {
		this.dispose();
	}
	
	public void setupGameListener() {
		
	}
	
	public void makeMenu() {
		
	}
	
	public static void main(String[] args) {
		GuiFrame g = new GuiFrame();
		g.setVisible(true);
	}
}
