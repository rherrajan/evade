/*
 * Marge, Java Bluetooth Framework
 * Copyright (C) 2006  Project Marge
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * owner@marge.dev.java.net
 * http://marge.dev.java.net
 */

package net.java.dev.marge.communication;

import java.io.IOException;

import javax.bluetooth.RemoteDevice;

import net.java.dev.marge.entity.ServerDevice;

/**
 * Listener interface that contains the methods that can be called during the
 * connection process.
 */
public interface ConnectionListener {

	/**
	 * This method is called by the listener when the current server device
	 * receives a new connection represented by a remote device.
	 * 
	 * @param serverDevice
	 *            The current <code>ServerDevice</code>.
	 * @param remoteDevice
	 *            The remote device that has connected on the server device.
	 * @param channelNR
	 *            counts the clients who connects to the server. 
	 */
	public void connectionEstablished(ServerDevice serverDevice, RemoteDevice remoteDevice, int channelNr);

	
//	/**
//	 * This method is called by the listener when the current server device
//	 * receives a new connection represented by a remote device.
//	 * 
//	 * @param serverDevice
//	 *            The current <code>ServerDevice</code>.
//	 * @param remoteDevice
//	 *            The remote device that has connected on the server device.
//	 */
//	public void connectionEstablished(ServerDevice serverDevice, RemoteDevice remoteDevice);

	/**
	 * This method is called by the listener when the current server device
	 * tries to receive a new connection and problem happens during this
	 * process.
	 * 
	 * @param e
	 *            IOException that happened.
	 */
	public void errorOnConnection(IOException e);
}
