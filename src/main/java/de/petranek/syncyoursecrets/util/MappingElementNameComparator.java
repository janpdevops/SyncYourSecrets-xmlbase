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
package de.petranek.syncyoursecrets.util;

import java.io.Serializable;
import java.util.Comparator;

import de.petranek.syncyoursecrets.xmlmapping.MappingElement;


/**
 * The Class MappingElementNameComparator compares MappingElements by their
 * names. This is useful, when displaying MappingElements in alphabetical order,
 * e.g. to present them for the user.
 * 
 * 
 * If either element does not have a name, the comparator uses the default
 * compare behaviour from MappingElement.
 * 
 * @author Jan Petranek
 */
public class MappingElementNameComparator implements
		Comparator<MappingElement>, Serializable {

	/**
	 * Generated Id.
	 */
	private static final long serialVersionUID = 8373773779935529466L;

	/**
	 * Compares mapping elements in the alphabetic order of their names.
	 * 
	 * @param a
	 *            first element to compare
	 * @param b
	 *            second element to compare
	 * @return -1, when a&lgt;b, 0, when a==b and 1, when a&gt;b
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(final MappingElement a, final MappingElement b) {
		if (a.getName() == null || b.getName() == null) {
			return a.compareTo(b);
		}
		return a.getName().compareTo(b.getName());
	}

}
