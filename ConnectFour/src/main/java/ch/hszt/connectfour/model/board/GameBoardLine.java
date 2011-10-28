package ch.hszt.connectfour.model.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents any kind of line on the {@link GameBoard}. Abstracts commonly columns / rows on the board.
 * @author Markus Vetsch
 * @version 1.0, 13.10.2011
 */
public abstract class GameBoardLine extends GameBoardSlotCollection 
{
	protected final int id;
	protected final GameBoard board;
	
	protected GameBoardSlot[] slots;

	/**
	 * Creates a new {@link GameBoardLine} with specified id, that is assigned to the {@link GameBoard}.
	 * To be called be implementing subclasses.
	 * @param id - The id of the {@link GameBoardLine} instance.
	 * @param board - The associated {@link GameBoard}.
	 */
	protected GameBoardLine(final int id, final GameBoard board) 
	{
		this.id = id;
		this.board = board;
		
		// create the slots
		
		this.slots = createSlots();
	}

	/**
	 * Returns the ID of current {@link GameBoardLine} as numeric value
	 * @return The ID of current {@link GameBoardLine}
	 */
	public int getId() 
	{
		return id;
	}

	/**
	 * Checks, if current {@link GameBoardLine} instance is of type {@link GameBoardColumn}.
	 * @return <b>true</b>, if this instance represents a {@link GameBoardRow}.
	 */
	boolean isColumn() 
	{
		return (this instanceof GameBoardColumn);
	}

	/**
	 * Checks, if current {@link GameBoardLine} instance is of type {@link GameBoardRow}.
	 * @return <b>true</b>, if this instance represents a {@link GameBoardRow}.
	 */
	boolean isRow() 
	{
		return (this instanceof GameBoardRow);
	}

	/**
	 * To be implemented by subclasses. Define the logic of building the underlying array of {@link GameBoardSlot}.
	 * @return An array of {@link GameBoardSlot} instances.
	 */
	protected abstract GameBoardSlot[] createSlots() ;

	/**
	 *  (non-Javadoc)
	 * @see ch.hszt.connectfour.model.board.GameBoardSlotCollection#asList()
	 */
	public List<GameBoardSlot> asList() 
	{
		List<GameBoardSlot> slotList = new ArrayList<GameBoardSlot>();
		
		for (GameBoardSlot slot : slots)
		{
			slotList.add(slot);
		}
		
		return slotList;
	}
}
