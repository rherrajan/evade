package evade;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

/**
 * To record and show Highscores. Will be deleted, when the program quits
 * 
 * @author Roland
 */
public class HighScore extends List implements CommandListener {

	private final static int scores = 10;

	private Main menu;
	private long[] times = new long[scores];
	private int lastScore;

	HighScore(Main menu) {
		super("HighScore", List.IMPLICIT);
		this.menu = menu;
		addCommand(new Command("Back", Command.BACK, 1));

		setCommandListener(this);

		showScores();
	}

	private void showScores() {
		deleteAll();

		for (int i = 0; i < times.length; i++) {
			append((i + 1) + ": " + times[i] / 1000.0 + " sec", null);
		}

		setSelectedIndex(lastScore, true);
	}

	/**
	 * The value will be set in the highscore at the correct position and the
	 * index will be set on it.
	 */
	public void addScore(long msec) {
		if (msec > times[9]) {
			times[9] = msec;

			for (int i = 8; i >= 0; i--) {
				if (times[i] < times[i + 1]) {
					long temp = times[i];
					times[i] = times[i + 1];
					times[i + 1] = temp;
					lastScore = i;
				} else {
					break;
				}
			}
		}

		showScores();
	}

	public void commandAction(Command c, Displayable d) {
		menu.showMainMenu();
	}

	public int getBest() {
		return (int)(times[0] / 1000);
	}
}
