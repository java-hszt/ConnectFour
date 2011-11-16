/**
 * Provides a set of useful enumerations.
 */
package ch.hszt.connectfour.model.enumeration;

import java.awt.Color;

/**
 * Enumeration representing the different drop colors.
 * @author Markus Vetsch
 * @version 1.0, 11.10.2011
 */
public enum DropColor
{	
	/**
	 * Represents drops with red color.
	 */
	RED(0, "Red"),
	/**
	 * Represents drops with yellow color
	 */
	YELLOW(1, "Yellow"),
	/**
	 * Represents drops with an unspecified color.
	 */
	UNKNOWN(-1, "Unknown");
	
	private final int id;
	private String niceName;
	
	/**
	 * Creates an instance of this enumeration with associated ID.
	 * @param id - The underlying ID.
	 */
	private DropColor(final int id, final String niceName)
	{
		this.id = id;
		this.niceName = niceName;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString()
	{
		return niceName;
	}
	
	/**
	 * Returns the corresponding AWT {@link Color} for UI representation.
	 * @return The corresponding AWT {@link Color}.
	 */
	public Color toAwtColor()
	{
		return (id == 0) ? Color.RED : Color.YELLOW;
	}

	/**
	 * Creates the associated {@link DropColor} for specified ID.
	 * @param id - The ID of the {@link DropColor}.
	 * @return The corresponding {@link DropColor} or {link DropColor#UNKNOWN} for an unknown ID.
	 */
	public static DropColor parse(int id)
	{
		switch(id)
		{
			case 0:
				return RED;
			case 1:
				return YELLOW;
			default:
				return UNKNOWN;
		}
	}
}
