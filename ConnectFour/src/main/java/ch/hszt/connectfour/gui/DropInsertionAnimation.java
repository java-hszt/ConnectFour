/**
 * 
 */
package ch.hszt.connectfour.gui;

import java.awt.Color;

/**
 * Customized animation for drop insertion.
 * @author Markus Vetsch
 * @version 1.0, 13.11.2011
 */
class DropInsertionAnimation implements DropAnimation
{
	private final GameFrame frame;
	private final SlotPanel target;
	private final Color color;
	
	/**
	 * Creates a new {@link DropInsertionAnimation} with specified target {@link SlotPanel} and {@link Color}.
	 * @param target - The target {@link SlotPanel} of the animation.
	 * @param color - The {@link Color} to use for the animation.
	 */
	DropInsertionAnimation(GameFrame frame, SlotPanel target, Color color)
	{
		this.frame = frame;
		this.target = target;
		this.color = color;
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.gui.DropAnimation#run()
	 */
	public void run()
	{
		String targetColumn = target.getColumn();
		int targetRow = Integer.parseInt(target.getKey().substring(1));
		
		final int topRow = 6;
		
		SlotPanel current = null, previous = null;

		for (int i = topRow; i >= targetRow; i--)
		{
			current = frame.getSlotPanel(targetColumn.concat(Integer.toString(i)));
			current.setColor(color);
			
			if (previous != null)
			{
				previous.setColor(Color.WHITE);
			}
			
			previous = current;
			current = null;
			
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException ex)
			{
				ex.printStackTrace();
			}
		}
	}

}
