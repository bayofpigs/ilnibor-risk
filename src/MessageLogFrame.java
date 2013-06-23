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
	public static final String lineMarker = "\n________________________\n\n";
	private static final Color DEFAULT_COLOR = Color.WHITE;
	private Dimension frameSize;
	private JTextPane text;
	private JScrollPane scrollText;
	private ImageIcon icon;
	
	public MessageLogFrame(){
		frameSize = new Dimension(200, 700);
		setSize(frameSize);
		setResizable(false);
		setLayout(new BorderLayout());
		icon = new ImageIcon("resources/messageIcon.png");		
		setIconImage(icon.getImage());
		setTitle("Messages");		
		setAlwaysOnTop(true);		
		// Changes launch location of the frame to be on the right side of the screen, centered vertically
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
		//Puts textPane onto scrollPane, then scrollPane onto jFrame
		scrollText = new JScrollPane(text);
		add(scrollText);
		write("Welcome to iLNibor's Risk! \n \n Created by: \n \n MIKE ZHANG \n AKHIL VELAGAPUDI \n SID SENKUMAR");
	}
	
	/**
	 * Writes a message to the log in color
	 * @param message The message to be shown
	 * @param color The color of the message text
	 */
	public void write(String message, Color color){
		message = splitLines(message, lineMarker.length());
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
	 * Writes with special conditions and/or special colors
	 * @param message The message to be written
	 * @param string The special condition switch
	 * @param color The color of the text
	 */
	public void write(String message, String condition, Color color) {
		if (condition.equals("NO_LINEMARKER")){
			message = splitLines(message, lineMarker.length());
			//Adds the text in the desired color
			StyledDocument doc = text.getStyledDocument();
	        Style style = text.addStyle("colorStyle", null);
	        StyleConstants.setForeground(style, color);
	        try { doc.insertString(doc.getLength(), message, style); }
	        catch (BadLocationException e){}
	        //Goes to the next line and changes the color back to white
	        StyleConstants.setForeground(style, DEFAULT_COLOR);
	        try { doc.insertString(doc.getLength(), "\n", style); }
	        catch (BadLocationException e){}
		}
		//Can add more conditions later if necessary
		else
		{
			
		}
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
		int lengthThisLine = 0;
		String[] words = message.split(" ");
		for (int i = 0; i < words.length; i++){
			if (words[i].equals("\n")){
				splitUp += words[i];
			//	System.out.println("Caught one");
			//	System.out.println(splitUp + "\n-----------------\n\n\n");
				lengthThisLine = 0;
			}
			else{
				if (lengthThisLine + words[i].length() + 1 > max){
					splitUp += "\n";
					lengthThisLine = 0;
				}
				splitUp += words[i] + " ";
				lengthThisLine += words[i].length() + 1;
			}
		}
	
		//System.out.println("========================");
		//System.out.println(splitUp);
		//System.out.println("------------------------");

		return splitUp;
	}
}