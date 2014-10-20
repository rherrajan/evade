package evade;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

/**
 * This is the class, who organizes the other classes. It decides, what to
 * display and reacts on the users input.
 */
public class Main implements CommandListener {

	/** The midlet can be used to change the display or stop the game */
	protected EvadeMidlet evadeMidlet;


	private List mainMenu;
	private final HighScore highScore;
	private final AboutScreen aboutScreen;
	private final OptionsScreen optionsScreen;
	
	private static final int maxLevel = 4;
	protected int level = 0;

	private int timeInLevel;

	private EvadeCanvas gameCanvas;




	public Main(EvadeMidlet evadeMidlet) {

		this.evadeMidlet = evadeMidlet;
		this.highScore = new HighScore(this);
		this.optionsScreen = new OptionsScreen(this);
		this.aboutScreen = new AboutScreen(this);	
	}

	public void showMainMenu() {
		class MainMenu extends List {
			MainMenu() {
				super("Evade", List.IMPLICIT);
				append("Start Game", null);
				append("Highscore", null);
				append("Options", null);
				append("About", null);
				
				addCommand(new Command("Exit", Command.EXIT, 1));

				setCommandListener(Main.this);
			}
		}

		this.mainMenu = new MainMenu();
		setCurrentDisplayable(mainMenu);
	}

	/**
	 * what will be displayed next.
	 */
	public void setCurrentDisplayable(Displayable nextDisplayable) {
		Display.getDisplay(evadeMidlet).setCurrent(nextDisplayable);
	}

	public void commandAction(Command c, Displayable d) {
		if (c == List.SELECT_COMMAND) {
			int index = mainMenu.getSelectedIndex();
			switch (index) {
			case 0:
				startSinglePlayer();
				break;
			case 1:
				showHighScore();
				break;
			case 2:
				showOptions();
				break;
			case 3:
				showAbout();
				break;
			default:
				System.err.println("Unknown list index: " + index);
				break;
			}

		} else if (c.getCommandType() == Command.EXIT) {
			evadeMidlet.destroyApp(false);
		} else {
			System.err.println("Unknown CommandType: " + c.getCommandType());
		}
	}

	protected void showOptions() {
		setCurrentDisplayable(optionsScreen);
	}
	
	protected void showAbout() {
		setCurrentDisplayable(aboutScreen);
	}

	protected void showHighScore() {
		setCurrentDisplayable(highScore);
	}


	
	/** Starts the singleplayer version of the game */
	protected void startSinglePlayer() {
		gameCanvas = new EvadeCanvas(this);

		gameCanvas.setLevel(level, timeInLevel);
		
		setCurrentDisplayable(gameCanvas);
		gameCanvas.populateGame();
		
		Thread game = new Thread(gameCanvas);
		game.start();
	}

	/** Tells this class, that the game has stopped */
	protected void gameEnds(long msec) {
		highScore.addScore(msec);
		setCurrentDisplayable(highScore);
		
		int best = highScore.getBest();
		
		if((best / EvadeCanvas.timeForLevel) > level && level<maxLevel){
			this.level++;
			this.timeInLevel = 0;
		} else {
			this.timeInLevel = (best-EvadeCanvas.timeForLevel*level)/2;
		}
	}

	public void pause() {
		gameCanvas.pause();
		
	}

	public void resume() {
		
		gameCanvas.resume();
		
		setCurrentDisplayable(gameCanvas);
		Thread game = new Thread(gameCanvas);
		game.start();
	}
}
