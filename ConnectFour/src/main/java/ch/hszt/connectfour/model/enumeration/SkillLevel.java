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
	EASY(0, "Easy"),
	/**
	 * Represents the advanced {@link SkillLevel}.
	 */
	ADVANCED(1, "Advanced"),
	/**
	 * Represents the hard {@link SkillLevel}.
	 */
	HARD(2, "Hard");
	
	private final int id;
	private final String name;
	
	/**
	 * Creates an instance of this enumeration with associated ID.
	 * @param id - The underlying ID.
	 */
	private SkillLevel(final int id, final String name)
	{
		this.id = id;
		this.name = name;
	}	
	
	/**
	 * Creates the associated {@link SkillLevel} for specified ID.
	 * @param id - The ID of the {@link SkillLevel}.
	 * @return The corresponding {@link SkillLevel} or {link SkillLevel#EASY} for an unknown ID.
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
				return EASY;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString()
	{
		return name;
	}
}
