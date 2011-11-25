/**
 * 
 */
package ch.hszt.connectfour.model.enumeration;

import ch.hszt.connectfour.model.board.GameBoardColumn;
import ch.hszt.connectfour.model.board.GameBoardDiagonal;
import ch.hszt.connectfour.model.game.DropSequence;

/**
 * Represents the type of a {@link DropSequence}
 * @author Markus Vetsch
 * @version 1.0, 21.11.2011
 */
public enum DropSequenceDirection
{
	/**
	 * Represents a {@link DropSequence} in direction of a {@link GameBoardColumn}
	 */
	COLUMN,
	/**
	 * Represents a {@link DropSequence} in direction of a {@link GameBoardRow}
	 */
	ROW,
	/**
	 * Represents a {@link DropSequence} in up right direction of a {@link GameBoardDiagonal}
	 */
	DIAGONAL_UP_RIGHT,
	/**
	 * Represents a {@link DropSequence} in low right direction of a {@link GameBoardDiagonal}
	 */
	DIAGONAL_LOW_RIGHT,
	/**
	 * Represents an indeterminate direction of a {@link DropSequence}.
	 */
	INDETERMINATE
}
