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

package net.java.dev.marge.entity.config;

import net.java.dev.marge.communication.CommunicationListener;

/**
 * Base Configuration class for creating a new <code>Device</code>. This
 * class holds all basic information for creating a Device using the
 * <code>CommunicationFactory</code>.
 */
public abstract class Configuration {

	protected CommunicationListener communicationListener;

	protected int readInterval;

	/**
	 * Default constructor.
	 * 
	 * @param communicationListener
	 *            Listener which will the notified for incoming messages.
	 */
	public Configuration(CommunicationListener communicationListener) {
		super();
		this.communicationListener = communicationListener;
		this.readInterval = 100;
	}

	/**
	 * Returns a Connection URL for use in the Generic Connection Framework,
	 * following the current configurations.
	 * 
	 * @return GCF connection URL.
	 */
	public abstract String getConnectionURL();

	/**
	 * Returns the current Listener that is notified on every new incoming
	 * message.
	 * 
	 * @return Listener for incomming messages.
	 */
	public CommunicationListener getCommunicationListener() {
		return communicationListener;
	}

	/**
	 * Sets the communication listener that will be notified on every new
	 * incoming message.
	 * 
	 * @param communicationListener
	 *            <code>CommunicationListener</code> for messages.
	 */
	public void setCommunicationListener(
			CommunicationListener communicationListener) {
		this.communicationListener = communicationListener;
	}

	/**
	 * Returns the current read interval between every incoming message check.
	 * 
	 * @return Read interval.
	 */
	public int getReadInterval() {
		return readInterval;
	}

	/**
	 * Sets the current read interval between every incoming message check.
	 */
	public void setReadInterval(int readInterval) {
		this.readInterval = readInterval;
	}

}