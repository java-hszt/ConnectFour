/**
 * 
 */
package ch.hszt.connectfour.control;

import java.util.TimerTask;

import ch.hszt.connectfour.model.game.GameStatistic;

/**
 * Represents the periodically executed task of updating the {@link GameStatistic}
 * @author Markus Vetsch
 * @version 1.0, 22.10.2011
 */
public class GameStatisticUpdateTask extends TimerTask
{
	private final GameController controller;
	private final GameStatistic statistic;
	
	/**
	 * Creates a new {@link GameStatisticUpdateTask} for specified {@link GameStatistic}.
	 */
	public GameStatisticUpdateTask(final GameController controller, final GameStatistic statistic)
	{
		this.controller = controller;
		this.statistic = statistic;
	}

	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run()
	{
		// Refresh the associated statistic
		
		controller.refreshStatistic(statistic);
	}

}
