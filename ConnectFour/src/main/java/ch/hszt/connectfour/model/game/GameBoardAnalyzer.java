/**
 * 
 */
package ch.hszt.connectfour.model.game;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import ch.hszt.connectfour.model.board.GameBoard;
import ch.hszt.connectfour.model.board.GameBoardColumn;
import ch.hszt.connectfour.model.board.GameBoardDiagonal;
import ch.hszt.connectfour.model.board.GameBoardRow;
import ch.hszt.connectfour.model.board.GameBoardSlot;
import ch.hszt.connectfour.model.board.GameBoardSlotCollection;
import ch.hszt.connectfour.model.enumeration.DropColor;
import ch.hszt.connectfour.model.enumeration.SkillLevel;

/**
 * Analyzes the situation on the {@link GameBoard} for 
 * an {@link CpuPlayer} in order to determine the best next turn.
 * @author Markus Vetsch
 * @version 1.0, 16.11.2011
 */
class GameBoardAnalyzer
{	
	private final CpuPlayer aiPlayer;
	private final DropColor aiColor;
	private final DropColor otherColor;
	private final SkillLevel aiLevel;
	
	GameBoardAnalyzer(final CpuPlayer aiPlayer)
	{
		this.aiPlayer = aiPlayer;
		this.aiColor = aiPlayer.getDropColor();
		this.otherColor = (aiColor == DropColor.RED) ? DropColor.YELLOW : DropColor.RED;
		this.aiLevel = aiPlayer.getLevel();
	}
	
//	private Map<String,Integer> getMatchingDropsPerColumn(GameBoard board)
//	{
//		Map<String, Integer> matchingDrops = new Hashtable<String, Integer>();
//		
//		for (GameBoardSlotCollection coll : board.getColumns())
//		{
//			GameBoardColumn column = (GameBoardColumn) coll;
//			int matchingDropCount = 0;
//			
//			for (GameBoardSlot slot : column)
//			{
//				if (!slot.isEmpty() && slot.getColor() == aiColor)
//				{
//					matchingDropCount++;
//				}
//			}
//			
//			matchingDrops.put(column.getKey(), Integer.valueOf(matchingDropCount));
//		}
//		
//		return matchingDrops;
//	}
//	
//	private Map<Integer, Integer> getMatchingDropsPerRow(GameBoard board)
//	{
//		Map<Integer, Integer> matchingDrops = new Hashtable<Integer, Integer>();
//		
//		for (GameBoardSlotCollection coll : board.getRows())
//		{
//			GameBoardRow row = (GameBoardRow) coll;
//			int matchingDropCount = 0;
//			
//			for (GameBoardSlot slot : row)
//			{
//				if (!slot.isEmpty() && slot.getColor() == aiColor)
//				{
//					matchingDropCount++;
//				}
//			}
//			
//			matchingDrops.put(Integer.valueOf(row.getId()), Integer.valueOf(matchingDropCount));
//		}
//		
//		return matchingDrops;
//	}
//	
//	private Map<String, Integer> getMatchingDropsPerDiagonal(GameBoard board)
//	{
//		Map<String, Integer> matchingDrops = new Hashtable<String, Integer>();
//		
//		for (GameBoardSlotCollection coll : board.getDiagonals())
//		{
//			GameBoardDiagonal diagonal = (GameBoardDiagonal) coll;
//			int matchingDropCount = 0;
//			
//			for (GameBoardSlot slot : diagonal)
//			{
//				if (!slot.isEmpty() && slot.getColor() == aiColor)
//				{
//					matchingDropCount++;
//				}
//			}
//			
//			matchingDrops.put(diagonal.getKey(), Integer.valueOf(matchingDropCount));
//		}
//		
//		return matchingDrops;
//	}
	
//	private List<List<GameBoardSlot>> getLongestCandidateSequence(GameBoard board, DropColor color)
//	{
//		List<List<GameBoardSlot>> candidates = new ArrayList<List<GameBoardSlot>>();
//		
//		for (GameBoardSlotCollection coll : board.getAll())
//		{
//			List<GameBoardSlot> slotList = coll.asList();
//			
//			for (int i = 0; i < slotList.size() - 1; i++)
//			{
//				GameBoardSlot current = slotList.get(i);
//				GameBoardSlot next = slotList.get(i+1);
//			}
//		}
//	}
	
//	
//	protected boolean isTopMostDropSameColor(String column)
//	{
//		return false;
//	}
//	
//	protected boolean hasSameColorDropRight(GameBoardSlot slot)
//	{
//		return false;
//	}
//	
//	protected boolean hasSameColorDropLeft(GameBoardSlot slot)
//	{
//		return false;
//	}
//	
//	protected boolean hasSameColorUpRight(GameBoardSlot slot)
//	{
//		return false;
//	}
//	
//	protected boolean hasSameColorDownRight(GameBoardSlot slot)
//	{
//		return false;
//	}
//	
//	protected boolean hasSameColorDownLeft(GameBoardSlot slot)
//	{
//		return false;
//	}
//	
//	protected boolean hasSameColorUpLeft(GameBoardSlot slot)
//	{
//		return false;
//	}
}
