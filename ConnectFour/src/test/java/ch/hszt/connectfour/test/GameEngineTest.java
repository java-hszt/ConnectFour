package ch.hszt.connectfour.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.hszt.connectfour.control.GameController;
import ch.hszt.connectfour.control.GameEngine;
import ch.hszt.connectfour.control.GameObserver;
import ch.hszt.connectfour.exception.GameException;

import ch.hszt.connectfour.model.board.GameBoardColumn;
import ch.hszt.connectfour.model.board.GameBoardSlot;
import ch.hszt.connectfour.model.enumeration.DropColor;
import ch.hszt.connectfour.model.game.Game;
import ch.hszt.connectfour.model.game.GameSettings;
import ch.hszt.connectfour.model.game.GameStatistic;
import ch.hszt.connectfour.model.game.GameStatus;
import ch.hszt.connectfour.model.game.Player;

/**
 * Performs predominantly tests on class {@link GameEngine} and associated game logic elements.
 * @author Markus Vetsch
 * @version 1.0, 24.10.2011
 */
public class GameEngineTest
{
	private Game game;
//	private Game game2;
//	private Game game3;
	
	private GameController controller;
	private GameObserver observer;
	private GameEngine engine;
	
	@Before
	public void prepareEngine()
	{
		game = UnitTestUtils.prepareGame();
//		game2 = TestUtil.prepareGame();
//		game3 = TestUtil.prepareGame();
		
		controller = new UnitTestUtils.GameControllerTest();
		observer = new GameObserver(controller);
		engine = GameEngine.getInstance();
		
		game.setObserver(observer);
//		game2.setObserver(observer);
//		game3.setObserver(observer);
	}
	
	@After
	public void tearDownEngine()
	{
		game = null;
		observer = null;
		engine = null;
	}
	
	@Test
	public void testSingleTurn() throws GameException
	{		
		controller.startGame(game);
		
		Player startPlayer = game.getStartPlayer();
		Player otherPlayer = game.getOtherPlayer();
		DropColor startColor = startPlayer.getColor();
		
		// make the game controller execute the turn
		
		controller.executeTurn(game, "C");
		
		GameStatistic statistic = game.getStatistic();
		GameStatus status = game.getStatus();
		
		GameBoardSlot updated = observer.getUpdatedSlot();
		
		// Verify updated slot
		
		assertNotNull("GameObserver doesn't provide recently updated GameBoardSlot!", updated);
		
			// Verify column position of slot
		
		assertEquals("GameBoardSlot, that was updated doesn't belong to column 'C'!", "C", updated.getColumn().getKey());
		assertEquals("GameBoardSlot, that was updated doesn't lie at position 1!", 1, updated.getPosition());
		assertEquals("GameBoardSlot, that was updated wasn't the one at position C1!", "C1", updated.getKey());
		
			// Verify content of slot
		
		assertFalse("GameBoardSlot is empty, although a drop was supposed to be inserted!", updated.isEmpty());
		assertEquals("Drop color in GameBoardSlot doesn't match the inserted drop!", startColor, updated.getColor());
		
		// Verify drop count of players
		
		assertEquals("Other player doesn't have initial drop count anymore!",
						GameSettings.NUMBER_OF_DROPS, otherPlayer.getDropCount());
		assertEquals("Player still has initial drop count!",
						GameSettings.NUMBER_OF_DROPS - 1, startPlayer.getDropCount());
		
		// Verify game statistic
		
		assertTrue("GameBoard doesn't contain any drops, although it should!", statistic.getNumberOfDropsOnBoard() > 0);
		
		// Verify game status
		
		assertEquals("Number of completed turns is supposed to be 1!", 1, status.countCompletedTurns());
		assertEquals("Number of remaining turns is supposed to be 41!", 41, status.countRemainingTurns());
		
			// Verify no winning situation
		
		assertFalse("GameStatus reports ConnectFour situation, but isn't supposed to be!", status.isConnectFour());
		assertNull("GameStatus reports a winner, but there isn't any!", status.getWinner());
		assertNull("GameStatus reports winning slots, but there aren't any!", status.getWinnerSlots());
		
			// Verify correct drop count per color
		
		if (startColor == DropColor.RED)
		{
			assertEquals("GameStatus reports, that there is currently no red drop on the board",
							1, status.countRedDrops());
			assertNotSame("GameStatus reports, that there is currently 1 yellow drop instead of 1 red drop on the board",
							1, status.countYellowDrops());
		}
		else
		{
			assertEquals("GameStatus reports, that there is currently no yellow drop on the board!",
							1, status.countYellowDrops());
			assertNotSame("GameStatus reports, that there is currently 1 red drop instead of 1 yellow drop on the board",
							1, status.countRedDrops());
		}
		
			// Verify toggle of current player
		
		assertNotSame("Current player is not supposed to be start player!", status.getCurrentPlayer(), startPlayer);
		assertEquals("Current Player is supposed to be other player!", status.getCurrentPlayer(), otherPlayer);
	}
	
