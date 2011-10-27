/**
 * 
 */
package ch.hszt.connectfour.model.enumeration;

/**
 * Enumeration representing the different skill levels.
 * @author Markus Vetsch
 * @version 1.0, 11.10.2011
 */
public enum SkillLevel
{
	/**
	 * Represents the easy {@link SkillLevel}.
	 */
	EASY(0),
	/**
	 * Represents the advanced {@link SkillLevel}.
	 */
	ADVANCED(1),
	/**
	 * Represents the hard {@link SkillLevel}.
	 */
	HARD(2),
	/**
	 * Represents an unspecified {@link SkillLevel}.
	 */
	UNKNOWN(-1);
	
	private final int id;
	
	/**
	 * Creates an instance of this enumeration with associated ID.
	 * @param id - The underlying ID.
	 */
	private SkillLevel(final int id)
	{
		this.id = id;
	}	
	
	/**
	 * Creates the associated {@link SkillLevel} for specified ID.
	 * @param id - The ID of the {@link SkillLevel}.
	 * @return The corresponding {@link SkillLevel} or {link SkillLevel#UNKNOWN} for an unknown ID.
	 */
	public static SkillLevel parse(int id)
	{
		switch(id)
		{
			case 0:
				return EASY;
			case 1:
				return ADVANCED;
			case 2:
				return HARD;
			default:
				return UNKNOWN;
		}
	}
}
