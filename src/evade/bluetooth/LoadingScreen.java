package evade.bluetooth;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.Ticker;

public class LoadingScreen extends Form {

	public LoadingScreen(String title, String text, Image image) {
		super(title);

		if (image != null) {
			this.append(new ImageItem(null, image, ImageItem.LAYOUT_CENTER, title));
		}

		this.setTicker(new Ticker(title));
		this.append("\n" + text);

		this.addCommand(new Command("Back", Command.BACK, 1));
		
	}
}
