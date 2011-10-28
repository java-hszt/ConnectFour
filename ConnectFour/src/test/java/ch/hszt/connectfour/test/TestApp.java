package ch.hszt.connectfour.test;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;

import ch.hszt.connectfour.control.GameController;
import ch.hszt.connectfour.control.GameStatisticUpdateTask;
import ch.hszt.connectfour.exception.GameException;
import ch.hszt.connectfour.model.board.GameBoardSlot;
import ch.hszt.connectfour.model.game.Game;
import ch.hszt.connectfour.model.game.GameStatistic;
import ch.hszt.connectfour.model.game.GameStatus;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.SwingConstants;
import java.awt.Color;

public class TestApp extends JFrame
{

	private JPanel	contentPane;
	private JTextField txtDuration;
	private JLabel lblStartTime;
	private JTextField txtStartTime;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					TestApp frame = new TestApp();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	private class GameControllerTest extends GameController
	{
		JTextField field;
		JTextField other;
		
		public GameControllerTest(JTextField field, JTextField other)
		{
			this.field = field;
			this.other = other;
		}

		@Override
		public void refreshAll(Game game)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void refreshStatus(GameStatus status)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void refreshStatistic(GameStatistic statistic)
		{
			if (other.getText() == null || other.getText().equals(""))
			{
				other.setText(statistic.getStartTime());
				other.update(other.getGraphics());
			}
			
			field.setText(statistic.getDuration());
			field.update(field.getGraphics());
		}

		@Override
		public void refreshSlot(GameBoardSlot slot)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void refreshSlots(Iterable<GameBoardSlot> slots)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void printMessage(String message)
		{
			// TODO Auto-generated method stub
			
		}
		
	}

	/**
	 * Create the frame.
	 */
	public TestApp()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 383, 187);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtDuration = new JTextField();
		txtDuration.setHorizontalAlignment(SwingConstants.LEFT);
		txtDuration.setBackground(Color.WHITE);
		txtDuration.setText("HH:mm:ss");
		txtDuration.setEditable(false);
		txtDuration.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtDuration.setBounds(109, 11, 252, 28);
		contentPane.add(txtDuration);
		txtDuration.setColumns(10);
		
		JLabel lblDuration = new JLabel("Duration:");
		lblDuration.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblDuration.setBounds(10, 18, 89, 14);
		contentPane.add(lblDuration);
		
		JButton btnStartGame = new JButton("Start Game");
		btnStartGame.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				GameControllerTest con = new GameControllerTest(txtDuration, txtStartTime);
				Game game = UnitTestUtils.prepareGame();
				
				try
				{
					con.startGame(game);
					TimerTask task = new GameStatisticUpdateTask(con, game.getStatistic());
					
					java.util.Timer timer = new Timer("TimerThread");
					timer.schedule(task, Calendar.getInstance().getTime(), 1000);
				}
				catch (GameException ex)
				{
					throw new RuntimeException("Unexpected error! " + ex.getMessage());
				}				
			}
		});
		btnStartGame.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnStartGame.setBounds(242, 104, 119, 34);
		contentPane.add(btnStartGame);
		
		lblStartTime = new JLabel("Start Time:");
		lblStartTime.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblStartTime.setBounds(10, 63, 89, 14);
		contentPane.add(lblStartTime);
		
		txtStartTime = new JTextField();
		txtStartTime.setHorizontalAlignment(SwingConstants.LEFT);
		txtStartTime.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtStartTime.setEditable(false);
		txtStartTime.setColumns(10);
		txtStartTime.setBackground(Color.WHITE);
		txtStartTime.setBounds(109, 56, 252, 28);
		contentPane.add(txtStartTime);
	}
}
