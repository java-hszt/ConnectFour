package ch.hszt.connectfour.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.SystemColor;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Font;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import ch.hszt.connectfour.exception.GameException;
import ch.hszt.connectfour.model.enumeration.PlayerType;
import ch.hszt.connectfour.model.enumeration.SkillLevel;
import ch.hszt.connectfour.model.game.Game;
import ch.hszt.connectfour.model.game.Player;
import ch.hszt.connectfour.model.game.PlayerFactory;

/**
 * Represents the {@link SetupFrame} enabling definition of game settings 
 * and control over starting / stopping / loading / saving a game.
 * @author Markus Vetsch, Daniel Stutz
 * @version 1.0, 29.10.2011
 */
public class SetupFrame extends JFrame
{
	private JPanel contentPane;
	private JPanel pnlFirstPlayer;
	private JPanel pnlSecondPlayer;
	
	private JTextField txtNameFirstPlayer;
	private JTextField txtNameSecondPlayer;
	
	private JLabel lblFirstPlayerTitle;
	private JLabel lblFirstPlayer;
	private JLabel lblNameFirstPlayer;	
	private JLabel lblLevelFirstPlayer;	
	
	private JLabel lblSecondPlayerTitle;
	private JLabel lblNameSecondPlayer;	
	private JLabel lblSecondPlayer;
	private JLabel lblLevelSecondPlayer;
	
	private JComboBox cbxLevelFirstPlayer;
	private JComboBox cbxLevelSecondPlayer;
	private JComboBox cbxFirstPlayer;
	private JComboBox cbxSecondPlayer;
	
	private JButton btnStartNewGame;
	private JButton btnLoadGame;
	private JButton btnSaveGame;
	private JButton btnStopGame;
	
	private GameFrame gameFrame;

