package ch.hszt.connectfour.test;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.hszt.connectfour.control.GameController;
import ch.hszt.connectfour.exception.GameException;
import ch.hszt.connectfour.model.board.GameBoard;
import ch.hszt.connectfour.model.board.GameBoardColumn;
import ch.hszt.connectfour.model.enumeration.DropColor;
import ch.hszt.connectfour.model.enumeration.SkillLevel;
import ch.hszt.connectfour.model.game.Game;
import ch.hszt.connectfour.model.game.GameSettings;
import ch.hszt.connectfour.model.game.GameStatistic;
import ch.hszt.connectfour.model.game.GameStatus;
import ch.hszt.connectfour.model.game.Player;

public class GameTest
{
	private GameController controller;
	private Game game;
	
	private Player startPlayer;
	private Player otherPlayer;
	
	@Before
	public void setupGame()
	{
		controller = new UnitTestUtils.GameControllerTest();
		
		game = UnitTestUtils.prepareGame();
		
		startPlayer = game.getStartPlayer();
		otherPlayer = game.getOtherPlayer();
	}
	
	@After
	public void tearDownGame()
	{
		game = null;
	}
	
	@Test
	public void testGameInit()
	{
		assertNotNull("Start player undefined!", startPlayer);
		assertNotNull("Other player undefined!", otherPlayer);
		
		assertNotNull(String.format("Name of player %s not set!", startPlayer.toString()), startPlayer.getName());
		assertNotNull(String.format("Name of player %s not set!", otherPlayer.toString()), otherPlayer.getName());
			
		assertNotSame(String.format("DropColor was undefined for player %s!", startPlayer.toString()),
						DropColor.UNKNOWN, startPlayer.getColor());
		assertNotSame(String.format("DropColor was undefined for player %s!", otherPlayer.toString()),
						DropColor.UNKNOWN, otherPlayer.getColor());
		assertNotSame("Same DropColor in use for both of the players!", startPlayer.getColor(), otherPlayer.getColor());
		
		assertNotSame(String.format("SkillLevel was undefined for player %s!", startPlayer.toString()),
						SkillLevel.UNKNOWN, startPlayer.getLevel());
		assertNotSame(String.format("SkillLevel was undefined for player %s!", otherPlayer.toString()),
						SkillLevel.UNKNOWN, otherPlayer.getLevel());
		
		assertEquals(String.format("Player %s doesn't have correct start count of dops!", startPlayer.toString()),
					GameSettings.NUMBER_OF_DROPS, startPlayer.getDropCount());
		assertEquals(String.format("Player %s doesn't have correct start count of dops!", otherPlayer.toString()),
					GameSettings.NUMBER_OF_DROPS, otherPlayer.getDropCount());
	}
	
	@Test
	public void testGameStartValid() throws GameException
	{
		assertNull("GameStatistic already initialized", game.getStatistic());
		assertNull("GameStatus already initialized", game.getStatus());
		
		controller.startGame(game);
		
		assertTrue("Game is not in state 'started'!", game.isStarted());
		
		GameStatistic statistic = game.getStatistic();
		GameStatus status = game.getStatus();
		
		assertNotNull("GameStatistic not initialized!", statistic);
		assertEquals("Number of drops on GameBoard is > 0!", 0, statistic.getNumberOfDropsOnBoard());
		assertNotNull("Start time of game was null!", statistic.getStartTime());
		assertNotNull("Game duration was null!", statistic.getDuration());
		
		assertNotNull("GameStatus not initialized!", status);
		assertTrue("Drop count of red drops was > 0 !", status.countRedDrops() == 0);
		assertTrue("Drop count of yellow drops was > 0 !", status.countYellowDrops() == 0);
		assertTrue("Drop count of total drops was > 0 !", status.countTotalDrops() == 0);
		assertTrue("Number of completed turns was > 0 !", status.countCompletedTurns() == 0);
		assertTrue("Number of remaining turns was < 42!", status.countRemainingTurns() == 2 * GameSettings.NUMBER_OF_DROPS);
		assertFalse("GameStatus says it's Connect Four, although it shouldn't!", status.isConnectFour());
		assertNull("GameStatus says, there is a winner, although it shouldn't!", status.getWinner());
		assertNull("GameStatus says, there are winning GameBoardSlots, although it shouldn't!", status.getWinnerSlots());
		
		assertNotNull("Game doesn't have any start player, although there should be any!", game.getStartPlayer());
		assertNotNull("GameStatus doesn't report a current player, although there should be any!", status.getCurrentPlayer());
		
		assertTrue("Start player of game doesn't match current player!", game.getStartPlayer().equals(status.getCurrentPlayer()));
	}
	
