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

import java.util.Vector;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

/**
 * Marge's Service and Device Search Listener implementation. It iss used in the
 * <code>DeviceDiscoverer</code> and <code>ServiceDiscoverer</code> classes.
 */
public class DefaultDiscoveryListener implements DiscoveryListener {

	private boolean inquiryCompleted;

	private ServiceSearchListener sListener;

	private InquiryListener iListener;

	private Vector foundDevices;

	private ServiceRecord[] foundServices;

	private RemoteDevice remoteDevice;

	private DefaultDiscoveryListener() {
		this.inquiryCompleted = true;
	}

	/**
	 * Constructor for Service Search.
	 * 
	 * @param remoteDevice
	 *            Device to be scanned.
	 * @param slistener
	 *            ServiceSearchListener to be notified of found Services.
	 */
	public DefaultDiscoveryListener(RemoteDevice remoteDevice,
			ServiceSearchListener slistener) {
		this();
		this.remoteDevice = remoteDevice;
		this.sListener = slistener;
	}

	/**
	 * Constructor for Device Search.
	 * 
	 * @param iListener
	 *            Listener notified of found Devices.
	 */
	public DefaultDiscoveryListener(InquiryListener iListener) {
		this();
		this.foundDevices = new Vector(5);
		this.iListener = iListener;
	}

	/**
	 * Returns the status of the Device Discovery process, saying if it has
	 * Finished.
	 * 
	 * @return Status of Device Discovery.
	 */
	public boolean hasInquiryFinished() {
		return this.inquiryCompleted;
	}

	/**
	 * Returns the devices discovered in the Search Process.
	 * 
	 * @return Devices discovered.
	 */
	public RemoteDevice[] getDiscoveredDevices() {
		RemoteDevice devices[] = new RemoteDevice[this.foundDevices.size()];
		this.foundDevices.copyInto(devices);
		return devices;
	}

	/**
	 * Marge implementation of <code>DiscoveryListener</code>. Called when a
	 * device is found during an inquiry. An inquiry searches for devices that
	 * are discoverable. The same device may be returned multiple times.
	 * 
	 * @param device
	 *            The device that was found during the inquiry.
	 * @param dclass
	 *            The service classes, major device class, and minor device
	 *            class of the remote device.
	 */
	public void deviceDiscovered(RemoteDevice device, DeviceClass dclass) {
		if (!this.foundDevices.contains(device)) {
			this.foundDevices.addElement(device);
		}
		this.iListener.deviceDiscovered(device, dclass);
	}

	/**
	 * Marge implementation of <code>DiscoveryListener</code>. Called when an
	 * inquiry is completed. The discType will be INQUIRY_COMPLETED if the
	 * inquiry ended normally or INQUIRY_TERMINATED if the inquiry was canceled
	 * by a call to DiscoveryAgent.cancelInquiry(). The discType will be
	 * INQUIRY_ERROR if an error occurred while processing the inquiry causing
	 * the inquiry to end abnormally.
	 * 
	 * @param discType
	 *            The type of request that was completed; either
	 *            INQUIRY_COMPLETED, INQUIRY_TERMINATED, or INQUIRY_ERROR.
	 */
	public void inquiryCompleted(int discType) {
		this.inquiryCompleted = true;
		switch (discType) {
		case INQUIRY_COMPLETED:
			this.iListener.inquiryCompleted(this.getDiscoveredDevices());
			break;
		case INQUIRY_TERMINATED:
			break;
		case INQUIRY_ERROR:
			this.iListener.inquiryError();
			break;
		}
	}
        
        // TODO make possible to get the response condition
	// (serviceSearchCompleted)

	/**
	 * Marge implementation of <code>DiscoveryListener</code>. Called when a
	 * service search is completed or was terminated because of an error.
	 * 
	 * @param transID
	 *            The transaction ID identifying the request which initiated the
	 *            service search.
	 * @param respCode
	 *            The response code that indicates the status of the
	 *            transaction.
	 */
	public void serviceSearchCompleted(int transID, int respCode) {
		switch (respCode) {
		case DiscoveryListener.SERVICE_SEARCH_COMPLETED:
			this.sListener.serviceSearchCompleted(this.remoteDevice,
					this.foundServices);
			break;
		case DiscoveryListener.SERVICE_SEARCH_DEVICE_NOT_REACHABLE:
			this.sListener.deviceNotReachable();
			break;
		case DiscoveryListener.SERVICE_SEARCH_NO_RECORDS:
			this.sListener.serviceSearchCompleted(this.remoteDevice,
					new ServiceRecord[] {});
			break;
		case DiscoveryListener.SERVICE_SEARCH_TERMINATED:
			break;
		case DiscoveryListener.SERVICE_SEARCH_ERROR:
			this.sListener.serviceSearchError();
			break;
		}
	}

	/**
	 * Marge implementation of <code>DiscoveryListener</code>. Called when
	 * service(s) are found during a service search.
	 * 
	 * @param transID
	 *            The transaction ID of the service search that is posting the
	 *            result.
	 * @param services
	 *            A list of services found during the search request.
	 */
	public void servicesDiscovered(int transID, ServiceRecord[] services) {
		this.foundServices = services;
	}

}
