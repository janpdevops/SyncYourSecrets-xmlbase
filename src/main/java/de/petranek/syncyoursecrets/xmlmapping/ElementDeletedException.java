/**
 * SyncYourSecrets-xmlbase provides a basic layer for SyncYourSecrets
 * 
 * 
 *    Copyright 2008 Jan Petranek
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
package de.petranek.syncyoursecrets.xmlmapping;

import de.petranek.syncyoursecrets.util.SysXmlBaseException;

/**
 * The Class ElementDeletedException signals, that operations were invoked on a
 * MappingElement which cannot be carried out because the MappingElement has
 * already been deleted.
 * 
 * @author Jan Petranek
 */
public class ElementDeletedException extends SysXmlBaseException {

	/** required serial Version. */
	private static final long serialVersionUID = 1859479050695137243L;

	/**
	 * Instantiates a new element deleted exception.
	 */
	public ElementDeletedException() {
		super();

	}

	/**
	 * Instantiates a new element deleted exception.
	 * 
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 */
	public ElementDeletedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Instantiates a new element deleted exception.
	 * 
	 * @param arg0
	 *            the arg0
	 */
	public ElementDeletedException(String arg0) {
		super(arg0);
	}

	/**
	 * Instantiates a new element deleted exception.
	 * 
	 * @param arg0
	 *            the arg0
	 */
	public ElementDeletedException(Throwable arg0) {
		super(arg0);
	}

}
