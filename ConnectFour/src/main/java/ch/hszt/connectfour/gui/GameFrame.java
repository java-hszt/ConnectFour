package ch.hszt.connectfour.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import ch.hszt.connectfour.control.GameController;
import ch.hszt.connectfour.control.GameObserver;
import ch.hszt.connectfour.control.GameStatisticUpdateTask;
import ch.hszt.connectfour.exception.GameException;
import ch.hszt.connectfour.model.board.GameBoardColumn;
import ch.hszt.connectfour.model.game.Game;
import ch.hszt.connectfour.model.game.GameStatistic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TreeMap;
import javax.swing.border.LineBorder;
import javax.swing.JTextArea;
import java.awt.Font;
import java.io.File;
import java.io.StringWriter;
import javax.swing.JScrollPane;

import org.aspectj.weaver.patterns.ConcreteCflowPointcut.Slot;

/**
 * Represents the frame presenting the game board allowing direct user interaction.
 * @author Daniel Stutz, Markus Vetsch
 * @version 1.0, 31.10.2011
 */
public class GameFrame extends JDialog
{
	//TODO: Extending JDialog, JInternalFrame or JFrame?
	
	private static final int COLUMNS = 7;
	private static final int ROWS = 6;
	
	private static final int WIDTH = 1024;
	private static final int HEIGHT = 768;
	
	private JLabel lblYellowPlayer;
	private JLabel lblRedPlayer;
	private JLabel lblNextDrop;
	private JLabel lblYellowDrops;
	private JLabel lblRedDrops;
	private JLabel lblPlayers;
	
	private Container contentPane;
	
	private JPanel boardPanel;
	private JPanel gameInfoPanel;
	
	JTextArea messageArea;
	
	private Timer timer;
	
	private AppImageCanvas canvas;
	
	private Map<String, List<SlotPanel>> slotPanels;
	
	private Game game;
	private GameController controller;
	
	/**
	 * Creates the {@link GameFrame}.
	 * @param owner - The owner {@link Frame}.
	 * @param isModal - Defines, whether this {@link GameFrame} is to be displayed as modal {@link Frame}.
	 * @param game - The {@link Game} to be linked to the {@link GameFrame}.
	 */
	public GameFrame(Frame owner, boolean isModal, Game game)
	{		
		super(owner, isModal);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		
		this.game = game;
		
		int xOrigin = owner.getX() + owner.getWidth();
		int yOrigin = owner.getY();
		
		// Resize the frame
		
		setBounds(xOrigin, yOrigin, WIDTH, HEIGHT);
		
		// Set title with players
		
		setTitle(String.format("Connect Four - Running Game: %1$s [%2$s drops] vs. %3$s [%4$s drops]",
								game.getStartPlayer().getName(), game.getStartPlayer().getDropColor(),
								game.getOtherPlayer().getName(), game.getOtherPlayer().getDropColor()));

		contentPane = getContentPane();
		
		// Add a window listener
		
		addWindowListener(new GameFrameAdapter());
		
		// Create the timer
		
		timer = new Timer("UpdateTimer");
		
		initialize();
	}
	
	/**
	 * Grants access to the underlying {@link Game}.
	 * @return The underlying {@link Game} instance.
	 */
	public Game getGame()
	{
		return game;
	}
	
	/**
	 * Grants access to the underlying {@link GameController}.
	 * @return The underlying {@link GameController} instance.
	 */
	public GameController getController()
	{
		//TODO: Concrete implementation of the game controller
		
		return controller;
	}
	
	/**
	 * Prints a message to the {@link JTextArea} in upper section of the {@link GameFrame}.
	 * @param message - The message to be printed.
	 */
	public void printMessage(String message)
	{
		messageArea.append(message + "\n");
		messageArea.invalidate();
	}

