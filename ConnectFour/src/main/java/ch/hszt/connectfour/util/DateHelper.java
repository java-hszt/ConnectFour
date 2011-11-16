/**
 * 
 */
package ch.hszt.connectfour.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Simple helper class for formatting {@link Date} instances.
 * @author Markus Vetsch
 * @version 1.0, 13.11.2011
 */
public class DateHelper
{
	private static DateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	
	/**
	 * Helper class can't be instantiated.
	 */
	private DateHelper()
	{		
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
	 * Returns the current date and time as {@link String}.
	 * @return The current date and time.
	 */
	public static String nowAsString()
	{
		return dateFormatter.format(now());
	}
	
	/**
	 * Returns a formatted {@link String} of passed {@link Date} instance.
	 * @param date - The date / time to be formatted
	 * @return The formatted date / time as {@link String}.
	 */
	public static String getFormattedDate(Date date)
	{
		return dateFormatter.format(date);
	}
}
