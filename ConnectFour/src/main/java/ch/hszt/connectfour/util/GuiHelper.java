/**
 * 
 */
package ch.hszt.connectfour.util;

import java.awt.Window;

import javax.swing.JOptionPane;

import ch.hszt.connectfour.model.enumeration.DialogResult;

/**
 * Simple utility class for straightforward implementation of some message / info / error boxes.
 * @author Markus Vetsch
 * @version 1.0, 13.11.2011
 */
public class GuiHelper
{	
	/**
	 * Not to be instantiated
	 */
	private GuiHelper()
	{		
	}
	
	/**
	 * Shows an information message box in style of {@link JOptionPane#INFORMATION_MESSAGE}.
	 * @param parent - The owning {@link Window}; can be set to <b>null</b>, if not specified.
	 * @param message - The message to be displayed in the box.
	 * @param caption - The caption of the box.
	 */
	public static void showInfo(Window parent, String message, String caption)
	{
		JOptionPane.showMessageDialog(parent, message, caption, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Shows a question message box in style of {@link JOptionPane#QUESTION_MESSAGE} that simply offers Yes / No buttons for answering.
	 * @param parent - The owning {@link Window}; can be set to <b>null</b>, if not specified.
	 * @param message - The message to be displayed in the box.
	 * @param caption - The caption of the box.
	 * @return A value of the {@link DialogResult} enumeration representing the answer of the question box.
	 */
	public static DialogResult showQuestion(Window parent, String message, String caption)
	{
		int option = JOptionPane.showOptionDialog(parent, message, caption,
													JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
													null, null, null);
		
		return DialogResult.getResult(option);
	}
	
	/**
	 * Shows an error message box in style of {@link JOptionPane#ERROR_MESSAGE}.
	 * @param parent - The owning {@link Window}; can be set to <b>null</b>, if not specified.
	 * @param message - The message to be displayed in the box.
	 * @param caption - The caption of the box.
	 */
	public static void showError(Window parent, String message, String caption)
	{
		JOptionPane.showMessageDialog(parent, message, caption, JOptionPane.ERROR_MESSAGE);
	}
}
