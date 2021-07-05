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
package de.petranek.syncyoursecrets.io;

import de.petranek.syncyoursecrets.util.SysXmlBaseException;

/**
 * The Class FileIOException signals or wraps error conditions resulting from
 * file access operations, carried out using FileHelper.
 */
public class FileIOException extends SysXmlBaseException {

	/**
	 * Generated Id.
	 */
	private static final long serialVersionUID = -1742099621437767906L;

	/**
	 * Instantiates a new file io exception.
	 */
	public FileIOException() {
		// nothing to do
	}

	/**
	 * Instantiates a new file io exception.
	 * 
	 * @param message
	 *            the message
	 */
	public FileIOException(String message) {
		super(message);
		// nothing to do
	}

	/**
	 * Instantiates a new file io exception.
	 * 
	 * @param cause
	 *            the cause
	 */
	public FileIOException(Throwable cause) {
		super(cause);
		// nothing to do
	}

	/**
	 * Instantiates a new file io exception.
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public FileIOException(String message, Throwable cause) {
		super(message, cause);
		// nothing to do
	}

}
