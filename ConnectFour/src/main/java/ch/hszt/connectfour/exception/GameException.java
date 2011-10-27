/**
 * Package providing customized classes for exception handling.
 */
package ch.hszt.connectfour.exception;

import ch.hszt.connectfour.model.game.Game;

/**
 * Custom exception class for {@link Game} specific exception handling.
 * @author Markus Vetsch
 * @version 1.0, 27.10.2011
 */
public class GameException extends Exception
{
	private Game game;

	/**
	 * @see java.lang.Exception#Exception().
	 */
	public GameException()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see java.lang.Exception#Exception(String)
	 */
	public GameException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see java.lang.Exception#Exception(Throwable)
	 */
	public GameException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see java.lang.Exception#Exception(String, Throwable)
	 */
	public GameException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Creates a customized exception for specified {@link Game} with the dedicated message text.
	 * @param game - The {@link Game} to link the exception to.
	 * @see java.lang.Exception#Exception(String)
	 */
	public GameException(String message, Game game)
	{
		super(message);
		this.game = game;
	}

	/**
	 * Creates a customized exception for specified {@link Game} with the dedicated message text.
	 * @param game - The {@link Game} to link the exception to.
	 * @see java.lang.Exception#Exception(String, Throwable)
	 */
	public GameException(String message, Throwable cause, Game game)
	{
		super(message, cause);
		this.game = game;
	}
	
	/**
	 * Gets access to the {@link Game} this {@link GameException} is linked to.
	 * @return - The {@link Game} the exception was raised for.
	 */
	public Game getGame()
	{
		return game;
	}

}
