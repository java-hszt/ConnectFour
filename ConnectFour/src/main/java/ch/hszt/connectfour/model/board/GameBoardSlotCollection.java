
package ch.hszt.connectfour.model.board;

import java.util.List;
import ch.hszt.connectfour.model.enumeration.DropColor;
/**
 * Abstract class for any collection of {@link GameBoardSlot}.
 * @author Markus Vetsch
 * @version 1.0, 13.10.2011
 */
public abstract class GameBoardSlotCollection implements Iterable<GameBoardSlot> 
{	
	/**
	 * Returns all {@link GameBoardSlot} instances.
	 * @return The {@link GameBoardSlot} instances in a {@link List}.
	 */
	public abstract List<GameBoardSlot> asList();

	/**
	 * Determines, if the current {@link GameBoardSlotCollection} provides "Four in a Row" of specified {@link DropColor}
	 * @param color -The {@link DropColor} to evaluate.
	 * @return <b>true</b>, if this instance provides four {@link GameBoardSlot} with the specified {@link DropColor} in a row.
	 */
	public boolean hasConnectFour(DropColor color) 
  	{
		boolean test = false;
		List<GameBoardSlot> slots = asList();
		
		int i = 0;
		
		do
		{
			// Set boolean flag to true and get the first slot
			
			test = true;
			GameBoardSlot current = slots.get(i);
			
			int j = i + 1;
			
			// Investigate in maximum the following 3 slots
			
			while (test && j < i + 4 && j < slots.size())
			{
				GameBoardSlot next = slots.get(j);
				
//				if (current == null || next == null || current.getColor() == null || next.getColor() == null)
//				{
//					throw new IllegalArgumentException();//test
//				}
//				
				// Check, if slots are not empty and if they equal in terms of the drop color
				
				test &= (!current.isEmpty()) && (!next.isEmpty()) && (current.getColor() == next.getColor());
				j++;
			}
			
			i++;
		}
		while (!test && i < slots.size() - 3);	// Outer loop condition; four in a row not found and >= 4 slots left
		
		return test;
  	}
}
