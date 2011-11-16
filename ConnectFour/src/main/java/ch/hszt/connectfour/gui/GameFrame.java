/**
 * 
 */
package ch.hszt.connectfour.gui;

import ch.hszt.connectfour.control.GameController;
import ch.hszt.connectfour.model.game.Game;

/**
 * Abstract definition of a frame providing the graphical represenstation of the game.
 * @author Markus Vetsch
 * @version 1.0, 13.11.2011
 */
public interface GameFrame
{
	/**
	 * Grants access to the underlying {@link Game}.
	 * @return The underlying {@link Game} instance.
	 */
	Game getGame();
	
	/**
	 * Grants access to the underlying {@link GameController}.
	 * @return The underlying {@link GameController} instance.
	 */
	GameController getController();
	
	/**
	 * Returns the {@link SlotPanel} for the given key or <b>null</b>, if there was no matching {@link SlotPanel} found.
	 * @param key - The key of the {@link SlotPanel} to be found.
	 * @throws IllegalArgumentException, if the key specified is <b>null</b>.
	 * @return The corresponding {@link SlotPanel} or <b>null</b>, if there was no matching {@link SlotPanel}.
	 */
	SlotPanel getSlotPanel(String key);
	
	/**
	 * Prints a message to a dedicated output device.
	 * @param message - The message to be printed.
	 */
	void printMessage(String message);
}
