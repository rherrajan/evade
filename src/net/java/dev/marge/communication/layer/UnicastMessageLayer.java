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
package net.java.dev.marge.communication.layer;

import java.io.IOException;

import javax.bluetooth.BluetoothStateException;

import net.java.dev.marge.communication.CommunicationListener;
import net.java.dev.marge.entity.Device;

/**
 * Layer that can be used to provide unicast behavior, where you can delivery 
 * messages only to selected destinations. You need to extend it to implement 
 * specific methods and set both devices on it to make it work.
 */
public abstract class UnicastMessageLayer implements CommunicationListener {

    private Device device;
    private static final byte TYPE_DATA = 0x00;
    private static final byte TYPE_COMMAND = 0x01;
    private static final String BROADCAST_ADDRESS = "FFFFFFFFFFFF";
    private static final String GETNAMESONSERVER_MESSAGE = "WhoWantsFreeBeer?";
    private static final String GETNAMESONSERVER_RESPONSE = "HellIWant!";
    private CommunicationListener communicationListener;

    /**
     * Contructor that receives a <code>Device</code> instance.
     * 
     * @param device
     *            The device (server or client).
     */
    public UnicastMessageLayer(Device device) {
        this.device = device;
        this.communicationListener = device.getCommunicationListener();
        this.device.setCommunicationListener(this);
    }

    /**
     * Sends the given bytes through the available Channels. This is a broadcast 
     * message.
     * 
     * @param message
     *            The bytes to be sent.
     */
    public void sendMessage(byte[] message) {
        sendMessage(BROADCAST_ADDRESS, message);
    }

    /**
     * Sends the given bytes through a specific device. This is a unicast message.
     * 
     * @param message
     *            The bytes to be sent.
     * @param to
     *            The device you want to send the message.
     */
    public void sendMessage(String to, byte[] message) {
        device.send(packMessage(to, TYPE_DATA, message));
    }

    /**
     * Sends a message to all devices that are connected to send its name. The response will 
     * be received in the abstract receiveNameOnServer.
     */
    public void getNamesOnServer() {
        device.send(packMessage(BROADCAST_ADDRESS, TYPE_COMMAND, GETNAMESONSERVER_MESSAGE.getBytes()));
    }

    /**
     * Sends a message to all devices that are connected to send its name. The response will 
     * 
     * @param to
     *            The bytes to be sent.
     * @param type
     *            The message type (Data or Command).
     * @param b
     *            The bytes to be sent.
     * @return Message packed.
     */
    private byte[] packMessage(String to, byte type, byte[] b) {
        String from = null;
        try {
            from = device.getBluetoothAddress();
        } catch (BluetoothStateException e) {
            this.errorOnSending(e);
        }

        byte[] fromAsBytes = from.getBytes();
        byte[] toAsBytes = to.getBytes();

        byte[] message = new byte[toAsBytes.length + fromAsBytes.length + 1 + b.length];
        int j = 0;
        for (int i = 0; i < toAsBytes.length; i++, j++) {
            message[j] = toAsBytes[i];
        }
        for (int i = 0; i < fromAsBytes.length; i++, j++) {
            message[j] = fromAsBytes[i];
        }
        message[j++] = type;
        for (int i = 0; i < b.length; i++, j++) {
            message[j] = b[i];
        }

        return message;
    }

    /**
     * Method that need to implemented and will be used to receive the message by this layer. 
     * 
     * @param from
     *            The device that sent the message.
     * @param message
     *            The bytes to be sent.
     */
    public abstract void receiveMessage(String from, byte[] message);

    /**
     * Method that need to implemented and will be used to receive the device 
     * address when request by getNamesOnServer method. 
     * 
     * @param address
     *            The device address found.
     * @param name
     *            The device name found.
     */
    public abstract void receiveNameOnServer(String address, String name);

    /**
     * This method is called by the device listener when the current device receives a
     * message and the layer process it and delegates to the abstract receiveMessage.
     * 
     * @param message
     *            Byte array received representing the message.
     */
    public final void receiveMessage(byte[] message) {
        try {
            int j = 0;
            byte[] temp = new byte[BROADCAST_ADDRESS.length()];

            for (int i = 0; i < temp.length; i++, j++) {
                temp[i] = message[j];
            }
            String to = new String(temp);
            if (to.equals(device.getBluetoothAddress()) || to.equals(BROADCAST_ADDRESS)) {
                for (int i = 0; i < temp.length; i++, j++) {
                    temp[i] = message[j];
                }
                String from = new String(temp);
                byte messageType = message[j++];
                int messageContentLength = message.length - (2 * temp.length + 1);
                byte[] messageContentAsBytes = new byte[messageContentLength];
                for (int i = 0; i < messageContentLength; i++, j++) {
                    messageContentAsBytes[i] = message[j];
                }
                if (messageType == TYPE_DATA) {
                    this.receiveMessage(from, messageContentAsBytes);
                } else {
                    String messageContent = new String(messageContentAsBytes);
                    if (messageContent.indexOf(GETNAMESONSERVER_MESSAGE) != -1) {
                        byte[] b = packMessage(from, TYPE_COMMAND, (GETNAMESONSERVER_RESPONSE + "=" + device.getDeviceName()).getBytes());
                        device.send(b);
                    } else if (messageContent.indexOf(GETNAMESONSERVER_RESPONSE) != -1) {
                        String name = messageContent.substring(messageContent.indexOf("="));
                        receiveNameOnServer(from, name);
                    }

                }
            }
        } catch (BluetoothStateException ex) {
            this.errorOnReceiving(ex);
        }
    }

    /**
     * This method delegates to the current device <code>CommunicationListener</code>
     * 
     * @param e
     *            IOException that can occur.
     */
    public void errorOnReceiving(IOException e) {
        this.communicationListener.errorOnReceiving(e);
    }

    /**
     * This method delegates to the current device <code>CommunicationListener</code>
     * 
     * @param e
     *            IOException that can occur.
     */    
    public void errorOnSending(IOException e) {
        this.communicationListener.errorOnSending(e);
    }
}

    
