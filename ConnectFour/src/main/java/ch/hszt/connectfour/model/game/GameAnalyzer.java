/**
 * 
 */
package ch.hszt.connectfour.model.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ch.hszt.connectfour.model.board.GameBoard;
import ch.hszt.connectfour.model.board.GameBoardColumn;
import ch.hszt.connectfour.model.board.GameBoardSlot;
import ch.hszt.connectfour.model.board.GameBoardSlotCollection;
import ch.hszt.connectfour.model.enumeration.DropColor;
import ch.hszt.connectfour.model.enumeration.DropSequenceDirection;

/**
 * Analyzes the situation in a certain {@link Game} for 
 * a certain {@link DropColor} in order to determine the best next turn.
 * @author Markus Vetsch
 * @version 1.0, 16.11.2011
 */
class GameAnalyzer
{	
	private static final int OPENING_TURN_COUNT = 4;
	
	private GameAnalyzer()
	{		
	}
	
	/**
	 * Determines, if the situation of affected {@link Game} is still in a phase, where opening turns are to be set.
	 * @param game - The {@link Game} to be analyzed
	 * @return <b>true</b>, if the number of completed turns gives a hint on the opening phase of a {@link Game}; otherwise <b>false</b>.
	 */
	static boolean isOpeningTurn(Game game)
	{
		return game.getStatus().countCompletedTurns() < OPENING_TURN_COUNT;
	}
	
	/**
	 * Creates an opening turn for the first four turns in the {@link Game} and specifies which {@link GameBoardColumn} to insert a drop into.
	 *  If the method is called later in the {@link Game},
	 *   i.e {@link GameStatus#countCompletedTurns()} > 4, <b>null</b> will be returned.
	 * @param game - The affected {@link Game}.
	 * @return The identifier of the {@link GameBoardColumn} to insert the drop into.
	 */
	static String createOpeningTurn(Game game)
	{
		int completedTurns = game.getStatus().countCompletedTurns();
		
		if (completedTurns < OPENING_TURN_COUNT)
		{
			return getRandomCenterColumn(game.getBoard());
		}
		
		return null;
	}
	
	static String findColumnWithEmptySlots(GameBoard board)
	{
		for (GameBoardSlotCollection coll : board.getColumns())
		{
			GameBoardColumn column = (GameBoardColumn) coll;
			
			if (column.hasEmptySlots())
			{
				return column.getKey();
			}
		}
		
		return null;
	}
	
	static List<SingleDropSequence> getSingleSequences(GameBoard board, DropColor color)
	{
		List<DropSequence> all = getAllSequences(board, color);
		Map<String,SingleDropSequence> singles = new Hashtable<String, SingleDropSequence>();
		
		for (GameBoardSlotCollection coll : board.getAll())
		{			
			for (GameBoardSlot slot : coll)
			{
				if (!slot.isEmpty() && (slot.getColor() == color))
				{
					SingleDropSequence single = new SingleDropSequence(slot);
					boolean isSingle = true;
					int counter = 0;
					
					while (isSingle && counter < all.size())
					{
						DropSequence next = all.get(counter);
						
						if (single.isSubSequenceOf(next))
						{
							isSingle = false;
						}
						
						counter++;
					}
					
					if (isSingle && !singles.containsKey(slot.getKey()))
					{
						singles.put(slot.getKey(), single);
					}
				}
			}
		}
		
		return new ArrayList<SingleDropSequence>(singles.values());
	}
	
	/**
	 * Determines {@link DropSequence} items matching to specified {@link DropSequenceDirection}.
	 * @param board - The underlying {@link GameBoard} to be analyzed.
	 * @param color - The {@link DropColor} to be respected for the analysis of the {@link GameBoard}.
	 * @param direction - The {@link DropSequenceDirection} to be respected. If passing {@link DropSequenceDirection#INDETERMINATE}, all possible {@link DropSequence} items will be found.
	 * @return The {@link List} of {@link DropSequence} items filtered by specified {@link DropSequenceDirection}. Result set 
	 * is identical with {@link GameAnalyzer#filterPossibleSequences(GameBoard, DropColor)}, 
	 * if {@link DropSequenceDirection#INDETERMINATE} is opted.
	 */
	static List<DropSequence> getSequencesByDirection(GameBoard board, DropColor color, DropSequenceDirection direction)
	{
		List<DropSequence> possibleSequences = filterPossibleSequences(board, color);
		List<DropSequence> directionSequences = new ArrayList<DropSequence>();
		
		if (direction == DropSequenceDirection.INDETERMINATE)
		{
			return possibleSequences;
		}
		else
		{
			for (DropSequence sequence : filterPossibleSequences(board, color))
			{
				if (sequence.getDirection() == direction)
				{
					directionSequences.add(sequence);
				}
			}
			
			return directionSequences;
		}		
	}
	
