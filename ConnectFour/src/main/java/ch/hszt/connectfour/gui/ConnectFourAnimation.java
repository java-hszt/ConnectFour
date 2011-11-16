/**
 * 
 */
package ch.hszt.connectfour.gui;

import java.awt.Color;
import java.util.Collection;
import java.util.List;

/**
 * Helper class for animation in connect four situation.
 * @author Markus Vetsch
 * @version 1.0, 13.11.2011
 */
class ConnectFourAnimation implements DropAnimation
{
	private final List<SlotPanel> panels;
	
	ConnectFourAnimation(List<SlotPanel> panels)
	{
		this.panels = panels;
	}
	
	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.gui.DropAnimation#run()
	 */
	public void run()
	{
		for (SlotPanel panel : panels)
		{
			panel.setColor(Color.GREEN);
		}
	}

}
