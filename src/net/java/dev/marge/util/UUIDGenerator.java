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
package net.java.dev.marge.util;

import javax.bluetooth.UUID;

/**
 * Class responsible for generating UUID services.
 */
public class UUIDGenerator {

    private static final String BASE_UUID = "12345678";

    /**
     * Generates a UUID using the hash of a given String as base.
     * 
     * @param name The base service name to generate the UUID.
     * @return The UUID generated based on the given String.
     */
    public static UUID generate(String name) {
        int hashCode = positiveHashCode(name);
        String hashCodeAsString = Integer.toHexString(hashCode);
        if (hashCodeAsString.length() < BASE_UUID.length()) {
            hashCodeAsString = BASE_UUID.substring(0, BASE_UUID.length() - hashCodeAsString.length()) + hashCodeAsString;
        }
        hashCodeAsString = hashCodeAsString + hashCodeAsString + hashCodeAsString + hashCodeAsString;
        return new UUID(hashCodeAsString, false);
    }

    public static int positiveHashCode(String s) {
        int hash = 0;
        for (int i = 0; i < s.length(); i++) {
            hash = 31 * hash + s.charAt(i);
        }
        return hash & 0x7fffffff;
    }
}
