import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Frame used to display useful messages to the user.
 * Can be edited later to add a chat functionality if over-the-Internet implementation finished
 * @author That Kid Named Sid
 */
public class MessageLogFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final String lineMarker = "\n________________________\n\n";
	private static final Color DEFAULT_COLOR = Color.WHITE;
	private Dimension frameSize;
	private JTextPane text;
	private JScrollPane scrollText;
	private ImageIcon icon;
	
	public MessageLogFrame(){
		frameSize = new Dimension(200, 700);
		setSize(frameSize);
		icon = new ImageIcon("resources/messageIcon.png");		
		setIconImage(icon.getImage());
		setTitle("Messages");		
		setAlwaysOnTop(true);		
		setResizable(false);
		setLayout(new BorderLayout());	
		// Changes launch location of the frame
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		setLocation((int) (ge.getMaximumWindowBounds().getWidth() -  getWidth()), (int) (ge.getMaximumWindowBounds().getHeight() - getHeight())/2);
		setupScrollingTextArea();
	}
	
	/**
	 * Sets up the scrolling text area to contain a JTextArea
	 */
	private void setupScrollingTextArea(){
		text = new JTextPane();
		text.setBackground(Color.BLACK);
		text.setEditable(false);
		text.setOpaque(true);
		//Auto-scrolls to latest text
		DefaultCaret caret = (DefaultCaret)text.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scrollText = new JScrollPane(text);
		add(scrollText);
		write("Welcome to iLNibor's Risk!\n\nCreated by: \n\nMIKE ZHANG AKHIL VELAGAPUDI \nSID SENKUMAR");
	}
	
	/**
	 * Writes a message to the log in color
	 * @param message The message to be shown
	 * @param color The color of the message text
	 */
	public void write(String message, Color color){
		message = splitLines(message, 30);
		//Adds the text in the desired color
		StyledDocument doc = text.getStyledDocument();
        Style style = text.addStyle("colorStyle", null);
        StyleConstants.setForeground(style, color);
        try { doc.insertString(doc.getLength(), message, style); }
        catch (BadLocationException e){}
        //Add the lineMarker in DEFAULT_COLOR
        StyleConstants.setForeground(style, DEFAULT_COLOR);
        try { doc.insertString(doc.getLength(), lineMarker, style); }
        catch (BadLocationException e){}
	}
	
	/**
	 * Writes to the log with DEFAULT_COLOR
	 * @param message The message to be shown
	 */
	public void write(String message){
		write(message, DEFAULT_COLOR);
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