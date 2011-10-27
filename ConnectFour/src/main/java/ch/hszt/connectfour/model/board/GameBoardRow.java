/**
 * 
 */
package ch.hszt.connectfour.model.board;

import java.util.Iterator;

/**
 * Represents a model-based row in the {@link GameBoard}
 * @author Markus Vetsch
 * @version 1.0, 13.10.2011
 */
public class GameBoardRow extends GameBoardLine
{
	/**
	 * Creates a new instance of a {@link GameBoardRow}.
	 * @param id - The ID of the row on the corresponding {@link GameBoard}.
	 * @param board - The specified {@link GameBoard}.
	 */
	GameBoardRow(int id, GameBoard board)
	{
		super(id, board);
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<GameBoardSlot> iterator()
	{
		return asList().iterator();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return String.format("GameBoardRow [%1$s-%2$s]",
							slots[0].getKey(), slots[GameBoard.NUMBER_OF_COLUMNS - 1]);
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.model.gui.GameBoardLine#createSlots()
	 */
	@Override
	protected GameBoardSlot[] createSlots()
	{
		GameBoardSlot[] slots = new GameBoardSlot[GameBoard.NUMBER_OF_COLUMNS];
		GameBoardColumn current = board.getColumnByKey("A");
		int i = 0;
		
		do
		{
			GameBoardSlot slot = current.getSlot(id);
			
			if (slot != null)
			{
				slots[i] = slot;
			}
			
			i++;
			
		} while ((current = current.getRight()) != null);
		
		return slots;
	}
}
