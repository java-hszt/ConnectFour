/**
 * 
 */
package ch.hszt.connectfour.model.game;

import ch.hszt.connectfour.model.board.GameBoard;
import ch.hszt.connectfour.model.enumeration.SkillLevel;

/**
 * @author Markus
 *
 */
public class AdvancedAIPlayer extends AIPlayer
{
  	/**
   	* Initializes important members of the player instance - to be called via concrete subclasses.
   	* @param name - the name of the {@link Player}. 
   	* @param level - the {@link SkillLevel} to assign to this {@link Player}
   	*/
	public AdvancedAIPlayer(String name, SkillLevel level)
	{
		super(name, level);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.model.game.AIPlayer#determineNextTurn()
	 */
	@Override
	public String determineNextTurn(GameBoard board)
	{
		// TODO: Implement logic for next turn determination
		return null;
	}

}