	private void initialize()
	{		
		contentPane.setLayout(null);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerSize(0);
		splitPane.setBounds(0, 70, 1024, 698);
		splitPane.setDividerLocation(0.75d);
		contentPane.add(splitPane);
			
		boardPanel = new JPanel();				
		boardPanel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		boardPanel.setLayout(null);
		boardPanel.setBackground(Color.BLUE);
		
		splitPane.setLeftComponent(boardPanel);		
		
		gameInfoPanel = new JPanel();		
		gameInfoPanel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		gameInfoPanel.setBackground(new Color(238, 232, 170));
		gameInfoPanel.setLayout(null);
		
		splitPane.setRightComponent(gameInfoPanel);
		
//		lblYellowPlayer = new JLabel("Yellow player :");
//		lblYellowPlayer.setBounds(24, 241, 229, 14);
//		gameInfoPanel.add(lblYellowPlayer);
//		
//		lblYellowDrops = new JLabel("Yellow drops: ");
//		lblYellowDrops.setBounds(24, 257, 229, 14);
//		gameInfoPanel.add(lblYellowDrops);
//		
//		lblRedPlayer = new JLabel("Red player: ");
//		lblRedPlayer.setBounds(24, 282, 229, 14);
//		gameInfoPanel.add(lblRedPlayer);
//						
//		lblRedDrops = new JLabel("Red drops:");
//		lblRedDrops.setBounds(24, 294, 229, 14);
//		gameInfoPanel.add(lblRedDrops);
		
		slotPanels = buildSlotPanels();
		
		addSlotPanels(boardPanel);
		
		canvas = new AppImageCanvas(loadApplicationImage());
		canvas.setBounds(768, 0, 249, 70);
		contentPane.add(canvas);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(new Color(130, 135, 144), 2));
		scrollPane.setBounds(0, 0, 768, 70);
		contentPane.add(scrollPane);
		
		messageArea = new JTextArea();
		messageArea.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		scrollPane.setViewportView(messageArea);
		messageArea.setEditable(false);
		messageArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
		messageArea.setLineWrap(true);
		
		printMessage("Successfully initialized GUI ...");
		
