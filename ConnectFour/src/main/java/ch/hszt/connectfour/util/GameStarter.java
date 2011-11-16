/**
 * 
 */
package ch.hszt.connectfour.util;

import java.awt.Window;

import ch.hszt.connectfour.gui.GameFrame;
import ch.hszt.connectfour.gui.MainGameFrame;
import ch.hszt.connectfour.model.game.Game;
import ch.hszt.connectfour.model.game.Player;

/**
 * Utility class to trigger relevant logic and actions on starting a {@link Game}.
 * Provides different overloads of <code>GameStarter.launch</code> method to initiiate a {@link Game} with correct settings.
 * @author Markus Vetsch
 * @version 1.0, 14.11.2011
 */
public class GameStarter
{
	/**
	 * The helper class can't be instantiated.
	 */
	private GameStarter()
	{		
	}
	
	/**
	 * Launches the specified {@link Game}.
	 * @param game - The {@link Game} to be launched.
	 * @return - The {@link GameFrame} for user interaction.
	 */
	public static GameFrame launch(Game game)
	{
		return launch(null, game);
	}
	
	/**
	 * Launches the specified {@link Game}.
	 * @param owner - The {@link Window} acting as owner for the {@link GameFrame} to be returned.
	 * @param game - The {@link Game} to be launched.
	 * @return The {@link GameFrame} for user interaction, that already provides access to the started {@link Game}.
	 */
	public static GameFrame launch(Window owner, Game game)
	{
		return new MainGameFrame(owner, game);
	}
	
	/**
	 * Launches a new {@link Game} with specified {@link Player} as participants.
	 * @param either - The either {@link Player}.
	 * @param other - The other {@link Player}.
	 * @return The {@link GameFrame} for user interaction, that already provides access to the started {@link Game}.
	 */
	public static GameFrame launch(Player either, Player other)
	{
		return launch(null, either, other);
	}
	
	/**
	 * Launches a new {@link Game} with specified {@link Player} as participants.
	 * @param owner - The {@link Window} acting as owner for the {@link GameFrame} to be returned.
	 * @param either - The either {@link Player}.
	 * @param other - The other {@link Player}.
	 * @return The {@link GameFrame} for user interaction, that already provides access to the started {@link Game}.
	 */
	public static GameFrame launch(Window owner, Player either, Player other)
	{
		Game game = new Game(either, other);
		return new MainGameFrame(owner, game);
	}
}
