import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * This class represents a JButton that is in the shape of any image
 * The image directory should be passed as a parameter in the constructor as a String
 * Any pixels in the image that are white are treated as transparent and will not be
 * visible 
 * Override the method buttonPressed() with whatever code you wish to
 * execute when the button is pressed
 * 
 * @author Sid
 */
public class ImageButton extends JLabel {
	private static final long serialVersionUID = 1L;
	public static Image image;
	public MouseAdapter mouseAdapter;

	public ImageButton(String fileName) {
		super(getImage(fileName));
		mouseAdapter = createMouseAdapter(mouseAdapter);
		addMouseListener(mouseAdapter);
	}

	/**
	 * Method to be overridden to do stuff
	 */
	public void buttonPressed() {
		System.out.println("Button has been pressed");
	}

	private static ImageIcon getImage(String fileName) {
		try {
			image = javax.imageio.ImageIO.read(new java.io.File(fileName));
		} catch (IOException e) {
			System.out.println(fileName);
			e.printStackTrace();
		}
		ImageIcon icon = new javax.swing.ImageIcon(makeColorTransparent(image, Color.WHITE));
		return icon;
	}

	public MouseAdapter createMouseAdapter(MouseAdapter adapter) {
		adapter = new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				//Underlying pixel is still white, so check if pixel is white
			    boolean transparent = ((BufferedImage) image).getRGB(e.getX(), e.getY()) == Color.white.getRGB();
				if (!transparent) {
					buttonPressed();
				}
			}
		};
		return adapter;
	}

	private static Image makeColorTransparent(Image image, final Color color) {
		ImageFilter filter = new RGBImageFilter() {
			public int markerRGB = color.getRGB() | 0xFF000000;
			public final int filterRGB(int x, int y, int rgb) {
				if ((rgb | 0xFF000000) == markerRGB) {
					return 0x00FFFFFF & rgb;
				} else {
					return rgb;
				}
			}
		};

		ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
		return Toolkit.getDefaultToolkit().createImage(ip);
	}

	// Tester
	public static void main(String[] args) throws Exception {
		javax.swing.JFrame frame = new javax.swing.JFrame("Custom Shape Button");
		frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		javax.swing.JPanel panel = new javax.swing.JPanel();
		panel.setBackground(java.awt.Color.GREEN);
		panel.setPreferredSize(new java.awt.Dimension(300, 200));
		frame.add(panel);
		frame.pack();

		// Add Image File location
		javax.swing.JLabel imageButton = new ImageButton("resources/Country Images/Alaska.png");

		// add the button to the panel so that it becomes visible
		panel.add(imageButton);
		frame.setLocation(0, 100);
		frame.setVisible(true);
	}
}