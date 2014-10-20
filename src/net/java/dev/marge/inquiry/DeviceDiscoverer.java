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

/**
 * Provides methods to search Bluetooth enabled devices in the area.
 */
public class DeviceDiscoverer {

	private static DeviceDiscoverer instance;

	private DiscoveryAgent agent;

	private DefaultDiscoveryListener listener;

	/**
	 * Constructor. To get a instance of this class use the static getInstance()
	 * method.
	 * 
	 * @throws BluetoothStateException
	 *             if can't get the Local Device.
	 */
	private DeviceDiscoverer() throws BluetoothStateException {
		this.agent = LocalDevice.getLocalDevice().getDiscoveryAgent();
		this.listener = null;
	}

	/**
	 * Starts a inquiry for devices.
	 * 
	 * @param type
	 *            The type of inquiry, use DiscoveryAgent constants.
	 * @param inquiryListener
	 *            The <code>InquiryListener</code> that will be notified.
	 * @throws BluetoothStateException
	 *             An exception can be trown during the inquiry.
	 */
	public void startInquiry(int type, InquiryListener inquiryListener)
			throws BluetoothStateException {
		this.listener = new DefaultDiscoveryListener(inquiryListener);
		this.agent.startInquiry(type, this.listener);
	}
        
       /**
	 * Starts a GIAC inquiry for devices.
	 * 
	 * @param inquiryListener
	 *            The <code>InquiryListener</code> that will be notified.
	 * @throws BluetoothStateException
	 *             An exception can be trown during the inquiry.
	 */
	public void startInquiryGIAC(InquiryListener inquiryListener)
			throws BluetoothStateException {
		this.listener = new DefaultDiscoveryListener(inquiryListener);
		this.agent.startInquiry(DiscoveryAgent.GIAC, this.listener);
	}
        
       /**
	 * Starts a LIAC inquiry for devices.
	 * 
	 * @param inquiryListener
	 *            The <code>InquiryListener</code> that will be notified.
	 * @throws BluetoothStateException
	 *             An exception can be trown during the inquiry.
	 */
	public void startInquiryLIAC(InquiryListener inquiryListener)
			throws BluetoothStateException {
		this.listener = new DefaultDiscoveryListener(inquiryListener);
		this.agent.startInquiry(DiscoveryAgent.LIAC, this.listener);
	}             
        
	/**
	 * Retrieves devices.
	 * 
	 * @param type
	 *            The type of retrieve, use DiscoveryAgent constants.
	 * @return RemoteDevice An array of remote devices.
	 */
	public RemoteDevice[] retrieveDevices(int type) {
		return this.agent.retrieveDevices(type);
	}

	/**
	 * Retrieves preknown devices.
	 * 
	 * @return RemoteDevice An array of remote devices.
	 */
	public RemoteDevice[] retrievePreknownDevices() {
		return this.agent.retrieveDevices(DiscoveryAgent.PREKNOWN);
	}        
        
	/**
	 * Retrieves cached devices.
	 * 
	 * @return RemoteDevice An array of remote devices.
	 */
	public RemoteDevice[] retrieveCachedDevices() {
		return this.agent.retrieveDevices(DiscoveryAgent.CACHED);
	}         
        
	/**
	 * Gets an array of RemoteDevice discovered.
	 * 
	 * @return Array of RemoteDevice.
	 */
	public RemoteDevice[] getDiscoveredDevices() {
		RemoteDevice[] devices;
		if (this.listener == null) {
			devices = new RemoteDevice[0];
		} else {
			devices = this.listener.getDiscoveredDevices();
		}
		return devices;
	}

	/**
	 * Cancels the inquiry.
	 */
	public void cancelInquiry() {
		if (this.listener != null) {
			this.agent.cancelInquiry(this.listener);
			this.listener = null;
		}
	}

	/**
	 * Checks if the inquiry has finished.
	 * 
	 * @return True if the inquiry has finished, false if not yet.
	 */
	public boolean hasInquiryFinished() {
		boolean response;
		if (this.listener == null) {
			response = true;
		} else {
			response = this.listener.hasInquiryFinished();
		}
		return response;
	}

	/**
	 * Returns a Singleton instance of DeviceDiscoverer.
	 * 
	 * @return Singleton DeviceDiscoverer instance.
	 */
	public static DeviceDiscoverer getInstance() throws BluetoothStateException {
		if (instance == null) {
			instance = new DeviceDiscoverer();
		}
		return instance;
	}

}
