/**
 * 
 */
package ch.hszt.connectfour.control;

import java.awt.Color;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import ch.hszt.connectfour.exception.GameException;
import ch.hszt.connectfour.gui.MainGameFrame;
import ch.hszt.connectfour.gui.SetupFrame;
import ch.hszt.connectfour.gui.SlotPanel;

import ch.hszt.connectfour.model.board.GameBoardSlot;
import ch.hszt.connectfour.model.enumeration.DialogResult;
import ch.hszt.connectfour.model.enumeration.DropColor;
import ch.hszt.connectfour.model.game.Game;
import ch.hszt.connectfour.model.game.GameStatistic;
import ch.hszt.connectfour.model.game.GameStatus;
import ch.hszt.connectfour.util.GameStarter;
import ch.hszt.connectfour.util.GuiHelper;

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
		// Update game status
		
		frame.setRemainingTurns(Integer.toString(status.countRemainingTurns()));
		frame.setCompletedTurns(Integer.toString(status.countCompletedTurns()));
		frame.setCurrentPlayer(status.getCurrentPlayer().getName());
		frame.setYellowDrops(Integer.toString(status.countYellowDrops()));
		frame.setRedDrops(Integer.toString(status.countRedDrops()));
		
		// Respect eventually connect four
		
		if (status.isConnectFour())
		{			
			// Print status messages and refresh affected slots by highlighting them
			
			List<GameBoardSlot> slots = status.getWinnerSlots();
			
			printMessage(String.format("Player %s has ConnectFour and won the current game ...",
										status.getWinner().getName()));
			printMessage(String.format("Winning slots: %s ...", getWinningSlots(slots)));
			
			refreshSlots(slots);
			
			// Handling for possible game restart
			
			try
			{
				SetupFrame owner = (SetupFrame)frame.getOwner();
				frame.stopTimer();
				
				DialogResult result = frame.askForRestart();
				
				if (result == DialogResult.YES)
				{					
					owner.updateGameFrame(null);
					
					Game newGame = new Game(frame.getGame().getStartPlayer(), frame.getGame().getOtherPlayer());
					
					frame.close();
					
					frame = (MainGameFrame)GameStarter.launch(owner, newGame);
				}
				else
				{
					stopGame(frame.getGame());
					owner.updateControls(frame.getGame().isStarted());
					frame.close();
				}
			}
			catch (GameException e)
			{
				printMessage("Game currently can't be stopped! Reason: " + e.getMessage());
			}			
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