	@Test(expected=GameException.class)
	public void testGameStartInvalid() throws GameException
	{
		controller.startGame(game);
		controller.startGame(game);
	}
	
	@Test
	public void testGameStopValid() throws GameException
	{
		controller.startGame(game);		
		controller.stopGame(game);
		
		assertFalse("Game is not in state 'stopped'!", game.isStarted());		
		assertNotNull("End time of the Game was null!", game.getStatistic().getEndTime());
	}
	
	@Test(expected=GameException.class)
	public void testGameStopInvalid() throws GameException
	{
		controller.stopGame(game);
	}
	
	@Test
	public void testDropCountReductionValid()
	{
		for (int i = GameSettings.NUMBER_OF_DROPS; i > 0; i--)
		{
			startPlayer.decrementDropCount();
			otherPlayer.decrementDropCount();
			
			assertEquals("Number of remaining drops is not correct!", i-1, startPlayer.getDropCount());
			assertEquals("Number of remaining drops is not correct!", i-1, otherPlayer.getDropCount());
			assertTrue("Players have different number of remaining drops!", startPlayer.getDropCount() == otherPlayer.getDropCount());
		}
	}
	
	@Test(expected=IllegalStateException.class)
	public void testDropCountReductionInvalid()
	{		
		for (int i = GameSettings.NUMBER_OF_DROPS + 1; i > 0; i--)
		{
			startPlayer.decrementDropCount();
		}
	}
	
	@Test
	public void testGameRestart() throws GameException
	{
		GameBoard board = game.getBoard();
		
		controller.startGame(game);
		
		for (int i = 0; i < 10; i++)
		{
			int columnId = new Random().nextInt(7);
			int colorId = new Random().nextInt(2);
			GameBoardColumn column = (GameBoardColumn) board.getColumns().get(columnId);
			
			board.insertDrop(column.getKey(), DropColor.parse(colorId));
		}
		
		controller.restartGame(game);
		
		GameStatistic statistic = game.getStatistic();
		GameStatus status = game.getStatus();
		
		assertTrue("Game isn't in state started, although it should be!", game.isStarted());
		
		assertNotNull("GameStatistic is null after restart!", statistic);
		assertNotNull("GameStatus is null after restart!", status);
		
		assertEquals("There are drops on the GameBoard, although there shouldn't be any!", 0, statistic.getNumberOfDropsOnBoard());
		assertEquals("There are red drops on the GameBoard, although there shouldn't be any!", 0, status.countRedDrops());
		assertEquals("There are yellow drops on the GameBoard, although there shouldn't be any!", 0, status.countYellowDrops());
		assertEquals("GameStatus reports there are completed turns, although there shouldn't be any!", 0, status.countCompletedTurns());
		assertEquals("GameStatus reports there are no remaining turns left, although there should be any!", 42, status.countRemainingTurns());
		
		assertNull("GameStatus reports a winner, although there shouldn't be any!", status.getWinner());
		assertNull("GameStatus reports winning slots, although there shouldn't be any!", status.getWinnerSlots());

		assertFalse("GameStatus reports a ConnectFour situation, although there shouldn't be any!", status.isConnectFour());
		
		assertNotNull("Game doesn't have any start player, although there should be any!", game.getStartPlayer());
		assertNotNull("GameStatus doesn't report a current player, although there should be any!", status.getCurrentPlayer());
		
		assertTrue("Start player of game doesn't match current player!", game.getStartPlayer().equals(status.getCurrentPlayer()));
	}
	
	@Test(expected=GameException.class)
	public void testGameRestartInvalid() throws GameException
	{
		controller.restartGame(game);
	}
}
