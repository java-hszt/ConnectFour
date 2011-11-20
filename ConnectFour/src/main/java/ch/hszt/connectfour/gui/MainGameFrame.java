package ch.hszt.connectfour.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import ch.hszt.connectfour.control.GameController;
import ch.hszt.connectfour.control.GameObserver;
import ch.hszt.connectfour.control.GameStatisticUpdateTask;
import ch.hszt.connectfour.control.LocalGameController;
import ch.hszt.connectfour.exception.GameException;
import ch.hszt.connectfour.model.board.GameBoardColumn;
import ch.hszt.connectfour.model.enumeration.DialogResult;
import ch.hszt.connectfour.model.game.CpuPlayer;
import ch.hszt.connectfour.model.game.Game;
import ch.hszt.connectfour.util.DateHelper;
import ch.hszt.connectfour.util.GuiHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TreeMap;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.JTextArea;
import java.awt.Font;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JScrollPane;

import javax.swing.JTextField;

import org.aspectj.weaver.patterns.ConcreteCflowPointcut.Slot;

/**
 * Represents the frame presenting the game board allowing direct user interaction.
 * @author Markus Vetsch
 * @version 1.0, 31.10.2011
 */
public class MainGameFrame extends JDialog implements GameFrame
{
	//TODO: Extending JDialog, JInternalFrame or JFrame?
	
	private static final int COLUMNS = 7;
	private static final int ROWS = 6;
	
	private static final int WIDTH = 1024;
	private static final int HEIGHT = 768;
	
	private JLabel lblYellowPlayer;
	private JLabel lblRedPlayer;
	private JLabel lblNextDrop;
	private JLabel lblPlayers;
	
	private Container contentPane;
	
	private JPanel boardPanel;
	private JPanel gameInfoPanel;
	
	private JTextArea messageArea;
	private JScrollPane scrollPane;
	
	private Timer timer;
	
	private AppImageCanvas canvas;
	
	private Map<String, List<SlotPanel>> slotPanels;
	
	private Game game;
	private GameController controller;
	
	private JTextField txtDuration;
	private JTextField txtCurrentPlayer;
	private JTextField txtCompletedTurns;
	private JTextField txtRemainingTurns;
	private JTextField txtYellowDrops;
	private JTextField txtRedDrops;
	
	private boolean isCurrentCpuPlayer;
	
	/**
	 * Creates the {@link MainGameFrame}.
	 * @param owner - The owner {@link Window}.
	 * @param game - The {@link Game} to be linked to the {@link MainGameFrame}.
	 */
	public MainGameFrame(Window owner, Game game)
	{		
		super(owner);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		
		// Set the game and the game controller
		
		this.game = game;		
		controller = new LocalGameController(this);
		
		int xOrigin = owner.getX() + owner.getWidth();
		int yOrigin = owner.getY();
		
		// Resize the frame
		
		setBounds(xOrigin, yOrigin, WIDTH, HEIGHT);
		
		// Set title with players
		
		setTitle(String.format("Connect Four - Running Game: %1$s [%2$s] vs. %3$s [%4$s]",
								game.getStartPlayer().getName(), game.getStartPlayer().getDropColor().toString(),
								game.getOtherPlayer().getName(), game.getOtherPlayer().getDropColor().toString()));

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
		return controller;
	}
	
	/**
	 * Prints a message to the {@link JTextArea} in upper section of the {@link MainGameFrame}.
	 * @param message - The message to be printed.
	 */
	public void printMessage(String message)
	{
		messageArea.append(message + "\n");
	}
	
