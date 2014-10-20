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

package net.java.dev.marge.factory;

import java.io.IOException;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.L2CAPConnection;
import javax.bluetooth.L2CAPConnectionNotifier;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.microedition.io.Connector;

import net.java.dev.marge.communication.CommunicationChannel;
import net.java.dev.marge.communication.ConnectionListener;
import net.java.dev.marge.communication.L2CAPCommunicationChannel;
import net.java.dev.marge.entity.ClientDevice;
import net.java.dev.marge.entity.ServerDevice;
import net.java.dev.marge.entity.config.ClientConfiguration;
import net.java.dev.marge.entity.config.ServerConfiguration;

/**
 * Create Client and Server Devices using the Bluetooth L2CAP Protocol.
 */
public class L2CAPCommunicationFactory implements CommunicationFactory {

	private static final String PROTOCOL_NAME = "btl2cap";

	/**
	 * Creates a <code>ClientDevice</code> under the given configurations. The
	 * Client will connect to the server using the L2CAP Protocol.
	 * 
	 * @param configuration
	 *            <code>ClientConfiguration</code> used to create the client.
	 * @return Created <code>ClientDevice</code>.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public ClientDevice connectToServer(ClientConfiguration configuration)
			throws IOException {

		ClientDeviceFactory clientFactory = new ClientDeviceFactory(
				configuration);
		clientFactory.start();

		synchronized (this) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (clientFactory.getException() != null) {
			throw clientFactory.getException();
		}

		return clientFactory.getDevice();
	}

	/**
	 * Creates a <code>ServerDevice</code> with the given configurations. The
	 * ServerDevice instance will be given to the <code>ConnetionListener</code>
	 * when the connection is estabilished and it will use the L2CAP Protocol.
	 * 
	 * @param configuration
	 *            <code>ServerConfiguration</code> used to create the server.
	 * @param listener
	 *            Listener that will be notified when a connection is
	 *            estabilished.
	 */
	public void waitClients(final ServerConfiguration configuration,
			final ConnectionListener listener) {
		new Thread() {
			public void run() {
				try{
					run_intern();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
			public void run_intern() {
				try {
					LocalDevice.getLocalDevice().setDiscoverable(
							DiscoveryAgent.GIAC);
					configuration.setProtocol(PROTOCOL_NAME);

					L2CAPConnectionNotifier conn = (L2CAPConnectionNotifier) Connector
							.open(configuration.getConnectionURL());
					L2CAPConnection connection = conn.acceptAndOpen();
					L2CAPCommunicationChannel channel = new L2CAPCommunicationChannel(
							connection);

					RemoteDevice remoteDevice = RemoteDevice
							.getRemoteDevice(channel.getConnection());

					ServerDevice serverDevice = new ServerDevice(configuration
							.getCommunicationListener(), channel, configuration
							.getReadInterval());

					
					listener.connectionEstablished(serverDevice, remoteDevice, 0);

					int connections = 0;
					while (++connections < configuration
							.getMaxNumberOfConnections()) {

						connection = conn.acceptAndOpen();
						channel = new L2CAPCommunicationChannel(connection);
						remoteDevice = RemoteDevice.getRemoteDevice(channel
								.getConnection());

						serverDevice.addChannel(channel);

						listener.connectionEstablished(serverDevice, remoteDevice, connections);
					}

				} catch (IOException e) {
					e.printStackTrace();
					listener.errorOnConnection(e);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	final public class ClientDeviceFactory extends Thread {

		private IOException exception;

		private ClientConfiguration configuration;

		private ClientDevice device;

		public ClientDeviceFactory(ClientConfiguration configuration) {
			this.configuration = configuration;
			this.exception = null;
		}
		
		public void run() {
			try{
				run_intern();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		public void run_intern() {
			try {
				CommunicationChannel channel = new L2CAPCommunicationChannel(
						(L2CAPConnection) Connector.open(configuration
								.getConnectionURL()));

				device = new ClientDevice(configuration
						.getCommunicationListener(), channel, configuration
						.getReadInterval());
			} catch (IOException e) {
				exception = e;
			}
			synchronized (L2CAPCommunicationFactory.this) {
				L2CAPCommunicationFactory.this.notify();
			}
		}

		public ClientDevice getDevice() {
			return device;
		}

		public IOException getException() {
			return exception;
		}
	}
}
