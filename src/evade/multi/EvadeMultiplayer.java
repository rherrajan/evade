package evade.multi;

import javax.microedition.midlet.MIDletStateChangeException;

import evade.EvadeMidlet;



/**
 * This is the Midlet to start the game with multiplayer option
 * 
 * @author Roland
 */
public class EvadeMultiplayer extends EvadeMidlet {

	
	protected void startApp() throws MIDletStateChangeException {

		if(main == null){
			main = new BTMain(this);
			main.showMainMenu();
		} else {
			main.resume();
		}
	}

	public void destroyApp(boolean unconditional) {
		((BTMain)main).stopGame();
		
		super.destroyApp(unconditional);
	}
}
