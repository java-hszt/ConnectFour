/**
 * Encapsulates the data model for classes in the direct game context.
 */
package ch.hszt.connectfour.model.game;

import ch.hszt.connectfour.model.enumeration.DropColor;
import ch.hszt.connectfour.model.enumeration.SkillLevel;

/**
 * Represents an artificial intelligence (= AI) {@link Player}.
 * @author Markus Vetsch
 * @version 1.0, 11.10.2011
 */
public class AIPlayer extends Player
{
	//TODO: Turn this to abstract => various implementation for different skill levels?
	
  	/**
   	* Initializes important members of the player instance - to be called via concrete subclasses.
   	* @param name - the name of the {@link Player}.
   	*/
	public AIPlayer(String name)
	{
		super(name);
	}

  	/**
   	* Initializes important members of the player instance - to be called via concrete subclasses.
   	* @param name - the name of the {@link Player}.
   	* @param color - the {@link DropColor} to assign to this {@link Player}. 
   	*/
	public AIPlayer(String name, DropColor color)
	{
		super(name, color);
	}

  	/**
   	* Initializes important members of the player instance - to be called via concrete subclasses.
   	* @param name - the name of the {@link Player}.
   	* @param color - the {@link DropColor} to assign to this {@link Player}. 
   	* @param level - the {@link SkillLevel} to assign to this {@link Player}
   	*/
	public AIPlayer(String name, DropColor color, SkillLevel level)
	{
		super(name, color, level);
	}

}
