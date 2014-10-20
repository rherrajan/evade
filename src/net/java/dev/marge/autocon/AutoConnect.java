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
package net.java.dev.marge.autocon;

import java.io.IOException;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

import net.java.dev.marge.communication.CommunicationListener;
import net.java.dev.marge.communication.ConnectionListener;
import net.java.dev.marge.entity.ClientDevice;
import net.java.dev.marge.entity.ServerDevice;
import net.java.dev.marge.entity.config.ClientConfiguration;
import net.java.dev.marge.entity.config.ServerConfiguration;
import net.java.dev.marge.factory.CommunicationFactory;
import net.java.dev.marge.factory.RFCOMMCommunicationFactory;
import net.java.dev.marge.util.UUIDGenerator;

/**
 * Create Client and Server Devices using the Bluetooth RFCOMM Protocol. It will automate the process of inquirying for devices and searching for services. 
 * It will automatically generate a service based on a name for the server and a client will connect on it. If there are more than one server running the same service, there will be no guarantee which one the client will connect. 
 * Also, this can take a long time if there is a lot of Bluetooth devices in the area. 
 */
public class AutoConnect {

    /**
     * Creates a <code>ServerDevice</code> that will have a service running with the specified server name.
     * This service will be generated by <code>UUIGenerator</code>. The ServerDevice instance will be given to the <code>ConnetionListener</code>
     * when the connection is estabilished and it will use the RFCOMM Protocol.
     * 
     * @param serverName
     *            used to find the server.
     * @param comListener
     *            <code>CommunicationListener</code> used to process the messages.
     * @param conListener
     *            <code>ConnectionListener</code> used to receive the server.
     */
    public static void createServer(String serverName, CommunicationListener comListener, final ConnectionListener conListener) {

        ConnectionListener listener = new ConnectionListener() {

            public void connectionEstablished(ServerDevice serverDevice, RemoteDevice remoteDevice, int channelNr) {
                serverDevice.startListening();
                conListener.connectionEstablished(serverDevice, remoteDevice, channelNr);
            }

            public void errorOnConnection(IOException e) {
                conListener.errorOnConnection(e);
            }
        };

        CommunicationFactory factory = new RFCOMMCommunicationFactory();
        ServerConfiguration sconf = new ServerConfiguration(comListener);
        sconf.setServerName(serverName);
        sconf.setUuid(UUIDGenerator.generate(serverName));
        factory.waitClients(sconf, listener);
    }

    /**
     * Creates a <code>ClientDevice</code> that automatically connects to a server with the specified name. 
     * The Client will connect to the server using the RFCOMM Protocol.
     * 
     * @param serverName
     *            used to find the server.
     * @param comListener
     *            <code>CommunicationListener</code> used to process the messages.
     * @return Created <code>ClientDevice</code>.
     * @throws IOException
     *             if an I/O error occurs.
     */
    public static ClientDevice connectToServer(String serverName, CommunicationListener comListener) throws IOException {
        DiscoveryAgent agent = LocalDevice.getLocalDevice().getDiscoveryAgent();
        String connectionURL = agent.selectService(UUIDGenerator.generate(serverName), ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);

        final ClientConfiguration config = new ClientConfiguration(connectionURL, comListener);

        CommunicationFactory factory = new RFCOMMCommunicationFactory();
        ClientDevice device = factory.connectToServer(config);
        device.startListening();
        return device;
    }
}
