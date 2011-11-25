/**
 * 
 */
package ch.hszt.connectfour.model.game;

import java.util.List;
import java.util.Random;

import ch.hszt.connectfour.model.board.GameBoard;
import ch.hszt.connectfour.model.board.GameBoardColumn;
import ch.hszt.connectfour.model.board.GameBoardSlot;
import ch.hszt.connectfour.model.enumeration.DropColor;
import ch.hszt.connectfour.model.enumeration.DropSequenceDirection;
import ch.hszt.connectfour.model.enumeration.SkillLevel;

/**
 * Represents a {@link CpuPlayer} with {@link SkillLevel#EASY}.
 * @author Markus Vetsch
 * @version 1.0, 31.10.2011
 */
public final class EasyCpuPlayer extends CpuPlayer
{
  	/**
   	* Initializes important members of the player instance - to be called via concrete subclasses.
   	* @param name - the name of the {@link Player}. 
   	* @param level - the {@link SkillLevel} to assign to this {@link Player}
   	*/
	EasyCpuPlayer(String name, SkillLevel level)
	{
		super(name, level);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.model.game.CpuPlayer#determineNextTurn()
	 */
	@Override
	public String determineNextTurn(Game game)
	{
		if (GameAnalyzer.isOpeningTurn(game))
		{
			return GameAnalyzer.createOpeningTurn(game);
		}
		
		GameBoard board = game.getBoard();
		
		DropColor own = getDropColor();
		DropColor opponent = (own == DropColor.RED) ? DropColor.YELLOW : DropColor.RED;
		
		List<DropSequence> ownSequences = gatherOwnSequences(game);
		List<DropSequence> opponentSequences = gatherOpponentSequences(game);
		List<SingleDropSequence> singles = GameAnalyzer.getSingleSequences(board, own);
		
		DropSequence selection = null;
		String turn = null;
		
		if (ownSequences.size() > 0)
		{
			selection = ownSequences.get(new Random().nextInt(ownSequences.size()));
		}
		else if (opponentSequences.size() > 0)
		{
			selection = opponentSequences.get(new Random().nextInt(opponentSequences.size()));
		}
		else if (singles.size() > 0)
		{
			selection = singles.get(new Random().nextInt(singles.size()));
		}
		
		if (selection != null)
		{
			turn = selection.getTurn();
		}
		
		if (turn == null)
		{
			turn = GameAnalyzer.findColumnWithEmptySlots(board);;
		}
		
		return turn;
	}

	@Override
	protected List<DropSequence> gatherOwnSequences(Game game)
	{
		DropSequenceDirection searchDirection = DropSequenceDirection.COLUMN;
		return GameAnalyzer.getSequencesByDirection(game.getBoard(), getDropColor(), searchDirection);
	}

	@Override
	protected List<DropSequence> gatherOpponentSequences(Game game)
	{
		DropColor opponentColor = (getDropColor() == DropColor.RED) ? DropColor.YELLOW : DropColor.RED;
		
		DropSequenceDirection searchDirection = DropSequenceDirection.COLUMN;
		return GameAnalyzer.getSequencesByDirection(game.getBoard(), opponentColor, searchDirection);
	}
}
