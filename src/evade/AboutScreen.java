package evade;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;


public class AboutScreen extends Form implements CommandListener {

	private Main menu;

	private final static String about = 
		"Software Development: \n" +
			" Roland - Game Logic \n" +
			" brunogh & lucastorri - Bluetooth \n" +
				"  https://marge.dev.java.net \n" +
			" Richard Carless - Mathematic Operations \n" +
		"\n" +
			
		"Graphic Designer: \n" +
			" Danc - Background \n" +
				"  http://lostgarden.com/2006/07/more-free-game-graphics.html \n" +
			" Clest - Characters \n" +
				"  http://www.rmxp.org/forums/viewtopic.php?f=159&t=26952 \n" +
		"\n" +
				
		"Team Management: \n" +
			" Roland - copy&paste :) \n";

	AboutScreen(Main menu) {
		super("About");
		this.menu = menu;
		
		append(new StringItem(null, about));
		
		addCommand(new Command("Back", Command.BACK, 1));

		setCommandListener(this);
	}

	public void commandAction(Command c, Displayable d) {
		menu.showMainMenu();
	}

}