		setVisible(true);
	}
	
	
	private SlotPanel getSlotPanel(String key)
	{
		String keyUpper = key.toUpperCase();
		String column = keyUpper.substring(0,1);
		
		for (SlotPanel panel : slotPanels.get(column))
		{
			if (panel.getKey().equals(keyUpper))
			{
				return panel;
			}
		}
		
		return null;
	}

	/**
	 * Adds the {@link GameFrame#slotPanels} to the specified parent container as {@link JPanel}.
	 * @param parent - The {@link JPanel} container, which the {@link SlotPanel} are to be added to.
	 */
	private void addSlotPanels(JPanel parent)
	{		
		final int labelOffset = 15;								// Label offset for row / column labels
		
		final int xOrigin = 50;									// Offset of panel origin
		final int yOrigin = 50;
		
		final int vertOffset = 15;
		final int horOffset = 10;
		
		final int gapX = SlotPanel.DIAMETER + horOffset;		// x offset of slot panels = panel diameter + customized offset
		final int gapY = SlotPanel.DIAMETER + vertOffset;		// y offset of slot panels = panel diameter + customized offset
		
		int positionX = parent.getX() + xOrigin;				// Absolute x position relative to orgigin of parent container
		int positionY = parent.getY() + yOrigin;				// Absolute y position relative to orgigin of parent container
		
		boolean rowLabelsCreated = false;						// Helper flag, to create row labels only once
		
		// Iterate all column keys in map
		
		for (String columnKey : slotPanels.keySet())
		{
			// Define label position of column label
			
			int columnLabelPosX = positionX + (SlotPanel.DIAMETER / 2) - 10;
			int columnLabelPosY = positionY - labelOffset;

			// Add column label
			
			BoardLabel columnLabel = new BoardLabel(columnKey, columnLabelPosX, columnLabelPosY, WIDTH, 50);
			parent.add(columnLabel);
			
			// Get all the panels of current column
			
			List<SlotPanel> columnPanels = slotPanels.get(columnKey);

			// Add slot panels per column from top to bottom
			// iterate backwards => highest row number on top, lowest on bottom
			
			for (int i = columnPanels.size() - 1; i >= 0; i--)
			{
				// Get panel, resize and add it to the container
				
				SlotPanel current = columnPanels.get(i);
				current.setBounds(positionX, positionY, current.getWidth(), current.getHeight());				
				parent.add(current);
				
				// Add row labels, if not created yet
				
				if (!rowLabelsCreated)
				{
					// Define the position of the label
					
					int rowLabelPosX = positionX - 2 * labelOffset;
					int rowLabelPosY = positionY + (SlotPanel.DIAMETER / 2) + 10;		// At about half the slot panel diameter, i.e. centered
					
					// Add row label
					
					BoardLabel rowLabel = new BoardLabel(Integer.toString(i+1), rowLabelPosX, rowLabelPosY, 50, HEIGHT);
					parent.add(rowLabel);
				}
				
				// Add vertical offset for next slot panel position
				
				positionY += gapY;
			}
			
			// Set flag for row label creation after first completion of inner loop
			// to prevent further label creation
			
			rowLabelsCreated = true;
			
			// Add horizontal offset for slot panel position in next column
			// Reset vertical position to start position
			
			positionX += gapX;
			positionY = parent.getY() + yOrigin;			
		}
	}

	/**
	 * Creates a {@link Map} of all {@link SlotPanel} for the {@link GameFrame}.
	 * @return A {@link Map} of the {@link SlotPanel} to be drawn on the {@link GameFrame} using the column identifier
	 *  as key and a {@link List} of associated {@link SlotPanel} as values.
	 */
	private Map<String, List<SlotPanel>> buildSlotPanels()
	{
		// TreeMap to enable natural sorting by String
		
		Map<String, List<SlotPanel>> panels = new TreeMap<String, List<SlotPanel>>();
		
		for (int i = 1; i <= COLUMNS; i++)
		{
			// Create column key by id
			
			String columnKey = GameBoardColumn.assignKeyById(i);
			
			// New column => new List of slot panels
			
			if (!panels.containsKey(columnKey))
			{
				panels.put(columnKey, new ArrayList<SlotPanel>());
			}
			
			List<SlotPanel> panelList = panels.get(columnKey);
			
			// Add the slot panels and directly assign a event handler
			
			for (int j = 1; j <= ROWS; j++)
			{
				panelList.add(new SlotPanel(columnKey, j, new SlotPanelMouseAdapter()));
			}
		}
		
		return panels;
	}	
	
	private Image loadApplicationImage()
	{
		String appDir = System.getProperty("user.dir");
		String imagePath = appDir + File.separator + "resources" + File.separator + "Image.png";
		
		File imageFile = new File(imagePath);
		
		if (imageFile.exists())
		{
			try
			{
				Image image = ImageIO.read(imageFile);
				return image;
			}
			catch (Exception e)
			{
				return null;
			}
		}
		
		return null;
	}
	
	/**
	 * Defines behavior of the frame, after initialization has completed.
	 * @author Markus Vetsch, Daniel Stutz
	 * @version 1.0, 31.10.2011
	 */
	private class GameFrameAdapter extends WindowAdapter
	{
		/* (non-Javadoc)
		 * @see java.awt.event.WindowAdapter#windowOpened(java.awt.event.WindowEvent)
		 */
		@Override
		public void windowOpened(WindowEvent e)
		{
			try
			{
				// TODO: Once controller completely implemented, start the game using the controller
				
				//controller.startGame(game);
				
				//TODO: Is it necessary to store a reference to the observer?
				
				//game.setObserver(new GameObserver(controller));
				game.start();
				
				printMessage("Game successfully started ...");
				
				//TODO: Start the timer	for update of game duration			
				
//				GameStatisticUpdateTask update = new GameStatisticUpdateTask(controller, game.getStatistic());
//				
//				timer.schedule(update, GameStatistic.now());
				
				printMessage("Timer for periodic update of game statistics started ...");
			}
			catch (GameException ex)
			{
				JOptionPane.showMessageDialog(e.getWindow(), "Game couldn't be started! Reason: " + ex.getMessage(),
												"Unable to start game", JOptionPane.ERROR_MESSAGE);
				printMessage("Error on starting game! Reason: " + ex.getMessage());
			}			
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
		 */
		public void windowClosed(WindowEvent e)
		{
			timer.cancel();		
		}
	}
	
	/**
	 * Defines behavior, after one of the {@link SlotPanel} received a click event.
	 * @author Markus Vetsch, Daniel Stutz
	 * @version 1.0, 31.10.2011
	 */
	private class SlotPanelMouseAdapter extends MouseAdapter
	{		
		/* (non-Javadoc)
		 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent e) 
		{			
			if (e.getComponent() != null)
			{
				SlotPanel panel = (SlotPanel) e.getComponent();
				
//				try
//				{
//					TODO - create real controller implementation
//					controller.executeTurn(game, panel.getColumn());
//				}
//				catch (GameException ex)
//				{
//					//TODO
//				}
//				catch (Exception ex)
//				{
//					//TODO
//				}				
			}
		}
	}
}
