
package ch.hszt.connectfour.model.board;

import java.util.Iterator;
import ch.hszt.connectfour.model.enumeration.DropColor;
/**
 * Encapsulates a model-based column on the game board.
 * @author Markus Vetsch
 * @version 1.0, 11.10.2011
 */
public class GameBoardColumn extends GameBoardLine 
{
	private final String key;

	/**
	 * Creates a new {@link GameBoardColumn} on associated {@link GameBoard}.
	 * @param id - The id of the {@link GameBoardColumn}.
	 * @param board - the associated {@link GameBoard}.
	 */
	GameBoardColumn(final int id, final GameBoard board) 
	{
		super(id, board);
		this.key = assignKeyById(id);
	}
	
	/**
	 * Returns the associated key by specified column ID.
	 * @param columnId - The ID of the column.
	 * @return The key matching the specified column ID.
	 */
	public static String assignKeyById(int columnId) 
	{
		switch (columnId)
		{
			case 1:
				return "A";
			case 2:
				return "B";
			case 3:
				return "C";
			case 4:
				return "D";
			case 5:
				return "E";
			case 6:
				return "F";
			case 7:
				return "G";
			default:
				return null;
		}
	}

	/**
	 *  (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<GameBoardSlot> iterator() 
	{
		return asList().iterator();
	}
	

	/**
	 * The index of the {@link GameBoardSlot} to be retrieved; The index is 1-based - not zero-based.
	 * @param slotIndex - The index of the {@link GameBoardSlot} to be looked up for.
	 * @return The associated {@link GameBoardSlot} or <b>null</b>, if the index is out of range.
	 */
	public GameBoardSlot getSlot(int slotIndex) 
	{
		if (slotIndex > 0 && slotIndex <= slots.length)
		{
			return slots[slotIndex-1];
		}
		
		return null;
	}

	
	/**
	 *  Returns the unique key of the {@link GameBoardColumn}.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() 
	{
		return String.format("GameBoardColumn [%1$s - %2$s]",
								getSlot(1).getKey(), getSlot(slots.length).getKey());
	}

	/**
	 * Returns the unique column key of the current {@link GameBoardColumn}.
	 * @return the unique column key.
	 */
	public String getKey() 
	{
		return key;
	}
	

	/**
	 * Returns the count of drops of specified {@link DropColor} in current {@link GameBoardColumn}.
	 * @param color - The {@link DropColor} to look up for.
	 * @return The amount of drops of specified {@link DropColor} in this {@link GameBoardColumn}.
	 */
	int getDropCountByColor(DropColor color)
	{
		int count = 0;
		
		for (int i = 0; i < slots.length; i++)
		{
			GameBoardSlot slot = getSlot(i);
			
			if (!slot.isEmpty() && slot.getColor() == color)
			{
				count++;
			}
		}
		
		return count;
	}

	/**
	 * Returns the next empty {@link GameBoardSlot} in ascending sequence (1-7).
	 * @return The next empty {@link GameBoardSlot} or <b>NULL</b> if none is empty.
	 */
	GameBoardSlot getNextEmptySlot() 
	{
		if (hasEmptySlots())
		{
			for (int i = 1; i <= slots.length; i++)
			{
				if (getSlot(i).isEmpty())
				{
					return slots[i-1];
				}
			}
		}	
		
		return null;
	}

	/**
	 * Returns the {@link GameBoardColumn} on the right of the {@link GameBoard}.
	 * @return - The {@link GameBoardColumn} on the right or <b>NULL</b>, if the right end of the {@link GameBoard} was reached.
	 */
	GameBoardColumn getRight() 
	{	
		if (id > 0)
		{
			if (id == GameBoard.NUMBER_OF_COLUMNS)
			{
				return null;
			}
			else
			{
				return board.getColumnById(id + 1);
			}
		}
		else
		{
			return null;
		}
	}

	/**
	 * Returns the {@link GameBoardColumn} on the left of the {@link GameBoard}.
	 * @return - The {@link GameBoardColumn} on the left or <b>NULL</b>, if the left end of the {@link GameBoard} was reached.
	 */
	GameBoardColumn getLeft() 
	{
		if (id > 0)
		{
			if (id == 1)
			{
				return null;
			}
			else
			{
				return board.getColumnById(id - 1);
			}
		}
		else
		{
			return null;
		}
	}

	/**
	 * Evaluates, if this {@link GameBoardColumn} has empty slots.
	 * @return <b>true</b>, if this instance contains empty slots; otherwise <b>false</b>.
	 */
	private boolean hasEmptySlots() 
	{
		for (int i = 1; i <= slots.length; i++)
		{
			if (getSlot(i).isEmpty())
			{
				return true;
			}
		}
		
		return false;
	}

	protected final GameBoardSlot[] createSlots() 
	{
		GameBoardSlot[] slotArr = new GameBoardSlot[GameBoard.NUMBER_OF_SLOTS];
		
		for (int i = 0; i < slotArr.length; i++)
		{
			slotArr[i] = new GameBoardSlot(i+1, this);
		}
		
		return slotArr;
	}
}
