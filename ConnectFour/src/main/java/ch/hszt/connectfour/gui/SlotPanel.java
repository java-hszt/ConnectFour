package ch.hszt.connectfour.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.aspectj.weaver.patterns.ConcreteCflowPointcut.Slot;

/**
 * Customized panel for slot depiction on {@link MainGameFrame}
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
	
	private boolean isDefault = true;

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
//	
//	/**
//	 * Returns the center pixel coordinates of the {@link SlotPanel}.
//	 * @return The center pixel coordinates as {@link Point}.
//	 */
//	public Point getCenter()
//	{
//		return new Point((int)circle.getCenterX(), (int)circle.getCenterY());
//	}
	
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
	
	/**
	 * Sets the current color of {@link SlotPanel}.
	 * @param color - The {@link Color} to be applied.
	 */
	public void setColor(Color color)
	{
		isDefault = false;
		
		Graphics2D g2d = (Graphics2D) getGraphics();
		
		fill(g2d, color);
		update(g2d);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) 
	{
		Graphics2D g2d = (Graphics2D) g;
		
		// Enable anti-aliasing
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// Fill circle initially
		
		if (isDefault)
		{
			fill(g2d, Color.WHITE);
		}		
		
		// draw outline of circle
		
		g2d.setColor(Color.BLACK);
		g2d.draw(circle);
	}
	
	public String toString()
	{
		return getKey();
	}
	
	private void fill(Graphics2D g, Color color)
	{
		g.setColor(color);
		g.fill(circle);
	}
}