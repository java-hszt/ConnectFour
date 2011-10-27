package ch.hszt.connectfour.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.hszt.connectfour.control.GameController;
import ch.hszt.connectfour.control.GameEngine;
import ch.hszt.connectfour.control.GameObserver;
import ch.hszt.connectfour.exception.GameException;

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
		game.start();
		
		Player startPlayer = game.getStartPlayer();
		Player otherPlayer = game.getOtherPlayer();
		DropColor startColor = startPlayer.getColor();
		
		// let the game controller executed the turn
		
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
}
