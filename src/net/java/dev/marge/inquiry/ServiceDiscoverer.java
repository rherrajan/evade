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

package net.java.dev.marge.inquiry;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;

import net.java.dev.marge.entity.config.MargeDefaults;

/**
 * Provides methods to performs Service Discovery in Remote Devices.
 */
public class ServiceDiscoverer {

	private static ServiceDiscoverer instance;

	private DiscoveryAgent agent;

	private DefaultDiscoveryListener listener;

	private int transID;

	/**
	 * Constructor. To get a instance of this class use the static getInstance
	 * method.
	 * 
	 * @throws BluetoothStateException
	 *            if can't get the Local Device.
	 */
	private ServiceDiscoverer() throws BluetoothStateException {
		this.agent = LocalDevice.getLocalDevice().getDiscoveryAgent();
		this.listener = null;
	}

	/**
	 * Performs a search of the given UUID's in the especified RemoteDevice. The
	 * search result will be notified to the <code>ServiceSearchListener</code>.
	 * 
	 * @param uuids
	 *           UUID's to be found.
	 * @param remoteDevice
	 *           Device to be searched.
	 * @param listener
	 *           Search result Listener.
	 * @throws BluetoothStateException
	 *            if can not use Bluetooth Search.
	 */
	public void startSearch(UUID uuids[], RemoteDevice remoteDevice, ServiceSearchListener listener) throws BluetoothStateException {
		this.listener = new DefaultDiscoveryListener(remoteDevice, listener);
		this.transID = this.agent.searchServices(null, uuids, remoteDevice, this.listener);
	}

	// TODO: When a client connects to an other client, the result is a NullPointerException
	public void startSearch(RemoteDevice remoteDevice, ServiceSearchListener listener) throws BluetoothStateException {
		this.startSearch(MargeDefaults.DEFAULT_UUID_ARRAY, remoteDevice, listener);
	}

	/**
	 * Stops a search that has been started using startSearch.
	 */
	public void cancelSearch() {
		if (this.listener != null) {
			this.agent.cancelServiceSearch(this.transID);
			this.listener = null;
		}
	}

	// TODO public ServiceRecord[] startSearch(RemoteDevice device)
	/**
	 * Returns a Singleton instance of ServiceDiscoverer.
	 * 
	 * @return Singleton ServiceDiscoverer instance.
	 */
	public static ServiceDiscoverer getInstance() throws BluetoothStateException {
		if (instance == null) {
			instance = new ServiceDiscoverer();
		}
		return instance;
	}

}
