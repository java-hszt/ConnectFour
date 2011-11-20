/**
 * Abstract representation of the game board independent of any user interface.
 */
package ch.hszt.connectfour.model.board;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import ch.hszt.connectfour.io.Serial;
import ch.hszt.connectfour.io.SerialObject;
import ch.hszt.connectfour.model.enumeration.DropColor;
/**
 * Represents model-based game board independent of the realization of the user interface.
 * @author Markus Vetsch
 * @version 1.0, 11.10.2011
 */
public class GameBoard implements Serial
{
	/**
	 * The maximum number of columns on the game board.
	 */
	public static final int NUMBER_OF_COLUMNS =  7;

	/**
	 * The maximum number of slots per column.
	 */
	public static final int NUMBER_OF_SLOTS =  6;
	
	private GameBoardLine[] columns;
	private GameBoardLine[] rows;
	private List<GameBoardDiagonal> diagonals;
	private Map<Integer, String> columnMap;

	/**
   	* Creates a new instance of the {@link GameBoard}.
   	*/
	public GameBoard() 
  	{
		// init game board structure => columns, rows, diagonals
		
		columnMap = new Hashtable<Integer, String>(NUMBER_OF_COLUMNS);
		
		columns = createColumns();		
		rows = createRows();
		diagonals = createDiagonals();
  	}

  	/**
  	 * Returns all relevant {@link GameBoard} elements (columns, rows, diagonals) represented by the list of {@link GameBoardSlotCollection} items.
  	 * @return All relevant {@link GameBoard} elements.
  	 */
  	public List<GameBoardSlotCollection> getAll() 
  	{
  		// Get all rows, columns and diagonals
  		
		List<GameBoardSlotCollection> slotCols = new ArrayList<GameBoardSlotCollection>();

		for (GameBoardLine column : columns)
		{
			slotCols.add(column);
		}		
		for (GameBoardLine row : rows)
		{
			slotCols.add(row);
		}		
		for (GameBoardDiagonal diagonal : diagonals)
		{
			slotCols.add(diagonal);
		}
		
		return slotCols;
  	}

  	/**
  	 * Returns all columns of the {@link GameBoard} as list of {@link GameBoardSlotCollection} items.
  	 * @return All the columns of the {@link GameBoard} as list of {@link GameBoardSlotCollection} items.
  	 */
  	public List<GameBoardSlotCollection> getColumns() 
  	{
		List<GameBoardSlotCollection> cols = new ArrayList<GameBoardSlotCollection>();
		
		for (GameBoardLine column : columns)
		{
			cols.add(column);
		}
		
		return cols;
  	}
  	
  	/**
  	 * Returns all rows of the {@link GameBoard} as list of {@link GameBoardSlotCollection} items.
  	 * Method is public for unit testing only !!!.
  	 * @return All the rows of the {@link GameBoard} as list of {@link GameBoardSlotCollection} items.
  	 */
  	public List<GameBoardSlotCollection> getRows()
  	{
  		List<GameBoardSlotCollection> rowList = new ArrayList<GameBoardSlotCollection>();
  		
  		for (GameBoardLine row : rows)
  		{
  			rowList.add(row);
  		}
  		
  		return rowList;
  	}
  	
  	/**
  	 * Returns all valid (i.e. consisting of >= 4 {@link GameBoardSlot}) diagonals of the 
  	 * {@link GameBoard} as list of {@link GameBoardSlotCollection} items.
  	 * Method is public for unit testing only !!!.
  	 * @return All the columns of the {@link GameBoard} as list of {@link GameBoardSlotCollection} items.
  	 */
  	public List<GameBoardSlotCollection> getDiagonals()
  	{
  		List<GameBoardSlotCollection> diagonalList = new ArrayList<GameBoardSlotCollection>();
  		
  		for (GameBoardDiagonal d : diagonals)
  		{
  			diagonalList.add(d);
  		}
  		
  		return diagonalList;
  	}

  	/**
   	* Inserts a drop at specified column, if possible.
   	* @param columnKey - The column identifier, which the drop shall be set for.
   	* @param color - The {@link DropColor} of the drop.
   	* @return The affected {@link GameBoardSlot}, if the drop was successfully inserted; otherwise <b>null</b> is returned.
   	* @throws IllegalArgumentException Thrown, if either an invalid columnKey was passed as argument or 
   	* if the associated column is already full and doesn't allow insertion of further drops.
   	*/
  	public synchronized GameBoardSlot insertDrop(String columnKey, DropColor color) 
  	{
  		// get correct column
  		
		GameBoardColumn column = getColumnByKey(columnKey);
		
		if (column != null)
		{
			// search for next slot
			
			GameBoardSlot slot = column.getNextEmptySlot();
			
			if (slot != null && slot.isEmpty())
			{
				// set the drop and return affected slot
				
				slot.setDrop(color);
				return slot;
			}
			else
			{				
				throw new IllegalArgumentException(String.format("Column %s is already full - can't insert further drops!", columnKey));
			}
		}
		else
		{
			throw new IllegalArgumentException(String.format("Column with key %s doesn't exist!", columnKey));
		}
  	}
  	
  	/**
  	 * Resets the {@link GameBoard} into its original state, i.e. without any drops set.
  	 */
  	public void reset()
  	{
  		for (GameBoardSlotCollection coll : getColumns())
  		{
  			for (GameBoardSlot slot : coll)
  			{
  				slot.clear();
  			}
  		}
  	}

