package evade.bluetooth;

import javax.microedition.lcdui.Displayable;

import net.java.dev.marge.communication.CommunicationListener;
import net.java.dev.marge.entity.Device;

/**
 * Instances of this class have to reacIton on events that happens with the
 * bluetooth connection
 * 
 * @author Roland
 */
public interface BTListener extends CommunicationListener {

	/**
	 * Is called before initialization If this cell phone is the server it will
	 * send a true;
	 */
	void setIsServer(boolean b);

	/**
	 * Indicates that the connection is done.
	 */
	void connectionEstablished();

	/**
	 * Displays the given Displayable
	 */
	public void setCurrentDisplayable(Displayable displayable);

	/**
	 * Sets the Device in the midlet. It can be used for sending Data.
	 */
	public void setDevice(Device device);
}
