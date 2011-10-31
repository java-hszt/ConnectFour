/**
 * 
 */
package ch.hszt.connectfour.model.enumeration;

import ch.hszt.connectfour.model.game.Game;
import ch.hszt.connectfour.model.game.Player;

/**
 * Represents the type of {@link Player} for the {@link Game}.
 * @author Markus Vetsch
 * @version 1.0, 29.10.2011
 */
public enum PlayerType
{
	HUMAN("Human Player"),
	AI_PLAYER("AI Player");
	
	private String niceName;
	
	private PlayerType(String niceName)
	{
		this.niceName = niceName;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString()
	{
		return niceName;
	}
}
