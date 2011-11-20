package ch.hszt.connectfour;

import java.awt.EventQueue;

import ch.hszt.connectfour.gui.SetupFrame;
import ch.hszt.connectfour.util.GuiHelper;

/**
 * Starting class of the application providing the main method.
 */
public class ConnectFour 
{
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		// Automatically generated code by GUI designer
		
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					SetupFrame frame = new SetupFrame();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					GuiHelper.showError(null,
										String.format("Error during startup: %s", e.getMessage()),
										"Unexpected error");
					e.printStackTrace();
				}
			}
		});
	}
}
