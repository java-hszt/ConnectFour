/**
 * 
 */
package ch.hszt.connectfour.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * Customized class for any label on the board in {@link MainGameFrame}.
 * @author Markus Vetsch
 * @version 1.0, 30.10.2011
 */
public class BoardLabel extends JLabel
{
	private int x, y;
	
	/**
	 * Creates a {@link BoardLabel} with specified text
	 *  using {@link SwingConstants#CENTER} as default for text alignment.
	 * @param text - The text to be displayed.
	 * @param x - The x position, at which the label shall be drawn.
	 * @param y - The y position, at which the label shall be drawn.
	 * @param width - The width of the label.
	 * @param height - The height of the label.
	 */
	public BoardLabel(String text, int x, int y, int width, int height)
	{
		this(text, JLabel.CENTER, x, y, width, height);
	}
	
	/**
	 * Creates a {@link BoardLabel} with specified text and alignment.
	 * @see SwingConstants for alignment constants.
	 * @param text - The text to be displayed.
	 * @param alignment - The text alignment; can be specified by using one of the {@link SwingConstants} values.
	 * @param x - The x position, at which the label shall be drawn.
	 * @param y - The y position, at which the label shall be drawn.
	 * @param width - The width of the label.
	 * @param height - The height of the label.
	 */
	public BoardLabel(String text, int alignment, int x, int y, int width, int height)
	{
		super(text, alignment);
		
		setFont(new Font("Tahoma", Font.BOLD, 32));
		setLayout(null);
		setSize(width, height);
		
		this.x = x;
		this.y = y;
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g)
	{		
		Graphics2D g2D = (Graphics2D) g;
		
		// Enable anti-aliasing
		
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.setColor(Color.WHITE);
		g2D.drawString(getText(), x, y);
		
	}
}
