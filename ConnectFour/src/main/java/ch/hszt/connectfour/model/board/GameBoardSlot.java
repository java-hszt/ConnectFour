package ch.hszt.connectfour.model.board;

import ch.hszt.connectfour.io.Serial;
import ch.hszt.connectfour.io.SerialObject;
import ch.hszt.connectfour.model.enumeration.DropColor;
/**
 * Encapsulates a single slot on the game board, which drops can be inserted to.
 * @author Markus Vetsch
 * @version 1.0, 11.10.2011
 */
public class GameBoardSlot implements Comparable<GameBoardSlot>, Serial
{
	private final int id;
	private transient final GameBoardColumn column;
	
	private boolean isEmpty;
	private DropColor color;

	/**
	* Creates a new {@link GameBoardSlot} instance on associated {@link GameBoardColumn}.
	* @param column - the associated {@link GameBoardColumn}.
	*/
	GameBoardSlot(final int id, final GameBoardColumn column) 
	{
		this.id = id;
		this.column = column;		
		this.isEmpty = true;
		this.color = DropColor.UNKNOWN;
	}
	
	/**
	 * Gets the key of the current instance, the is build by the column identifier and the row index.
	 * @return The unique key as {@link String} of this instance.
	 */
	public String getKey()
	{
		return column.getKey().concat(Integer.toString(id));
	}

	/**
	 * Returns the {@link GameBoardColumn}, which this {@link GameBoardSlot} belongs to.
	 * @return The associated {@link GameBoardColumn}
	 */
	public GameBoardColumn getColumn() 
	{
		return column;
	}
	
	/**
	* Returns the currently assigned {@link DropColor}.
	* @return the {@link DropColor}.
	*/
	public DropColor getColor() 
	{
		return color;
	}
	
	/**
	* Sets a drop with specified color for this {@link GameBoardSlot}.
	* @param color - the {@link DropColor} to be used.
	*/
	public synchronized void setDrop(DropColor color) 
	{
		if (color != DropColor.UNKNOWN)
		{			
			this.color = color;
			this.isEmpty = false;
		}
		else
		{
			throw new IllegalArgumentException(String.format("No drop to be set with undefined color %s!", color.toString()));
		}
	}
	
	/**
	* Determines, whether this {@link GameBoardSlot} instance is empty, i.e. contains a drop.
	* @return <b>true</b>, if the {@link GameBoardSlot} is empty; otherwise <b>false</b>.
	*/
	public boolean isEmpty() 
	{
		return isEmpty;
	}
	
	/**
	*  Returns the position identifier of the {@link GameBoardSlot} on the {@link GameBoard}.
	* @see java.lang.Object#toString()
	*/
	@Override
	public String toString() 
	{
		return String.format("GameBoardSlot [%s]", getKey());
	}
	
	/**
	*  (non-Javadoc)
	* @see java.lang.Comparable#compareTo(java.lang.Object)
	*/
	public int compareTo(GameBoardSlot other) 
	{
		return Integer.valueOf(id).compareTo(Integer.valueOf(other.id));
	}

	/**
	 * Returns the position of the slot in associated {@link GameBoardColumn}.
	 * @return The position (1-6) of the slot.
	 */
	public int getPosition() 
	{
		return id;
	}
	
	/*
	 * -------------------------------------
	 * NAVIGATION IN ALL DIRECTIONS
	 * -------------------------------------
	 */
	
	/**
	 * Looks up for the {@link GameBoardSlot} to the right (i.e. in same {@link GameBoardRow}, but different {@link GameBoardColumn})
	 * @return The {@link GameBoardSlot} to the right or <b>null</b>, if already on right edge of the {@link GameBoard}.
	 */
	public GameBoardSlot getRight()
	{
		GameBoardColumn rightColumn = column.getRight();
		
		// Search slot in same row index, but different column
		
		return (rightColumn == null) ? null : rightColumn.getSlot(id);
	}
	
