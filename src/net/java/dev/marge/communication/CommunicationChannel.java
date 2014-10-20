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

import javax.microedition.io.Connection;

/**
 * Abstract representation of a communication channel. A communication channel
 * uses a communication protocol that can be L2CAP or RFCOMM. A communication
 * channel is used to manipulate the connection, receive and send messages.
 */
public abstract class CommunicationChannel {

	/**
	 * Closes the connection.
	 */
	public final void close() {
		this.disconnect();
	}

	/**
	 * Makes a disconnection.
	 */
	protected abstract void disconnect();

	/**
	 * Receives a message.
	 * 
	 * @return byte[] Byte array representing the message received.
	 * @throws IOException
	 *             IOException is trown if a I/O problem happens during the
	 *             reading.
	 */
	public abstract byte[] receive() throws IOException;

	/**
	 * Sends a new message in a byte array format. It calls the sendMessage
	 * method.
	 * 
	 * @param message
	 *            The message that the device wants to send in a byte array
	 *            format.
	 * @throws IOException
	 *             IOException is trown if a I/O problem happens during the
	 *             sending.
	 */
	public void send(byte[] message) throws IOException {
		this.sendMessage(message);
	}

	/**
	 * Sends a message.
	 * 
	 * @param message
	 *            The message that the device wants to send in a byte array
	 *            format.
	 * @throws IOException
	 *             IOException is trown if a I/O problem happens during the
	 *             sending.
	 */
	protected abstract void sendMessage(byte[] message) throws IOException;

	/**
	 * Returns the current connection.
	 * 
	 * @return Current connection
	 */
	public abstract Connection getConnection();

}