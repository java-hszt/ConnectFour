package ch.hszt.connectfour.test;

import static org.junit.Assert.*;

import java.util.List;

import ch.hszt.connectfour.model.board.GameBoard;
import ch.hszt.connectfour.model.board.GameBoardColumn;
import ch.hszt.connectfour.model.board.GameBoardDiagonal;
import ch.hszt.connectfour.model.board.GameBoardRow;
import ch.hszt.connectfour.model.board.GameBoardSlot;
import ch.hszt.connectfour.model.board.GameBoardSlotCollection;
import ch.hszt.connectfour.model.enumeration.DropColor;
import ch.hszt.connectfour.model.game.Game;

import org.junit.After;
import org.junit.Before;

/**
 * Accomplishes unit tests for the model-based representation of the game board
 * @author Markus Vetsch
 */
public class GameBoardModelTest
{
	private Game game;
	private GameBoard board;

	@Before
	public void setupGame()
	{
		game = UnitTestUtils.prepareGame();
		board = game.getBoard();
	}
	
	@After
	public void tearDownGame()
	{
		game = null;
	}
	
	@org.junit.Test
	public void testGameBoardInitalization()
	{		
		assertNotNull("GameBoard not intialized!", board);
		
		List<GameBoardSlotCollection> columns = board.getColumns();
		
		assertNotNull("GameBoard doesn't provide any GameBoardColumn!", columns);
		assertEquals("GameBoard doens't provide expected count of GameBoardColumn!", 7, columns.size());
		
		for (int i = 0; i < columns.size(); i++)
		{
			GameBoardColumn current = (GameBoardColumn) columns.get(i);
			List<GameBoardSlot> slots = current.asList();
			
			assertNotNull(String.format("%s doesn't provide any GameBoardSlot!", current.toString()), slots);
			assertEquals(String.format("%s doesn't provide expected count of GameBoardSlot!", current.toString()), 6, slots.size());
			
			for (int j = 1; j <= slots.size(); j++)
			{
				GameBoardSlot currentSlot = current.getSlot(j);
				
				assertNotNull(String.format("GameBoardSlot %d not initialized!", j), currentSlot);
				assertEquals(String.format("%1$s doesn't belong to %2$s!", 
												currentSlot.toString(), currentSlot.getColumn().toString()),
											currentSlot.getColumn(), current);
				
				assertEquals(String.format("%s does't lie at position %d!",
								currentSlot.toString(), j),
							currentSlot.getPosition(), j);
				
				assertTrue("GameBoardSlot %s is not empty after initialization!", currentSlot.isEmpty()); 
			}
		}
	}
	
