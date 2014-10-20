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

import net.java.dev.marge.communication.ConnectionListener;
import net.java.dev.marge.entity.ClientDevice;
import net.java.dev.marge.entity.config.ClientConfiguration;
import net.java.dev.marge.entity.config.ServerConfiguration;

/**
 * Base methods for other Communication Factories create
 * <code>ClientDevice</code> and <code>ServerDevice</code>.
 */
public interface CommunicationFactory {

	/**
	 * Creates a <code>ClientDevice</code> under the given configurations.
	 * 
	 * @param configuration
	 *            <code>ClientConfiguration</code> used to create the client.
	 * @return Created <code>ClientDevice</code>.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public ClientDevice connectToServer(ClientConfiguration configuration)
			throws IOException;

	/**
	 * Creates a <code>ServerDevice</code> with the given configurations. The
	 * ServerDevice instance will be given to the <code>ConnetionListener</code>
	 * when the connection is estabilished.
	 * 
	 * @param configuration
	 *            <code>ServerConfiguration</code> used to create the server.
	 * @param connectionListener
	 *            Listener that will be notified when a connection is
	 *            estabilished.
	 */
	public void waitClients(final ServerConfiguration configuration,
			final ConnectionListener connectionListener);
}