	/**
	 * Returns the {@link SlotPanel} for the given key or <b>null</b>, if there was no matching {@link SlotPanel} found.
	 * @param key - The key of the {@link SlotPanel} to be found.
	 * @throws IllegalArgumentException, if the key specified is <b>null</b>.
	 * @return The corresponding {@link SlotPanel} or <b>null</b>, if there was no matching {@link SlotPanel}.
	 */
	public SlotPanel getSlotPanel(String key)
	{
		if (key == null)
		{
			throw new IllegalArgumentException("The specified key was null!");
		}
		
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
	 * Runs the animation whenever inserting a drop.
	 * @param targetPanel - The target {@link SlotPanel} for the animation
	 * @param color - The {@link Color} of the drop to be inserted.
	 */
	public void runInsertAnimation(SlotPanel targetPanel, Color color)
	{
		new DropInsertionAnimation(this, targetPanel, color).run();
	}
	
	/**
	 * Runs the animation whenever a connect four situation is notified.
	 * @param panels - The affected list of {@link SlotPanel} instances.
	 */
	public void runConnectFourAnimation(List<SlotPanel> panels)
	{
		new ConnectFourAnimation(panels).run();
	}
	
	/**
	 * Closes / hides this instance and releases allocated resources.
	 */
	public void close()
	{
		setVisible(false);
		dispose();
	}
	
	/**
	 * Sets the game duration information.
	 * @param duration - The current duration information.
	 */
	public void setDuration(String duration)
	{
		txtDuration.setText(duration);
		txtDuration.invalidate();
	}
	
	/**
	 * Sets the information about current player for next turn.
	 * @param currentPlayer - The information about current player for next turn.
	 */
	public void setCurrentPlayer(String currentPlayer)
	{
		txtCurrentPlayer.setText(currentPlayer);
		txtCurrentPlayer.invalidate();
	}
	
	/**
	 * Sets the information about the number of completed turns.
	 * @param completedTurns - The information about the number of completed turns.
	 */
	public void setCompletedTurns(String completedTurns)
	{
		txtCompletedTurns.setText(completedTurns);
		txtCompletedTurns.invalidate();
	}
	
	/**
	 * Sets the information about the number of remaining turns.
	 * @param remainingTurns - The information about the number of remaining turns.
	 */
	public void setRemainingTurns(String remainingTurns)
	{
		txtRemainingTurns.setText(remainingTurns);
		txtRemainingTurns.invalidate();
	}
	
	/**
	 * Sets the information about the number of yellow drops on the game board.
	 * @param yellowDrops - The information about the number of yellow drops.
	 */
	public void setYellowDrops(String yellowDrops)
	{
		txtYellowDrops.setText(yellowDrops);
		txtYellowDrops.invalidate();
	}
	
	/**
	 * Sets the information about the number of red drops on the game board.
	 * @param redDrops - The information about the number of yellow red.
	 */
	public void setRedDrops(String redDrops)
	{
		txtRedDrops.setText(redDrops);
		txtRedDrops.invalidate();
	}
	
	public void updateIsCurrentCpuPlayer(boolean isCurrentCpuPlayer)
	{
		this.isCurrentCpuPlayer = isCurrentCpuPlayer;
		updateSlotPanels(!isCurrentCpuPlayer);
		
		if (this.isCurrentCpuPlayer)
		{
			// Turn execution of CPU player to take place in separate thread
			
			CpuPlayerTurn turn = new CpuPlayerTurn();
			Thread cpuPlayerThread = new Thread(turn, "CpuPlayerThread");
			cpuPlayerThread.start();
		}
	}

	/**
	 * Starts the timer, that periodically updates statistic information in the information section in a separate thread.
	 */
	public void startTimer()
	{
		GameStatisticUpdateTask update = new GameStatisticUpdateTask(controller, game.getStatistic());
		
		// Assign task, start immediately with a period of 1 second (1000 ms)
		
		timer.schedule(update, DateHelper.now(), 1000);
	}
	
	/**
	 * Stops the timer, that periodically updates statistic information in the information section.
	 */
	public void stopTimer()
	{
		timer.cancel();
	}
	
	/**
	 * Asks, whether a game restart shall be realized using the same game settings.
	 * @param isDraw - Indicates, if the recent {@link Game} ended with a draw.
	 * @return The {@link DialogResult}, which the question was answered with.
	 */
	public DialogResult askForRestart(boolean isDraw)
	{
		if (isDraw)
		{
			return GuiHelper.showQuestion(this,
										"The previous game ended with a draw. Would you like to start a new game with identical settings?",
										"Draw");
		}
		else
		{
			String winner = game.getStatus().getWinner().getName();
			return GuiHelper.showQuestion(this, 
											String.format("The winner of previous game is player %s! Would you like to start a new game with identical settings?", winner),
											"Connect Four");
		}		
	}
	
	/*
	 * ------------------------------------------------------
	 * GUI INIT CODE
	 * ------------------------------------------------------
	 */

	/**
	 * GUI initialization code
	 */
	private void initialize()
	{		
		contentPane.setLayout(null);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerSize(0);
		splitPane.setBounds(0, 70, 1024, 698);
		splitPane.setDividerLocation(0.75d);
		contentPane.add(splitPane);
			
		boardPanel = new JPanel();				
		boardPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		boardPanel.setLayout(null);
		boardPanel.setBackground(Color.BLUE);
		
		// board as left component in split panel
		
		splitPane.setLeftComponent(boardPanel);		
		
		gameInfoPanel = new JPanel();		
		gameInfoPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		gameInfoPanel.setBackground(new Color(238, 232, 170));
		gameInfoPanel.setLayout(null);
		
		// info panel as right component in split panel
		
		splitPane.setRightComponent(gameInfoPanel);
		
		JLabel lblDuration = new JLabel("Game Duration:");
		lblDuration.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblDuration.setBounds(10, 11, 235, 28);
		gameInfoPanel.add(lblDuration);
		
		JLabel lblCurrentPlayer = new JLabel("Current Player:");
		lblCurrentPlayer.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblCurrentPlayer.setBounds(10, 83, 235, 28);
		gameInfoPanel.add(lblCurrentPlayer);
		
		JLabel lblCompletedTurns = new JLabel("Completed Turns:");
		lblCompletedTurns.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblCompletedTurns.setBounds(10, 157, 235, 28);
		gameInfoPanel.add(lblCompletedTurns);
		
		JLabel lblRemainingTurns = new JLabel("Remaining Turns:");
		lblRemainingTurns.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblRemainingTurns.setBounds(10, 232, 235, 28);
		gameInfoPanel.add(lblRemainingTurns);
		
		JLabel lblYellowDrops = new JLabel("Inserted Drops YELLOW:");
		lblYellowDrops.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblYellowDrops.setBounds(10, 303, 235, 28);
		gameInfoPanel.add(lblYellowDrops);
		
		JLabel lblRedDrops = new JLabel("Inserted Drops RED:");
		lblRedDrops.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblRedDrops.setBounds(10, 373, 235, 28);
		gameInfoPanel.add(lblRedDrops);
		
		txtDuration = new JTextField();
		txtDuration.setText("HH:mm:ss");
		txtDuration.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtDuration.setEditable(false);
		txtDuration.setBounds(10, 44, 217, 28);
		gameInfoPanel.add(txtDuration);
		txtDuration.setColumns(10);
		
		txtCurrentPlayer = new JTextField();
		txtCurrentPlayer.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtCurrentPlayer.setEditable(false);
		txtCurrentPlayer.setColumns(10);
		txtCurrentPlayer.setBounds(10, 118, 217, 28);
		gameInfoPanel.add(txtCurrentPlayer);
		
		txtCompletedTurns = new JTextField();
		txtCompletedTurns.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtCompletedTurns.setEditable(false);
		txtCompletedTurns.setColumns(10);
		txtCompletedTurns.setBounds(10, 193, 217, 28);
		gameInfoPanel.add(txtCompletedTurns);
		
		txtRemainingTurns = new JTextField();
		txtRemainingTurns.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtRemainingTurns.setEditable(false);
		txtRemainingTurns.setColumns(10);
		txtRemainingTurns.setBounds(10, 264, 217, 28);
		gameInfoPanel.add(txtRemainingTurns);
		
		txtYellowDrops = new JTextField();
		txtYellowDrops.setText("0");
		txtYellowDrops.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtYellowDrops.setEditable(false);
		txtYellowDrops.setColumns(10);
		txtYellowDrops.setBounds(10, 334, 217, 28);
		gameInfoPanel.add(txtYellowDrops);
		
		txtRedDrops = new JTextField();
		txtRedDrops.setText("0");
		txtRedDrops.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtRedDrops.setEditable(false);
		txtRedDrops.setColumns(10);
		txtRedDrops.setBounds(10, 406, 217, 28);
		gameInfoPanel.add(txtRedDrops);
		
		// The application image
		
		canvas = new AppImageCanvas(loadApplicationImage());
		canvas.setBounds(768, 0, 249, 70);
		contentPane.add(canvas);
		
		scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(new Color(130, 135, 144), 2));
		scrollPane.setBounds(0, 0, 768, 70);
		contentPane.add(scrollPane);
		
		messageArea = new JTextArea();
		scrollPane.setViewportView(messageArea);
		messageArea.setEditable(false);
		messageArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
		messageArea.setLineWrap(true);
		
		// This piece of code enables auto-refresh of text area
		
		DefaultCaret caret = (DefaultCaret) messageArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		// Create slot panels
		
		slotPanels = buildSlotPanels();		
		addSlotPanels(boardPanel);
		
		printMessage("Successfully initialized GUI ...");
		
		setVisible(true);
	}
	
