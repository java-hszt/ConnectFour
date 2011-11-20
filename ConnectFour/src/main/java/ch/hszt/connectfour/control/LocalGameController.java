/**
 * 
 */
package ch.hszt.connectfour.control;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import ch.hszt.connectfour.exception.GameException;
import ch.hszt.connectfour.gui.MainGameFrame;
import ch.hszt.connectfour.gui.SetupFrame;
import ch.hszt.connectfour.gui.SlotPanel;

import ch.hszt.connectfour.model.board.GameBoardSlot;
import ch.hszt.connectfour.model.enumeration.DialogResult;
import ch.hszt.connectfour.model.enumeration.DropColor;
import ch.hszt.connectfour.model.game.CpuPlayer;
import ch.hszt.connectfour.model.game.Game;
import ch.hszt.connectfour.model.game.GameStatistic;
import ch.hszt.connectfour.model.game.GameStatus;
import ch.hszt.connectfour.model.game.Player;
import ch.hszt.connectfour.util.GameStarter;

/**
 * Concrete implementation of a {@link GameController} for a local game.
 * @author Markus Vetsch
 * @version 1.0, 13.11.2011
 */
public class LocalGameController extends GameController
{
	private MainGameFrame frame;
	
	/**
	 * Creates a new instance of {@link LocalGameController}.
	 * @param frame - The associated {@link MainGameFrame}, which the {@link LocalGameController} shall interact with.
	 */
	public LocalGameController(MainGameFrame frame)
	{
		this.frame = frame;
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.control.GameController#refreshStatus(ch.hszt.connectfour.model.game.GameStatus)
	 */
	@Override
	public void refreshStatus(GameStatus status)
	{
		// Update game status for information section
		
		frame.setRemainingTurns(Integer.toString(status.countRemainingTurns()));
		frame.setCompletedTurns(Integer.toString(status.countCompletedTurns()));
		frame.setCurrentPlayer(String.format("%1$s [%2$s]",
											status.getCurrentPlayer().getName(),
											status.getCurrentPlayer().getDropColor().toString()));
		frame.setYellowDrops(Integer.toString(status.countYellowDrops()));
		frame.setRedDrops(Integer.toString(status.countRedDrops()));
		
		// Update information about kind of current player
		
		frame.updateIsCurrentCpuPlayer(status.isCurrentCpuPlayer());
		
		// Respect eventually connect four
		
		if (status.isConnectFour())
		{			
			handleConnectFour(status);	
		}
		
		// Respect eventually a draw otherwise
		
		else if (status.isDraw())
		{
			handleDraw(status);
		}		
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.control.GameController#refreshStatistic(ch.hszt.connectfour.model.game.GameStatistic)
	 */
	@Override
	public void refreshStatistic(GameStatistic statistic)
	{
		frame.setDuration(statistic.getDuration());
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.control.GameController#refreshSlot(ch.hszt.connectfour.model.board.GameBoardSlot)
	 */
	@Override
	public void refreshSlot(GameBoardSlot slot)
	{
		if (slot == null)
		{
			throw new IllegalArgumentException("Specified GameBoardSlot was null!");
		}
		
		SlotPanel panel = frame.getSlotPanel(slot.getKey());
		
		if (panel != null)
		{
			if (slot.isEmpty())
			{
				panel.setColor(Color.WHITE);
			}
			else
			{
				// Run animation of drop sinking to target slot panel
				
				frame.runInsertAnimation(panel, slot.getColor().toAwtColor());
				
				DropColor color = slot.getColor();
				String player = frame.getGame().getPlayerByColor(color).getName();
				
				printMessage(String.format("%1$s inserted drop with %2$s color in slot %3$s ...",
											player, color.toString(), slot.getKey()));
			}
		}	
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.control.GameController#refreshSlots(java.lang.Iterable)
	 */
	@Override
	public void refreshSlots(Iterable<GameBoardSlot> slots)
	{		
		// Set all panels to green color for connect four
		
		for (GameBoardSlot slot : slots)
		{
			SlotPanel panel = frame.getSlotPanel(slot.getKey());
			panel.setColor(Color.GREEN);
		}
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.control.GameController#printMessage(java.lang.String)
	 */
	@Override
	public void printMessage(String message)
	{
		frame.printMessage(message);
	}
	
	/**
	 * Handles a ConnectFour situation and executes the necessary actions on GUI and data model side
	 * @param status - The current {@link GameStatus} providing the ConnectFour situation.
	 */
	private void handleConnectFour(GameStatus status)
	{
		// Print status messages and refresh affected slots by highlighting them
		
		List<GameBoardSlot> slots = status.getWinnerSlots();
		
		printMessage(String.format("Player %s has ConnectFour and won the current game ...",
									status.getWinner().getName()));
		printMessage(String.format("Winning slots: %s ...", getWinningSlots(slots)));
		
		refreshSlots(slots);
		
		// Handling for possible game restart
		
		evaluateGameRestart(status);		
	}
	
	/**
	 * Handles a draw situation and executes the necessary actions on GUI and data model side
	 * @param status - The current {@link GameStatus} providing the draw situation.
	 */
	private void handleDraw(GameStatus status)
	{
		// Print message to text area
		
		printMessage("There are no possible turns remaining - game ends with a draw ...");
		
		// Handling for possible game restart
		
		evaluateGameRestart(status);
	}

	/**
	 * Performs necessary steps, after previous {@link Game} has ended and asks for a restart with identical game settings.
	 * @param status - The {@link GameStatus} of previously ended {@link Game}.
	 */
	private void evaluateGameRestart(GameStatus status)
	{
		try
		{
			SetupFrame owningFrame = (SetupFrame)frame.getOwner();
			frame.stopTimer();
			
			// Ask for game restart - slightly different question according to draw / connect four
			
			DialogResult result = frame.askForRestart(status.isDraw());
			
			// Handle result of the restart question
			
			if (result == DialogResult.YES)
			{					
				// reset reference to game frame for owning frame
				
				owningFrame.updateGameFrame(null);
				
				Player either = frame.getGame().getStartPlayer();
				Player other = frame.getGame().getOtherPlayer();
				
				// reset drop counts per player
				
				either.resetDropCount();
				other.resetDropCount();
				
				// create new game with same players as in previous game
				
				Game newGame = new Game(either, other);
				
				// close and dispose the frame - create new frame for new game
				
				frame.close();				
				frame = (MainGameFrame)GameStarter.launch(owningFrame, newGame);
			}
			else
			{
				// otherwise stop previous game
				
				stopGame(frame.getGame());
				
				// update the controls in the owning frame according to game status
				
				owningFrame.updateControls(frame.getGame().isStarted());
				
				// close and dispose the frame - create new frame for new game
				
				frame.close();
			}
		}
		catch (GameException e)
		{
			printMessage("Game currently can't be stopped! Reason: " + e.getMessage());
		}
	}

	/**
	 * Returns a {@link String} representation of the specified set of {@link GameBoardSlot}.
	 * @param slots - The affected set of {@link GameBoardSlot}.
	 * @return The {@link String} representation of the specified <code>Iterable<GameBoardSlot></code> set.
	 */
	private String getWinningSlots(Iterable<GameBoardSlot> slots)
	{
		StringBuilder sb = new StringBuilder();
		
		for (GameBoardSlot slot : slots)
		{
			sb.append(String.format("%s, ", slot.getKey()));
		}

		return sb.substring(0, sb.length() - 2);
	}
}
