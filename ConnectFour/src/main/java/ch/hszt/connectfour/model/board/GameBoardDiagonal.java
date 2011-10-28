
package ch.hszt.connectfour.model.board;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
/**
 * Represents a model-based diagonal on the {@link GameBoard}.
 * @author Markus Vetsch
 * @version 1.0, 11.10.2011
 */
public class GameBoardDiagonal extends GameBoardSlotCollection 
{
	/**
	 *  Defines the minimum length of a valid diagonal.
	 */
	private static final int MINIMUM_LENGTH =  4;
	
	private final GameBoardSlot start;
	
	private GameBoardSlot end;
	private List<GameBoardSlot> diagonalSlots;

	/**
	 * Creates a new {@link GameBoardDiagonal} with specified {@link GameBoardSlot} as start point.
	 * @param start - The starting point of the {@link GameBoardDiagonal}.
	 * @param searchDown - <b>true</b>, if diagonal shall be built in down right direction; 
	 * otherwise, diagonal is built in up right direction.
	 */
	GameBoardDiagonal(final GameBoardSlot start, boolean searchDown)
	{
		this.start = start;		
		diagonalSlots = build(searchDown);
	}

	/**
	 * Evaluates, if a valid {@link GameBoardDiagonal} can be created
	 *  from given starting point as {@link GameBoardSlot} in specified search direction
	 * @param start - The starting point of the evaluation as {@link GameBoardSlot}.
	 * @param searchDown - <b>true</b>, if diagonal shall be built in down right direction; 
	 * otherwise, diagonal is built in up right direction.
	 * @return <b>true</b>, if the diagonal is valid, i.e. contains >= {@link GameBoardDiagonal#MINIMUM_LENGTH} {@link GameBoardSlot} instances .
	 */
	public static boolean isValid(GameBoardSlot start, boolean searchDown)
	{
		return lookupLength(start, searchDown) >= MINIMUM_LENGTH;
	}

	/**
	 *  (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return String.format("GameBoardDiagonal %s", getKey());
	}
	
	/**
	 * Returns the key of current {@link GameBoardDiagonal}.
	 * @return The key of the {@link GameBoardDiagonal} instance.
	 */
	public String getKey()
	{
		return String.format("%1$s-%2$s / %2$s-%1$s", start.getKey(), end.getKey());
	}

	/**
	 * @see ch.hszt.connectfour.model.board.GameBoardSlotCollection#asList()
	 */
	public List<GameBoardSlot> asList() 
	{
		return diagonalSlots;
	}

	/**
   	*  (non-Javadoc)
   	* @see java.lang.Iterable#iterator()
   	*/
	public Iterator<GameBoardSlot> iterator() 
  	{
		return diagonalSlots.iterator();
  	}

  	/**
   	* Returns the length of current {@link GameBoardDiagonal}.
   	* @return The length of the {@link GameBoardDiagonal}, which is equal to the amount of {@link GameBoardSlot} instances on it.
   	*/
  	int getLength() 
  	{
		return diagonalSlots.size();
  	}

  	/**
   	* Evaluates, if this {@link GameBoardDiagonal} is identical with the {@link GameBoardDiagonal} specified in the argument.
   	* @param other - The {@link GameBoardDiagonal} to be checked for identity.
   	* @return <b>true</b>, if both instances are identical, i.e. define a {@link GameBoardDiagonal} 
   	* with same start and end {@link GameBoardSlot} in either direction.
   	*/
  	boolean IsIdenticalWith(GameBoardDiagonal other) 
  	{
		// start slots / end slots match or in case of
		// reverse direction, i.e start slot matches 
		// end slot and end slot matches start slot
		
		return (start.toString().equals(other.start.toString()) && end.toString().equals(other.end.toString())) 
				|| (start.toString().equals(other.end.toString()) && end.toString().equals(other.start.toString()));
  	}

  	private List<GameBoardSlot> build(boolean searchDown) 
  	{
		SortedMap<String,GameBoardSlot> slotMap = new TreeMap<String, GameBoardSlot>();
		GameBoardSlot previous = null;
		GameBoardSlot current = start;

		do
		{
			// Add item to internal map
			
			if (!slotMap.containsKey(current.toString()))
			{
				slotMap.put(current.toString(), current);
			}	
			
			// Assign current to previous slot reference and try to get successor slot
			
			previous = current;
			current = (searchDown) ? getNextLowerRight(current) : getNextUpperRight(current);
			
			if (current == null)
			{
				// If no successor to be found anymore, end of diagonal is reached
				
				end = previous;
			}
			
		} while (current != null);

		return new ArrayList<GameBoardSlot>(slotMap.values());
  	}

  	private static GameBoardSlot getNextLowerRight(GameBoardSlot current)
  	{
  		// Get next slot in lower right position
  		
		int currentSlotPos = current.getPosition();
		int nextSlotPos = currentSlotPos + 1;
		GameBoardColumn next = current.getColumn().getRight();
		
		// return null, if no more slots on game board
		
		if (next == null || nextSlotPos > GameBoard.NUMBER_OF_SLOTS + 1)
		{
			return null;
		}
		
		return next.getSlot(nextSlotPos);
  	}

  	private static GameBoardSlot getNextUpperRight(GameBoardSlot current)
  	{
  		// Get next slot in lower right position
  		
		int currentSlotPos = current.getPosition();
		int nextSlotPos = currentSlotPos - 1;
		GameBoardColumn next = current.getColumn().getRight();
		
		// return null, if no more slots on game board
		
		if (next == null || nextSlotPos < 1)
		{
			return null;
		}
		
		return next.getSlot(nextSlotPos);
  	}

  	private static int lookupLength(GameBoardSlot start, boolean searchDown)
  	{
  		GameBoardSlot current = start;
		GameBoardSlot next = null;
		int slotCount = 1;
		
		// Search at least once for a successor slot
		
		do
		{
			// search down right or up right according to flag
			
			next = searchDown ? getNextLowerRight(current) : getNextUpperRight(current);
			
			if (next != null)
			{
				slotCount++;
			}
			
			current = next;
			
		} while (next != null);
		
		return slotCount;
  	}
}
