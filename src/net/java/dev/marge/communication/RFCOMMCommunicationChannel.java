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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.io.Connection;
import javax.microedition.io.StreamConnection;

/**
 * Representation of a RFCOMM communication channel.
 */
public class RFCOMMCommunicationChannel extends CommunicationChannel {

	protected StreamConnection connection;

	protected DataOutputStream out;

	protected DataInputStream in;

	/**
	 * Contructor that receives the stream connection.
	 * 
	 * @param connection
	 *            The stream connection.
	 * @throws IOException
	 *             IOException can occurs during it is trying to open the input
	 *             and output.
	 */
	public RFCOMMCommunicationChannel(StreamConnection connection)
			throws IOException {
		this.connection = connection;
		this.in = connection.openDataInputStream();
		this.out = connection.openDataOutputStream();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.java.dev.marge.communication.CommunicationChannel#receive()
	 */
	public byte[] receive() throws IOException {
		int available = this.in.available();
		if (available == 0) {
			return null;
		}
		byte[] received = new byte[available];
		this.in.read(received);
		return received;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.java.dev.marge.communication.CommunicationChannel#sendMessage(byte[])
	 */
	public void sendMessage(final byte[] message) throws IOException {
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
			if (in != null) {
				in.close();
			}
		} catch (IOException e) {
		}
		try {
			if (out != null) {
				out.close();
			}
		} catch (IOException e) {
		}
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (IOException e) {
		}
		in = null;
		out = null;
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
	 * Return the data input stream of the connection used in the constructor.
	 * 
	 * @return DataInputStream The data input stream.
	 */
	public DataInputStream getDataInputStream() {
		return in;
	}

	/**
	 * Return the data output stream of the connection used in the constructor.
	 * 
	 * @return DataOutputStream The data input stream.
	 */
	public DataOutputStream getDataOutputStream() {
		return out;
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
				out.write(this.message);
				out.flush();
			} catch (IOException e) {
				this.ex = e;
			}
		}

		/**
		 * Returns the IOException. This IOException is saved if a I/O problem
		 * happens during it is triying to send the message and you can get it
		 * by this method.
		 * 
		 * @return IOException instance.
		 */
		public IOException getException() {
			return ex;
		}
	}
}