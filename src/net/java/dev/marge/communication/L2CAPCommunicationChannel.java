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

import javax.bluetooth.L2CAPConnection;
import javax.microedition.io.Connection;

/**
 * Representation of a L2CAP communication channel.
 */
public class L2CAPCommunicationChannel extends CommunicationChannel {

	protected L2CAPConnection connection;

	protected boolean listening;

	/**
	 * Contructor that receives the L2CAP connection.
	 * 
	 * @param connection
	 *            The L2CAP connection.
	 */
	public L2CAPCommunicationChannel(L2CAPConnection connection) {
		this.connection = connection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.java.dev.marge.communication.CommunicationChannel#receive()
	 */
	public byte[] receive() throws IOException {
		byte[] received = null;
		if (this.connection.ready()) {
			byte buffer[] = new byte[this.connection.getReceiveMTU()];
			int bytesReceived = this.connection.receive(buffer);
			if (bytesReceived == 0) {
				return null;
			}
			received = new byte[bytesReceived];
			for (int i = 0; i < bytesReceived; i++) {
				received[i] = buffer[i];
			}
		}
		return received;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.java.dev.marge.communication.CommunicationChannel#sendMessage(byte[])
	 */
	public void sendMessage(byte[] message) throws IOException {
		MessageSender sender = new MessageSender(message);
		sender.start();
		if (sender.getException() != null) {
			throw sender.getException();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.java.dev.marge.communication.CommunicationChannel#disconnect()
	 */
	public void disconnect() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (IOException e) {
		}
		connection = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.java.dev.marge.communication.CommunicationChannel#getConnection()
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Thread class that sends the messages.
	 */
	public final class MessageSender extends Thread {

		private byte[] message;

		private IOException ex;

		/**
		 * Contructor that receives the byte array message that will be sent.
		 * 
		 * @param message
		 *            Byte array to send.
		 */
		public MessageSender(byte[] message) {
			this.message = message;
			this.ex = null;
		}

		public void run() {
			try{
				run_intern();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		public void run_intern() {
			try {
				connection.send(message);
			} catch (IOException e) {
				this.ex = e;
			}
		}

		/**
		 * Returns the IOException. This IOException is saved if a I/O problem
		 * happens during it is triying to send the message and you can get it
		 * by this method.
		 * 
		 * @return IOExcepion instance.
		 */
		public IOException getException() {
			return ex;
		}
	}
}