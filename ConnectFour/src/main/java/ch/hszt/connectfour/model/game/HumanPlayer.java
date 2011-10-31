/**
 * 
 */
package ch.hszt.connectfour.model.game;

import ch.hszt.connectfour.model.enumeration.DropColor;
import ch.hszt.connectfour.model.enumeration.PlayerType;
import ch.hszt.connectfour.model.enumeration.SkillLevel;

/**
 * Represents a real human being {@link Player}.
 * @author Markus Vetsch
 * @version 1.0, 11.10.2011
 */
public class HumanPlayer extends Player
{
  	/**
   	* Initializes important members of the player instance - to be called via concrete subclasses.
   	* @param name - the name of the {@link Player}.
   	*/
	public HumanPlayer(String name)
	{
		super(PlayerType.HUMAN, name);
	}
}
