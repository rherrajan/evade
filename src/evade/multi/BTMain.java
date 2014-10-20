package evade.multi;

import java.io.IOException;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import net.java.dev.marge.entity.Device;
import evade.Main;
import evade.bluetooth.BTListener;
import evade.bluetooth.ConnectMenu;

/**
 * This is the class, who organizes the other classes. It decides, what to
 * display and reacts on the users input.
 * 
 * @author Roland
 */
public class BTMain extends Main implements BTListener {

	private List btMenu;
	private MultiplayerCanvas gameCanvas;

	private Device device;

	private boolean isServer;

	public BTMain(EvadeMultiplayer evadeMultiplayer) {
		super(evadeMultiplayer);
	}

	public void showMainMenu() {
		class BTMenu extends List {
			BTMenu() {
				super("Evade", List.IMPLICIT);
				append("Start Game", null);
				append("Connect", null);
				append("Highscore", null);
				append("Options", null);
				append("About", null);
				
				addCommand(new Command("Exit", Command.EXIT, 1));

				setCommandListener(BTMain.this);
			}
		}

		if (btMenu == null) {
			this.btMenu = new BTMenu();
		}

		setCurrentDisplayable(btMenu);
	}

	public void commandAction(Command c, Displayable d) {
		if (c == List.SELECT_COMMAND) {
			int index = btMenu.getSelectedIndex();
			switch (index) {
			case 0:
				waitForStart();
				break;
			case 1:
				connect();
				break;
			case 2:
				showHighScore();
				break;
			case 3:
				showOptions();
				break;
			case 4:
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

	private void connect() {
		if (device == null) {
			// Start the connection
			ConnectMenu connectMenu = new ConnectMenu(this);
			this.setCurrentDisplayable(connectMenu);
		} else {
			stopGame();
		}

	}

	private void waitForStart() {
		if (device == null) {
			startSinglePlayer();
		} else {
			gameCanvas.readyForGame();
		}
	}

	public void stopGame() {
		if(device != null){
			// Stop the current Connection
			device.stopListenning();
			device.close();
			device = null;	
		}

		gameCanvas = null;
		
		btMenu.delete(1);
		btMenu.insert(1, "Connect", null);
	}
	
	protected void gameEnds(long msec) {
		super.gameEnds(msec);
		startGame();
	}

	private void startGame() {
		if(isServer){
			gameCanvas = new ServerCanvas(this, level);
		} else {
			gameCanvas = new MultiplayerCanvas(this);
		}
		gameCanvas.populateGame();
	}

	public void connectionEstablished() {
		System.out.println("connectionEstablished");

		btMenu.delete(1);
		btMenu.insert(1, "Disconnect", null);
		btMenu.setSelectedIndex(0, true);

		device.startListening();

		if (gameCanvas == null) {
			startGame();
		}

		showMainMenu();
	}

	public void setDevice(Device device) {
		this.device = device;
		
		if(device == null){
			// no device was found
			showMainMenu();
		}
	}

	public void setIsServer(boolean isServer) {
		this.isServer = isServer;
		System.out.println("isServer: " + isServer);
	}

	public synchronized void receiveMessage(byte[] message, int channelNr) {
		String msg = new String(message);

		// System.out.println("receiveMessage: " + msg);

		int idx = msg.indexOf(';');
		while (idx != -1) {

			// process the first command
			String command = msg.substring(0, idx);

			int index = command.indexOf(':');

			String cmdString = command.substring(0, index);
			String valueStr = command.substring(index + 1, command.length());

			int playerNr;
			
			if(isServer){
				// The clients numbers can be from 1 to 7
				playerNr = channelNr+1;
			} else {
				// The server has always number 0
				playerNr = 0;
			}
			
			gameCanvas.processCommand(cmdString, valueStr, playerNr);

			// delete it from the message
			msg = msg.substring(idx + 1);

			idx = msg.indexOf(';');
		}
	}

	/** send to a specific client */
	public void send(String command, String value, int clientNr) {
		String msg = command + ":" + value + ";";
		device.send(msg.getBytes(), clientNr-1);
	}
	
	/** send to all clients */
	public void send(String command, String value) {
		String msg = command + ":" + value + ";";
		device.send(msg.getBytes());
	}

	public void errorOnReceiving(IOException e) {
		e.printStackTrace();
	}

	public void errorOnSending(IOException e) {
		e.printStackTrace();
	}

}
