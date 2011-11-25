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
	public void testGameBoardInitialization()
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
	public void testGameBoardNavigation()
	{
		GameBoardSlot upLeft = board.getColumnByKey("A").getSlot(6);
		GameBoardSlot upRight = board.getColumnByKey("G").getSlot(6);
		GameBoardSlot lowLeft = board.getColumnByKey("A").getSlot(1);
		GameBoardSlot lowRight = board.getColumnByKey("G").getSlot(1);
		
		GameBoardSlot arbitrary = board.getColumnByKey("D").getSlot(4);
		
		// First verify positions
		
		assertEquals("Upper left GameBoardSlot isn't on position A6 on GameBoard!", "A6", upLeft.getKey());
		assertEquals("Upper right GameBoardSlot isn't on position G6 on GameBoard!", "G6", upRight.getKey());
		assertEquals("Lower left GameBoardSlot isn't on position A1 on GameBoard!", "A1", lowLeft.getKey());
		assertEquals("Lower right GameBoardSlot isn't on position G6 on GameBoard!", "G1", lowRight.getKey());
		
		assertEquals("Arbitrary slot isn't on position D4 on GameBoard!", "D4", arbitrary.getKey());
		
		// Test navigation from upLeft
		
		assertNull("Upper left GameBoardSlot can't have up left neighbour!", upLeft.getUpLeft());
		assertNull("Upper left GameBoardSlot can't have up right neighbour!", upLeft.getUpRight());
		assertNull("Upper left GameBoardSlot can't have top neighbour!", upLeft.getTop());
		assertNull("Upper left GameBoardSlot can't have left neighbour!", upLeft.getLeft());
		assertNull("Upper left GameBoardSlot can't have low left neighbour!", upLeft.getLowLeft());
		
		assertNotNull("Upper left GameBoardSlot must have right neighbour!", upLeft.getRight());
		assertEquals("Right neighbour of upper left doesn't lie at position B6 on GameBoard!", "B6", upLeft.getRight().getKey());
		assertNotNull("Upper left GameBoardSlot must have low right neighbour!", upLeft.getLowRight());
		assertEquals("Lower Right neighbour of upper left doesn't lie at position B5 on GameBoard!", "B5", upLeft.getLowRight().getKey());
		assertNotNull("Upper left GameBoardSlot must have bottom neighbour!", upLeft.getBottom());
		assertEquals("Bottom neighbour of upper left doesn't lie at position A5 on GameBoard!", "A5", upLeft.getBottom().getKey());
		
		// Test navigation from upRight
		
		assertNull("Upper right GameBoardSlot can't have up left neighbour!", upRight.getUpLeft());
		assertNull("Upper right GameBoardSlot can't have up right neighbour!", upRight.getUpRight());
		assertNull("Upper right GameBoardSlot can't have top neighbour!", upRight.getTop());
		assertNull("Upper right GameBoardSlot can't have left neighbour!", upRight.getRight());
		assertNull("Upper right GameBoardSlot can't have low left neighbour!", upRight.getLowRight());
		
		assertNotNull("Upper right GameBoardSlot must have left neighbour!", upRight.getLeft());
		assertEquals("Left neighbour of upper right doesn't lie at position F6 on GameBoard!", "F6", upRight.getLeft().getKey());
		assertNotNull("Upper right GameBoardSlot must have low left neighbour!", upRight.getLowLeft());
		assertEquals("Left neighbour of upper right doesn't lie at position F5 on GameBoard!", "F5", upRight.getLowLeft().getKey());
		assertNotNull("Upper right GameBoardSlot must have bottom neighbour!", upRight.getBottom());
		assertEquals("Bottom neighbour of upper right doesn't lie at position G5 on GameBoard!", "G5", upRight.getBottom().getKey());
		
		// Test navigation from lowLeft
		
		assertNull("Lower left GameBoardSlot can't have up left neighbour!", lowLeft.getUpLeft());
		assertNull("Lower left GameBoardSlot can't have low right neighbour!", lowLeft.getLowRight());
		assertNull("Lower left GameBoardSlot can't have bottom neighbour!", lowLeft.getBottom());
		assertNull("Lower left GameBoardSlot can't have left neighbour!", lowLeft.getLeft());
		assertNull("Lower left GameBoardSlot can't have low left neighbour!", lowLeft.getLowLeft());
		
		assertNotNull("Lower left GameBoardSlot must have right neighbour!", lowLeft.getRight());
		assertEquals("Right neighbour of lower left doesn't lie at position B1 on GameBoard!", "B1", lowLeft.getRight().getKey());
		assertNotNull("Lower left GameBoardSlot must have up right neighbour!", lowLeft.getUpRight());
		assertEquals("Upper right neighbour of lower left doesn't lie at position B2 on GameBoard!", "B2", lowLeft.getUpRight().getKey());
		assertNotNull("Lower left GameBoardSlot must have top neighbour!", lowLeft.getTop());
		assertEquals("Top neighbour of lower left doesn't lie at position A2 on GameBoard!", "A2", lowLeft.getTop().getKey());
		
		// Test navigation from lowRight
		
		assertNull("Lower left GameBoardSlot can't have up right neighbour!", lowRight.getUpRight());
		assertNull("Lower left GameBoardSlot can't have low right neighbour!", lowRight.getLowRight());
		assertNull("Lower left GameBoardSlot can't have bottom neighbour!", lowRight.getBottom());
		assertNull("Lower left GameBoardSlot can't have right neighbour!", lowRight.getRight());
		assertNull("Lower left GameBoardSlot can't have low left neighbour!", lowRight.getLowLeft());
		
		assertNotNull("Lower left GameBoardSlot must have left neighbour!", lowRight.getLeft());
		assertEquals("Left neighbour of lower right doesn't lie at position F1 on GameBoard!", "F1", lowRight.getLeft().getKey());
		assertNotNull("Lower left GameBoardSlot must have up left neighbour!", lowRight.getUpLeft());
		assertEquals("Upper Left neighbour of lower right doesn't lie at position F2 on GameBoard!", "F2", lowRight.getUpLeft().getKey());
		assertNotNull("Lower left GameBoardSlot must have top neighbour!", lowRight.getTop());
		assertEquals("Top neighbour of lower right doesn't lie at position G2 on GameBoard!", "G2", lowRight.getTop().getKey());
		
		// Test navigation from arbitrary
		
		assertNotNull(String.format("Arbitrary %s must have up right neighbour!", arbitrary.toString()), arbitrary.getUpRight());
		assertEquals(String.format("Upper right neighbour of arbitrary %s doesn't lie at position E5!", arbitrary.toString()),
					"E5", arbitrary.getUpRight().getKey());
		assertNotNull(String.format("Arbitrary %s must have low right neighbour!", arbitrary.toString()), arbitrary.getLowRight());
		assertEquals(String.format("Lower right neighbour of arbitrary %s doesn't lie at position E3!", arbitrary.toString()),
				"E3", arbitrary.getLowRight().getKey());
		assertNotNull(String.format("Arbitrary %s must have bottom neighbour!", arbitrary.toString()), arbitrary.getBottom());
		assertEquals(String.format("Bottom neighbour of arbitrary %s doesn't lie at position D3!", arbitrary.toString()),
				"D3", arbitrary.getBottom().getKey());
		assertNotNull(String.format("Arbitrary %s must have right neighbour!", arbitrary.toString()), arbitrary.getRight());
		assertEquals(String.format("Right neighbour of arbitrary %s doesn't lie at position E4!", arbitrary.toString()),
				"E4", arbitrary.getRight().getKey());
		assertNotNull(String.format("Arbitrary %s must have low left neighbour!", arbitrary.toString()), arbitrary.getLowLeft());
		assertEquals(String.format("Lower left neighbour of arbitrary %s doesn't lie at position C3!", arbitrary.toString()),
				"C3", arbitrary.getLowLeft().getKey());
		assertNotNull(String.format("Arbitrary %s must have left neighbour!", arbitrary.toString()), arbitrary.getLeft());
		assertEquals(String.format("Left neighbour of arbitrary %s doesn't lie at position C4!", arbitrary.toString()),
				"C4", arbitrary.getLeft().getKey());
		assertNotNull(String.format("Arbitrary %s must have up left neighbour!", arbitrary.toString()), arbitrary.getUpLeft());
		assertEquals(String.format("Upper left neighbour of arbitrary %s doesn't lie at position C5!", arbitrary.toString()),
				"C5", arbitrary.getUpLeft().getKey());
		assertNotNull(String.format("Arbitrary %s must have top neighbour!", arbitrary.toString()), arbitrary.getTop());
		assertEquals(String.format("Top neighbour of arbitrary %s doesn't lie at position D5!", arbitrary.toString()),
				"D5", arbitrary.getTop().getKey());
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
