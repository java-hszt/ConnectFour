
package ch.hszt.connectfour.control;

import ch.hszt.connectfour.exception.GameException;

import ch.hszt.connectfour.model.board.GameBoard;
import ch.hszt.connectfour.model.board.GameBoardSlot;
import ch.hszt.connectfour.model.game.Game;
import ch.hszt.connectfour.model.game.GameStatistic;
import ch.hszt.connectfour.model.game.GameStatus;
import ch.hszt.connectfour.model.game.Player;

/**
 * Encapsulates all the relevant game logic.
 * @author Markus Vetsch
 * @version 1.0, 13.10.2011
 */
public class GameEngine 
{
	private static final GameEngine instance =  new GameEngine();	// Singleton

	/**
	 * Can't be used for instance creation outside of the class.
	 * Use {@link GameEngine#getInstance()} instead to retrieve an instance of this class.
	 */
	private GameEngine() 
	{		
	}

	/**
	 * Returns the one and only {@link GameEngine} instance.
	 * @return The {@link GameEngine} instance.
	 */
	public static GameEngine getInstance()
	{
		return instance;
	}

	/**
	 * Sets a drop in specified column, if possible.
	 * @param game - The associated {@link Game}.
	 * @param column - The column identifier to set the drop into.
	 * @throws GameException Thrown, if a drop can't be inserted into specified column at this moment.
	 */
	public synchronized void setDrop(Game game, String column) throws GameException
	{
		if (game.isStarted())
		{
			GameStatus status = game.getStatus();
			GameBoard board = game.getBoard();
			Player current = status.getCurrentPlayer();
			
			try
			{
				GameBoardSlot slot = board.insertDrop(column, current.getColor());
				
				// Update drop count of player and update game status
				
				current.decrementDropCount();
				
				updateGame(game, slot);
			}
			catch (Exception ex)
			{
				throw new GameException(String.format("Drop couldn't be inserted into column %1$s! Reason: %2$s", column, ex.getMessage()),
										ex, game);
			}			
		}
		else
		{
			throw new GameException("Game is not started yet - no drops can be set!", game);
		}
	}

	/**
	 * Updates current game and underlying data (e.g. {@link GameStatus} or {@link GameStatistic}).
	 * Internally calls {@link GameEngine#updateGame(Game, GameBoardSlot)},
	 *  but as no {@link GameBoardSlot} affected, the argument remains <b>null</b>
	 * @param game - The {@link Game} to update.
	 */
	public synchronized void updateGame(Game game) 
	{
		updateGame(game, null);
	}
	
	/**
	 * Updates current game and underlying data (e.g. {@link GameStatus} or {@link GameStatistic})
	 * @param game - The {@link Game} to update.
	 * @param slot - The {@link GameBoardSlot}, that was updated.
	 */
	public synchronized void updateGame(Game game, GameBoardSlot slot)
	{
		GameStatus.update(game);
		game.notifyChanged(slot);
	}
}
