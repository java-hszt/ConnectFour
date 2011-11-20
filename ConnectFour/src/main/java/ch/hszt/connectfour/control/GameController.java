package ch.hszt.connectfour.control;

import ch.hszt.connectfour.exception.GameException;
import ch.hszt.connectfour.model.board.GameBoardSlot;
import ch.hszt.connectfour.model.game.Game;
import ch.hszt.connectfour.model.game.GameStatistic;
import ch.hszt.connectfour.model.game.GameStatus;

/**
 * Handles and delegates communication between GUI and underlying model.
 * @author Markus Vetsch
 * @version 1.0, 22.10.2011
 */
public abstract class GameController
{
	// These methods fulfill general communication from user interface to the model
	// such as (starting, stopping, restarting game, execution of single turn) and thus aren't declared abstract.
	// Nevertheless, feel free to override the methods for additional logic to be accomplished.
	
	/**
	 * Starts the specified {@link Game}.
	 * @param game - The {@link Game} to be started.
	 * @throws GameException Thrown, if any unexpected error occurred during the start of the {@link Game}.
	 */
	public void startGame(Game game) throws GameException
	{
		game.start();
		
		printMessage("Game successfully started ...");
		
		GameEngine.getInstance().updateGame(game);
	}
	
	/**
	 * Stops the specified {@link Game}.
	 * @param game - The {@link Game} to be stopped.
	 * @throws GameException Thrown, if any unexpected error occurred while stopping the {@link Game}.
	 */
	public void stopGame(Game game) throws GameException
	{
		game.stop();
	}
	
	/**
	 * Restarts the specified {@link Game}.
	 * @param game - The {@link Game} to be restarted.
	 * @throws GameException Thrown, if any unexpected error occurred during the restart of the {@link Game}.
	 */
	public void restartGame(Game game) throws GameException
	{
		game.restart();
		GameEngine.getInstance().updateGame(game);
	}
	
	/**
	 * Executes a turn on specified column for the {@link Game}.
	 * @param game - The corresponding {@link Game} to execute a turn for.
	 * @param column - The column to be affected by the turn.
	 * @throws GameException Thrown, if any unexpected problem occurred during execution of the turn.
	 */
	public void executeTurn(Game game, String column) throws GameException
	{
		GameEngine.getInstance().setDrop(game, column);
	}
	
	/**
	 * Refreshes all relevant information for specified {@link Game}.
	 * @param game - The {@link Game} to refresh all relevant information such as {@link GameStatistic} and {@link GameStatus}.
	 */
	public void refreshAll(Game game)
	{
		refreshStatus(game.getStatus());
		refreshStatistic(game.getStatistic());
	}
	
	// These methods define communication from the model to the user interface
	// and shall be implemented depending on the realization of the user interface.
	// That's why they're declared abstract
	
	/**
	 * Refreshes the specified {@link GameStatus}.
	 * @param status - The {@link GameStatus} to be refreshed.
	 */
	public abstract void refreshStatus(GameStatus status);
	
	/**
	 * Refreshes the specified {@link GameStatistic}.
	 * @param statistic - The {@link GameStatistic} to be refreshed.
	 */
	public abstract void refreshStatistic(GameStatistic statistic);
	
	/**
	 * Refreshes the appearance of specified {@link GameBoardSlot}.
	 * @param slot - The {@link GameBoardSlot} to be refreshed.
	 */
	public abstract void refreshSlot(GameBoardSlot slot);
	
	/**
	 * Refreshes a set of {@link GameBoardSlot} instances, over which iteration is possible.
	 * @param slots - A set of {@link GameBoardSlot} allowing element-wise iteration.
	 */
	public abstract void refreshSlots(Iterable<GameBoardSlot> slots);
	
	/**
	 * Prints the specified message.
	 * @param message - The message to be printed to a dedicated device.
	 */
	public abstract void printMessage(String message);
}
