package evade;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;


public class OptionsScreen extends Form implements CommandListener {

	private final Main menu;
	private final TextField ballTime;
	private TextField timeForLevel;
	private TextField ballTime_increase;

	OptionsScreen(Main menu) {
		super("Options");
		this.menu = menu;

		append(ballTime = new TextField("ballTime", String.valueOf(EvadeCanvas.ballTime), 100, TextField.DECIMAL));
		append(ballTime_increase = new TextField("ballTime_increase", String.valueOf(EvadeCanvas.ballTime_increase), 100, TextField.DECIMAL));
		append(timeForLevel = new TextField("timeForLevel", String.valueOf(EvadeCanvas.timeForLevel), 100, TextField.DECIMAL));

		addCommand(new Command("Back", Command.BACK, 1));

		setCommandListener(this);
	}

	public void commandAction(Command c, Displayable d) {

		EvadeCanvas.ballTime = Integer.valueOf(ballTime.getString()).intValue();
		EvadeCanvas.ballTime_increase = Integer.valueOf(ballTime_increase.getString()).intValue();
		EvadeCanvas.timeForLevel = Integer.valueOf(timeForLevel.getString()).intValue();
		
		menu.showMainMenu();
	}
}
