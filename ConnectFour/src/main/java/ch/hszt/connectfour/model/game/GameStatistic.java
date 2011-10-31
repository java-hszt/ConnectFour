/**
 * 
 */
package ch.hszt.connectfour.model.game;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import ch.hszt.connectfour.model.board.GameBoard;

/**
 * Provides useful meta information about the game.
 * @author Markus Vetsch
 * @version 1.0, 14.10.2011
 */
public class GameStatistic
{
	private static DateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	
	private final Game game;
	
	private Date startTime;
	private Date continuedTime;
	private Date endTime;
	
	/**
	 * Creates a new instance of {@link GameStatistic} linked to the specified {@link Game}.
	 * @param game - The {@link Game} this {@link GameStatistic} instance belongs to.
	 */
	GameStatistic(final Game game)
	{
		this.game = game;
		startTime = now();
	}	
	
	/**
	 * Returns the current date and time as {@link Date}.
	 * @return The current date and time.
	 */
	public static Date now()
	{
		return Calendar.getInstance().getTime();
	}
	
	/**
	 * Retrieves the start time of associated {@link Game}.
	 * @return The start date and time as formatted {@link String}.
	 */
	public String getStartTime()
	{
		return dateFormatter.format(startTime);
	}
	
	/**
	 * Returns the date and time, at which the {@link Game} was continued,
	 *  if it's been restored from a previous session.
	 * @return The date and time, at which the {@link Game} was continued or <b>NULL</b> otherwise.
	 */
	public String getContinuedTime()
	{
		if (continuedTime == null)
		{
			return null;
		}
		
		return dateFormatter.format(continuedTime);
	}
	
	/**
	 * Sets the date / time, at which a {@link Game} was continued.
	 * @param continuedTime - The date / time, at which the {@link Game} was continued.
	 */
	public void setContinuedTime(Date continuedTime)
	{
		this.continuedTime = continuedTime;
	}
	
	/**
	 * Retrieves the end date and time of associated {@link Game}.
	 * @return The end date and time as formatted {@link String}.
	 */
	public String getEndTime()
	{
		return dateFormatter.format(endTime);
	}
	
	/**
	 * Sets the date / time, at which a {@link Game} ended.
	 * @param endTime The date / time, at which a {@link Game} ended.
	 */
	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}
	
	/**
	 * Retrieves a {@link String} representation of the duration of current {@link Game}
	 * in format HH:mm:ss.
	 * @return The {@link Game} duration as {@link String} representation.
	 */
	public String getDuration()
	{
		long duration = now().getTime() - startTime.getTime();
		
		long hours = TimeUnit.MILLISECONDS.toHours(duration);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
		minutes = (hours == 0) ? minutes : minutes - (60 * hours);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(duration);
		seconds = (minutes == 0) ? seconds : seconds - (60 * minutes);
		
		return String.format("%1$02d:%2$02d:%3$02d", hours, minutes, seconds);
	}
	
	/**
	 * Retrieves the count of drops on associated {@link GameBoard}.
	 * @return The count of drops currently on {@link GameBoard}.
	 */
	public int getNumberOfDropsOnBoard()
	{
		if (game.getStatus() == null)
		{
			return 0;
		}
		
		return game.getStatus().countTotalDrops();
	}
}
