/**
 * 
 */
package ch.hszt.connectfour.model.game;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import ch.hszt.connectfour.io.Serial;
import ch.hszt.connectfour.io.SerialObject;
import ch.hszt.connectfour.model.board.GameBoard;
import ch.hszt.connectfour.util.DateHelper;

/**
 * Provides useful meta information about the game.
 * @author Markus Vetsch
 * @version 1.0, 14.10.2011
 */
public class GameStatistic implements Serial
{
	private transient final Game game;
	
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
		startTime = DateHelper.now();
	}
	
	/**
	 * Retrieves the start time of associated {@link Game}.
	 * @return The start date and time as formatted {@link String}.
	 */
	public String getStartTime()
	{
		return DateHelper.getFormattedDate(startTime);
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
		
		return DateHelper.getFormattedDate(continuedTime);
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
		return DateHelper.getFormattedDate(endTime);
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
		long duration = DateHelper.now().getTime() - startTime.getTime();
		
		long hours = TimeUnit.MILLISECONDS.toHours(duration);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
		minutes = (hours == 0) ? minutes : minutes - (60 * hours);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(duration);
		seconds = (minutes == 0) ? seconds : seconds - (60 * minutes);
		
		// Format elapsed time to HH:mm:ss
		
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

	public void save(SerialObject obj)
	{
		obj.saveDate(startTime, "startTime");
		
		if (endTime != null)
		{
			obj.saveDate(endTime, "endTime");
		}
	}

	public Serial load(SerialObject obj)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
