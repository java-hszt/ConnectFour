/**
 * 
 */
package ch.hszt.connectfour.control;

import java.util.Observable;
import java.util.Observer;

import ch.hszt.connectfour.model.board.GameBoardSlot;
import ch.hszt.connectfour.model.game.Game;

/**
 * This class acts as main observer of the {@link Game} and is notified about changes within the associated {@link Game}.
 * @author Markus Vetsch
 * @version 1.0, 22.10.2011
 */
public class GameObserver implements Observer
{
	private final GameController controller;
	
	private GameBoardSlot updatedSlot;
	
	/**
	 * Creates a new instance of the game controller.
	 * @param controller - The {@link GameController} used for communication exchange.
	 */
	public GameObserver(final GameController controller)
	{
		this.controller = controller;
		updatedSlot = null;
	}
	
	/**
	 * Retrieves the {@link GameBoardSlot} affected and updated by last turn.
	 * @return The {@link GameBoardSlot}, that was most recently updated or <b>null</b>, if none was updated.
	 */
	public GameBoardSlot getUpdatedSlot()
	{
		return updatedSlot;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, Object arg)
	{
		// now get the update information
		
		if (o instanceof Game)
		{
			Game game = (Game) o;
			
			// check for changed slot on the game board
			
			if (arg != null && arg instanceof GameBoardSlot)
			{
				// refresh slot via controller
				
				updatedSlot = (GameBoardSlot) arg;
				controller.refreshSlot(updatedSlot);
			}
			else
			{
				updatedSlot = null;
			}
			
			// refresh everything in any case
			
			controller.refreshAll(game);
		}		
	}
}