	/**
	 * Looks up for the {@link GameBoardSlot} to the left (i.e. in same {@link GameBoardRow}, but different {@link GameBoardColumn})
	 * @return The {@link GameBoardSlot} to the left or <b>null</b>, if already on left edge of the {@link GameBoard}.
	 */
	public GameBoardSlot getLeft()
	{
		GameBoardColumn leftColumn = column.getLeft();
		
		// Search slot in same row index, but different column
		
		return (leftColumn == null) ? null : leftColumn.getSlot(id);
	}
	
	
	/**
	 * Looks up for the {@link GameBoardSlot} on the top of current (i.e in different {@link GameBoardRow}, but same {@link GameBoardColumn})
	 * @return The {@link GameBoardSlot} on the top of current or <b>null</b>, if already on top edge of the {@link GameBoard}.
	 */
	public GameBoardSlot getTop()
	{
		// Search slot in higher row index
		
		return (id == GameBoard.NUMBER_OF_SLOTS) ? null : column.getSlot(id + 1);
	}
	
	/**
	 * Looks up for the {@link GameBoardSlot} on the bottom of current (i.e in different {@link GameBoardRow}, but same {@link GameBoardColumn})
	 * @return The {@link GameBoardSlot} on the bottom of current or <b>null</b>, if already on bottom edge of the {@link GameBoard}.
	 */
	public GameBoardSlot getBottom()
	{
		// Search slot in lower row index
		
		return (id == 1) ? null : column.getSlot(id - 1);
	}
	
	/**
	 * Looks up for the {@link GameBoardSlot} in lower right of current (i.e. in different {@link GameBoardRow} and different {@link GameBoardColumn}).
	 * @return The {@link GameBoardSlot} in lower right of current or <b>null</b>, if right or bottom edge of the GameBoard already reached.
	 */
	public GameBoardSlot getLowRight()
	{
		GameBoardColumn rightColumn = column.getRight();
		
		// Respect cases where no right column or bottom row
		
		if (rightColumn == null)
		{
			return null;
		}
		if (id == 1)
		{
			return null;
		}
		
		// Search slot in right column and lower row index
		
		return rightColumn.getSlot(id - 1);
	}
	
	/**
	 * Looks up for the {@link GameBoardSlot} in upper right of current (i.e. in different {@link GameBoardRow} and different {@link GameBoardColumn}).
	 * @return The {@link GameBoardSlot} in upper right of current or <b>null</b>, if right or top edge of the GameBoard already reached.
	 */
	public GameBoardSlot getUpRight()
	{
		GameBoardColumn rightColumn = column.getRight();
		
		// Respect cases where no right column or top row
		
		if (rightColumn == null)
		{
			return null;
		}
		if (id == GameBoard.NUMBER_OF_SLOTS)
		{
			return null;
		}
		
		// Search slot in right column and higher row index
		
		return rightColumn.getSlot(id + 1);
	}
	
	/**
	 * Looks up for the {@link GameBoardSlot} in lower left of current (i.e. in different {@link GameBoardRow} and different {@link GameBoardColumn}).
	 * @return The {@link GameBoardSlot} in lower left of current or <b>null</b>, if left or bottom edge of the GameBoard already reached.
	 */
	public GameBoardSlot getLowLeft()
	{
		GameBoardColumn leftColumn = column.getLeft();
		
		// Respect cases where no left column or bottom row
		
		if (leftColumn == null)
		{
			return null;
		}
		if (id == 1)
		{
			return null;
		}
		
		// Search slot in left column and lower row index
		
		return leftColumn.getSlot(id - 1);
	}
	
	/**
	 * Looks up for the {@link GameBoardSlot} in upper left of current (i.e. in different {@link GameBoardRow} and different {@link GameBoardColumn}).
	 * @return The {@link GameBoardSlot} in upper left of current or <b>null</b>, if left or top edge of the GameBoard already reached.
	 */
	public GameBoardSlot getUpLeft()
	{
		GameBoardColumn leftColumn = column.getLeft();
		
		// Respect cases where no left column or top row
		
		if (leftColumn == null)
		{
			return null;
		}
		if (id == GameBoard.NUMBER_OF_SLOTS)
		{
			return null;
		}
		
		// Search slot in left column and higher row index
		
		return leftColumn.getSlot(id + 1);
	}
	
	/**
	 * Clears the content of current {@link GameBoardSlot}.
	 */
	void clear()
	{
		if (!isEmpty())
		{
			color = DropColor.UNKNOWN;
			isEmpty = true;
		}
	}	

	public void save(SerialObject obj)
	{
		obj.saveInt(id, "id");
		obj.saveString(column.getKey(), "column");
		obj.saveBoolean(isEmpty, "isEmpty");
		obj.saveString(color.toString(), "color");
	}

	public Serial load(SerialObject obj)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
