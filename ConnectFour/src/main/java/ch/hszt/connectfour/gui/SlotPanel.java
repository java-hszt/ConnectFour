package ch.hszt.connectfour.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

import org.aspectj.weaver.patterns.ConcreteCflowPointcut.Slot;

/**
 * Customized panel for slot depiction on {@link GameFrame}
 * @author Daniel Stutz, Markus Vetsch
 * @version 1.0, 31.10.2011
 */
public class SlotPanel extends JPanel 
{
	/**
	 * The diameter of circle-shaped {@link SlotPanel}.
	 */
	static final int DIAMETER = 90;
	
	private final String key;	
	private final String column;
	private final Ellipse2D.Double circle;

	/**
	 * Creates a new circle-shaped {@link SlotPanel}.
	 * @param column - The column identifier of the column in game board, the {@link SlotPanel} belongs to.
	 * @param position - The position of the {@link SlotPanel} within the column.
	 * @param adapter - The associated {@link MouseAdapter} for event handling.
	 */
	public SlotPanel(String column, int position, MouseAdapter adapter) 
	{
		setSize(DIAMETER, DIAMETER);
		
		this.circle = new Ellipse2D.Double(getX(), getY(), getWidth(), getHeight());
		this.column = column;
		this.key = column.concat(Integer.toString(position));
		
		addMouseListener(adapter);
	}
	
	/**
	 * Returns the key of the {@link SlotPanel}.
	 * @return The key of the {@link SlotPanel} assembled by column identfier and position.
	 */
	public String getKey()
	{
		return key;
	}
	
	/**
	 * Returns the column identifier, the {@link SlotPanel} belongs to.
	 * @return The column identifier the SlotPanel belongs to.
	 */
	public String getColumn()
	{
		return column;
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) 
	{
		Graphics2D g2d = (Graphics2D) g;
		
		// Enable anti-aliasing
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(Color.WHITE);
		g2d.fill(circle);
	}

//	// super.paintComponent clears offscreen pixmap,
//	// since we're using double buffering by default.
//	protected void clear(Graphics g)
//	{
//		super.paintComponent(g);
//	}
//
//	protected Ellipse2D.Double getCircle() 
//	{
//		return (circle);
//	}
}