import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.JLabel;

public class TurnIndicator extends JPanel {
	private static final long serialVersionUID = 8776342095199288987L;
	private Dimension displayDimension;
	private JLabel textLabel;
	
	public TurnIndicator() {
		displayDimension = new Dimension(200, 70);
		textLabel = new JLabel();
		textLabel.setHorizontalAlignment(SwingConstants.CENTER);
		textLabel.setVerticalAlignment(SwingConstants.CENTER);
		
		this.setMinimumSize(displayDimension);
		this.setMaximumSize(displayDimension);
		this.setPreferredSize(displayDimension);
		this.setLayout(new BorderLayout());
		this.add(textLabel, BorderLayout.CENTER);
		
		this.setOpaque(true);
		this.setBackground(Color.gray);
	}
	
	public void changeColor(Color c) {
		this.setBackground(c);
	}
	
	public Dimension getDim() {
		return displayDimension;
	}
	
	public void setText(int state) {
		switch(state) {
		case Engine.PRE_GAME:
			textLabel.setText("<html><font color = \"white\" size = \"5\" face = \"georgia\">OCCUPY</font></html>");
			break;
		case Engine.REINFORCE:
			textLabel.setText("<html><font color = \"white\" size = \"5\" face = \"georgia\">REINFORCE</font></html>");
			break;
		case Engine.RECRUIT:
			textLabel.setText("<html><font color = \"white\" size = \"5\" face = \"georgia\">REINFORCE</font></html>");
			break;
		case Engine.ATTACK_A:
			textLabel.setText("<html><font color = \"white\" size = \"5\" face = \"georgia\">ATTACK</font></html>");
			break;
		case Engine.FORTIFY_A:
			textLabel.setText("<html><font color = \"white\" size = \"5\" face = \"georgia\">FORTIFY</font></html>");
			break;
		default:
			//textLabel.setText("INVALID GAME STATE See ColorTurnIndicator Class");
			break;
		}
	}
}
