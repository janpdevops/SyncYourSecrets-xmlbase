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
 * The SysInvalidArgumentException indicates, that the core classes were used
 * with an invalid argument.
 * 
 * @author Jan Petranek
 */
public class SysInvalidArgumentException extends SysXmlBaseException {

	/** Generated Id. */
	private static final long serialVersionUID = 2962316927081278004L;

	/**
	 * Instantiates a new sys invalid argument exception.
	 */
	public SysInvalidArgumentException() {
		// nothing to do
	}

	/**
	 * Instantiates a new sys invalid argument exception.
	 * 
	 * @param message
	 *            the message
	 */
	public SysInvalidArgumentException(String message) {
		super(message);
		// nothing to do
	}

	/**
	 * Instantiates a new sys invalid argument exception.
	 * 
	 * @param cause
	 *            the cause
	 */
	public SysInvalidArgumentException(Throwable cause) {
		super(cause);
		// nothing to do
	}

	/**
	 * Instantiates a new sys invalid argument exception.
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public SysInvalidArgumentException(String message, Throwable cause) {
		super(message, cause);
		// nothing to do
	}

}
