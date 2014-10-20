package evade.bluetooth;

import java.io.IOException;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import net.java.dev.marge.communication.ConnectionListener;
import net.java.dev.marge.entity.ServerDevice;
import net.java.dev.marge.entity.config.ServerConfiguration;
import net.java.dev.marge.factory.RFCOMMCommunicationFactory;
import net.java.dev.marge.inquiry.DeviceDiscoverer;
import net.java.dev.marge.inquiry.InquiryListener;

/**
 * This Menu will make a bluetooth connection with the help of the user.
 * The user has to decide if he wants to be a server or client.
 * 
 * @author Roland
 */
public class ConnectMenu extends List implements CommandListener, InquiryListener, ConnectionListener {

	private DevicesList deviceList;

	BTListener btMenu;

	public ConnectMenu(BTListener btListener) {
		super("Multiplayer", List.IMPLICIT);

		this.btMenu = btListener;

		this.addCommand(new Command("Back", Command.BACK, 1));
		this.addCommand(new Command("OK", Command.OK, 1));

		this.append("Connect to Server", null);
		this.append("Act as Server", null);

		this.setCommandListener(this);
		this.deviceList = new DevicesList(this, btMenu);
	}

	public void commandAction(Command c, Displayable arg1) {
		if (c.getCommandType() == Command.BACK) {
			btMenu.setDevice(null);
		} else {
			if (this.getSelectedIndex() == 0) {
				startClient();
			} else {
				startServer();
			}
		}
	}

	private void startServer() {
		
		btMenu.setIsServer(true);
		RFCOMMCommunicationFactory factory = new RFCOMMCommunicationFactory();
		ServerConfiguration serverConfiguration = new ServerConfiguration(btMenu);
		
		serverConfiguration.setMaxNumberOfConnections(2);
		
		factory.waitClients(serverConfiguration, this);
		
		String name;
		
		try {	
			name = LocalDevice.getLocalDevice().getBluetoothAddress();
		} catch (BluetoothStateException ex) {
			name = "";
		}
		LoadingScreen loadingScreen = new LoadingScreen("Server", "Starting... " + name, null);
		loadingScreen.setCommandListener(this);
		btMenu.setCurrentDisplayable(loadingScreen);
	}

	private void startClient() {
		try {
			btMenu.setIsServer(false);
			DeviceDiscoverer.getInstance().startInquiryGIAC(this);
			btMenu.setCurrentDisplayable(this.deviceList);
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		}
	}

	public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass arg1) {
		this.deviceList.addRemoteDevice(remoteDevice);
	}

	public void inquiryError() {
		System.out.println("inquiryError");
	}

	public void inquiryCompleted(RemoteDevice[] remoteDevices) {
		System.out.println("inquiryCompleted " + remoteDevices.length );
	}

	public void connectionEstablished(ServerDevice serverDevice, RemoteDevice remoteDevice, int channelNr) {
//		System.out.println("serverDevice: " + serverDevice);
//		System.out.println("remoteDevice: " + remoteDevice);
//		System.out.println("channelNr: " + channelNr);
		
		btMenu.setDevice(serverDevice);
//		btMenu.connectionEstablished(channelNr, remoteDevice);
		btMenu.connectionEstablished();
	}

	public void errorOnConnection(IOException e) {
		e.printStackTrace();
	}

	public void initialisationSucessful() {
		System.out.println("initialisationSucessful");
	}
}
