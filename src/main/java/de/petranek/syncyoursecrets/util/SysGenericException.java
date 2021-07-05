/**
 * SyncYourSecrets-xmlbase provides a basic layer for SyncYourSecrets
 * 
 * 
 *    Copyright 2009 Jan Petranek
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.   
 * You may obtain a copy of the License at   
 *     http://www.apache.org/licenses/LICENSE-2.0   
 *    
 * Unless required by applicable law or agreed to in writing, software   
 * distributed under the License is distributed on an "AS IS" BASIS,   
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   
 * See the License for the specific language governing permissions and   
 * limitations under the License.   
 * 
 */
package de.petranek.syncyoursecrets.util;

/**
 * The SysGenericException is the root of all Exceptions used in
 * SyncYourSecrets. More specific Exceptions shall derive from this.
 * 
 * @author Jan Petranek
 */
public class SysGenericException extends Exception {

	/** Generated Id. */
	private static final long serialVersionUID = 4430105596418660133L;

	/**
	 * Instantiates a new sys generic exception.
	 */
	public SysGenericException() {
		super();
	}

	/**
	 * Instantiates a new sys generic exception.
	 * 
	 * @param message
	 *            the message
	 */
	public SysGenericException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new sys generic exception.
	 * 
	 * @param cause
	 *            the cause
	 */
	public SysGenericException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new sys generic exception.
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public SysGenericException(String message, Throwable cause) {
		super(message, cause);
	}

}
