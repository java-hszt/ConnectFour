
package ch.hszt.connectfour.model.game;

import java.util.Observable;
import ch.hszt.connectfour.control.GameEngine;
import ch.hszt.connectfour.control.GameObserver;
import ch.hszt.connectfour.exception.GameException;
import ch.hszt.connectfour.model.board.GameBoard;
import ch.hszt.connectfour.model.board.GameBoardSlot;
import ch.hszt.connectfour.model.enumeration.DropColor;

/**
 * @author Markus Vetsch
 * @version 1.0, 11.10.2011
 * Represents an the definition of a game.
 */
public class Game extends Observable
{
	private boolean isStarted;
	
  	private final GameBoard board;  
  	
  	private GameStatus status;
  	private GameStatistic statistic;
  	private GameSettings settings;
  	
//  	private GameObserver observer;

  	/**
  	 * Creates an instance of a {@link Game} with associated pair of {@link Player}.
  	 * @param firstPlayer - The first {@link Player}, i.e. not necessarily the {@link Player} defined by {@link Game#getStartPlayer()}.
  	 * @param secondPlayer - The first {@link Player}, i.e. not necessarily the {@link Player} defined by {@link Game#getOtherPlayer()}.
  	 */
  	public Game(final Player firstPlayer, final Player secondPlayer) 
  	{
  		// initialization of game board and game settings
  		
  		isStarted = false;
  		
		board = new GameBoard();
		settings = new GameSettings(firstPlayer, secondPlayer);
  	}
  	
  	public void setObserver(GameObserver observer)
  	{
  		addObserver(observer);
		// 	this.observer = observer;
  	}
  	
  	/**
  	 * Returns the information, whether the current {@link Game} was already started earlier.
  	 * @return <b>true</b>, if the {@link Game} was already started; otherwise <b>false</b>.
  	 */
  	public boolean isStarted()
  	{
  		return isStarted;
  	}

  	/**
  	 * Enables access to the {@link GameBoard}
  	 * @return The corresponding {@link GameBoard}.
  	 */
  	public GameBoard getBoard() 
  	{
		return board;
  	}

  	/**
  	 * Returns the {@link Player} opening the {@link Game} with the first turn.
  	 * @return - The {@link Player} opening the {@link Game}.
  	 */
  	public Player getStartPlayer() 
  	{
		return settings.getStartPlayer();
  	}
  	
  	/**
  	 * Returns the {@link Player}, that is the successor of {@link Game#getStartPlayer()}.
  	 * @return The successor of {@link Game#getStartPlayer()}.
  	 */
  	public Player getOtherPlayer()
  	{
  		return settings.getOtherPlayer();
  	}
  	
  	/**
  	 * Determines the associated {@link Player} for the specific {@link DropColor}.
  	 * @param color - The {@link DropColor}, which the {@link Player} was assigned.
  	 * @return The {@link Player} using the specified {@link DropColor} or <b>null</b>, if {@link DropColor#UNKNOWN} was specified.
  	 */
  	public Player getPlayerByColor(DropColor color)
  	{
  		return settings.getPlayerByColor(color);
  	}

  	/**
  	 * Grants access to the current {@link GameStatus}.
  	 * @return The current {@link GameStatus}.
  	 */
  	public GameStatus getStatus()
  	{
  		return status;
  	}
  	
  	/**
  	 * Sets the current {@link GameStatus}.
  	 * @param status - The {@link GameStatus} to be set.
  	 */
  	void setStatus(GameStatus status)
  	{
  		this.status = status;
  	}
  	
  	/**
  	 * Grants access to the current {@link GameStatistic}.
  	 * @return The current {@link GameStatistic}.
  	 */
  	public GameStatistic getStatistic()
  	{
  		return statistic;
  	}
  	
  	/**
  	 * Notifies any associated {@link GameObserver} about changes within the game model.
  	 */
  	public void notifyChanged()
  	{
  		notifyChanged(null);
  	}
  	
  	/**
  	 * Notifies any associated {@link GameObserver} about changes within the game model.
  	 * @param slot - If not <b>null</b>, this specifies an update of the corresponding {@link GameBoardSlot}.
  	 */
  	public void notifyChanged(GameBoardSlot slot)
  	{
  		setChanged();
  		notifyObservers(slot);
  	}

  	/**
  	 * Executes all dedicated actions to start the {@link Game}.
  	 * @throws GameException Thrown, if the {@link Game} was already started earlier.
  	 * The latter can be checked by evaluating the flag {@link Game#isStarted()}.
  	 */
  	public void start() throws GameException
  	{
  		if (isStarted())
  		{
  			throw new GameException("Game can't be started, as it was already started before!", this);
  		}
  		
  		// Create game statistic and immediately update game status for the first time
	  		
		statistic = new GameStatistic(this);
		GameEngine.getInstance().updateGame(this);
		
		isStarted = true;
  	}

  	/**
  	 * Executes all dedicated actions to stop the {@link Game}
  	 * @throws IllegalStateException Thrown, if the {@link Game} wasn't started yet.
  	 * The latter can be checked by evaluating the flag {@link Game#isStarted()}.
  	 */
  	public void stop() throws GameException
  	{
  		if (!isStarted())
  		{
  			throw new GameException("Game can't be stopped, as it wasn't started yet!", this);
  		}
  		
  		isStarted = false;
  		
  		statistic.setEndTime(GameStatistic.now());
		
  		//TODO serialization of game  		
  	}
  	
  	/**
  	 * Restarts the current {@link Game}.
  	 * @throws GameException Thrown, if the {@link Game} was already started earlier or 
  	 * if any unexpected problems on restart of the {@link Game} occurred.
  	 */
  	public void restart() throws GameException
  	{
  		// perform reset only, if game was started
  		
  		if (isStarted())
  		{ 
  			// Clear status and statistics
  			
  			status = null;
  			statistic = null;
  			
  			isStarted = false;
  			
  			// reset the game board
  			
  			board.reset();
  			
  			// reevaluate the start player
  			
  			settings.reEvaluateStartPlayer();
  			
  			// finally restart the game
  			
  			start();
  		}
  		else
  		{
  			throw new GameException("Game can't be restarted, as it wasn't started before!", this);
  		}
  	}
  	
  	/**
  	 * Executes all dedicated actions to save the {@link Game}.
  	 */
  	public void save()
  	{
  		//TODO serialization of game
  	}
  	  	
  	/**
  	 * Executes all dedicated actions to restore the {@link Game}.
  	 */
  	public void load()
  	{
  		//TODO deserialization of game
  		statistic.setContinuedTime(GameStatistic.now());
  	}
}
