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

/**
 * Listener interface that contains the methods that can be called during the
 * communication process.
 */
public interface CommunicationListener {

	/**
	 * This method is called by the listener when the current device receives a
	 * message sent by another.
	 * 
	 * @param message
	 *            Byte array received representing the message.
	 */
	public abstract void receiveMessage(byte[] message, int fromWhom);

	/**
	 * This method is called by the listener when the current device tries to
	 * receive a message sent by another and a problem happens during this
	 * process.
	 * 
	 * @param e
	 *            IOException that can occur.
	 */
	public abstract void errorOnReceiving(IOException e);

	/**
	 * This method is called by the listener when the current device tries to
	 * send a message to another and a problem happens during this process.
	 * 
	 * @param e
	 *            IOException that can occur.
	 */
	public abstract void errorOnSending(IOException e);

}