	/* -----------------------------------------------------------------
	 * HELPER FOR GUI INIT (BUILDING SLOT PANELS / APP IMAGE)
	 * -----------------------------------------------------------------
	 */

	/**
	 * Adds the {@link MainGameFrame#slotPanels} to the specified parent container as {@link JPanel}.
	 * @param parent - The {@link JPanel} container, which the {@link SlotPanel} are to be added to.
	 */
	private void addSlotPanels(JPanel parent)
	{		
		//TODO: Configurable and relative to screen size?
		
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
	 * Creates a {@link Map} of all {@link SlotPanel} for the {@link MainGameFrame}.
	 * @return A {@link Map} of the {@link SlotPanel} to be drawn on the {@link MainGameFrame} using the column identifier
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
	
	/**
	 * Loads the application image to be depicted in the separate canvas
	 * @return The loaded {@link Image} to be depicted.
	 */
	private Image loadApplicationImage()
	{
		//TODO: Move to config
		
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
	 * Enables / disables event handling on all the {@link SlotPanel} on the game board.
	 * @param enable - If <b>true</b>, enables event handling for all the {@link SlotPanel} on the game board;
	 *  otherwise, disables event handling entirely (e.g. for {@link CpuPlayer}).
	 */
	private void updateSlotPanels(boolean enable)
	{
		for (Component component : boardPanel.getComponents())
		{
			if (component instanceof SlotPanel)
			{
				SlotPanel panel = (SlotPanel) component;
				panel.updateMouseAdapter(enable);
			}
		}
	}
	
	/* ---------------------------------------------------------
	 * LISTENER / ADAPTER / HELPER IMPLEMENTATIONS
	 * ---------------------------------------------------------
	 */
	
	/**
	 * Defines behavior of the frame, after initialization has completed.
	 * @author Markus Vetsch
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
				// Set the game observer
				
				game.setObserver(new GameObserver(controller));	
				
				Window owner = getOwner();
				
				// Update relation to setup frame as owning frame
				
				if (owner != null && owner instanceof SetupFrame)
				{
					SetupFrame frame = (SetupFrame) owner;
					frame.updateGameFrame((MainGameFrame)e.getWindow());
				}
				
				// Start the game now
				
				controller.startGame(game);				
				
				// Start the timer in a background thread
				
				startTimer();
				
				printMessage("Timer for periodic update of game statistics started ...");
			}
			catch (GameException ex)
			{
				GuiHelper.showError(e.getWindow(), "Game couldn't be started! Reason: " + ex.getMessage(),
												"Unable to start game");
				printMessage("Error on starting game! Reason: " + ex.getMessage());
			}			
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
		 */
		public void windowClosed(WindowEvent e)
		{
			stopTimer();
		}
	}
	
	/**
	 * Defines behavior, after one of the {@link SlotPanel} notified a click event.
	 * @author Markus Vetsch
	 */
	private class SlotPanelMouseAdapter extends MouseAdapter
	{		
		/* (non-Javadoc)
		 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent e) 
		{			
			if (e.getComponent() != null && e.getComponent() instanceof SlotPanel)
			{
				SlotPanel panel = (SlotPanel) e.getComponent();
				
				try
				{
					controller.executeTurn(game, panel.getColumn());
				}
				catch (GameException ex)
				{
					controller.printMessage("Last turn couldn't be executed! Reason: " + ex.getMessage());
				}
				catch (Exception ex)
				{
					controller.printMessage("Unexpected error occurred upon execution of last turn! Reason: " + ex.toString());
				}				
			}
		}
	}
	
	/**
	 * Implementation of separate thread to simulate turns for a {@link CpuPlayer}.
	 */
	private class CpuPlayerTurn implements Runnable
	{		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run()
		{			
			try
			{				
				CpuPlayer player = (CpuPlayer) game.getStatus().getCurrentPlayer();
				
				// Simulate CPU thinking time (1 to 5 seconds)
				
				final int thinkingTime = (1 + new Random().nextInt(5)) * 1000;				
				controller.printMessage(String.format("CPU Player [%s] thinking about next turn ...",
														player.getDropColor().toString()));
				
				Thread.sleep(thinkingTime);
				
				// Let the CPU player decide, which column to insert the drop into
				
				Game game = getGame();
				String column = player.determineNextTurn(game.getBoard());

				// Finally execute the turn
				
				controller.executeTurn(game, column);
			}
			catch (GameException ex)
			{
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			catch (InterruptedException ex)
			{
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}		
	}
}