	/**
	 * Filters possible {@link DropSequence} items, that could be supplemented by inserting a drop in
	 *  an adjacent {@link GameBoardSlot} before or after the sequence.
	 * @param board - The {@link GameBoard} to be analyzed.
	 * @param color - The {@link DropColor} to be respected.
	 * @return A {@link List} of {@link DropSequence} items with a length of 2 - 3 drops
	 */
	private static List<DropSequence> filterPossibleSequences(GameBoard board, DropColor color)
	{
		List<DropSequence> possibleSequences = new ArrayList<DropSequence>();
		
		// Filter the sequences, that allow drop insertion in adjacent slots
		
		for (DropSequence sequence : getAllSequences(board, color))
		{
			// Respect a sequence, if it's possible to set a drop in adjacent slots before / after the sequence
			
			if (sequence.getSlotBefore() != null || sequence.getSlotAfter() != null)
			{
				possibleSequences.add(sequence);
			}
		}
		
		return possibleSequences;
	}
	
	
	/**
	 * Searches for a column by random on the {@link GameBoard}.
	 * @param board - The {@link GameBoard} model representation.
	 * @return The identifier of affected {@link GameBoardColumn} as {@link String}.
	 */
	private static String getRandomCenterColumn(GameBoard board)
	{
		final int columnOffset = 2;		// Attempt of setting drop to the center columns
		
		// Attention - 1-based index
		
		int columnId = columnOffset + (new Random().nextInt(board.getColumns().size() - columnOffset));
		GameBoardColumn column = board.getColumnById(columnId);
				
		return column.getKey();
	}
	
	/**
	 * Searches and retrieves all sequences of drops with same {@link DropColor} 
	 * with a minimum length of 2 and a maximum length of 3 drops.
	 * @param board - The {@link GameBoard} to search for the drops.
	 * @param color - The {@link DropColor} to be searched for sequential drops.
	 * @return - A {@link List} of {@link DropSequence} containing all drop sequences 
	 * of specified {@link DropColor} with a length of 2 or 3 drops. 
	 */
	private static List<DropSequence> getAllSequences(GameBoard board, DropColor color)
	{
		List<DropSequence> sequences = new ArrayList<DropSequence>();
		
		// Search all sequences of 3 or 2 drops of same color
		
		List<DropSequence> seqOfThree = searchDropSequences(board, color, 3);
		List<DropSequence> seqOfTwo = searchDropSequences(board, color, 2);
		
		sequences.addAll(seqOfThree);

		// Exclude subsequences of length two included in such of length 3
		
		for (DropSequence dsTwo : seqOfTwo)
		{
			boolean isSubSequence = false;
			int seqCounter = 0;
			
			while (!isSubSequence && seqCounter < seqOfThree.size())
			{
				DropSequence dsThree = seqOfThree.get(seqCounter);
				isSubSequence = dsTwo.isSubSequenceOf(dsThree);
				
				seqCounter++;
			}
			
			if (!isSubSequence)
			{
				sequences.add(dsTwo);
			}
		}
		
		return sequences;
	}
	
	/**
	 * Searches all sequences of drops with same {@link DropColor} with specified length.
	 * @param board - The {@link GameBoard} to search for the drops.
	 * @param color - The {@link DropColor} to be searched for sequential drops.
	 * @param length - The dedicated length of the {@link DropSequence}.
	 * @return - A {@link List} of {@link DropSequence} containing all drop sequences with matching {@link DropColor} and length.
	 */
	private static List<DropSequence> searchDropSequences(GameBoard board, DropColor color, int length)
	{
		List<DropSequence> sequences = new ArrayList<DropSequence>();
		
		// Iterate through all rows / columns / diagonals of the game board
		
		for (GameBoardSlotCollection coll : board.getAll())
		{
			// Search each slot collection for a possible sequence
			
			DropSequence sequence = searchDropSequence(coll, color, length);
			
			// Only respect the sequence, if its length is > 0 and matching the specified length
			
			if (sequence.length() > 0 && sequence.length() == length)
			{
				sequences.add(sequence);
			}
		}
		
		return sequences;
	}
	
	/**
	 * Searches a sequence of drops with same {@link DropColor} with specified length.
	 * @param coll - The {@link GameBoardSlotCollection} to search for the drops.
	 * @param color - The {@link DropColor} to be searched for sequential drops.
	 * @param length - The dedicated length of the {@link DropSequence}.
	 * @return - A {@link DropSequence} containing all drops with matching {@link DropColor} and length. 
	 * If <code>length < 2</code> or <code>length > 3</code> an empty {@link DropSequence} is returned.
	 */
	private static DropSequence searchDropSequence(GameBoardSlotCollection coll, DropColor color, int length)
	{		
		boolean test = false;
		List<GameBoardSlot> slots = coll.asList();
		DropSequence sequence = new DropSequence();
		
		// Return an empty list for invalid length
		
		if (length < 2 || length > 3)
		{
			return sequence;
		}
		
		int i = 0;
		
		do
		{
			// Set boolean flag to true and get the first slot
			
			test = true;
			GameBoardSlot current = slots.get(i);

			// Add first slot by default, if color matches
			
			if (current.getColor() == color)
			{
				sequence.add(current);
				
				// Start searching the next slot for a sequence
				
				int j = i + 1;
				
				// Investigate in maximum the following slots up to the specified length of the sequence
				
				while (test && j < i + length && j < slots.size())
				{
					GameBoardSlot next = slots.get(j);
					sequence.add(next);
			
					// Check, if slots are not empty and if they equal in terms of the drop color
					
					test &= (!current.isEmpty()) && (!next.isEmpty()) && (current.getColor() == next.getColor());
					j++;
				}
				
				// If loop was left and the test condition failed, clear the sequence
				
				if (!test)
				{
					sequence.clear();
				}
			}
			else
			{
				test = false;
			}
			
			// Try next slot as start slot of the sequence
			
			i++;
			
		} while (!test && i < slots.size() - (length - 1));	// Outer loop condition; sequence of specified length not found and <= length slots left	
		
		
		// Immediately evaluate the sequence
		
		sequence.evaluate();
		
		return sequence;
	}
}
