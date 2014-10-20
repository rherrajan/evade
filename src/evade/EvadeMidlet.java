package evade;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;


public class EvadeMidlet extends MIDlet {

	protected Main main;
	
	protected void startApp() throws MIDletStateChangeException {
		if(main == null){
			main = new Main(this);
			main.showMainMenu();
		} else {
			main.resume();
		}
	}
	
	public void destroyApp(boolean unconditional){
		notifyDestroyed();
	}

	protected void pauseApp() {
		main.pause();
		super.notifyPaused();
	}
	
	
}