	@org.junit.Test
	public void testDropInsertionValid()
	{
		for (GameBoardSlotCollection coll : board.getColumns())
		{
			assertFalse(String.format("%s mustn't be four in row with red drops!", coll.toString()), coll.hasConnectFour(DropColor.RED));
			assertFalse(String.format("%s mustn't be four in row with yellow drops!", coll.toString()), coll.hasConnectFour(DropColor.YELLOW));
		}
		
		GameBoardSlot a1Slot = board.insertDrop("A", DropColor.RED);
		GameBoardSlot b1Slot = board.insertDrop("B", DropColor.YELLOW);
		GameBoardSlot c1Slot = board.insertDrop("C", DropColor.RED);
		GameBoardSlot d1Slot = board.insertDrop("d", DropColor.YELLOW);
		GameBoardSlot e1Slot = board.insertDrop("e", DropColor.RED);
		GameBoardSlot f1Slot = board.insertDrop("f", DropColor.YELLOW);
		GameBoardSlot g1Slot = board.insertDrop("g", DropColor.RED);
		
		GameBoardSlot[] firstRow = new GameBoardSlot[] { a1Slot, b1Slot, c1Slot, d1Slot, e1Slot, f1Slot, g1Slot };
		
		for (GameBoardSlot slot : firstRow)
		{
			assertNotNull("GameBoard slot was null!", slot);
			assertTrue(String.format("Unexpected slot position %d - should be 1!", slot.getPosition()), checkSlotPosition(slot, 1));
		}
		
		assertTrue(String.format("Slot %s is empty or is filled with wrong drop color!", a1Slot.getKey()), checkRedSlot(a1Slot));
		assertTrue(String.format("Slot %s is empty or is filled with wrong drop color!", b1Slot.getKey()), checkYellowSlot(b1Slot));
		assertTrue(String.format("Slot %s is empty or is filled with wrong drop color!", c1Slot.getKey()), checkRedSlot(c1Slot));
		assertTrue(String.format("Slot %s is empty or is filled with wrong drop color!", d1Slot.getKey()), checkYellowSlot(d1Slot));
		assertTrue(String.format("Slot %s is empty or is filled with wrong drop color!", e1Slot.getKey()), checkRedSlot(e1Slot));
		assertTrue(String.format("Slot %s is empty or is filled with wrong drop color!", f1Slot.getKey()), checkYellowSlot(f1Slot));
		assertTrue(String.format("Slot %s is empty or is filled with wrong drop color!", g1Slot.getKey()), checkRedSlot(g1Slot));
		
		// Vertical connect four test
		
		GameBoardSlot a2Slot = board.insertDrop("A", DropColor.RED);
		GameBoardSlot a3Slot = board.insertDrop("A", DropColor.RED);
		GameBoardSlot a4Slot = board.insertDrop("A", DropColor.RED);
		
		// Horizontal connect four test
		
		GameBoardSlot b2Slot = board.insertDrop("B", DropColor.YELLOW);
		GameBoardSlot c2Slot = board.insertDrop("C", DropColor.YELLOW);
		GameBoardSlot d2Slot = board.insertDrop("D", DropColor.YELLOW);
		GameBoardSlot e2Slot = board.insertDrop("E", DropColor.YELLOW);
		
		// Diagonal connect four test
		
		GameBoardSlot f2Slot = board.insertDrop("F", DropColor.RED);
		GameBoardSlot e3Slot = board.insertDrop("E", DropColor.RED);
		GameBoardSlot d3Slot = board.insertDrop("D", DropColor.YELLOW);
		GameBoardSlot d4Slot = board.insertDrop("D", DropColor.RED);
		
		for (GameBoardSlotCollection coll : board.getColumns())
		{
			GameBoardColumn column = (GameBoardColumn) coll;
			
			if (column.getKey().equals("A"))
			{
				assertTrue("Connect four was expected in column A, but wasn't!", column.hasConnectFour(DropColor.RED));
			}			
		}
		
		for (GameBoardSlotCollection coll : board.getRows())
		{
			GameBoardRow row = (GameBoardRow) coll;
			
			if (row.getId() == 2)
			{
				assertTrue("Connect four was expected in row 2, but wasn't!", row.hasConnectFour(DropColor.YELLOW));
			}
		}
		
		for (GameBoardSlotCollection coll : board.getDiagonals())
		{
			GameBoardDiagonal diagonal = (GameBoardDiagonal) coll;
			
			if (diagonal.getKey().equals("G1-B6 / B6-G1") || diagonal.getKey().equals("B6-G1 / G1-B6"))
			{
				assertTrue(String.format("Connect four was expected in diagonal %s, but wasn't!", diagonal.getKey()),
							diagonal.hasConnectFour(DropColor.RED));
			}
		}
	}
	
	@org.junit.Test(expected=IllegalArgumentException.class)
	public void testDropInsertionInvalid()
	{
		for (int i = 0; i < 7; i++)
		{
			board.insertDrop("A", DropColor.RED);
			board.insertDrop("B", DropColor.YELLOW);
			board.insertDrop("C", DropColor.RED);
			board.insertDrop("D", DropColor.YELLOW);
			board.insertDrop("E", DropColor.RED);
			board.insertDrop("F", DropColor.YELLOW);
			board.insertDrop("G", DropColor.RED);
		}
		
		board.insertDrop("A", DropColor.RED);
		board.insertDrop("B", DropColor.YELLOW);
		board.insertDrop("C", DropColor.RED);
		board.insertDrop("D", DropColor.YELLOW);
		board.insertDrop("E", DropColor.RED);
		board.insertDrop("F", DropColor.YELLOW);
		board.insertDrop("G", DropColor.RED);
	}
	
	@org.junit.Test(expected=IllegalArgumentException.class)
	public void testDropInsertionException()
	{	
		// Invalid color or column key specified => exception expected
		
		board.insertDrop("A", DropColor.UNKNOWN);
		board.insertDrop("X", DropColor.RED);
		board.insertDrop("Y", DropColor.YELLOW);
	}
	
	private boolean checkSlotPosition(GameBoardSlot slot, int expectedPosition)
	{
		return slot.getPosition() == expectedPosition;
	}
	
	private boolean checkRedSlot(GameBoardSlot slot)
	{
		return (!slot.isEmpty() && slot.getColor() == DropColor.RED);
	}
	
	private boolean checkYellowSlot(GameBoardSlot slot)
	{
		return (!slot.isEmpty() && slot.getColor() == DropColor.YELLOW);
	}
}
