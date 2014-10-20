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

import javax.bluetooth.DeviceClass;
import javax.bluetooth.RemoteDevice;

/**
 * Listener interface that contains the methods that can be called during the
 * inquiry for devices process.
 */
public interface InquiryListener {

	/**
	 * This method is called by the listener when a device is discovered during
	 * the inquiry process.
	 * 
	 * @param device
	 *            The remote device that was discovered.
	 * @param deviceClass
	 *            The device class.
	 */
	public void deviceDiscovered(RemoteDevice device, DeviceClass deviceClass);

	/**
	 * This method is called by the listener when the inquiry for devices is
	 * completed.
	 * 
	 * @param devices
	 *            An array of remote devices found during the inquiry process.
	 */
	public void inquiryCompleted(RemoteDevice[] devices);

	/**
	 * This method is called by the listener when an error happens during the
	 * inquiry for devices process.
	 */
	public void inquiryError();
}
