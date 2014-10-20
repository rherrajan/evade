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

package net.java.dev.marge.entity;

import net.java.dev.marge.communication.CommunicationChannel;
import net.java.dev.marge.communication.CommunicationListener;

/**
 * Representation of a Bluetooth client device.
 */
public class ClientDevice extends Device {

	/**
	 * Constructor used to create a client. The incoming read messages of the given Channel will be
	 * forwarded to the Listener at every Read Interval.
	 * 
	 * @param communicationListener
	 *            <code>CommunicationListener</code> which will the notified
	 *            for incoming messages.
	 * @param channel
	 *            <code>CommunicationChannel</code> used for communication.
	 * @param readInterval
	 *            Time between each message reading.
	 */
	public ClientDevice(CommunicationListener communicationListener,
			CommunicationChannel channel, int readInterval) {
		super(communicationListener, channel, readInterval);
	}

}