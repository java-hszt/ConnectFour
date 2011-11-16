/**
 * 
 */
package ch.hszt.connectfour.model.enumeration;

import javax.swing.JOptionPane;

/**
 * Enumeration representing possible answers of a
 *  {@link JOptionPane#showOptionDialog(java.awt.Component, Object, String, int, int, javax.swing.Icon, Object[], Object)} return value.
 * @author Markus Vetsch
 * @version 1.0, 14.11.2011
 */
public enum DialogResult
{
	/**
	 * Represents the answer of a YES button.
	 */
	YES(JOptionPane.YES_OPTION),
	/**
	 * Represents the answer of a NO button.
	 */
	NO(JOptionPane.NO_OPTION),	
	/**
	 * Represents the answer of a CANCEL button.
	 */
	CANCEL(JOptionPane.CANCEL_OPTION),
	/**
	 * Represents the answer of a CLOSE button or the default close behavior.
	 */
	CLOSE(JOptionPane.CLOSED_OPTION);
	
	private final int optionCode;
	
	private DialogResult(int optionCode)
	{
		this.optionCode = optionCode;
	}
	
	public static DialogResult getResult(int option)
	{
		switch (option)
		{
			case JOptionPane.YES_OPTION:
				return YES;
			case JOptionPane.NO_OPTION:
				return NO;
			case JOptionPane.CANCEL_OPTION:
				return CANCEL;
			case JOptionPane.CLOSED_OPTION:
				return CLOSE;
			default:
				return null;
		}
	}
}
