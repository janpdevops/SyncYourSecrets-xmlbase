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
 * The Class SysXmlBaseException signals error conditions from the
 * SyncYourSecrets-xmlbase layer. Subclasses may specificy more detailed types
 * of errors.
 */
public class SysXmlBaseException extends SysGenericException {

	/**
	 * Generated Id.
	 */
	private static final long serialVersionUID = 861466769008283581L;

	/**
	 * Instantiates a new sys xml base exception.
	 */
	public SysXmlBaseException() {
		// nothing to do
	}

	/**
	 * Instantiates a new sys xml base exception.
	 * 
	 * @param message
	 *            the message
	 */
	public SysXmlBaseException(String message) {
		super(message);
		// nothing to do
	}

	/**
	 * Instantiates a new sys xml base exception.
	 * 
	 * @param cause
	 *            the cause
	 */
	public SysXmlBaseException(Throwable cause) {
		super(cause);
		// nothing to do
	}

	/**
	 * Instantiates a new sys xml base exception.
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public SysXmlBaseException(String message, Throwable cause) {
		super(message, cause);
		// nothing to do
	}

}
