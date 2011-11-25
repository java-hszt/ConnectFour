/**
 * 
 */
package ch.hszt.connectfour.model.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.hszt.connectfour.model.board.GameBoardSlot;
import ch.hszt.connectfour.model.enumeration.DropColor;
import ch.hszt.connectfour.model.enumeration.DropSequenceDirection;

/**
 * Represents a sequence of drops with same {@link DropColor}, a minimum length of 2 and a maximum length of 4 drops.
 * @author Markus Vetsch
 * @version 1.0, 21.11.2011
 */
public class DropSequence
{
	/**
	 * The minimum length of a drop sequence.
	 */
	static final int MIN_LENGTH = 2;
	/**
	 * The maximum length of a drop sequence.
	 */
	static final int MAX_LENGTH = 4;
	
	private List<GameBoardSlot> slots;
	
	private DropSequenceDirection direction;
	private GameBoardSlot before;
	private GameBoardSlot after;
	
	private boolean allowsDropBefore;
	private boolean allowsDropAfter;
	
	DropSequence()
	{
		direction = DropSequenceDirection.INDETERMINATE;
		this.slots = new ArrayList<GameBoardSlot>();
	}
	
	private DropSequenceDirection evaluateDirection()
	{
		if (isHorizontalSequence())
		{
			return DropSequenceDirection.ROW;
		}
		else if (isVerticalSequence())
		{
			return DropSequenceDirection.COLUMN;
		}
		else if (isDiagonalSequence())
		{
			return getDiagonalDirection();
		}
		
		return DropSequenceDirection.INDETERMINATE;
	}
	
	boolean isSubSequenceOf(DropSequence other)
	{
		return other.slots.containsAll(this.slots);
	}
	
	DropSequenceDirection getDirection()
	{
		return direction;
	}
	
	String getTurn()
	{
		if (after != null && before != null)
		{
			int random = new Random().nextInt(2);
			return (random == 0) ? after.getColumn().getKey() : before.getColumn().getKey();
		}
		else if (after != null && before == null)
		{
			return after.getColumn().getKey();
		}
		else
		{
			return before.getColumn().getKey();
		}
	}

	void evaluate()
	{
		direction = evaluateDirection();
		determineBeforeAfter();
	}
	
	int length()
	{
		return slots.size();
	}
	
	void add(GameBoardSlot slot)
	{
		if (!slots.contains(slot) && slots.size() != MAX_LENGTH)
		{
			slots.add(slot);
		}
	}
	
	void clear()
	{
		slots.clear();
	}

	GameBoardSlot getSlotBefore()
	{
		if (allowsDropBefore())
		{
			return before;
		}
		
		return null;
	}
	
	GameBoardSlot getSlotAfter()
	{
		if (allowsDropAfter())
		{
			return after;
		}
		
		return null;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (GameBoardSlot slot : slots)
		{
			sb.append(slot.getKey() + ",");
		}
		
		String slotList = sb.toString().substring(0, sb.length()-1);
		return String.format("DropSequence [%s]", slotList);
	}
	
	private void determineBeforeAfter()
	{
		if (direction != DropSequenceDirection.INDETERMINATE)
		{
			boolean isBeforeEmpty = false, isBeforeLowerMost = false;
			boolean isAfterEmpty = false, isAfterLowerMost = false;
			
			// Search for adjacent slots before / after the sequence
			// in respect to the direction
			
			before = getBefore(direction);
			after = getAfter(direction);
			
			if (before != null)
			{
				isBeforeEmpty = before.isEmpty();
				isBeforeLowerMost = before.getBottom() == null || !before.getBottom().isEmpty();
			}
			if (after != null)
			{
				isAfterEmpty = after.isEmpty();
				isAfterLowerMost = after.getBottom() == null || !after.getBottom().isEmpty();
			}			
			
			// Drop insertion possible, if either in lower most row or if slot in bottom is not empty
			
			allowsDropBefore = isBeforeEmpty && isBeforeLowerMost;
			allowsDropAfter = isAfterEmpty && isAfterLowerMost;
		}
		else
		{
			throw new IllegalStateException("Undefined DropSequenceDirection!");
		}
	}
	
	private GameBoardSlot getBefore(DropSequenceDirection direction)
	{
		if (slots.size() == 0)
		{
			return null;
		}
		
		GameBoardSlot current = slots.get(0);
		
		if (direction == DropSequenceDirection.COLUMN)
		{
			return current.getBottom();
		}
		else if (direction == DropSequenceDirection.ROW)
		{
			return current.getLeft();
		}
		else if (direction == DropSequenceDirection.DIAGONAL_LOW_RIGHT)
		{
			return current.getUpLeft();
		}
		else if (direction == DropSequenceDirection.DIAGONAL_UP_RIGHT)
		{
			return current.getLowLeft();
		}
		else
		{
			return null;
		}
	}
	
	private GameBoardSlot getAfter(DropSequenceDirection direction)
	{
		if (slots.size() == 0)
		{
			return null;
		}
		
		GameBoardSlot current = slots.get(length() - 1);
		
		if (direction == DropSequenceDirection.COLUMN)
		{
			return current.getTop();
		}
		else if (direction == DropSequenceDirection.ROW)
		{
			return current.getRight();
		}
		else if (direction == DropSequenceDirection.DIAGONAL_LOW_RIGHT)
		{
			return current.getLowRight();
		}
		else if (direction == DropSequenceDirection.DIAGONAL_UP_RIGHT)
		{
			return current.getUpRight();
		}
		else
		{
			return null;
		}
	}
	
	private boolean allowsDropBefore()
	{
		return before != null && allowsDropBefore;
	}
	
	private boolean allowsDropAfter()
	{
		return after != null && allowsDropAfter;
	}
	
	private DropSequenceDirection getDiagonalDirection()
	{
		GameBoardSlot first = slots.get(0);
		GameBoardSlot second = slots.get(1);
		
		boolean isDownward = first.getPosition() > second.getPosition();
		boolean isRight = first.getColumn().getKey().compareTo(second.getColumn().getKey()) < 0;
		
		boolean isLowRight = isDownward && isRight;
		boolean isUpRight = !isDownward && isRight;
		
		if (isLowRight)
		{
			return DropSequenceDirection.DIAGONAL_LOW_RIGHT; 
		}
		else if (isUpRight)
		{
			return DropSequenceDirection.DIAGONAL_UP_RIGHT;
		}
		else
		{
			return DropSequenceDirection.INDETERMINATE;
		}
	}
	
	private boolean isDiagonalSequence()
	{
		for (int i = 0; i < length() - 1; i++)
		{		
			String currentColumn = slots.get(i).getColumn().getKey();
			String nextColumn = slots.get(i + 1).getColumn().getKey();
			
			int currentRow = slots.get(i).getPosition();
			int nextRow = slots.get(i+1).getPosition();

			if (currentColumn.equals(nextColumn) || currentRow == nextRow)
			{
				return false;
			}
		}
		
		return true;
	}
	
	private boolean isHorizontalSequence()
	{
		for (int i = 0; i < length() - 1; i++)
		{		
			int currentRow = slots.get(i).getPosition();
			int nextRow = slots.get(i+1).getPosition();

			if (currentRow != nextRow)
			{
				return false;
			}
		}
		
		return true;
	}
	
	private boolean isVerticalSequence()
	{
		for (int i = 0; i < length() - 1; i++)
		{
			String currentColumn = slots.get(i).getColumn().getKey();
			String nextColumn = slots.get(i + 1).getColumn().getKey();

			if (!currentColumn.equals(nextColumn))
			{
				return false;
			}
		}
		
		return true;
	}
}
