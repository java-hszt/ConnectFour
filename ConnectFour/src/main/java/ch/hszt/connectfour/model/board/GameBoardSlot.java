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