	@Test
	public void simulateGame() throws GameException
	{
		controller.startGame(game);
		
		// Init statistic, status and player references
		
		GameStatistic statistic = game.getStatistic();
		GameStatus status = game.getStatus();
		Player startPlayer = game.getStartPlayer();
		Player otherPlayer = game.getOtherPlayer();
		Player currentPlayer = status.getCurrentPlayer();
		Player redPlayer = game.getPlayerByColor(DropColor.RED);
		Player yellowPlayer = game.getPlayerByColor(DropColor.YELLOW);
		DropColor start = startPlayer.getColor();
		DropColor other = otherPlayer.getColor();
		
		// Check for correct start player
		
		assertEquals("Current player isn't equal to start player!", startPlayer, currentPlayer);
		
		int executedTurns = 0;
		int remainingTurns = 2 * GameSettings.NUMBER_OF_DROPS;
		boolean isConnectFour = false;
		
		// Test ends, if all turns completed or if connect four situation detected
		
		while (executedTurns <= 2 * GameSettings.NUMBER_OF_DROPS && !isConnectFour)
		{
			// Define the column randomly
			
			String column = defineTargetColumn(game);
			
			// make the game controller execute the turn, update the status and store the updated slot
						
			controller.executeTurn(game, column);			
			status = game.getStatus();
			
			// Check for connect four situation
			
			isConnectFour = status.isConnectFour();
			
			GameBoardSlot slot = observer.getUpdatedSlot();
			
			currentPlayer = status.getCurrentPlayer();
			
			executedTurns++;
			remainingTurns--;
			
			controller.printMessage(String.format("Turn #%1$d executed - %2$d turns remaining ...", executedTurns, remainingTurns));
			
			// Verify updated slot
			
			assertNotNull(String.format("Turn [#%1$d]: GameObserver doesn't provide recently updated GameBoardSlot!", executedTurns), slot);
			
			// Verify column position of slot
			
			assertEquals(String.format("Turn [#%1$d]: GameBoardSlot, that was updated doesn't belong to column %2$s!", executedTurns, column),
							column, slot.getColumn().getKey());
			
			// Verify content of slot and correct player assignment
			// Check further, that current player was toggled correctly
			
			assertFalse("GameBoardSlot is empty, although a drop was supposed to be inserted!", slot.isEmpty());
			
			if (executedTurns % 2 == 0)
			{
				assertEquals(String.format("Turn [#%d]: Wrong player for next turn!", executedTurns), startPlayer, currentPlayer);
				assertEquals(String.format("Turn [#%d]: Drop color in GameBoardSlot doesn't match the recently inserted drop!", executedTurns),
								otherPlayer.getColor(), slot.getColor());				
			}
			else
			{
				assertEquals(String.format("Turn [#%d]: Wrong player for next turn!", executedTurns), otherPlayer, currentPlayer);
				assertEquals(String.format("Turn [#%d]: Drop color in GameBoardSlot doesn't match the recently inserted drop!", executedTurns),
								startPlayer.getColor(), slot.getColor());
			}
			
			// Verify count of remaining drops
			
			int startPlayerCountSet = (start == DropColor.RED) ? status.countRedDrops() : status.countYellowDrops();
			int otherPlayerCountSet = (other == DropColor.RED) ? status.countRedDrops() : status.countYellowDrops();
			
			int redCount = status.countRedDrops();
			int yellowCount = status.countYellowDrops();
			
			assertEquals(String.format("Turn [#%d]: Start player has wrong count of remaining drops!", executedTurns),
							GameSettings.NUMBER_OF_DROPS - startPlayerCountSet, startPlayer.getDropCount());
			
			assertEquals(String.format("Turn [#%d]: Other player has wrong count of remaining drops!", executedTurns),
							GameSettings.NUMBER_OF_DROPS - otherPlayerCountSet, otherPlayer.getDropCount());
			
			assertEquals(String.format("Turn [#%d]: GameStatus doesn'r report correct count of red drops onthe game board", executedTurns),
							redCount, GameSettings.NUMBER_OF_DROPS - redPlayer.getDropCount());
			
			assertEquals(String.format("Turn [#%d]: GameStatus doesn'r report correct count of yellow drops onthe game board", executedTurns),
							yellowCount, GameSettings.NUMBER_OF_DROPS - yellowPlayer.getDropCount());
			
			// Verify game statistic and status
			
			assertEquals(String.format("Turn [#%d]: GameStatistic doesn't report expected count of drops on the game board!", executedTurns),
							statistic.getNumberOfDropsOnBoard(), executedTurns);
			
			assertEquals(String.format("Turn [#%d]: GameStatus doesn't report correct count of remaining turns!", executedTurns),
							status.countRemainingTurns(), remainingTurns);
			
			if (isConnectFour)
			{
				controller.printMessage(String.format("ConnectFour detected in turn #%d !", executedTurns));
				
				assertNotNull(String.format("Turn [#%d]: No winner reported by GameStatus, although ConnectFour detected!", executedTurns),
								status.getWinner());
				assertNotNull(String.format("Turn [#%d]: No winning slots available by GameStatus, although ConnectFour detected!", executedTurns),
								status.getWinnerSlots());
				
				controller.printMessage(String.format("The winner is %s !", status.getWinner().toString()));
				controller.printMessage(String.format("Winner slots: %s", Arrays.toString(status.getWinnerSlots().toArray())));
			}
		}
	}
	
	private String defineTargetColumn(Game game)
	{
		String key = null;
		boolean hasSpace = false;
		
		while (!hasSpace)
		{
			int columnId = new Random().nextInt(7);
			GameBoardColumn column = (GameBoardColumn) game.getBoard().getColumns().get(columnId);

			for (GameBoardSlot slot : column)
			{
				if (slot.isEmpty())
				{
					hasSpace = true;
					key = column.getKey();
					break;
				}
			}
		}		
		
		return key;
	}
}