	/**
	 * Creates the {@link SetupFrame}.
	 */
	public SetupFrame()
	{		
		setTitle("Connect Four - Game Setup");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 350);
		setResizable(false);
		
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.window);
		contentPane.setBorder(null);
		setContentPane(contentPane);
		
		// Initialize all child components
		
		initialize();
	}
	
	/**
	 * Creates a {@link Game} of underlying settings within the {@link SetupFrame}.
	 * @return A {@link Game} instance created according to defined settings within the {@link SetupFrame}.
	 */
	public Game createGame()
	{
		// Create first player
		
		PlayerType typeFirst = (PlayerType) cbxFirstPlayer.getSelectedItem();
		SkillLevel levelFirst = (SkillLevel) cbxLevelFirstPlayer.getSelectedItem();
		String nameFirst = txtNameFirstPlayer.getText();
		
		Player first = PlayerFactory.createPlayer(typeFirst, nameFirst, levelFirst);
		
		// Create second player
		
		PlayerType typeSecond = (PlayerType) cbxSecondPlayer.getSelectedItem();
		SkillLevel levelSecond = (SkillLevel) cbxLevelSecondPlayer.getSelectedItem();
		String nameSecond = txtNameSecondPlayer.getText();
		
		Player second = PlayerFactory.createPlayer(typeSecond, nameSecond, levelSecond);
		
		// Finally create the game
		
		return new Game(first, second);
	}
	
	private void initialize()
	{		
		// Entire settings panel
		
		JPanel pnlSettings = new JPanel();
		pnlSettings.setBackground(new Color(176, 224, 230));
		pnlSettings.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		
		// Buttons to start, stop, load, save game
		
		btnStartNewGame = new JButton("Start New Game");
		btnStartNewGame.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnStartNewGame.addActionListener(new StartGameActionListener(this));
		
		btnStopGame = new JButton("Stop Game");
		btnStopGame.setEnabled(false);
		btnStopGame.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnStopGame.addActionListener(new StopGameActionListener(this));
		
		btnLoadGame = new JButton("Load Game");
		btnLoadGame.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		btnSaveGame = new JButton("Save Game");
		btnSaveGame.setEnabled(false);
		btnSaveGame.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		// Panels for first / second player setup
		
		pnlSecondPlayer = new JPanel();
		pnlSecondPlayer.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		pnlSecondPlayer.setLayout(null);
		pnlSecondPlayer.setBackground(new Color(250, 250, 210));

		pnlFirstPlayer = new JPanel();
		pnlFirstPlayer.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		pnlFirstPlayer.setLayout(null);
		pnlFirstPlayer.setBackground(new Color(250, 250, 210));
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(pnlSettings, GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(pnlSettings, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
		);		
		
		GroupLayout gl_pnlSettings = new GroupLayout(pnlSettings);
		gl_pnlSettings.setHorizontalGroup(
			gl_pnlSettings.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlSettings.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlSettings.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pnlSettings.createSequentialGroup()
							.addComponent(btnStartNewGame)
							.addGap(18)
							.addComponent(btnStopGame, GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))
						.addComponent(pnlFirstPlayer, GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE))
					.addGap(18)
					.addGroup(gl_pnlSettings.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pnlSettings.createSequentialGroup()
							.addComponent(btnLoadGame, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(btnSaveGame, GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE))
						.addComponent(pnlSecondPlayer, GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_pnlSettings.setVerticalGroup(
			gl_pnlSettings.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlSettings.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlSettings.createParallelGroup(Alignment.TRAILING)
						.addComponent(pnlSecondPlayer, GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
						.addComponent(pnlFirstPlayer, GroupLayout.PREFERRED_SIZE, 243, GroupLayout.PREFERRED_SIZE))
					.addGap(21)
					.addGroup(gl_pnlSettings.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnStartNewGame, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnLoadGame, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSaveGame, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnStopGame, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		
		pnlSettings.setLayout(gl_pnlSettings);
		
		// Elements of player panels; labels, combo boxes and text fields
		
		lblNameFirstPlayer = new JLabel("Name:");
		lblNameFirstPlayer.setBounds(10, 202, 40, 17);
		pnlFirstPlayer.add(lblNameFirstPlayer);
		lblNameFirstPlayer.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		txtNameFirstPlayer = new JTextField();
		txtNameFirstPlayer.setFont(new Font("Tahoma", Font.PLAIN, 14));
		pnlFirstPlayer.add(txtNameFirstPlayer);
		txtNameFirstPlayer.setBounds(86, 196, 180, 29);
		txtNameFirstPlayer.setColumns(10);
		
		lblFirstPlayer = new JLabel("Player:");
		lblFirstPlayer.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFirstPlayer.setBounds(10, 70, 51, 17);
		pnlFirstPlayer.add(lblFirstPlayer);
		
		lblLevelFirstPlayer = new JLabel("Level:");
		lblLevelFirstPlayer.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblLevelFirstPlayer.setBounds(10, 136, 40, 17);
		pnlFirstPlayer.add(lblLevelFirstPlayer);
		
		cbxFirstPlayer = new JComboBox();
		cbxFirstPlayer.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cbxFirstPlayer.setModel(new DefaultComboBoxModel(PlayerType.values()));
		cbxFirstPlayer.addActionListener(new PlayerComboBoxItemListener());
		cbxFirstPlayer.setBounds(86, 64, 180, 29);		
		pnlFirstPlayer.add(cbxFirstPlayer);
		
		cbxLevelFirstPlayer = new JComboBox();
		cbxLevelFirstPlayer.setModel(new DefaultComboBoxModel(SkillLevel.values()));
		cbxLevelFirstPlayer.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cbxLevelFirstPlayer.setBounds(86, 130, 180, 29);
		pnlFirstPlayer.add(cbxLevelFirstPlayer);
		
		lblFirstPlayerTitle = new JLabel("First Player");
		lblFirstPlayerTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblFirstPlayerTitle.setBounds(90, 11, 121, 17);
		pnlFirstPlayer.add(lblFirstPlayerTitle);

		lblNameSecondPlayer = new JLabel("Name:");
		lblNameSecondPlayer.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNameSecondPlayer.setBounds(10, 202, 40, 17);
		pnlSecondPlayer.add(lblNameSecondPlayer);
		
		txtNameSecondPlayer = new JTextField();
		txtNameSecondPlayer.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtNameSecondPlayer.setColumns(10);
		txtNameSecondPlayer.setBounds(88, 196, 180, 29);
		pnlSecondPlayer.add(txtNameSecondPlayer);
		
		lblSecondPlayer = new JLabel("Player:");
		lblSecondPlayer.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSecondPlayer.setBounds(10, 70, 51, 17);
		pnlSecondPlayer.add(lblSecondPlayer);
		
		cbxSecondPlayer = new JComboBox();
		cbxSecondPlayer.setModel(new DefaultComboBoxModel(PlayerType.values()));
		cbxSecondPlayer.addActionListener(new PlayerComboBoxItemListener());
		cbxSecondPlayer.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cbxSecondPlayer.setBounds(88, 64, 180, 29);
		pnlSecondPlayer.add(cbxSecondPlayer);
		
		lblLevelSecondPlayer = new JLabel("Level:");
		lblLevelSecondPlayer.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblLevelSecondPlayer.setBounds(10, 137, 40, 17);
		pnlSecondPlayer.add(lblLevelSecondPlayer);
		
		cbxLevelSecondPlayer = new JComboBox();
		cbxLevelSecondPlayer.setModel(new DefaultComboBoxModel(SkillLevel.values()));
		cbxLevelSecondPlayer.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cbxLevelSecondPlayer.setBounds(88, 130, 180, 29);
		pnlSecondPlayer.add(cbxLevelSecondPlayer);
		
		lblSecondPlayerTitle = new JLabel("Second Player");
		lblSecondPlayerTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblSecondPlayerTitle.setBounds(81, 11, 157, 17);
		pnlSecondPlayer.add(lblSecondPlayerTitle);
		
		contentPane.setLayout(gl_contentPane);
		
		// Default selection of player combo boxes
		
		cbxFirstPlayer.setSelectedIndex(0);
		cbxSecondPlayer.setSelectedIndex(0);
	}
	
	private void updateControls(boolean isGameStarted)
	{
		// Update control status depending on whether a game is running
		
		cbxFirstPlayer.setEnabled(!isGameStarted);
		cbxLevelFirstPlayer.setEnabled(!isGameStarted);
		txtNameFirstPlayer.setEnabled(!isGameStarted);
		
		cbxSecondPlayer.setEnabled(!isGameStarted);
		cbxLevelSecondPlayer.setEnabled(!isGameStarted);
		txtNameSecondPlayer.setEnabled(!isGameStarted);
		
		btnStopGame.setEnabled(isGameStarted);
		btnLoadGame.setEnabled(!isGameStarted);
		btnSaveGame.setEnabled(isGameStarted);
		
		if (isGameStarted)
		{			
			btnStartNewGame.setText("Restart Game");
		}
		else
		{
			btnStartNewGame.setText("Start New Game");
		}
	}
	
	private class PlayerComboBoxItemListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() != null && e.getSource() instanceof JComboBox)
			{
				JComboBox comboBox = (JComboBox) e.getSource();
				
				if (comboBox.getSelectedItem() == PlayerType.HUMAN)
				{
					if (comboBox.equals(cbxFirstPlayer))
					{
						cbxLevelFirstPlayer.setEnabled(false);
						txtNameFirstPlayer.setEnabled(true);
						txtNameFirstPlayer.setText(null);
					}
					else if (comboBox.equals(cbxSecondPlayer))
					{
						cbxLevelSecondPlayer.setEnabled(false);
						txtNameSecondPlayer.setEnabled(true);
						txtNameSecondPlayer.setText(null);
					}
				}
				else
				{
					if (comboBox.equals(cbxFirstPlayer))
					{
						cbxLevelFirstPlayer.setEnabled(true);
						txtNameFirstPlayer.setEnabled(false);
						txtNameFirstPlayer.setText("CPU Player");						
					}
					else if (comboBox.equals(cbxSecondPlayer))
					{
						cbxLevelSecondPlayer.setEnabled(true);
						txtNameSecondPlayer.setEnabled(false);
						txtNameSecondPlayer.setText("CPU Player");
					}
				}
			}			
		}
	}
	
	private class StopGameActionListener implements ActionListener
	{
		private SetupFrame frame;
		
		public StopGameActionListener(SetupFrame frame)
		{
			this.frame = frame;
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e)
		{
			Game game = gameFrame.getGame();
			
			if (game != null)
			{
				try
				{
					//TODO: Use game controller to start the game
					
					//gameFrame.getController().stopGame(game);
					game.stop();
					
					// Hide and dispose the game frame
					
					gameFrame.setVisible(false);
					gameFrame.dispose();
					
					// Update controls of setup screen
					
					frame.updateControls(false);
				}
				catch (GameException ex)
				{
					JOptionPane.showMessageDialog(frame, "Game can't be stopped! Reason: " + ex.getMessage(),
							"Invalid game settings", JOptionPane.ERROR_MESSAGE);
				}				
			}
		}
	}
	
	private class StartGameActionListener implements ActionListener
	{
		private SetupFrame frame;
		
		public StartGameActionListener(SetupFrame frame)
		{
			this.frame = frame;
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e)
		{
			if (evaluateSettings())
			{
				// Move setup frame
				
				setLocation(0, 0);
				
				// Update controls of setup frame
				
				frame.updateControls(true);
				
				// Create game and launch the game frame
				
				Game game = createGame();
				gameFrame = new GameFrame(frame, false, game);
			}
			else
			{
				JOptionPane.showMessageDialog(frame, "Game can't be started - please check all entered settings!",
												"Invalid game settings", JOptionPane.ERROR_MESSAGE);
			}			
		}
		
		private boolean evaluateSettings()
		{
			return isPlayerTypeDefined() && isPlayerLevelDefined() && isPlayerNameDefined();
		}
		
		private boolean isPlayerTypeDefined()
		{
			return cbxFirstPlayer.getSelectedItem() != null 
					&& cbxSecondPlayer.getSelectedItem() != null;
		}
		
		private boolean isPlayerNameDefined()
		{
			String firstPlayerName = txtNameFirstPlayer.getText();
			String secondPlayerName = txtNameSecondPlayer.getText();
			
			return firstPlayerName != null && !firstPlayerName.equals("")
					&& secondPlayerName != null && !secondPlayerName.equals("");
		}
		
		private boolean isPlayerLevelDefined()
		{			
			return cbxLevelFirstPlayer.getSelectedItem() != null 
					&& cbxLevelSecondPlayer.getSelectedItem() != null;
		}
	}
}
