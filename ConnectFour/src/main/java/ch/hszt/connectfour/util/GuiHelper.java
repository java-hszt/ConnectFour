/**
 * 
 */
package ch.hszt.connectfour.util;

import java.awt.Window;

import javax.swing.JOptionPane;

import ch.hszt.connectfour.model.enumeration.DialogResult;

/**
 * @author Markus Vetsch
 * @version 1.0, 13.11.2011
 *
 */
public class GuiHelper
{	
	private GuiHelper()
	{		
	}
	
	public static void showInfo(Window parent, String message, String caption)
	{
		JOptionPane.showMessageDialog(parent, message, caption, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static DialogResult showQuestion(Window parent, String message, String caption)
	{
		int option = JOptionPane.showOptionDialog(parent, message, caption,
													JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
													null, null, null);
		
		return DialogResult.getResult(option);
	}
	
	public static void showError(Window parent, String message, String caption)
	{
		JOptionPane.showMessageDialog(parent, message, caption, JOptionPane.ERROR_MESSAGE);
	}
}
