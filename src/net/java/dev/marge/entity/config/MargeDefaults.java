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
import javax.bluetooth.UUID;

/**
 * Interface with some default values used by the framework, if you do not
 * specify things in <code>Configuration</code>. It has the deafult UUID,
 * server name and service.
 */
public interface MargeDefaults {

	public static final String DEFAULT_UUID = "D0FFBEE2D0FFBEE2D0FFBEE2D0FFBEE2";
        
        public static final UUID[] DEFAULT_UUID_ARRAY = new UUID[] { new UUID(DEFAULT_UUID, false) };

	public static final String DEFAULT_SERVER_NAME = "We_Love_Duff_Beer";
       
}
