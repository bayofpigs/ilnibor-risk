import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

/**
 * Frame used to display useful messages.
 * Can be edited later to add a chat functionality if over-the-Internet implementation finished
 * @author That Kid Named Sid
 */
public class MessageLogFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final String lineMarker = "\n_________________________\n";
	private ImageIcon icon; // The icon of the game on the title bar of the program
	private Dimension frameSize; // The size of the frame in general
	private JTextArea text;
	private JScrollPane scrollText;
	
	public MessageLogFrame(){
		frameSize = new Dimension(200, 700);		
		icon = new ImageIcon("resources/messageIcon.png");		
		setTitle("Messages");		
		setIconImage(icon.getImage());
		setSize(frameSize);
		setAlwaysOnTop(true);		
		// Changes launch location of the frame
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		setLocation((int) (ge.getMaximumWindowBounds().getWidth() -  getWidth()), (int) (ge.getMaximumWindowBounds().getHeight() - getHeight())/2);
		
		setResizable(false);
		setLayout(new BorderLayout());	
		setupScrollingTextArea();
	}
	
	/**
	 * Sets up the scrolling text area to contain a JTextArea
	 */
	private void setupScrollingTextArea(){
		text = new JTextArea();
		DefaultCaret caret = (DefaultCaret)text.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		text.setBackground(Color.BLACK);
		text.setEnabled(false);
		text.setOpaque(true);
		scrollText = new JScrollPane(text);
		add(scrollText);
	}
	
	/**
	 * Updates the display text
	 * @param message The message to be added to the display
	 */
	public void write(String message){
		message = splitLines(message, 25);
		if (text.getText().length() > 0)
			text.setText(text.getText() + lineMarker + message);
		else
			text.setText(message);
	}
	
	/**
	 * Makes a string's appearance fit within the MessageLogFrame's width
	 * Allows messages to appear on the next line so horizontal scrolling is unnecessary
	 * @param message The message to be split
	 * @param max The max number of characters per line
	 * @return The word split into lines of max length
	 */
	private String splitLines(String message, int max){
		String splitUp = "";
		int count = 0;
		
		String[] words = message.split(" ");
		for (int i = 0; i < words.length; i++){
			if (count + words[i].length() > max){
				splitUp += "\n";
				count = 0;
			}
			splitUp += words[i] + " ";
			count += words[i].length();
		}
		return splitUp;
	}
}