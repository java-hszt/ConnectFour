
package ch.hszt.connectfour.model.board;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ch.hszt.connectfour.io.Serial;
import ch.hszt.connectfour.io.SerialObject;
import ch.hszt.connectfour.model.enumeration.DropColor;
import ch.hszt.connectfour.model.game.Game;
/**
 * Abstract class for any collection of {@link GameBoardSlot}.
 * @author Markus Vetsch
 * @version 1.0, 13.10.2011
 */
public abstract class GameBoardSlotCollection implements Iterable<GameBoardSlot>, Serial 
{
	/**
	 * Returns all {@link GameBoardSlot} instances.
	 * @return The {@link GameBoardSlot} instances in a {@link List}.
	 */
	public abstract List<GameBoardSlot> asList();
	
	/**
	 * Returns the exact {@link List} of {@link GameBoardSlot}, that builds the "ConnectFour".
	 * @return The {@link List} of {@link GameBoardSlot} building the "ConnectFour" in a row.
	 */
	public List<GameBoardSlot> getConnectFourList()
	{
		List<GameBoardSlot> sourceList = asList();
		
		int startIndex = 0;
		int endIndex = 0;
		boolean isFourInRow = false;
		
		// Start iterating with the first element in the slot collection
		
		while (!isFourInRow && startIndex < sourceList.size() - 1)
		{
			GameBoardSlot start = sourceList.get(startIndex);
			endIndex = startIndex + 1;
			
			// Continue search for next elements, if they contain drops of the same color
			
			while (!isFourInRow && sourceList.get(endIndex).getColor() == start.getColor())
			{				
				endIndex++;

				// Condition for four drops of same color in a row
				
				if ((endIndex - startIndex) == 4)
				{
					isFourInRow = true;
				}
			}
			
			// Otherwise perform search again for next start element
			
			if (!isFourInRow)
			{
				startIndex++;
			}
		}
		
		// Return a sublist of the entire slot collection
		
		return sourceList.subList(startIndex, endIndex);
	}
	
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
		
				// Check, if slots are not empty and if they equal in terms of the drop color
				
				test &= (!current.isEmpty()) && (!next.isEmpty()) && (current.getColor() == next.getColor());
				j++;
			}
			
			i++;
		}
		while (!test && i < slots.size() - 3);	// Outer loop condition; four in a row not found and >= 4 slots left
		
		return test;
  	}
	
	public void save(SerialObject obj)
	{
		obj.saveList(asList());
	}
	
	public Serial load(SerialObject obj)
	{
		//TODO;
		return null;
	}
}
