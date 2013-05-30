import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
 
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
	private JPanel mainPanel;
	private JPanel sidePanel;
	private String MENUPANEL = "Menu";
	private String GAMEPANEL = "Game";
	private ImageIcon icon = new ImageIcon("resources/iconimage.png");
	private Dimension frameSize;
	
	public GuiFrame() {
		makeMenu();
		//this.setBackground(Color.white);
		frameSize = new Dimension(1179, 700);
		setTitle("iLNibor's RISK");
		setIconImage(icon.getImage());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(frameSize);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(new BorderLayout());
		cards = new JPanel(new CardLayout());
		
		mainMenu = new MainMenuPanel();
		gameBoard = new GameBoardPanel();
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setMinimumSize(frameSize);
		mainPanel.setMaximumSize(frameSize);
		mainPanel.setPreferredSize(frameSize);
		mainPanel.add(mainMenu, BorderLayout.WEST);
		
		sidePanel = new JPanel(new BorderLayout());
		JLabel title = new JLabel("<html>" +
									"<font size = \"20\" color = \"black\"><u>________</u></font><br><br><br><br>" +
									"<div style = \"color:red\">" +
									"<h1><font size = \"5\" face = \"georgia\">Team iLNibor</font></h1></div>" +
									"<p face = \"verdana\"><font size = \"5\" face = \"georgia\">" +
									"Mike Zhang" +
									"<br>Akhil Velegapudi" +
									"<br>Sid Senthilkumar</font><br>" +
									"And of course..." +
									"<font size = \"20\" color = \"black\" face = \"georgia\"><br><u><i>Robin Li</i></u></font></p>" +
									//"<p style = \"color:gray\">" +
									"<font size = \"20\" color = \"black\"><u>________</u></font>" + 
									//"<font size = \"5\">Motto:<br><i>\"For Great Justice:<br>&nbsp;Take off every Zig\"</i></font></p>" +
								  "</html>");
		sidePanel.setBackground(Color.white);
		sidePanel.add(title, BorderLayout.WEST);
		mainPanel.add(sidePanel);
		
		cards.add(mainPanel, MENUPANEL);
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
