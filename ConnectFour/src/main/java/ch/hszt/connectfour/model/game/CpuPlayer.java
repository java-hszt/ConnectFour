/**
 * Encapsulates the data model for classes in the direct game context.
 */
package ch.hszt.connectfour.model.game;

import ch.hszt.connectfour.model.board.GameBoard;
import ch.hszt.connectfour.model.enumeration.PlayerType;
import ch.hszt.connectfour.model.enumeration.SkillLevel;

/**
 * Represents an artificial intelligence {@link Player} represented by the CPU.
 * @author Markus Vetsch
 * @version 1.0, 11.10.2011
 */
public abstract class CpuPlayer extends Player
{
  	/**
   	* Initializes important members of the player instance - to be called via concrete subclasses.
   	* @param name - the name of the {@link Player}. 
   	* @param level - the {@link SkillLevel} to assign to this {@link Player}
   	*/
	protected CpuPlayer(String name, SkillLevel level)
	{
		super(PlayerType.CPU_PLAYER, name, level);
	}

	/**
	 * Determines the column of next turn of the {@link CpuPlayer}.
	 * @param board - The {@link GameBoard} for evaluation of next turn.
	 * @return The column identifier for execution of next turn. 
	 */
	public abstract String determineNextTurn(GameBoard board);
}
