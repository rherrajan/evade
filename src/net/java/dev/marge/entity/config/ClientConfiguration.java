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
package net.java.dev.marge.entity.config;

import javax.bluetooth.ServiceRecord;

import net.java.dev.marge.communication.CommunicationListener;

/**
 * Configuration for creating a <code>ClientDevice</code>. Hold all the
 * information used for creating a Client using the
 * <code>CommunicationFactory</code>.
 * 
 */
public class ClientConfiguration extends Configuration {

    private ServiceRecord service;
    private String connectionURL;

    /**
     * Constructor.
     * 
     * @param service
     *            Remote Service that the client will connect.
     * @param communicationListener
     *            <code>CommunicationListener</code> which will the notified
     *            for incoming messages.
     */
    public ClientConfiguration(ServiceRecord service,
            CommunicationListener communicationListener) {
        super(communicationListener);
        this.service = service;
        this.connectionURL = null;
    }

    //TODO
    public ClientConfiguration(String connectionURL, CommunicationListener communicationListener) {
        super(communicationListener);
        this.connectionURL = connectionURL;
        this.service = null;
    }

    /**
     * Returns a URL to connect in the specified ServiceRecord.
     * 
     * @return Service connection URL.
     */
    public String getConnectionURL() {
        // TODO see about encypt and authenticate
        return (service == null) ? connectionURL : this.service.getConnectionURL(
                ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
    }
}
