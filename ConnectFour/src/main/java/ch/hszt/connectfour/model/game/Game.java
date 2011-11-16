
package ch.hszt.connectfour.model.game;

import java.util.Observable;
import ch.hszt.connectfour.control.GameEngine;
import ch.hszt.connectfour.control.GameObserver;
import ch.hszt.connectfour.exception.GameException;
import ch.hszt.connectfour.io.Serial;
import ch.hszt.connectfour.io.SerialObject;
import ch.hszt.connectfour.model.board.GameBoard;
import ch.hszt.connectfour.model.board.GameBoardSlot;
import ch.hszt.connectfour.model.enumeration.DropColor;
import ch.hszt.connectfour.util.DateHelper;

/**
 * Represents the abstract definition of a game.
 * Extends {@link Observable} to offer observers notifications about changes on a {@link Game} instance.
 * @author Markus Vetsch
 * @version 1.0, 11.10.2011 
 */
public class Game extends Observable implements Serial
{
	private boolean isStarted;
	
  	private final GameBoard board;  
  	
  	private GameStatus status;
  	private GameStatistic statistic;
  	private GameSettings settings;

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
  	
  	/**
  	 * Sets an observer to watch this instance for any modifications.
  	 * @param observer - The {@link GameObserver} for surveillance.
  	 */
  	public void setObserver(GameObserver observer)
  	{
  		addObserver(observer);
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
  	 * @throws GameException Thrown, if the {@link Game} wasn't started yet.
  	 * The latter can be checked by evaluating the flag {@link Game#isStarted()}.
  	 */
  	public void stop() throws GameException
  	{
  		if (!isStarted())
  		{
  			throw new GameException("Game can't be stopped, as it wasn't started yet!", this);
  		}
  		
  		isStarted = false;
  		
  		statistic.setEndTime(DateHelper.now());
		
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

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.io.Serial#save(ch.hszt.connectfour.io.SerialObject)
	 */
	public void save(SerialObject obj)
	{
		obj.saveBoolean(isStarted, "isStarted");
		obj.saveSerial(settings);
		obj.saveSerial(statistic);
		obj.saveSerial(status);
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.io.Serial#load(ch.hszt.connectfour.io.SerialObject)
	 */
	public Serial load(SerialObject obj)
	{
		//TODO deserialization of game
  		statistic.setContinuedTime(DateHelper.now());
		
		// TODO Auto-generated method stub
		return null;
	}
}