  	/**
   	* Returns the corresponding {@link GameBoardColumn} by the id.
   	* @param id - The id of the {@link GameBoardColumn}.
   	* @return The associated {@link GameBoardColumn} or <b>NULL</b>, if no matching one found.
   	*/
  	GameBoardColumn getColumnById(int id) 
  	{
		if (columnMap.containsKey(id))
		{
			return getColumnByKey(columnMap.get(id));
		}
		
		return null;
  	}

  	/**
   	* Returns the corresponding {@link GameBoardColumn} by the associated key.
   	* @param columnName - The key of the {@link GameBoardColumn} as {@link String}.
   	* @return The associated {@link GameBoardColumn} or <b>NULL</b>, if no matching one found.
   	*/
  	public GameBoardColumn getColumnByKey(String columnName) 
  	{
		for (GameBoardLine line : columns)
		{
			if (line.isColumn())
			{
				GameBoardColumn column = (GameBoardColumn)line;
				
				if (column.getKey().equals(columnName.toUpperCase()))
				{
					return column;
				}
			}
		}
		
		return null;
  	}

  	private GameBoardLine[] createColumns() 
  	{
		GameBoardLine[] cols = new GameBoardColumn[GameBoard.NUMBER_OF_COLUMNS];
		
		for (int i = 0; i < cols.length; i++)
		{
			int columnId = i+1;
			
			GameBoardColumn column = new GameBoardColumn(columnId, this);
			cols[i] = column;
			
			if (!columnMap.containsKey(columnId))
			{
				columnMap.put(columnId, column.getKey());
			}
		}

		return cols;
  	}

  	private GameBoardLine[] createRows() 
  	{
		GameBoardLine[] rows = new GameBoardRow[GameBoard.NUMBER_OF_SLOTS];
		
		for (int i = 0; i < rows.length; i++)
		{
			int rowId = i+1;
			rows[i] = new GameBoardRow(rowId, this);
		}
		
		return rows;
  	}

  	private List<GameBoardDiagonal> createDiagonals() 
  	{
		List<GameBoardDiagonal> diagonalList = new ArrayList<GameBoardDiagonal>();
		
		diagonalList.addAll(createDownRightDiagonals());
		diagonalList.addAll(createUpRightDiagonals());
		
		return diagonalList;
  	}

  	private List<GameBoardDiagonal> createDownRightDiagonals() 
  	{
		List<GameBoardDiagonal> rightOnes = new ArrayList<GameBoardDiagonal>();
		
		// Search by upmost slots in all columns, i.e in first row
		
		for (GameBoardLine column : columns)
		{
			GameBoardSlot start = ((GameBoardColumn) column).getSlot(1);
			
			if (GameBoardDiagonal.isValid(start, true))
			{
				GameBoardDiagonal diagonal = new GameBoardDiagonal(start, true);
			
				// evaluate validity and uniqueness of diagonal
				
				if (!isDuplicate(diagonal, rightOnes))
				{
					rightOnes.add(new GameBoardDiagonal(start, true));
				}				
			}
		}
		
		// Now search by all slots downwards in first column
		
		GameBoardColumn leftMost = getColumnByKey("A");
		
		for (int i = 1; i <= NUMBER_OF_SLOTS; i++)
		{
			GameBoardSlot start = leftMost.getSlot(i);
			
			// evaluate validity and uniqueness of diagonal
			
			if (GameBoardDiagonal.isValid(start, true))
			{
				GameBoardDiagonal diagonal = new GameBoardDiagonal(start, true);
				
				if (!isDuplicate(diagonal, rightOnes))
				{
					rightOnes.add(new GameBoardDiagonal(start, true));
				}	
			}
		}
		
		return rightOnes;
  	}
  	
  	private boolean isDuplicate(GameBoardDiagonal diagonal, List<GameBoardDiagonal> diagonals)
  	{
  		for (GameBoardDiagonal d : diagonals)
  		{
  			if (diagonal.IsIdenticalWith(d))
  			{
  				return true;
  			}
  		}
  		
  		return false;
  	}

  	/**
  	 * Creates the <code>List<GameBoardDiagonal></code> in up right direction (bottom left to top right)
  	 * @return The entire list of {@link GameBoardDiagonal}.
  	 */
  	private List<GameBoardDiagonal> createUpRightDiagonals() 
  	{
		List<GameBoardDiagonal> leftOnes = new ArrayList<GameBoardDiagonal>();
		
		// Search by lower most slots in column
		
		for (GameBoardLine column : columns)
		{
			GameBoardSlot start = ((GameBoardColumn) column).getSlot(GameBoard.NUMBER_OF_SLOTS);
			
			if (GameBoardDiagonal.isValid(start, false))
			{
				GameBoardDiagonal diagonal = new GameBoardDiagonal(start, false);
				diagonal.toString();
				
				if (!isDuplicate(diagonal, leftOnes))
				{
					leftOnes.add(new GameBoardDiagonal(start, false));
				}				
			}
		}
		
		// Now search by all slots in first column
		
		GameBoardColumn leftMost = getColumnByKey("A");
		
		for (int i = 1; i <= GameBoard.NUMBER_OF_SLOTS; i++)
		{
			GameBoardSlot start = leftMost.getSlot(i);
			
			if (GameBoardDiagonal.isValid(start, false))
			{
				GameBoardDiagonal diagonal = new GameBoardDiagonal(start, false);
				
				if (!isDuplicate(diagonal, leftOnes))
				{
					leftOnes.add(new GameBoardDiagonal(start, false));
				}	
			}
		}
		
		return leftOnes;
  	}

	public void save(SerialObject obj)
	{
		for (GameBoardSlotCollection coll : getAll())
		{
			obj.saveList(coll.asList());
		}
	}

	public Serial load(SerialObject obj)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
