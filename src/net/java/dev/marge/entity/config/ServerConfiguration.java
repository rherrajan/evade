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

import java.util.Enumeration;
import java.util.Hashtable;

import javax.bluetooth.UUID;

import net.java.dev.marge.communication.CommunicationListener;

/**
 * Configuration for creating a <code>ServerDevice</code>. Hold all the
 * information used for creating a Server using the
 * <code>CommunicationFactory</code>.
 * 
 */
public class ServerConfiguration extends Configuration {

    //TODO verify if authorize, encrypt, authenticate are not conflicting
    private UUID uuid;
    private int maxNumberOfConnections;
    private String serverName;
    private String protocol;
    private Hashtable parameters;
    public static final String PARAMETER_ENCRYPT = "encrypt";
    public static final String PARAMETER_AUTHORIZE = "authorize";
    public static final String PARAMETER_AUTHENTICATE = "authenticate";
    public static final String PARAMETER_L2CAP_RECEIVEMTU = "receiveMTU";
    public static final String PARAMETER_L2CAP_TRANSMITMTU = "transmitMTU";
    public static final String PARAMETER_MASTER = "master";

    /**
     * Constructor.
     * 
     * @param communicationListener
     *            <code>CommunicationListener</code> which will the notified for incoming messages.
     */
    public ServerConfiguration(CommunicationListener communicationListener) {
        this(communicationListener, new UUID(MargeDefaults.DEFAULT_UUID, false), MargeDefaults.DEFAULT_SERVER_NAME);
    }

    public ServerConfiguration(CommunicationListener communicationListener, UUID uuid, String serverName) {
        super(communicationListener);
        this.uuid = uuid;
        this.serverName = serverName;
        this.maxNumberOfConnections = 1;
        this.protocol = "btssp";
        this.parameters = new Hashtable(3);
    }

    /**
     * Returns a URL used to create a Server Service.
     * 
     * @return Service creation URL.
     */
    public String getConnectionURL() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getProtocol());
        sb.append("://localhost:");
        sb.append(this.getUuid().toString());
        sb.append(";name=");
        sb.append(this.getServerName());

        Enumeration headersNames = parameters.keys();
        if (headersNames != null) {
            while (headersNames.hasMoreElements()) {
                String key = (String) headersNames.nextElement();
                sb.append(";");
                sb.append(key);
                sb.append("=");
                sb.append(parameters.get(key));
            }
        }
        return sb.toString();
    }

    /**
     * Returns the Server name.
     * 
     * @return Server name.
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * Sets the name that will be used to create the server.
     * 
     * @param serverName
     *            New server name.
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * Returns the current Universally Unique Identifier of the server.
     * 
     * @return Universally Unique Identifier.
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Sets the server Universally Unique Identifier of the server.
     * 
     * @param uuid
     *            New Universally Unique Identifier.
     */
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Returns the Bluetooth Stack protocol name that will be used to create.
     * this Server.
     * 
     * @return Bluetooth Stack protocol name.
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Sets the Bluetooth Stack protocol name that will be used to create this
     * Server.
     * 
     * @param protocol
     *            Bluetooth Stack protocol name.
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * Returns if the server will use authentication or not.
     * 
     * @return flag.
     */
    public boolean getAuthenticate() {
        return this.getBooleanValueFromParameters(PARAMETER_AUTHENTICATE);
    }

    /**
     * Sets if the server will use authentication.
     * 
     * @param authenticate
     */
    public void setAuthenticate(boolean authenticate) {
        this.setBooleanIntoParametersConvertingToString(PARAMETER_AUTHENTICATE, authenticate);
    }

    /**
     * Returns the flag indicating if the server will need Authorization.
     * 
     * @return Authorization flag.
     */
    public boolean getAuthorize() {
        return this.getBooleanValueFromParameters(PARAMETER_AUTHORIZE);
    }

    /**
     * Sets the flag indicating if the server will need Authorization.
     * 
     * @param authorize
     *            Authorization flag.
     */
    public void setAuthorize(boolean authorize) {
        this.setBooleanIntoParametersConvertingToString(PARAMETER_AUTHORIZE, authorize);
    }

    /**
     * Returns the flag indicating if the server will use data Encryption.
     * 
     * @return Encryption flag.
     */
    public boolean getEncrypt() {
        return this.getBooleanValueFromParameters(PARAMETER_ENCRYPT);
    }

    /**
     * Sets the flag indicating if the server will use data Encryption.
     * 
     * @param encrypt
     *            Encryption flag.
     */
    public void setEncrypt(boolean encrypt) {
        this.setBooleanIntoParametersConvertingToString(PARAMETER_ENCRYPT, encrypt);
    }

    /**
     * Adds a parameter that will be added in the connection string
     * 
     * @param key
     *            Key.
     * @param value
     *            Value.
     */
    public void addParameter(String key, String value) {
        this.parameters.put(key, value);
    }

    /**
     * Removes the parameter identified by the key.
     * 
     * @param key
     *            Key.
     */
    public void removeParameter(String key) {
        this.parameters.remove(key);
    }

    /**
     * Returns the parameters hashtable.
     * 
     * @return Hastable.
     */
    public Hashtable getParameters() {
        return parameters;
    }

    /**
     * Sets the parameter Hashtable
     * 
     * @param parameters
     *            Hashtable.
     */
    public void setParameters(Hashtable parameters) {
        this.parameters = parameters;
    }

    /**
     * Returns the maximum number of connections that the Server will support
     * 
     * @return Maximum number of connections.
     */
    public int getMaxNumberOfConnections() {
        return maxNumberOfConnections;
    }

    /**
     * Sets the maximum number of connections that the Server will support. This
     * number is limited by 7 by the Bluetooth technology.
     * 
     * @param maxNumberOfConnections
     *            The new maximum number of connections.
     */
    public void setMaxNumberOfConnections(int maxNumberOfConnections) {
        this.maxNumberOfConnections = maxNumberOfConnections;
    }

    /**
     * Converts a String to boolean.
     * 
     * @param value
     */
    private boolean stringToBoolean(String value) {
        return value != null && value.toLowerCase().equals("true");
    }

    /**
     * Gets the boolean from String parameter.
     * 
     * @param parameter
     */
    private boolean getBooleanValueFromParameters(String parameter) {
        return this.stringToBoolean((String) this.parameters.get(parameter));
    }

    /**
     * Sets boolean value into parameters.
     * 
     * @param name
     */
    private void setBooleanIntoParametersConvertingToString(String parameter, boolean b) {
        this.addParameter(parameter, new Boolean(b).toString());
    }
}
