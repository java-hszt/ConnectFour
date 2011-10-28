
package ch.hszt.connectfour.model.game;

import java.util.List;

import ch.hszt.connectfour.model.board.GameBoard;
import ch.hszt.connectfour.model.board.GameBoardSlot;
import ch.hszt.connectfour.model.board.GameBoardSlotCollection;
import ch.hszt.connectfour.model.enumeration.DropColor;
/**
 * Encapsulates and provides relevant information about the current status of a game.
 * @author Markus Vetsch
 * @version 1.0, 13.10.2011
 */
public class GameStatus
{
	private boolean isConnectFour =  false;
	
	private GameBoardSlotCollection connectFour;
	private List<GameBoardSlot> winnerSlots;
	private DropColor winnerColor;
	
	private int turnsLeft =  0;
	private int turnsCompleted =  0;
	private int yellowDrops =  0;
	private int redDrops =  0;
	
	private Player currentPlayer;
	
	private Game game;

	/**
	 * Creates a new instance of the {@link GameStatus}.
	 * Use static method {@link GameStatus#update(Game)} to update the {@link GameStatus} for specified {@link Game}.
	 * @param game - The {@link Game}, for which the {@link GameStatus} will be created.
	 */
	private GameStatus(Game game) 
	{
		this.game = game;
		
		winnerColor = DropColor.UNKNOWN;
		evaluate(game);
	}

	/**
	 * Updates the {@link GameStatus} of the specifed {@link Game} by assigning a new instance to it.
	 * @param game - The {@link Game}, to update the {@link GameStatus} for.
	 */
	public static void update(Game game)
	{
		game.setStatus(new GameStatus(game));	
	}	
	
	/**
	 * Retrieves the {@link Player} currently in charge to set the next turn.
	 * @return The {@link Player} currently in charge to set the next turn.
	 */
	public Player getCurrentPlayer()
	{
		return currentPlayer;
	}
	
	void setCurrentPlayer(Player player)
	{
		currentPlayer = player;
	}

	/**
	 * Retrieves the number of possible turns left.
	 * @return The number of turns left as integer.
	 */
	public int countRemainingTurns()
	{
		return turnsLeft;
	}
	
	/**
	 * Retrieves the number of completed turns.
	 * @return The number of completed turns as integer.
	 */
	public int countCompletedTurns()
	{
		return turnsCompleted;
	}
		
	/**
	 * Retrieves the total count of drops on the underlying {@link GameBoard}.
	 * @return The total count of drops.
	 */
	public int countTotalDrops()
	{
		return yellowDrops + redDrops;
	}
	
	/**
	 * Retrieves the total count of drops with {@link DropColor#YELLOW} on the underlying {@link GameBoard}.
	 * @return The total count of yellow drops.
	 */
	public int countYellowDrops()
	{
		return yellowDrops;
	}
	
	/**
	 * Retrieves the total count of drops with {@link DropColor#RED} on the underlying {@link GameBoard}.
	 * @return The total count of red drops.
	 */
	public int countRedDrops()
	{
		return redDrops;
	}
	
	/**
	 * Returns the game winning {@link List} of {@link GameBoardSlot}.
	 * @return The game winning {@link GameBoardSlot} instances or <b>NULL</b>, if neither player has "ConnectFour".
	 * The latter can be evaluated by checking {@link GameStatus#isConnectFour()} value.
	 */
	public List<GameBoardSlot> getWinnerSlots()
	{
		return winnerSlots;
	}
	
	/**
	 * Determines, if there is a connect four situation in associated {@link Game}
	 * @return <b>true</b>, if there is a connect four situation; otherwise <b>false</b>.
	 */
	public boolean isConnectFour()
	{
		return isConnectFour;
	}
	
	/**
	 * Returns the winning {@link Player} if a connect four situation is given.
	 * @return The winning {@link Player} or <b>null</b>, if no winner determined yet, i.e. no connect four situation is given.
	 * The latter can be evaluated by checking {@link GameStatus#isConnectFour()} value.
	 */
	public Player getWinner()
	{
		if (winnerColor == DropColor.UNKNOWN)
		{
			return null;
		}
		
		return game.getPlayerByColor(winnerColor);
	}

	private void evaluate(Game game) 
	{
		GameBoard board = game.getBoard();
		
		// Update all relevant information of the game
		
		updateAll(game);
		
		if (isConnectFour)
		{
			winnerSlots = connectFour.getConnectFourList();
		}
	}
	
	private void updateAll(Game game)
	{
		GameBoard board = game.getBoard();
		
		// Check for connect four
		
		isConnectFour = checkIsConnectFour(board);
		
		// Check turn information
		
		turnsLeft = getNumberOfPossibleTurns(board);
		turnsCompleted = getNumberOfCompletedTurns(board);
		yellowDrops = getNumberOfDropsByColor(board, DropColor.YELLOW);
		redDrops = getNumberOfDropsByColor(board, DropColor.RED);
		
		// Evaluate current player
		
		currentPlayer = evaluateCurrentPlayer(game);
	}

	private boolean checkIsConnectFour(GameBoard board) 
	{
		// Iterate over all rows, columns, diagonals
		
		for (GameBoardSlotCollection coll : board.getAll())
		{
			if (coll.hasConnectFour(DropColor.RED))
			{
				connectFour = coll;
				winnerColor = DropColor.RED;
				return true;
			}			
			else if (coll.hasConnectFour(DropColor.YELLOW))
			{
				connectFour = coll;
				winnerColor = DropColor.YELLOW;
				return true;
			}
		}
		
		return false;
	}

	private int getNumberOfPossibleTurns(GameBoard board) 
	{
		int possibleTurns = 0;
		
		for (GameBoardSlotCollection column : board.getColumns())
		{
			for (GameBoardSlot slot : column)
			{
				if (slot.isEmpty())
				{
					possibleTurns++;
				}
			}
		}
		
		return possibleTurns;
	}

	private int getNumberOfCompletedTurns(GameBoard board) 
	{
		int completedTurns = 0;
		
		for (GameBoardSlotCollection column : board.getColumns())
		{
			for (GameBoardSlot slot : column)
			{
				if (!slot.isEmpty())
				{
					completedTurns++;
				}
			}
		}
		
		return completedTurns;
	}

  	private int getNumberOfDropsByColor(GameBoard board, DropColor color) 
  	{
		int dropCount = 0;
		
		for (GameBoardSlotCollection column : board.getColumns())
		{
			for (GameBoardSlot slot : column)
			{
				if (!slot.isEmpty() && slot.getColor() == color)
				{
					dropCount++;
				}
			}
		}
		
		return dropCount;
  	}

  	private Player evaluateCurrentPlayer(Game game)
  	{
  		// Evaluate current player by number of completed turns
  		// Even number => start player; odd number => second player
  		
  		if (turnsCompleted % 2 == 0)
  		{
  			return game.getStartPlayer();
  		}
  		else
  		{
  			return game.getOtherPlayer();
  		}
  	}
}
