package ch.hszt.connectfour.test;

import ch.hszt.connectfour.control.GameController;
import ch.hszt.connectfour.control.GameEngine;
import ch.hszt.connectfour.exception.GameException;
import ch.hszt.connectfour.model.board.GameBoardSlot;
import ch.hszt.connectfour.model.enumeration.DropColor;
import ch.hszt.connectfour.model.enumeration.SkillLevel;
import ch.hszt.connectfour.model.game.Game;
import ch.hszt.connectfour.model.game.GameStatistic;
import ch.hszt.connectfour.model.game.GameStatus;
import ch.hszt.connectfour.model.game.HumanPlayer;
import ch.hszt.connectfour.model.game.Player;

public class UnitTestUtils
{
	static Game prepareGame()
	{
		Player first = new HumanPlayer("TestPlayer1", DropColor.RED, SkillLevel.EASY);
		Player second = new HumanPlayer("TestPlayer2", DropColor.YELLOW, SkillLevel.EASY);
		
		return new Game(first, second);
	}
	
	static class GameControllerTest extends GameController
	{
		public void refreshAll(Game game)
		{
			// Dummy implementation
			System.out.println("Game information updated !!!");
		}		

		public void refreshStatus(GameStatus status)
		{
			// Dummy implementation
			System.out.println("Game status updated !!!");
		}

		public void refreshStatistic(GameStatistic statistic)
		{
			// Dummy implementation
			System.out.println("Game statistic updated !!!");			
		}

		public void refreshSlot(GameBoardSlot slot)
		{
			// Dummy implementation
			System.out.println("Slot updated !!!");
		}
		
		public void refreshSlots(Iterable<GameBoardSlot> slots)
		{
			// Dummy implementation
			System.out.println("Slots updated !!!");
		}

		public void executeTurn(Game game, String column) throws GameException
		{			
			super.executeTurn(game, column);
			System.out.println("Drop inserted in column " + column + " !!!");
		}
		
		public void printMessage(String message)
		{
			//Dummy implementation
			System.out.println(message);
		}

		public void startGame(Game game) throws GameException
		{
			super.startGame(game);
			System.out.println("Game was started !!!");
		}

		public void stopGame(Game game) throws GameException
		{
			super.stopGame(game);
			System.out.println("Game was stopped !!!");
		}

		public void restartGame(Game game) throws GameException
		{
			super.restartGame(game);
			System.out.println("Game was restarted !!!");
		}
	}
}
