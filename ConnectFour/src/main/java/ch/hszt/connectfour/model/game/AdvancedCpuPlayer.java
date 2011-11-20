/**
 * 
 */
package ch.hszt.connectfour.model.game;

import ch.hszt.connectfour.model.board.GameBoard;
import ch.hszt.connectfour.model.enumeration.SkillLevel;

/**
 * Represents {@link CpuPlayer} with {@link SkillLevel#ADVANCED}.
 * @author Markus Vetsch
 * @version 1.0, 31.10.2011
 */
public final class AdvancedCpuPlayer extends CpuPlayer
{
  	/**
   	* Initializes important members of the player instance - to be called via concrete subclasses.
   	* @param name - the name of the {@link Player}. 
   	* @param level - the {@link SkillLevel} to assign to this {@link Player}
   	*/
	AdvancedCpuPlayer(String name, SkillLevel level)
	{
		super(name, level);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.model.game.CpuPlayer#determineNextTurn()
	 */
	@Override
	public String determineNextTurn(GameBoard board)
	{
		// TODO: Implement logic for next turn determination
		return null;
	}

}
