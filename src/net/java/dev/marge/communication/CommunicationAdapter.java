/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.java.dev.marge.communication;

import java.io.IOException;

/**
 * 
 * @author brunogh
 */
public class CommunicationAdapter implements CommunicationListener {

	public void errorOnReceiving(IOException e) {
	}

	public void errorOnSending(IOException e) {
	}

	public void receiveMessage(byte[] message, int fromWhom) {
	}

}
