/**
 * 
 */
package ch.hszt.connectfour.model.game;

import java.util.Random;
import ch.hszt.connectfour.model.enumeration.DropColor;

/**
 * Stores and provides relevant settings of a {@link Game}.
 * @author Markus Vetsch
 * @version 1.0, 14.10.2011
 */
public class GameSettings
{
	/**
	 * Defines the initial count of drops per player.
	 */
	public final static int NUMBER_OF_DROPS = 21;
	
	private final Player firstPlayer;
	private final Player secondPlayer;
	
	private Player startPlayer;
	
	/**
	 * Creates the game settings.
	 * @param firstPlayer - The first {@link Player} not necessarily being Player represented by {@link GameSettings#getStartPlayer()}.
	 * @param secondPlayer - The second {@link Player} not necessarily being Player represented by {@link GameSettings#getOtherPlayer()()}.
	 */
	GameSettings(final Player firstPlayer, final Player secondPlayer)
	{
		this.firstPlayer = firstPlayer;
		this.secondPlayer = secondPlayer;
		
		// Assign drop color
		
		assignDropColor();
		
		// Determine start player randomly
		
		startPlayer = evaluateStartPlayer();
	}
	
	/**
	 * Returns the {@link Player} opening the game with the first turn.
	 * @return The {@link Player} opening the game.
	 */
	Player getStartPlayer()
	{
		return startPlayer;
	}
	
	/**
	 * Returns the {@link Player} being the successor of {@link GameSettings#getStartPlayer()}.
	 * @return The successor {@link Player} of {@link GameSettings#getStartPlayer()}.
	 */
	Player getOtherPlayer()
	{
		return (firstPlayer.equals(startPlayer)) ? secondPlayer : firstPlayer;
	}
	
	/**
	 * Gets the {@link Player} instance linked to specified {@link DropColor};
	 * @param color - The {@link DropColor} to look up.
	 * @return The {@link Player} associated to the specified {@link DropColor}.
	 */
	Player getPlayerByColor(DropColor color)
  	{
  		return (firstPlayer.getDropColor() == color) ? firstPlayer : secondPlayer;
  	}
	
	/**
	 * Reevaluates the start player opening the game.
	 */
	void reEvaluateStartPlayer()
	{
		startPlayer = evaluateStartPlayer();
	}
	
	private void assignDropColor()
	{
		firstPlayer.setDropColor(randomColor());
		
		if (firstPlayer.getDropColor() == DropColor.RED)
		{
			secondPlayer.setDropColor(DropColor.YELLOW);
		}
		else
		{
			secondPlayer.setDropColor(DropColor.RED);
		}
	}
	
	private DropColor randomColor()
	{
		Random rand = new Random();
		return DropColor.parse(rand.nextInt(2));
	}

  	private Player evaluateStartPlayer() 
  	{
		DropColor color = randomColor();
		
		if (color != DropColor.UNKNOWN)
		{
			return getPlayerByColor(color);
		}
		else
		{
			// TODO: eventually throw exception
			return null;
		}			
  	}
}
