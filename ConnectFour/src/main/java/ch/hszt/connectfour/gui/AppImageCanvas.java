/**
 * 
 */
package ch.hszt.connectfour.gui;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;

/**
 * Customized canvas for display of application image.
 * @author Markus Vetsch
 * @version 1.0, 31.10.2011
 */
public class AppImageCanvas extends Canvas
{
	private Image image;
	
	/**
	 * Creates a {@link AppImageCanvas} with associated {@link Image}.
	 * @param image - The {@link Image} to draw in the canvas.
	 */
	public AppImageCanvas(Image image)
	{
		this.image = image;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Canvas#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g)
	{
		g.drawImage(image, 0, 0, this);
	}
}
