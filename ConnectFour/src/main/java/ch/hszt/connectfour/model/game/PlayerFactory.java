package ch.hszt.connectfour.model.game;

import ch.hszt.connectfour.model.enumeration.PlayerType;
import ch.hszt.connectfour.model.enumeration.SkillLevel;

/**
 * Factory class for creation of {@link Player} instances.
 * @author Markus Vetsch
 * @version 1.0, 31.10.2011
 */
public class PlayerFactory
{
	/**
	 * Constructor not to be used - class can't be instantiated.
	 */
	private PlayerFactory()
	{		
	}
	
	/**
	 * Factory method for creation of any {@link Player} object. 
	 * The appropriate {@link Player} instance will be created according to specified argument values.
	 * @param type - The {@link PlayerType}
	 * @param name - The name of the {@link Player}.
	 * @param level - The {@link SkillLevel} of the {@link Player}.
	 * @return The correct underlying inherited type of {@link Player}.
	 */
	public static Player createPlayer(PlayerType type, String name, SkillLevel level)
	{
		if (type == PlayerType.HUMAN)
		{
			return new HumanPlayer(name);
		}
		else if (type == PlayerType.CPU_PLAYER)
		{
			if (level == SkillLevel.EASY)
			{
				return new EasyCpuPlayer(name, level);
			}
			else if (level == SkillLevel.ADVANCED)
			{
				return new AdvancedCpuPlayer(name, level);
			}
			else if (level == SkillLevel.HARD)
			{
				return new HardCpuPlayer(name, level);
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}
}
