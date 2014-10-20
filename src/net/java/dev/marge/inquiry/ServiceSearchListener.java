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

import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

/**
 * Listener interface that contains the methods that can be called during the
 * service search process.
 */
public interface ServiceSearchListener {

	/**
	 * This method is called by the listener when the search process has been
	 * completed in a remote device.
	 * 
	 * @param remoteDevice
	 *            The remote device that was searched.
	 * @param services
	 *            The services found.
	 */
	public void serviceSearchCompleted(RemoteDevice remoteDevice,
			ServiceRecord services[]);

	/**
	 * This method is called by the listener when a remote device could not be
	 * reachable.
	 */
	public void deviceNotReachable();

	/**
	 * This method is called by the listener when an error happens during the
	 * search process.
	 */
	public void serviceSearchError();

}
