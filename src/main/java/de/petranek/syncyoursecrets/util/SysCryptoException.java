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
 * The Class ParseException signals errors when en/decrypting data.
 */
public class SysCryptoException extends SysXmlBaseException {

	/** Generated Id. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new sys crypto exception.
	 */
	public SysCryptoException() {
		// nothing to do
	}

	/**
	 * The Constructor.
	 * 
	 * @param message
	 *            the message
	 */
	public SysCryptoException(String message) {
		super(message);
		// nothing to do
	}

	/**
	 * The Constructor.
	 * 
	 * @param cause
	 *            the cause
	 */
	public SysCryptoException(Throwable cause) {
		super(cause);
		// nothing to do
	}

	/**
	 * The Constructor.
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public SysCryptoException(String message, Throwable cause) {
		super(message, cause);
		// nothing to do
	}

}
