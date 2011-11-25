/**
 * 
 */
package ch.hszt.connectfour.model.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.hszt.connectfour.model.board.GameBoardSlot;
import ch.hszt.connectfour.model.enumeration.DropSequenceDirection;

/**
 * Represents a {@link DropSequence} consisting of a single drop.
 * @author Markus Vetsch
 * @version 1.0, 24.11.2011
 */
class SingleDropSequence extends DropSequence
{
	private final GameBoardSlot slot;
	
	private List<Neighbour> neighbours;
	
	SingleDropSequence(final GameBoardSlot slot)
	{
		this.slot = slot;
		add(slot);
		
		neighbours = new ArrayList<Neighbour>();
		
		searchNeighbours();
	}
	
	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.model.game.DropSequence#length()
	 */
	int length()
	{
		return 1;
	}
	
	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.model.game.DropSequence#getDirection()
	 */
	DropSequenceDirection getDirection()
	{
		return DropSequenceDirection.INDETERMINATE;
	}
	
	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.model.game.DropSequence#getTurn()
	 */
	String getTurn()
	{
		List<GameBoardSlot> candidates = getPossibleSlots();
		
		if (candidates.size() > 0)
		{
			GameBoardSlot candidate = candidates.get(new Random().nextInt(candidates.size()));
			return candidate.getColumn().getKey();
		}
		else
		{
			return null;
		}
	}
	
	private List<GameBoardSlot> getPossibleSlots()
	{		
		List<GameBoardSlot> slots = new ArrayList<GameBoardSlot>();
		
		for (Neighbour neighbour : neighbours)
		{
			if (neighbour.exists() && neighbour.canSetDrop())
			{
				slots.add(neighbour.getSlot());
			}
		}
		
		return slots;
	}

	private void searchNeighbours()
	{
		// Search all 8 possible neighbours
		
		neighbours.add(new Neighbour(slot.getTop()));
		neighbours.add(new Neighbour(slot.getUpRight()));
		neighbours.add(new Neighbour(slot.getRight()));
		neighbours.add(new Neighbour(slot.getLowRight()));
		neighbours.add(new Neighbour(slot.getBottom()));
		neighbours.add(new Neighbour(slot.getLowLeft()));
		neighbours.add(new Neighbour(slot.getLeft()));
		neighbours.add(new Neighbour(slot.getUpLeft()));
	}
	
	private class Neighbour
	{
		private final GameBoardSlot slot;
		
		Neighbour(GameBoardSlot slot)
		{
			this.slot = slot;
		}
		
		GameBoardSlot getSlot()
		{
			return slot;
		}
		
		boolean exists()
		{
			return slot != null;
		}
		
		boolean canSetDrop()
		{
			boolean isLowerMost = slot.getBottom() != null && !slot.getBottom().isEmpty();
			return slot.isEmpty() && isLowerMost;
		}
	}
}
