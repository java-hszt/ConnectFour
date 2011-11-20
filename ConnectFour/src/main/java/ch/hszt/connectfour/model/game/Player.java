
package ch.hszt.connectfour.model.game;

import ch.hszt.connectfour.io.Serial;
import ch.hszt.connectfour.io.SerialObject;
import ch.hszt.connectfour.model.enumeration.DropColor;
import ch.hszt.connectfour.model.enumeration.PlayerType;
import ch.hszt.connectfour.model.enumeration.SkillLevel;
/**
 * Abstract class for any player participating at a game.
 * @author Markus Vetsch
 * @version 1.0, 11.10.2011
 */
public abstract class Player implements Serial
{
	private final String name;  	 	
  	private final SkillLevel level;
  	private final PlayerType type;
  	
  	private DropColor color; 
  	private int dropCount;
  	
  	/**
   	* Initializes important members of the player instance - to be called via concrete subclasses.
  	 * @param type - The associated {@link PlayerType}.
  	 * @param name - the name of the {@link Player}.
   	*/
  	protected Player(PlayerType type, final String name)
  	{
  		this(type, name, SkillLevel.EASY);
  	}

  	/**
   	* Initializes important members of the player instance - to be called via concrete subclasses.
  	 * @param type - The associated {@link PlayerType}.
  	 * @param name - the name of the {@link Player}.
  	 * @param level - the {@link SkillLevel} to assign to this {@link Player}
   	*/
  	protected Player(PlayerType type, final String name, final SkillLevel level) 
  	{
  		this.type = type;
		this.name = name;
		this.level = level;
		
		// define initial drop count
		
		dropCount = GameSettings.NUMBER_OF_DROPS;
  	}
  	
  	/**
  	 * Resets the drop count of current {@link Player} to the default value {@link GameSettings#NUMBER_OF_DROPS}.
  	 */
  	public void resetDropCount()
  	{
  		dropCount = GameSettings.NUMBER_OF_DROPS;
  	}
  	
  	/**
  	 * Determines, whether this {@link Player} is a human player.
  	 * @return <b>true</b>, if this instance is human player; otherwise <b>false</b>, if the {@link Player} is a CPU player.
  	 */
  	public boolean isHumanPlayer()
  	{
  		return type == PlayerType.HUMAN;
  	}
  	
  	/**
  	 * Sets the specified {@link DropColor} for this {@link Player}.
  	 * @param color - The {@link DropColor} to be assigned.
  	 */
  	public void setDropColor(DropColor color)
  	{
  		this.color = color;
  	}

  	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (!(obj instanceof Player))
		{
			return false;
		}
		Player other = (Player) obj;
		if (color != other.color)
		{
			return false;
		}
		if (level != other.level)
		{
			return false;
		}
		if (name == null)
		{
			if (other.name != null)
			{
				return false;
			}
		}
		else if (!name.equals(other.name))
		{
			return false;
		}
		return true;
	}

	/**
   	*  Returns a {@link String} representation of current {@link Player}.
   	* @see java.lang.Object#toString()
   	*/
  	@Override
  	public String toString() 
  	{
		return String.format("Player [name=%1s, color=%2s, level=%3s]",
								name, color, level);
  	}
  	
  	/**
  	 * Returns the number of drops, that remain for current {@link Player}.
  	 * @return The number of remaining drops.
  	 */
  	public int getDropCount()
  	{
  		return dropCount;
  	}
  	
  	/**
  	 * Reduces the count of remaining drops by 1.
  	 */
  	public void decrementDropCount()
  	{
  		if (dropCount > 0)
  		{
  			dropCount--;
  		}
  		else
  		{
  			throw new IllegalStateException("No further drops remaining!");
  		}
  	}

  	/**
   	* Returns the associated {@link DropColor}.
   	* @return the associated drop color.
   	*/
  	public DropColor getDropColor() 
  	{
		return color;
  	}

  	/**
   	* Returns the associated {@link SkillLevel}.
   	* @return the associated skill level.
   	*/
  	public SkillLevel getLevel() 
  	{
		return level;
  	}

  	/**
   	* Returns the name of current {@link Player} instance.
   	* @return the name of the player.
   	*/
  	public String getName() 
  	{
		return name;
  	}
  	
  	public void save(SerialObject obj)
  	{
  		obj.saveString(name, "name");
  		obj.saveString(level.toString(), "level");
  		obj.saveString(type.toString(), "type");
  		obj.saveString(color.toString(), "color");
  		obj.saveInt(dropCount, "dropCount");
  	}
  	
  	public Serial load(SerialObject obj)
  	{
  		//TODO deserialization
  		return null;
  	}
}
